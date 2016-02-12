package co.xarx.trix.services.notification;

import co.xarx.trix.api.NotificationView;
import co.xarx.trix.domain.Notification;
import co.xarx.trix.util.Logger;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gcm.server.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

@Component
public class AndroidNotificationSender implements NotificationSender {

	private ObjectMapper mapper;
	private Sender sender;

	AndroidNotificationSender() {
	}

	@Autowired
	public AndroidNotificationSender(Sender sender) {
		this.sender = sender;
		this.mapper = new ObjectMapper();
		mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
		mapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
	}

	@Override
	public Map<String, NotificationResult> sendMessageToDevices(NotificationView notification, Collection<String> d) throws IOException {
		MulticastResult multicastResult;

		List<String> devices = new ArrayList<>(d);

		String notificationJson = mapper.valueToTree(notification).toString();
		Message message = new Message.Builder().addData("message", notificationJson).delayWhileIdle(true).timeToLive(86400).build();

		Logger.info("sending messages... " + devices.size() + " hash: " + notification.hash);
		multicastResult = sender.send(message, devices, 5);

		Map<String, NotificationResult> resultMap = new HashMap<>();
		List<Result> results = multicastResult.getResults();
		for (int i = 0; i < results.size(); i++) {
			Result r = results.get(i);
			NotificationResult notificationResult = new NotificationResult();
			notificationResult.setStatus(r.getMessageId() == null ?
					Notification.Status.SERVER_ERROR : Notification.Status.SUCCESS);
			notificationResult.setErrorMessage(r.getErrorCodeName());
			if(Constants.ERROR_NOT_REGISTERED.equals(notificationResult.getErrorMessage())) {
				notificationResult.setDeviceDeactivated(true);
			}
			resultMap.put(devices.get(i), notificationResult);
		}
		return resultMap;
	}

	@Override
	public Integer getBatchSize() {
		return 1000;
	}
}
