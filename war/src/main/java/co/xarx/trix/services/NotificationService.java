package co.xarx.trix.services;

import co.xarx.trix.api.NotificationView;
import co.xarx.trix.domain.Notification;
import co.xarx.trix.domain.Post;
import co.xarx.trix.util.Logger;
import co.xarx.trix.util.TrixUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;
import org.eclipse.persistence.jpa.jpql.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class NotificationService {

	private final int GCM_WINDOW_SIZE = 1000;

	private ObjectMapper mapper;
	private Sender sender;

	@Autowired
	public NotificationService(Sender sender) {
		this.sender = sender;
		this.mapper = new ObjectMapper();
		mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
		mapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
	}


	@Value("${spring.profiles.active:'dev'}")
	private String profile;

	public List<Notification> sendNotifications(NotificationView notification, Post post, Set<String> devices){
		Assert.isNotNull(devices, "Null regIds");
		Assert.isNotNull(notification, "Null notification");

		List<List<String>> parts = TrixUtil.partition(new ArrayList<>(devices), GCM_WINDOW_SIZE);

		String notificationJson = mapper.valueToTree(notification).toString();

		Message message = new Message.Builder()
					.addData("message", notificationJson)
					.delayWhileIdle(true)
					.timeToLive(86400)
					.build();

		Map<String, Result> results;
		List<Notification> notifications = new ArrayList<>();
		for (List<String> part : parts) {

			try {
				results = sendMessageToDevices(message, part, notification.hash);

				notifications.addAll(this.getNotifications(results, notification, post, notification.hash));
			} catch (IOException e) {
				notifications.addAll(this.getNotifications(part, notification, post, "Failed to send to GCM",
						co.xarx.trix.util.Constants.Notification.STATUS_SEND_ERROR, notification.hash));
			}

			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
			}
		}

		return notifications;
	}

	private List<Notification> getNotifications(List<String> devices, NotificationView notification, Post post, String errorMessage, String status, String hash) {
		Assert.isNotNull(devices, "Null devices");
		Assert.isNotNull(notification, "Null notification");

		notification.hash = hash;
		List<Notification> notis = new ArrayList<>();

		for (String device : devices) {
			Notification noti = new Notification(device, hash, status, notification.message, notification.type);
			noti.post = post;
			noti.errorCodeName = errorMessage;
			notis.add(noti);
		}

		return notis;
	}


	private List<Notification> getNotifications(Map<String, Result> results, NotificationView notification, Post post, String hash) {
		Assert.isNotNull(results, "Null results");
		Assert.isNotNull(notification, "Null notification");

		notification.hash = hash;
		List<Notification> notis = new ArrayList<>();

		for (String device : results.keySet()) {
			Result r = results.get(device);
			String status = r.getMessageId() == null ?
					co.xarx.trix.util.Constants.Notification.STATUS_GCM_ERROR :
					co.xarx.trix.util.Constants.Notification.STATUS_SUCCESS;
			Notification noti = new Notification(device, hash, status, notification.message, notification.type);
			noti.message = notification.message;
			noti.regId = device;
			noti.post = post;
			noti.type = notification.type;
			noti.hash = hash;
			noti.errorCodeName = r.getErrorCodeName();
			notis.add(noti);
		}

		return notis;
	}

	public Map<String, Result> sendMessageToDevices(Message message, List<String> devices, String notificationHash) throws IOException {
		MulticastResult multicastResult;

		Logger.info("sending messages... " + devices.size() + " hash: " + notificationHash);
		multicastResult = sender.send(message, devices, 5);

		Map<String, Result> resultMap = new HashMap<>();
		List<Result> results = multicastResult.getResults();
		for (int i = 0; i < results.size(); i++) {
			Result r = results.get(i);
			resultMap.put(devices.get(i), r);
		}
		return resultMap;
	}
}
