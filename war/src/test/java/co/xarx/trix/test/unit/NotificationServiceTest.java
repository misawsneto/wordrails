package co.xarx.trix.test.unit;

import co.xarx.trix.api.NotificationView;
import co.xarx.trix.domain.Notification;
import co.xarx.trix.domain.Post;
import co.xarx.trix.services.notification.AndroidNotificationSender;
import co.xarx.trix.services.notification.NotificationService;
import co.xarx.trix.test.TestArtifactsFactory;
import co.xarx.trix.util.Constants;
import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;
import org.hamcrest.collection.IsIterableContainingInOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({MulticastResult.class, Result.class})
public class NotificationServiceTest {

	private NotificationService notificationService;

	public void setUpSuccess() throws Exception {
		Sender sender = PowerMockito.mock(Sender.class);

		MulticastResult result = PowerMockito.mock(MulticastResult.class);

		List<Result> results = new ArrayList<>();
		for (int i = 0; i < 1000; i++) {
			Result r = PowerMockito.mock(Result.class);
			when(r.getErrorCodeName()).thenReturn(null);
			when(r.getMessageId()).thenReturn("");
			results.add(r);
		}

		when(result.getResults()).thenReturn(results);
		when(sender.send(any(Message.class), anyListOf(String.class), anyInt())).thenReturn(result);

		AndroidNotificationSender notificationSender = new AndroidNotificationSender(sender);

		this.notificationService = new NotificationService(notificationSender);
	}

	public void setUpGCMError() throws Exception {
		Sender sender = PowerMockito.mock(Sender.class);

		MulticastResult result = PowerMockito.mock(MulticastResult.class);

		List<Result> results = new ArrayList<>();
		for (int i = 0; i < 300; i++) {
			Result r = PowerMockito.mock(Result.class);
			when(r.getErrorCodeName()).thenReturn("");
			when(r.getMessageId()).thenReturn(null);
			results.add(r);
		}
		for (int i = 300; i < 1000; i++) {
			Result r = PowerMockito.mock(Result.class);
			when(r.getErrorCodeName()).thenReturn(null);
			when(r.getMessageId()).thenReturn("");
			results.add(r);
		}

		when(result.getResults()).thenReturn(results);
		when(sender.send(any(Message.class), anyListOf(String.class), anyInt())).thenReturn(result);

		this.notificationService = new NotificationService(sender);
	}

	public void setUpSendError() throws Exception {
		Sender sender = PowerMockito.mock(Sender.class);

		when(sender.send(any(Message.class), anyListOf(String.class), anyInt())).thenThrow(new IOException());

		this.notificationService = new NotificationService(sender);
	}

	@Test
	public void testSuccessful() throws Exception {
		setUpSuccess();

		List<String> successTypes = new ArrayList<>();
		Set<String> devices = new HashSet<>();
		for (int i = 0; i < 1000; i++) {
			devices.add(i + "");
			successTypes.add(Constants.Notification.STATUS_SUCCESS);
		}

		NotificationView notification = new NotificationView();
		notification.type = Notification.Type.POST_ADDED.toString();
		notification.message = "Dummy title";
		Post post = TestArtifactsFactory.createPost();


		List<Notification> notifications = notificationService.sendNotifications(notification, post, devices);
		List<String> notiDevices = new ArrayList<>();
		List<String> notiStatuses = new ArrayList<>();
		for (Notification n : notifications) {
			notiDevices.add(n.regId);
			notiStatuses.add(n.status);
		}



		assertEquals(notifications.size(), 1000);
		assertThat(devices, IsIterableContainingInOrder.contains(notiDevices.toArray()));
		assertThat(successTypes, IsIterableContainingInOrder.contains(notiStatuses.toArray()));
	}


	@Test
	public void testGCMError() throws Exception {
		setUpGCMError();

		List<String> errorTypes = new ArrayList<>();
		List<String> successTypes = new ArrayList<>();
		Set<String> devicesError = new HashSet<>();
		Set<String> devicesSucess = new HashSet<>();
		Set<String> devices = new HashSet<>();
		for (int i = 0; i < 300; i++) {
			devicesError.add(i + "");
			errorTypes.add(Constants.Notification.STATUS_GCM_ERROR);
		}
		for (int i = 300; i < 1000; i++) {
			devicesSucess.add(i + "");
			successTypes.add(Constants.Notification.STATUS_SUCCESS);
		}
		devices.addAll(devicesError);
		devices.addAll(devicesSucess);

		NotificationView notification = new NotificationView();
		notification.type = Notification.Type.POST_ADDED.toString();
		notification.message = "Dummy title";
		Post post = TestArtifactsFactory.createPost();

		List<Notification> notifications = notificationService.sendNotifications(notification, post, devices);

		List<String> notiDevices = new ArrayList<>();
		List<String> notiStatusesSucess = new ArrayList<>();
		List<String> notiStatusesError = new ArrayList<>();
		for (Notification n : notifications) {
			notiDevices.add(n.regId);
			if(n.status.equals(Constants.Notification.STATUS_GCM_ERROR))
				notiStatusesError.add(Constants.Notification.STATUS_GCM_ERROR);
			else if(n.status.equals(Constants.Notification.STATUS_SUCCESS))
				notiStatusesSucess.add(Constants.Notification.STATUS_SUCCESS);
		}

		assertEquals(notifications.size(), 1000);
		assertThat(devices, IsIterableContainingInOrder.contains(notiDevices.toArray()));
		assertThat(errorTypes, IsIterableContainingInOrder.contains(notiStatusesError.toArray()));
		assertThat(successTypes, IsIterableContainingInOrder.contains(notiStatusesSucess.toArray()));
	}


	@Test
	public void testSendError() throws Exception {
		setUpSendError();

		List<String> errorTypes = new ArrayList<>();
		Set<String> devices = new HashSet<>();
		for (int i = 0; i < 1000; i++) {
			devices.add(i + "");
			errorTypes.add(Constants.Notification.STATUS_SEND_ERROR);
		}

		NotificationView notification = new NotificationView();
		notification.type = Notification.Type.POST_ADDED.toString();
		notification.message = "Dummy title";
		Post post = TestArtifactsFactory.createPost();


		List<Notification> notifications = notificationService.sendNotifications(notification, post, devices);

		List<String> notiDevices = new ArrayList<>();
		List<String> notiStatuses = new ArrayList<>();
		for (Notification n : notifications) {
			notiDevices.add(n.regId);
			notiStatuses.add(n.status);
		}

		assertEquals(notifications.size(), 1000);
		assertThat(devices, IsIterableContainingInOrder.contains(notiDevices.toArray()));
		assertThat(errorTypes, IsIterableContainingInOrder.contains(notiStatuses.toArray()));
	}
}
