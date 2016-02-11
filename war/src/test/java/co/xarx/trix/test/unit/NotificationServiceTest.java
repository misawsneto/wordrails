package co.xarx.trix.test.unit;

import co.xarx.trix.api.NotificationView;
import co.xarx.trix.domain.Notification;
import co.xarx.trix.domain.Post;
import co.xarx.trix.persistence.AppleCertificateRepository;
import co.xarx.trix.services.notification.AndroidNotificationSender;
import co.xarx.trix.services.notification.AppleNotificationSender;
import co.xarx.trix.services.notification.NotificationService;
import co.xarx.trix.test.TestArtifactsFactory;
import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;
import com.relayrides.pushy.apns.ApnsClient;
import com.relayrides.pushy.apns.ApnsPushNotification;
import com.relayrides.pushy.apns.PushNotificationResponse;
import io.netty.util.concurrent.Future;
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
	private AppleCertificateRepository appleCertificateRepository;

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

		PushNotificationResponse pnr = PowerMockito.mock(PushNotificationResponse.class);
		when(pnr.isAccepted()).thenReturn(true);

		Future f = PowerMockito.mock(Future.class);

		ApnsClient apnsClient = PowerMockito.mock(ApnsClient.class);
		when(apnsClient.sendNotification(any(ApnsPushNotification.class))).thenReturn(f);

		AndroidNotificationSender androidNotificationSender = new AndroidNotificationSender(sender);
		AppleNotificationSender appleNotificationSender = PowerMockito.mock(AppleNotificationSender.class);
		when(appleNotificationSender.getAPNsClient(anyBoolean())).thenReturn(apnsClient);
		when(appleNotificationSender.sendMessageToDevices(any(NotificationView.class), anyList())).thenCallRealMethod();

		this.notificationService = new NotificationService(androidNotificationSender, appleNotificationSender);
	}

	public void setUpSendError() throws Exception {
		Sender sender = PowerMockito.mock(Sender.class);

		MulticastResult result = PowerMockito.mock(MulticastResult.class);

		AndroidNotificationSender androidNotificationSender = new AndroidNotificationSender(sender);
		AppleNotificationSender appleNotificationSender = new AppleNotificationSender(null);

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

		this.notificationService = new NotificationService(androidNotificationSender, appleNotificationSender);
	}

	public void setUpServerError() throws Exception {
		Sender sender = PowerMockito.mock(Sender.class);

		when(sender.send(any(Message.class), anyListOf(String.class), anyInt())).thenThrow(new IOException());

		AndroidNotificationSender androidNotificationSender = new AndroidNotificationSender(sender);
//		AppleNotificationSender appleNotificationSender = new AppleNotificationSender(appleCertificateRepository);
		AppleNotificationSender appleNotificationSender = PowerMockito.mock(AppleNotificationSender.class);

		this.notificationService = new NotificationService(androidNotificationSender, appleNotificationSender);
	}

	@Test
	public void testSuccessful() throws Exception {
		setUpSuccess();

		List<String> successTypes = new ArrayList<>();
		Set<String> devices = new HashSet<>();
		for (int i = 0; i < 1000; i++) {
			devices.add(i + "");
			successTypes.add(String.valueOf(Notification.Status.SUCCESS));
		}

		NotificationView notification = new NotificationView();
		notification.type = Notification.Type.POST_ADDED.toString();
		notification.message = "Dummy title";
		Post post = TestArtifactsFactory.createPost();

		List<Notification> androidNotifications = notificationService.sendAndroidNotifications(notification, post, devices);
//		List<Notification> appleNotifications = notificationService.sendAppleNotifications(notification, post, devices);

//
		List<String> notiAndroidDevices = new ArrayList<>();
		List<String> notiAppleDevices = new ArrayList<>();
//
		List<String> notiAndroidStatuses = new ArrayList<>();
//		List<String> notiAppleStatuses = new ArrayList<>();
//
		for (Notification n : androidNotifications) {
			notiAndroidDevices.add(n.regId);
			notiAndroidStatuses.add(n.status);
		}

//		for (Notification n : appleNotifications) {
//			notiAppleDevices.add(n.regId);
//			notiAppleStatuses.add(n.status);
//		}

		assertEquals(androidNotifications.size(), 1000);
//		assertEquals(appleNotifications.size(), 1000);

		assertThat(devices, IsIterableContainingInOrder.contains(notiAndroidDevices.toArray()));
//		assertThat(devices, IsIterableContainingInOrder.contains(notiAppleDevices.toArray()));

		assertThat(successTypes, IsIterableContainingInOrder.contains(notiAndroidStatuses.toArray()));
//		assertThat(successTypes, IsIterableContainingInOrder.contains(notiAppleDevices.toArray()));
	}


	@Test
	public void testSendError() throws Exception {
		setUpSendError();

		List<String> errorTypes = new ArrayList<>();
		List<String> successTypes = new ArrayList<>();
		Set<String> devicesError = new HashSet<>();
		Set<String> devicesSucess = new HashSet<>();
		Set<String> devices = new HashSet<>();

		for (int i = 0; i < 300; i++) {
			devicesError.add(i + "");
			errorTypes.add(String.valueOf(Notification.Status.SERVER_ERROR));
		}
		for (int i = 300; i < 1000; i++) {
			devicesSucess.add(i + "");
			successTypes.add(String.valueOf(Notification.Status.SUCCESS));
		}

		devices.addAll(devicesError);
		devices.addAll(devicesSucess);

		NotificationView notification = new NotificationView();
		notification.type = Notification.Type.POST_ADDED.toString();
		notification.message = "Dummy title";
		Post post = TestArtifactsFactory.createPost();

		List<Notification> notifications = notificationService.sendAndroidNotifications(notification, post, devices);
//		List<Notification> notifications = notificationService.sendAppleNotifications(notification, post, devices);

		List<String> notiDevices = new ArrayList<>();
		List<String> notiStatusesSucess = new ArrayList<>();
		List<String> notiStatusesError = new ArrayList<>();
		for (Notification n : notifications) {
			notiDevices.add(n.regId);
			if(n.status.equals(String.valueOf(Notification.Status.SERVER_ERROR)))
				notiStatusesError.add(String.valueOf(Notification.Status.SERVER_ERROR));
			else if(n.status.equals(String.valueOf(Notification.Status.SUCCESS)))
				notiStatusesSucess.add(String.valueOf(Notification.Status.SUCCESS));
		}

		assertEquals(notifications.size(), 1000);
		assertThat(devices, IsIterableContainingInOrder.contains(notiDevices.toArray()));
		assertThat(errorTypes, IsIterableContainingInOrder.contains(notiStatusesError.toArray()));
		assertThat(successTypes, IsIterableContainingInOrder.contains(notiStatusesSucess.toArray()));
	}


	@Test
	public void testServerError() throws Exception {
		setUpServerError();

		List<String> errorTypes = new ArrayList<>();
		Set<String> devices = new HashSet<>();
		for (int i = 0; i < 1000; i++) {
			devices.add(i + "");
			errorTypes.add(String.valueOf(Notification.Status.SERVER_ERROR));
		}

		List<String> appleErrorTypes = new ArrayList<>();
		Set<String> appleDevices = new HashSet<>();
		for (int i = 0; i < 1000; i++) {
			appleDevices.add(i + "");
			appleErrorTypes.add(String.valueOf(Notification.Status.SERVER_ERROR));
		}


		NotificationView notification = new NotificationView();
		notification.type = Notification.Type.POST_ADDED.toString();
		notification.message = "Dummy title";
		Post post = TestArtifactsFactory.createPost();


		List<Notification> androidNotifications = notificationService.sendAndroidNotifications(notification, post, devices);
//		List<Notification> appleNotifications= notificationService.sendAppleNotifications(notification, post, appleDevices);

		List<String> notiAndroifDevices = new ArrayList<>();
		List<String> notiAndroidStatuses = new ArrayList<>();
		for (Notification n : androidNotifications) {
			notiAndroifDevices.add(n.regId);
			notiAndroidStatuses.add(n.status);
		}

		List<String> notiAppleDevices = new ArrayList<>();
		List<String> notiAppleStatuses = new ArrayList<>();
		for (Notification n : androidNotifications) {
			notiAppleDevices.add(n.regId);
			notiAppleStatuses.add(n.status);
		}


		assertEquals(androidNotifications.size(), 1000);
//		assertEquals(appleNotifications.size(), 1000);

		assertThat(devices, IsIterableContainingInOrder.contains(notiAndroifDevices.toArray()));
		assertThat(errorTypes, IsIterableContainingInOrder.contains(notiAndroidStatuses.toArray()));
	}
}
