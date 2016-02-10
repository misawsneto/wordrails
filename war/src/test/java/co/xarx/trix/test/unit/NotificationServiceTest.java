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
//		AppleNotificationSender appleNotificationSender = Mock(AppleNotificationSender.class);
		appleCertificateRepository = PowerMockito.mock(AppleCertificateRepository.class);

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

		AndroidNotificationSender androidNotificationSender = new AndroidNotificationSender(sender);
		AppleNotificationSender appleNotificationSender = new AppleNotificationSender(appleCertificateRepository);

//		PushNotificationResponse<SimpleApnsPushNotification> response = new PushNotificationResponse<SimpleApnsPushNotification>() {
//			@Override
//			public SimpleApnsPushNotification getPushNotification() {
//				return null;
//			}
//
//			@Override
//			public boolean isAccepted() {
//				//This should be true for tests
//				return true;
//			}
//
//			@Override
//			public String getRejectionReason() {
//				return null;
//			}
//
//			@Override
//			public Date getTokenInvalidationTimestamp() {
//				return null;
//			}
//		};
//
//		Future<PushNotificationResponse<SimpleApnsPushNotification>> responseFuture = new Future<PushNotificationResponse<SimpleApnsPushNotification>>() {
//			@Override
//			public boolean isSuccess() {
//				return false;
//			}
//
//			@Override
//			public boolean isCancellable() {
//				return false;
//			}
//
//			@Override
//			public Throwable cause() {
//				return null;
//			}
//
//			@Override
//			public Future<PushNotificationResponse<SimpleApnsPushNotification>> addListener(GenericFutureListener<? extends Future<? super PushNotificationResponse<SimpleApnsPushNotification>>> genericFutureListener) {
//				return null;
//			}
//
//			@Override
//			public Future<PushNotificationResponse<SimpleApnsPushNotification>> addListeners(GenericFutureListener<? extends Future<? super PushNotificationResponse<SimpleApnsPushNotification>>>... genericFutureListeners) {
//				return null;
//			}
//
//			@Override
//			public Future<PushNotificationResponse<SimpleApnsPushNotification>> removeListener(GenericFutureListener<? extends Future<? super PushNotificationResponse<SimpleApnsPushNotification>>> genericFutureListener) {
//				return null;
//			}
//
//			@Override
//			public Future<PushNotificationResponse<SimpleApnsPushNotification>> removeListeners(GenericFutureListener<? extends Future<? super PushNotificationResponse<SimpleApnsPushNotification>>>... genericFutureListeners) {
//				return null;
//			}
//
//			@Override
//			public Future<PushNotificationResponse<SimpleApnsPushNotification>> sync() throws InterruptedException {
//				return null;
//			}
//
//			@Override
//			public Future<PushNotificationResponse<SimpleApnsPushNotification>> syncUninterruptibly() {
//				return null;
//			}
//
//			@Override
//			public Future<PushNotificationResponse<SimpleApnsPushNotification>> await() throws InterruptedException {
//				return null;
//			}
//
//			@Override
//			public Future<PushNotificationResponse<SimpleApnsPushNotification>> awaitUninterruptibly() {
//				return null;
//			}
//
//			@Override
//			public boolean await(long l, TimeUnit timeUnit) throws InterruptedException {
//				return false;
//			}
//
//			@Override
//			public boolean await(long l) throws InterruptedException {
//				return false;
//			}
//
//			@Override
//			public boolean awaitUninterruptibly(long l, TimeUnit timeUnit) {
//				return false;
//			}
//
//			@Override
//			public boolean awaitUninterruptibly(long l) {
//				return false;
//			}
//
//			@Override
//			public PushNotificationResponse<SimpleApnsPushNotification> getNow() {
//				return null;
//			}
//
//			@Override
//			public boolean cancel(boolean b) {
//				return false;
//			}
//
//			@Override
//			public boolean isCancelled() {
//				return false;
//			}
//
//			@Override
//			public boolean isDone() {
//				return false;
//			}
//
//			@Override
//			public PushNotificationResponse<SimpleApnsPushNotification> get() throws InterruptedException, ExecutionException {
//				return response;
//			}
//
//			@Override
//			public PushNotificationResponse<SimpleApnsPushNotification> get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
//				return null;
//			}
//		};
//
//		when(appleNotificationSender.getAPNsClient(anyBoolean())).thenReturn(apnsClient);
//		when(apnsClient.sendNotification(any())).thenReturn(responseFuture);

		this.notificationService = new NotificationService(androidNotificationSender, appleNotificationSender);
	}

	public void setUpSendError() throws Exception {
		Sender sender = PowerMockito.mock(Sender.class);

		MulticastResult result = PowerMockito.mock(MulticastResult.class);
		appleCertificateRepository = PowerMockito.mock(AppleCertificateRepository.class);

		AndroidNotificationSender androidNotificationSender = new AndroidNotificationSender(sender);
		AppleNotificationSender appleNotificationSender = new AppleNotificationSender(appleCertificateRepository);

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
