package co.xarx.trix.services.notification;

import co.xarx.trix.api.NotificationView;
import co.xarx.trix.domain.MobileNotification;
import co.xarx.trix.services.AmazonCloudService;
import co.xarx.trix.services.notification.client.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

@Component
public class FCM2Client implements NotificationServerClient {

	private Map<String, NotificationResult> errorDevices;
	private Map<String, NotificationResult> successDevices;
	private FCMSender sender;

	@Autowired
	private AmazonCloudService amazonCloudService;

	@Autowired
	public FCM2Client(FCMSender sender){
		this.sender = sender;
		this.errorDevices = new HashMap<>();
		this.successDevices = new HashMap<>();
	}

	@Override
	public Map<String, NotificationResult> getErrorDevices() {
		return errorDevices;
	}

	@Override
	public Map<String, NotificationResult> getSuccessDevices() {
		return successDevices;
	}

	@Override
	public void send(NotificationView notificationView, Collection<String> d) throws IOException {
		MulticastResult multicastResult;

		List<String> devices = new ArrayList<>(d);

		Message message = createMessage(notificationView);

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
			}else{
				NotificationResult notificationResult = new NotificationResult();
				notificationResult.setStatus(MobileNotification.Status.SUCCESS);
				notificationResult.setMessageId(r.getMessageId());
				successDevices.put(devices.get(i), notificationResult);
			}
		}
	}

	public Message createMessage(NotificationView notificationView){

		Message.Builder builder = new Message.Builder()
//				.notification(notification)
				.contentAvailable(true)
				.mutableContent(true)
				.addData("title", notificationView.post.stationName)
				.addData("body", notificationView.post.title)
				.addData("type", notificationView.type)
				.addData("hash", notificationView.hash)
				.addData("entityId", String.valueOf(notificationView.postId))
				.addData("stationId", notificationView.post.stationId + "")
				.addData("stationName", notificationView.post.stationName)
				.addData("entityType", "POST")
//				.delayWhileIdle(true)
				.priority(Message.Priority.HIGH)
				.timeToLive(0);

		String image;
		try {
			image = amazonCloudService.getPublicImageURL(notificationView.post.getImageLargeHash());
			builder.addData("imageUrl", image);
		} catch (IOException e) {
			e.printStackTrace();
		}


		return builder.build();

	}
}
