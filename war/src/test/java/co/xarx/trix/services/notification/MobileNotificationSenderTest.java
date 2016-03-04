package co.xarx.trix.services.notification;

import co.xarx.trix.domain.Notification;
import co.xarx.trix.services.notification.stubs.NotificationServerClientError;
import co.xarx.trix.services.notification.stubs.NotificationServerClientInactives;
import co.xarx.trix.services.notification.stubs.NotificationServerClientSuccess;
import co.xarx.trix.services.notification.util.DummyNotificationData;
import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static junit.framework.TestCase.assertEquals;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class MobileNotificationSenderTest {

	@Test
	public void testSuccess() throws Exception {
		NotificationServerClientSuccess client = new NotificationServerClientSuccess();
		MobileNotificationSender sender = new MobileNotificationSender(client, 0);

		DummyNotificationData dummy = new DummyNotificationData(2);

		Map<String, NotificationResult> notifications = sender.sendMessageToDevices(dummy.notification, dummy.devices);

		String[] successTypes = {Notification.Status.SUCCESS.toString(), Notification.Status.SUCCESS.toString()};
		List<String> statuses = notifications.values().stream()
				.map((notificationResult) -> notificationResult.getStatus().toString()).collect(Collectors.toList());

		assertEquals(notifications.size(), dummy.devices.size());
		assertTrue(CollectionUtils.isEqualCollection(notifications.keySet(), dummy.devices));
		assertThat(statuses, contains(successTypes));
	}

	@Test(expected = IOException.class)
	public void testServerError() throws Exception {
		NotificationServerClientError client = new NotificationServerClientError();
		MobileNotificationSender sender = new MobileNotificationSender(client, 0);

		DummyNotificationData dummy = new DummyNotificationData(2);

		sender.sendMessageToDevices(dummy.notification, dummy.devices);
	}

	@Test
	public void testSendError() throws IOException {
		NotificationServerClientInactives client = new NotificationServerClientInactives();
		MobileNotificationSender sender = new MobileNotificationSender(client, 0);

		DummyNotificationData dummy = new DummyNotificationData(3);

		Map<String, NotificationResult> notifications = sender.sendMessageToDevices(dummy.notification, dummy.devices);

		Iterator it = dummy.devices.iterator();
		NotificationResult first = notifications.get(it.next());
		NotificationResult second = notifications.get(it.next());
		NotificationResult third = notifications.get(it.next());

		assertEquals(notifications.size(), dummy.devices.size());
		assertTrue(CollectionUtils.isEqualCollection(notifications.keySet(), dummy.devices));
		assertEquals(first.getStatus(), Notification.Status.SUCCESS);
		assertEquals(second.getStatus(), Notification.Status.SERVER_ERROR);
		assertEquals(third.getStatus(), Notification.Status.SERVER_ERROR);
		assertTrue(third.isDeviceDeactivated());
	}

}
