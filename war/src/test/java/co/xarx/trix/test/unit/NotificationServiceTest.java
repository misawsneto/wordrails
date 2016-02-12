package co.xarx.trix.test.unit;

import co.xarx.trix.api.NotificationView;
import co.xarx.trix.domain.Notification;
import co.xarx.trix.domain.Post;
import co.xarx.trix.services.notification.AndroidNotificationSender;
import co.xarx.trix.services.notification.AppleNotificationSender;
import co.xarx.trix.services.notification.NotificationService;
import co.xarx.trix.test.TestArtifactsFactory;
import com.google.android.gcm.server.*;
import com.google.common.collect.Lists;
import com.relayrides.pushy.apns.ApnsClient;
import com.relayrides.pushy.apns.ApnsPushNotification;
import com.relayrides.pushy.apns.PushNotificationResponse;
import com.relayrides.pushy.apns.util.SimpleApnsPushNotification;
import io.netty.util.concurrent.Future;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static junit.framework.TestCase.assertEquals;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({MulticastResult.class, Result.class})
public class NotificationServiceTest {

	private static class DummyData {
		public DummyData(Collection<String> devices, NotificationView notification, Post post) {
			this.devices = devices;
			this.notification = notification;
			this.post = post;
		}

		Collection<String> devices;
		NotificationView notification;
		Post post;
	}

	private DummyData getDummyData(Integer numOfDevices) {
		List<String> devices = new ArrayList<>();
		for (int i = 0; i < numOfDevices; i++) {
			devices.add("device" + i);
		}

		NotificationView notification = new NotificationView();
		notification.type = Notification.Type.POST_ADDED.toString();
		notification.message = "Dummy title";
		Post post = TestArtifactsFactory.createPost();

		return new DummyData(devices, notification, post);
	}

	public void testSuccess(Collection<String> devices, Callable<List<Notification>> callable) throws Exception {

		List<Notification> notifications = callable.call();

		//a list with 10 Notification.Status.SUCCESS
		String[] successTypes = {Notification.Status.SUCCESS.toString(), Notification.Status.SUCCESS.toString()};
		Set<String> deviceCodes = notifications.stream().map(Notification::getRegId).collect(Collectors.toSet());
		List<String> statuses = notifications.stream().map(Notification::getStatus).collect(Collectors.toList());

		assertEquals(notifications.size(), 2);
		assertThat(devices, contains(deviceCodes.toArray()));
		assertThat(statuses, contains(successTypes));
	}

	public void testServerError(Collection<String> devices, Callable<List<Notification>> callable) throws Exception {
		List<Notification> notifications = callable.call();

		Set<String> deviceCodes = notifications.stream().map(Notification::getRegId).collect(Collectors.toSet());
		Notification first = notifications.get(0);
		Notification second = notifications.get(1);
		Notification third = notifications.get(2);

		assertEquals(notifications.size(), 3);
		assertThat(devices, contains(deviceCodes.toArray()));
		assertEquals(first.getStatus(), Notification.Status.SUCCESS.toString());
		assertEquals(second.getStatus(), Notification.Status.SERVER_ERROR.toString());
		assertEquals(third.getStatus(), Notification.Status.SERVER_ERROR.toString());
		assertTrue(third.deviceDeactivated);
	}

	public void testSendError(Collection<String> devices, Callable<List<Notification>> callable) throws Exception {
		List<Notification> notifications = callable.call();

		String[] errorTypes = {Notification.Status.SEND_ERROR.toString(), Notification.Status.SEND_ERROR.toString()};
		Set<String> deviceCodes = notifications.stream().map(Notification::getRegId).collect(Collectors.toSet());
		List<String> statuses = notifications.stream().map(Notification::getStatus).collect(Collectors.toList());

		assertEquals(notifications.size(), 2);
		assertThat(devices, contains(deviceCodes.toArray()));
		assertThat(statuses, contains(errorTypes));
	}

