package co.xarx.trix.services.notification;

import com.relayrides.pushy.apns.ApnsClient;
import com.relayrides.pushy.apns.ClientNotConnectedException;
import com.relayrides.pushy.apns.PushNotificationResponse;
import io.netty.util.concurrent.Future;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.sql.SQLException;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.*;

@RunWith(PowerMockRunner.class)
public class AppleNotificationSenderTest extends AbstractNotificationSenderTest {

	@Override
	NotificationSender sender() {
		return appleNS;
	}

	private AppleNotificationSender appleNS;
	private ApnsClient apnsClient;
	private PushNotificationResponse resultSuccess;
	private PushNotificationResponse resultError;
	private PushNotificationResponse resultErrorDeactivated;

	@Before
	public void setUp() throws CertificateException, InterruptedException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, SQLException, IOException {
		apnsClient = mock(ApnsClient.class);
		appleNS = spy(new AppleNotificationSender());
		doReturn(apnsClient).when(appleNS).getAPNsClient(anyBoolean());

		resultSuccess = mock(PushNotificationResponse.class);
		resultError = mock(PushNotificationResponse.class);
		resultErrorDeactivated = mock(PushNotificationResponse.class);

		when(resultSuccess.isAccepted()).thenReturn(true);
		when(resultError.isAccepted()).thenReturn(false);
		when(resultErrorDeactivated.isAccepted()).thenReturn(false);
		when(resultErrorDeactivated.getTokenInvalidationTimestamp()).thenReturn(new Date());

		when(apnsClient.getReconnectionFuture()).thenReturn(mock(Future.class));
	}

	@Test
	public void testSuccess() throws Exception {
		doReturn(resultSuccess).when(appleNS).get(any(), any());

		super.testSuccess();
	}

	@Test(expected = IOException.class)
	public void testSendError() throws Exception {
		Exception e = new InterruptedException();
		doThrow(e).when(appleNS).get(any(), any());

		DummyData dummy = getDummyData(2);

		appleNS.sendMessageToDevices(dummy.notification, dummy.devices);
	}

	@Test
	public void testServerError() throws Exception {
		doReturn(resultSuccess)
				.doReturn(resultError)
				.doReturn(resultErrorDeactivated)
				.when(appleNS).get(any(), any());

		testSuccessAndErrorStatus();
	}

	@Test
	public void testReconnectSuccess() throws Exception {
		Exception e = new ExecutionException(new ClientNotConnectedException());
		doReturn(resultSuccess)
				.doThrow(e)
				.doReturn(resultError)
				.doThrow(e)
				.doReturn(resultErrorDeactivated)
				.when(appleNS).get(any(), any());

		testSuccessAndErrorStatus();
	}

	@Test(expected = IOException.class)
	public void testAppleReconnectFail() throws Exception {
		Exception e = new ExecutionException(new ClientNotConnectedException());
		doThrow(e).when(appleNS).get(any(), any());

		when(apnsClient.getReconnectionFuture().await()).thenThrow(InterruptedException.class);

		DummyData dummy = getDummyData(2);

		appleNS.sendMessageToDevices(dummy.notification, dummy.devices);
	}
}
