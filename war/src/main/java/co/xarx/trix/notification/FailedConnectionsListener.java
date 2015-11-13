package co.xarx.trix.notification;

import com.relayrides.pushy.apns.FailedConnectionListener;
import com.relayrides.pushy.apns.PushManager;
import com.relayrides.pushy.apns.util.SimpleApnsPushNotification;

import javax.net.ssl.SSLHandshakeException;

/**
 * Created by jonas on 06/08/15.
 */

public class FailedConnectionsListener implements FailedConnectionListener<SimpleApnsPushNotification> {

	public void handleFailedConnection(final PushManager<? extends SimpleApnsPushNotification> pushManager,
	                                   final Throwable cause) {

		if (cause instanceof SSLHandshakeException) {
			System.out.println("Error -- time to shut it down");

			try {
				pushManager.shutdown();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}