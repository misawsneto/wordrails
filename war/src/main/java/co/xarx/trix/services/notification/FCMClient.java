package co.xarx.trix.services.notification;

import co.xarx.trix.api.NotificationView;
import co.xarx.trix.domain.MobileNotification;
import co.xarx.trix.domain.NotificationType;
import co.xarx.trix.services.notification.NotificationResult;
import co.xarx.trix.services.notification.NotificationServerClient;
import com.google.android.gcm.server.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

@Component
public class FCMClient implements NotificationServerClient {

    private Map<String, NotificationResult> errorDevices;
	private Map<String, NotificationResult> successDevices;
    private FCMSender sender;

    @Autowired
    public FCMClient(FCMSender sender){
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
        Notification notification = new Notification.Builder(notificationView.post.featuredImageHash)
                .title(notificationView.title)
                .body(notificationView.message)
                .build();

        return new Message.Builder()
                .notification(notification)
                .addData("type", notificationView.type)
                .addData("entityId", String.valueOf(notificationView.postId))
                .delayWhileIdle(true)
                .priority(Message.Priority.HIGH)
                .timeToLive(0).build();

    }
}
