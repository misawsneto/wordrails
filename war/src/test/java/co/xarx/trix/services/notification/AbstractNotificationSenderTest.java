package co.xarx.trix.services.notification;

import co.xarx.trix.TestArtifactsFactory;
import co.xarx.trix.api.NotificationView;
import co.xarx.trix.domain.Notification;
import co.xarx.trix.domain.Post;
import org.apache.commons.collections.CollectionUtils;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static junit.framework.TestCase.assertEquals;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public abstract class AbstractNotificationSenderTest {

	abstract NotificationSender sender();

	protected static class DummyData {
		public DummyData(Collection<String> devices, NotificationView notification, Post post) {
			this.devices = devices;
			this.notification = notification;
			this.post = post;
		}

		Collection<String> devices;
		NotificationView notification;
		Post post;
	}

	protected DummyData getDummyData(Integer numOfDevices) {
		List<String> devices = new ArrayList<>();
		for (int i = 0; i < numOfDevices; i++) {
			devices.add("device" + i);
		}

		NotificationView notification = TestArtifactsFactory.createNotification();
		Post post = TestArtifactsFactory.createPost();

		return new DummyData(devices, notification, post);
	}

	public void testSuccess() throws Exception {
		DummyData dummy = getDummyData(2);

		Map<String, NotificationResult> notifications = sender().sendMessageToDevices(dummy.notification, dummy.devices);

		String[] successTypes = {Notification.Status.SUCCESS.toString(), Notification.Status.SUCCESS.toString()};
		List<String> statuses = notifications.values().stream()
				.map((notificationResult) -> notificationResult.getStatus().toString()).collect(Collectors.toList());

		assertEquals(notifications.size(), dummy.devices.size());
		assertTrue(CollectionUtils.isEqualCollection(notifications.keySet(), dummy.devices));
		assertThat(statuses, contains(successTypes));
	}

	public void testSuccessAndErrorStatus() throws IOException {
		DummyData dummy = getDummyData(3);

		Map<String, NotificationResult> notifications = sender().sendMessageToDevices(dummy.notification, dummy.devices);

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
