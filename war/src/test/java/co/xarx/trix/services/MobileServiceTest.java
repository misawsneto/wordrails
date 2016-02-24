package co.xarx.trix.services;

import co.xarx.trix.TestArtifactsFactory;
import co.xarx.trix.api.NotificationView;
import co.xarx.trix.domain.Post;
import co.xarx.trix.services.notification.NotificationService;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
public class MobileServiceTest {

	private MobileService mobileService;

	private AsyncService asyncService;

	@Before
	public void setUp() throws Exception {
		NotificationService notificationService = mock(NotificationService.class);
		when(notificationService.getErrorNotifications(anyList(), any(), any(), any(), any())).thenCallRealMethod();

		asyncService = mock(AsyncService.class);
		mobileService = new MobileService(notificationService, asyncService, null, null, null);
	}

//	@Test
	public void testSendSuccessfullNotifications() {
		Post post = TestArtifactsFactory.createPost();
		NotificationView notification = TestArtifactsFactory.createNotification();
		List<String> androidDeviceCodes = Lists.newArrayList("android1", "android2");
		List<String> appleDeviceCodes = Lists.newArrayList("apple1", "apple2");

		mobileService.sendNotifications(post, notification, androidDeviceCodes, appleDeviceCodes);
	}
}
