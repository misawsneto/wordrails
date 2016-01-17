package co.xarx.trix.services;

import co.xarx.trix.config.multitenancy.TenantContextHolder;
import co.xarx.trix.domain.Notification;
import co.xarx.trix.domain.Post;
import co.xarx.trix.security.auth.TrixAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class MobileService {
	@Lazy
	@Autowired
	private AsyncService asyncService;
	@Autowired
	private TrixAuthenticationProvider authProvider;

	public void buildNotification(Post post) {
		Notification notification = new Notification();
		notification.type = Notification.Type.POST_ADDED.toString();
		notification.station = post.station;
		notification.post = post;
		notification.message = post.title;
		notification.person = authProvider.getLoggedPerson();

		try {
			if (post.station != null) {
				asyncService.notifyAndroid(TenantContextHolder.getCurrentNetworkId(), post.station.id, notification);
				asyncService.notifyApple(TenantContextHolder.getCurrentNetworkId(), post.station.id, notification);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