	//------------------------------------ TEST SUCCESS APPLE ------------------------------------

	public NotificationService setUpSuccessApple() throws ExecutionException, InterruptedException, CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, SQLException, IOException {
		PushNotificationResponse pnr = mock(PushNotificationResponse.class);
		AppleNotificationSender appleNotificationSender = spy(AppleNotificationSender.class);

		when(pnr.isAccepted()).thenReturn(true);

		Future f = mock(Future.class);
		when(f.get()).thenReturn(pnr);

		ApnsClient apnsClient = mock(ApnsClient.class);
		when(apnsClient.sendNotification(any(ApnsPushNotification.class))).thenReturn(f);

		doReturn(apnsClient).when(appleNotificationSender).getAPNsClient(anyBoolean());
		doCallRealMethod().when(appleNotificationSender).sendMessageToDevices(any(NotificationView.class), anyList());

		return new NotificationService(mock(AndroidNotificationSender.class), appleNotificationSender);
	}

	@Test
	public void testSuccessApple() throws Exception {
		NotificationService notificationService = setUpSuccessApple();

		DummyData dummy = getDummyData(2);

		testSuccess(dummy.devices, () -> notificationService.sendAppleNotifications(dummy.notification, dummy.post, dummy.devices));

	}

	//------------------------------------ TEST SUCCESS ANDROID ------------------------------------

	public NotificationService setUpSuccessAndroid() throws Exception {
		Sender sender = mock(Sender.class);

		MulticastResult result = mock(MulticastResult.class);

		Result r = mock(Result.class);
		when(r.getErrorCodeName()).thenReturn(null);
		when(r.getMessageId()).thenReturn("");

		List<Result> results = Lists.newArrayList(r, r);

		when(result.getResults()).thenReturn(results);
		when(sender.send(any(Message.class), anyListOf(String.class), anyInt())).thenReturn(result);

		AndroidNotificationSender androidNotificationSender = new AndroidNotificationSender(sender);
		AppleNotificationSender appleNotificationSender = mock(AppleNotificationSender.class);

		return new NotificationService(androidNotificationSender, appleNotificationSender);
	}

	@Test
	public void testSuccessfulAndroid() throws Exception {
		NotificationService notificationService = setUpSuccessAndroid();

		DummyData dummy = getDummyData(2);

		testSuccess(dummy.devices, () -> notificationService.sendAndroidNotifications(dummy.notification, dummy.post, dummy.devices));
	}

	//------------------------------------ TEST SERVER ERROR APPLE ------------------------------------

	public NotificationService setUpServerErrorApple() throws Exception {
		PushNotificationResponse successPnr = mock(PushNotificationResponse.class);
		PushNotificationResponse errorPnr = mock(PushNotificationResponse.class);
		PushNotificationResponse errorDeactivatedPnr = mock(PushNotificationResponse.class);
		AppleNotificationSender appleNotificationSender = spy(AppleNotificationSender.class);

		when(successPnr.isAccepted()).thenReturn(true);
		when(errorPnr.isAccepted()).thenReturn(false);
		when(errorDeactivatedPnr.isAccepted()).thenReturn(false);
		when(errorDeactivatedPnr.getTokenInvalidationTimestamp()).thenReturn(new Date());

		Future<PushNotificationResponse<SimpleApnsPushNotification>> f = mock(Future.class);
		when(f.get()).thenReturn(successPnr).thenReturn(errorPnr).thenReturn(errorDeactivatedPnr);

		ApnsClient apnsClient = mock(ApnsClient.class);
		when(apnsClient.sendNotification(any(ApnsPushNotification.class))).thenReturn(f);

		doReturn(apnsClient).when(appleNotificationSender).getAPNsClient(anyBoolean());
		doCallRealMethod().when(appleNotificationSender).sendMessageToDevices(any(NotificationView.class), anyList());

		return new NotificationService(mock(AndroidNotificationSender.class), appleNotificationSender);
	}


