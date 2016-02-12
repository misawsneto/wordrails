package co.xarx.trix.services;

import co.xarx.trix.TestArtifactsFactory;
import co.xarx.trix.api.NotificationView;
import co.xarx.trix.domain.Post;
import co.xarx.trix.services.notification.NotificationService;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.List;

@RunWith(PowerMockRunner.class)
public class MobileServiceTest {

	@InjectMocks
	private MobileService mobileService;

	@Mock
	NotificationService notificationService;
	@Mock
	AsyncService asyncService;

	@Test
	public void testSendNotification() {
		Post post = TestArtifactsFactory.createPost();
		NotificationView notification = TestArtifactsFactory.createNotification();
		List<String> androidDeviceCodes = Lists.newArrayList("android1", "android2");
		List<String> appleDeviceCodes = Lists.newArrayList("apple1", "apple2");

		mobileService.sendNotifications(post, notification, androidDeviceCodes, appleDeviceCodes);
	}
}
