package co.xarx.trix.services;

import co.xarx.trix.TestArtifactsFactory;
import co.xarx.trix.api.NotificationView;
import co.xarx.trix.domain.Notification;
import co.xarx.trix.domain.Post;
import co.xarx.trix.services.notification.NotificationResult;
import co.xarx.trix.services.notification.NotificationSender;
import co.xarx.trix.services.notification.NotificationService;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Result;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static junit.framework.TestCase.assertEquals;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({MulticastResult.class, Result.class})
public class NotificationServiceTest {

	private NotificationService notificationService;
	private NotificationSender notificationSender;
	private NotificationView notification;
	private Post post;
	private List<String> deviceCodes;

	@Before
	public void setUp() throws Exception {
		notificationService = new NotificationService();
		notificationSender = mock(NotificationSender.class);
		when(notificationSender.getBatchSize()).thenReturn(1000);
		notification = TestArtifactsFactory.createNotification();
		post = TestArtifactsFactory.createPost();
		deviceCodes = Lists.newArrayList("device1", "device2");
	}

	@Test
	public void testSuccess() throws Exception {
		NotificationResult nrs = new NotificationResult();
		nrs.setStatus(Notification.Status.SUCCESS);
		NotificationResult nre = new NotificationResult();
		nre.setStatus(Notification.Status.SERVER_ERROR);

		Map<String, NotificationResult> results = new HashMap<>();
		results.put(deviceCodes.get(0), nrs);
		results.put(deviceCodes.get(1), nre);

		when(notificationSender.sendMessageToDevices(notification, deviceCodes)).thenReturn(results);

		List<Notification> notifications = notificationService.sendNotifications(notificationSender, notification,
				post, deviceCodes, Notification.DeviceType.ANDROID);

		//a list with 10 Notification.Status.SUCCESS
		String[] successTypes = {Notification.Status.SUCCESS.toString(), Notification.Status.SERVER_ERROR.toString()};
		Set<String> devices = notifications.stream().map(Notification::getRegId).collect(Collectors.toSet());
		List<String> statuses = notifications.stream().map(Notification::getStatus).collect(Collectors.toList());

		assertEquals(notifications.size(), deviceCodes.size());
		assertThat(devices, contains(deviceCodes.toArray()));
		assertThat(statuses, contains(successTypes));
	}

	@Test
	public void testSendError() throws Exception {
		when(notificationSender.sendMessageToDevices(notification, deviceCodes)).thenThrow(IOException.class);

		List<Notification> notifications = notificationService.sendNotifications(notificationSender, notification,
				post, deviceCodes, Notification.DeviceType.ANDROID);

		String[] errorTypes = {Notification.Status.SEND_ERROR.toString(), Notification.Status.SEND_ERROR.toString()};
		Set<String> devices = notifications.stream().map(Notification::getRegId).collect(Collectors.toSet());
		List<String> statuses = notifications.stream().map(Notification::getStatus).collect(Collectors.toList());

		assertEquals(notifications.size(), deviceCodes.size());
		assertThat(devices, contains(deviceCodes.toArray()));
		assertThat(statuses, contains(errorTypes));
	}
}
