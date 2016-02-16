package co.xarx.trix.services.notification;

import com.google.common.collect.Lists;
import com.notnoop.apns.ApnsNotification;
import com.notnoop.apns.ApnsService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
	private ApnsService apnsClient;
	private ApnsNotification result;
	private ApnsNotification resultError;
	private ApnsNotification resultErrorDeactivated;

	@Before
	public void setUp() throws SQLException {
		apnsClient = mock(ApnsService.class);

		appleNS = spy(new AppleNotificationSender());
		doReturn(apnsClient).when(appleNS).getAPNsClient(anyBoolean());
		doReturn(new HashMap<String, Date>()).when(apnsClient).getInactiveDevices();

		result = mock(ApnsNotification.class);
		resultError = mock(ApnsNotification.class);
		resultErrorDeactivated = mock(ApnsNotification.class);
	}

	@Test
	public void testSuccess() throws Exception {
		Collection<ApnsNotification> results = Lists.newArrayList(result, result);
		doReturn(results).when(appleNS).send(any(), any(), any());

		super.testSuccess();
	}

	@Test(expected = IOException.class)
	public void testServerError() throws Exception {
		Exception e = new IOException();
		doThrow(e).when(appleNS).send(any(), any(), any());

		DummyData dummy = getDummyData(2);

		appleNS.sendMessageToDevices(dummy.notification, dummy.devices);
	}

	@Test
	public void testSendError() throws IOException {
		Collection<ApnsNotification> apnsNotifications = Lists.newArrayList(result, resultError, resultErrorDeactivated);

		Map<String, Date> deactivated = new HashMap<>();
		deactivated.put("device2", new Date());
		deactivated.put("device1", new Date());

		when(apnsClient.getInactiveDevices()).thenReturn(deactivated);

		doReturn(apnsNotifications)
				.when(appleNS).send(any(), any(), any());

		testSuccessAndErrorStatus();
	}

//	@Test
//	public void testReconnectSuccess() throws Exception {
//		Exception e = new ExecutionException(new ClientNotConnectedException());
//		doReturn(result)
//				.doThrow(e)
//				.doReturn(resultError)
//				.doThrow(e)
//				.doReturn(resultErrorDeactivated)
//				.when(appleNS).send(any(), any(), any());
//
//		testSuccessAndErrorStatus();
//	}

//	@Test(expected = IOException.class)
//	public void testAppleReconnectFail() throws Exception {
//		Exception e = new ExecutionException(new ClientNotConnectedException());
//		doThrow(e).when(appleNS).send(any(), any(), any());
//
//		when(apnsClient.getInactiveDevices()).thenThrow(NetworkIOException.class);
//
//		DummyData dummy = getDummyData(2);
//
//		appleNS.sendMessageToDevices(dummy.notification, dummy.devices);
//	}
}