	@Test
	public void testServerErrorApple() throws Exception {
		NotificationService notificationService = setUpServerErrorApple();

		DummyData dummy = getDummyData(3);

		testServerError(dummy.devices, () ->
				notificationService.sendAppleNotifications(dummy.notification, dummy.post, dummy.devices));
	}

	//------------------------------------ TEST SERVER ERROR ANDROID ------------------------------------

	public NotificationService setUpServerErrorAndroid() throws Exception {
		Sender sender = mock(Sender.class);

		MulticastResult result = mock(MulticastResult.class);

		AndroidNotificationSender androidNotificationSender = new AndroidNotificationSender(sender);
		AppleNotificationSender appleNotificationSender = mock(AppleNotificationSender.class);

		Result rs = mock(Result.class);
		when(rs.getErrorCodeName()).thenReturn(null);
		when(rs.getMessageId()).thenReturn("");

		Result re = mock(Result.class);
		when(re.getErrorCodeName()).thenReturn(null);
		when(re.getMessageId()).thenReturn(null);

		Result rd = mock(Result.class);
		when(rd.getErrorCodeName()).thenReturn(Constants.ERROR_NOT_REGISTERED);
		when(rd.getMessageId()).thenReturn(null);

		List<Result> results = Lists.newArrayList(rs, re, rd); //success, error and error with device deactivated

		when(result.getResults()).thenReturn(results);
		when(sender.send(any(Message.class), anyListOf(String.class), anyInt())).thenReturn(result);

		return new NotificationService(androidNotificationSender, appleNotificationSender);
	}


	@Test
	public void testServerErrorAndroid() throws Exception {
		NotificationService notificationService = setUpServerErrorAndroid();

		DummyData dummy = getDummyData(3);

		testServerError(dummy.devices, () ->
				notificationService.sendAndroidNotifications(dummy.notification, dummy.post, dummy.devices));
	}

	//------------------------------------ TEST SEND ERROR APPLE ------------------------------------

	public NotificationService setUpSendErrorApple() throws Exception {
		ApnsClient apnsClient = mock(ApnsClient.class);
		when(apnsClient.sendNotification(any(ApnsPushNotification.class))).thenThrow(InterruptedException.class);

		AppleNotificationSender appleNotificationSender = spy(AppleNotificationSender.class);
		doReturn(apnsClient).when(appleNotificationSender).getAPNsClient(anyBoolean());
		doCallRealMethod().when(appleNotificationSender).sendMessageToDevices(any(NotificationView.class), anyList());

		return new NotificationService(mock(AndroidNotificationSender.class), appleNotificationSender);
	}

	@Test
	public void testSendErrorApple() throws Exception {
		NotificationService notificationService = setUpSendErrorApple();

		DummyData dummy = getDummyData(2);

		testSendError(dummy.devices, () ->
				notificationService.sendAppleNotifications(dummy.notification, dummy.post, dummy.devices));
	}

	//------------------------------------ TEST SEND ERROR ANDROID ------------------------------------

	public NotificationService setUpSendErrorAndroid() throws Exception {
		Sender sender = mock(Sender.class);

		when(sender.send(any(Message.class), anyListOf(String.class), anyInt())).thenThrow(new IOException());

		AndroidNotificationSender androidNotificationSender = new AndroidNotificationSender(sender);
		AppleNotificationSender appleNotificationSender = mock(AppleNotificationSender.class);

		return new NotificationService(androidNotificationSender, appleNotificationSender);
	}

	@Test
	public void testSendErrorAndroid() throws Exception {
		NotificationService notificationService = setUpSendErrorAndroid();

		DummyData dummy = getDummyData(2);

		testSendError(dummy.devices, () ->
				notificationService.sendAndroidNotifications(dummy.notification, dummy.post, dummy.devices));
	}
}
