package co.xarx.trix.services.notification;

import co.xarx.trix.domain.Notification;
import co.xarx.trix.services.notification.stubs.NotificationServerClientError;
import co.xarx.trix.services.notification.stubs.NotificationServerClientInactives;
import co.xarx.trix.services.notification.stubs.NotificationServerClientSuccess;
import co.xarx.trix.services.notification.util.DummyNotificationData;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static junit.framework.TestCase.assertEquals;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;

public class NotificationServiceTest {

	private NotificationService notificationService = new NotificationService();

	@Before
	public void setUp() throws Exception {
		notificationService = new NotificationService();
	}

	public void testSend(NotificationSender sender, DummyNotificationData dummy, String[] types) {
		List<Notification> notifications = notificationService.sendNotifications(sender, dummy.notification,
				dummy.post, dummy.devices, Notification.DeviceType.ANDROID);
		Set<String> devices = notifications.stream().map(Notification::getRegId).collect(Collectors.toSet());
		List<String> statuses = notifications.stream().map(Notification::getStatus).collect(Collectors.toList());

		assertEquals(notifications.size(), dummy.devices.size());
		assertThat(devices, contains(dummy.devices.toArray()));
		assertThat(statuses, contains(types));
	}

	@Test
	public void testSuccess() throws Exception {
		NotificationServerClient client = new NotificationServerClientSuccess();
		NotificationSender sender = new MobileNotificationSender(client, 1000);

		DummyNotificationData dummy = new DummyNotificationData(2);

		String[] types = {
				Notification.Status.SUCCESS.toString(),
				Notification.Status.SUCCESS.toString()
		};

		testSend(sender, dummy, types);
	}

	@Test
	public void testServerError() throws Exception {
		NotificationServerClient client = new NotificationServerClientInactives();
		NotificationSender sender = new MobileNotificationSender(client, 1000);

		DummyNotificationData dummy = new DummyNotificationData(3);

		String[] types = {
				Notification.Status.SUCCESS.toString(),
				Notification.Status.SERVER_ERROR.toString(),
				Notification.Status.SERVER_ERROR.toString()
		};

		testSend(sender, dummy, types);
	}

	@Test
	public void testSendError() throws Exception {
		NotificationServerClient client = new NotificationServerClientError();
		NotificationSender sender = new MobileNotificationSender(client, 1000);

		DummyNotificationData dummy = new DummyNotificationData(2);

		String[] types = {
				Notification.Status.SEND_ERROR.toString(),
				Notification.Status.SEND_ERROR.toString()
		};

		testSend(sender, dummy, types);
	}
}
