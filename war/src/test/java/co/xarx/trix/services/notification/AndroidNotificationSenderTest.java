package co.xarx.trix.services.notification;

import com.google.android.gcm.server.*;
import com.google.common.collect.Lists;
import org.junit.Before;
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
import java.util.List;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.doThrow;
import static org.powermock.api.mockito.PowerMockito.mock;

@RunWith(PowerMockRunner.class)
@PrepareForTest({MulticastResult.class, Result.class})
public class AndroidNotificationSenderTest extends AbstractNotificationSenderTest {

	@Override
	NotificationSender sender() {
		return androidNS;
	}

	private AndroidNotificationSender androidNS;
	private Sender sender;
	private MulticastResult multicastResult;
	private Result rs;
	private Result re;
	private Result rd;

	@Before
	public void setUp() throws CertificateException, InterruptedException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, SQLException, IOException {
		sender = mock(Sender.class);
		androidNS = new AndroidNotificationSender(sender);

		multicastResult = mock(MulticastResult.class);

		rs = mock(Result.class);
		when(rs.getErrorCodeName()).thenReturn(null);
		when(rs.getMessageId()).thenReturn("");

		re = mock(Result.class);
		when(re.getErrorCodeName()).thenReturn(null);
		when(re.getMessageId()).thenReturn(null);

		rd = mock(Result.class);
		when(rd.getErrorCodeName()).thenReturn(Constants.ERROR_NOT_REGISTERED);
		when(rd.getMessageId()).thenReturn(null);

		when(sender.send(any(Message.class), anyListOf(String.class), anyInt())).thenReturn(multicastResult);
	}

	@Test
	public void testSuccess() throws Exception {
		List<Result> results = Lists.newArrayList(rs, rs);
		when(multicastResult.getResults()).thenReturn(results);

		super.testSuccess();
	}

	@Test(expected = IOException.class)
	public void testSendError() throws Exception {
		Exception e = new IOException();
		doThrow(e).when(sender).send(any(), anyList(), anyInt());

		DummyData dummy = super.getDummyData(3);

		sender().sendMessageToDevices(dummy.notification, dummy.devices);
	}

	@Test
	public void testServerError() throws Exception {
		List<Result> results = Lists.newArrayList(rs, re, rd);
		when(multicastResult.getResults()).thenReturn(results);

		super.testSuccessAndErrorStatus();
	}
}
