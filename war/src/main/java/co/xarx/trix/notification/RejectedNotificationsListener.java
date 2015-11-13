package co.xarx.trix.notification;

import com.relayrides.pushy.apns.PushManager;
import com.relayrides.pushy.apns.RejectedNotificationListener;
import com.relayrides.pushy.apns.RejectedNotificationReason;
import com.relayrides.pushy.apns.util.SimpleApnsPushNotification;

/**
 * Created by jonas on 06/08/15.
 */

public class RejectedNotificationsListener implements RejectedNotificationListener<SimpleApnsPushNotification> {

	public void handleRejectedNotification(
			final PushManager<? extends SimpleApnsPushNotification> pushManager,
			final SimpleApnsPushNotification notification,
			final RejectedNotificationReason reason) {

		System.err.format("%s was rejected with rejection reason %s\n", notification, reason);
	}
}