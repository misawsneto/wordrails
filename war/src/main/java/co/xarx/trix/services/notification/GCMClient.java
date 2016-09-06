package co.xarx.trix.services.notification;

import co.xarx.trix.api.NotificationView;
import co.xarx.trix.domain.MobileNotification;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gcm.server.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class GCMClient implements NotificationServerClient {

	private ObjectMapper mapper;
	private Sender sender;

	private Map<String, NotificationResult> errorDevices;

	@Autowired
	public GCMClient(Sender sender) {
		this.errorDevices = new HashMap<>();
		this.sender = sender;
		this.mapper = new ObjectMapper();
		mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
		mapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
		mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
	}

	@Override
	public Map<String, NotificationResult> getErrorDevices() {
		return errorDevices;
	}

	@Override
	public void send(NotificationView notification, Collection<String> d) throws IOException {
		MulticastResult multicastResult;

		List<String> devices = new ArrayList<>(d);

		String notificationJson = mapper.valueToTree(notification).toString();
		Message message = new Message.Builder().addData("message", notificationJson).delayWhileIdle(true).timeToLive(86400).build();

		multicastResult = sender.send(message, devices, 5);

		List<Result> results = multicastResult.getResults();
		for (int i = 0; i < results.size(); i++) {
			Result r = results.get(i);
			if(r.getMessageId() == null) {
				NotificationResult notificationResult = new NotificationResult();
				notificationResult.setStatus(MobileNotification.Status.SERVER_ERROR);
				notificationResult.setErrorMessage(r.getErrorCodeName());
				if (Constants.ERROR_NOT_REGISTERED.equals(notificationResult.getErrorMessage())) {
					notificationResult.setDeviceDeactivated(true);
				}
				errorDevices.put(devices.get(i), notificationResult);
			}
		}
	}

	@Override
	public void start() {
		//it does nothing
	}

	@Override
	public void stop() {
		//it does nothing
	}
}
