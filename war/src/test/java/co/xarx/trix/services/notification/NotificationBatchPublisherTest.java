package co.xarx.trix.services.notification;

import co.xarx.trix.domain.MobileNotification;
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

public class NotificationBatchPublisherTest {

	private NotificationBatchPublisher notificationBatchPublisher = new NotificationBatchPublisher();

	@Before
	public void setUp() throws Exception {
		notificationBatchPublisher = new NotificationBatchPublisher();
	}

	public void testSend(NotificationSender sender, DummyNotificationData dummy, String[] types) {
		List<MobileNotification> mobileNotifications = notificationBatchPublisher.sendNotifications(sender, dummy.notification,
				dummy.devices, MobileNotification.DeviceType.ANDROID);
		Set<String> devices = mobileNotifications.stream().map(MobileNotification::getRegId).collect(Collectors.toSet());
		List<String> statuses = mobileNotifications.stream().map(MobileNotification::getStatus).collect(Collectors.toList());

		assertEquals(mobileNotifications.size(), dummy.devices.size());
		assertThat(devices, contains(dummy.devices.toArray()));
		assertThat(statuses, contains(types));
	}

	@Test
	public void testSuccess() throws Exception {
		NotificationServerClient client = new NotificationServerClientSuccess();
		NotificationSender sender = new MobileNotificationSender(client, 1000);

		DummyNotificationData dummy = new DummyNotificationData(2);

		String[] types = {
				MobileNotification.Status.SUCCESS.toString(),
				MobileNotification.Status.SUCCESS.toString()
		};

		testSend(sender, dummy, types);
	}

	@Test
	public void testServerError() throws Exception {
		NotificationServerClient client = new NotificationServerClientInactives();
		NotificationSender sender = new MobileNotificationSender(client, 1000);

		DummyNotificationData dummy = new DummyNotificationData(3);

		String[] types = {
				MobileNotification.Status.SUCCESS.toString(),
				MobileNotification.Status.SERVER_ERROR.toString(),
				MobileNotification.Status.SERVER_ERROR.toString()
		};

		testSend(sender, dummy, types);
	}

	@Test
	public void testSendError() throws Exception {
		NotificationServerClient client = new NotificationServerClientError();
		NotificationSender sender = new MobileNotificationSender(client, 1000);

		DummyNotificationData dummy = new DummyNotificationData(2);

		String[] types = {
				MobileNotification.Status.SEND_ERROR.toString(),
				MobileNotification.Status.SEND_ERROR.toString()
		};

		testSend(sender, dummy, types);
	}
}
