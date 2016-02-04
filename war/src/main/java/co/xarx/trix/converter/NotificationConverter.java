package co.xarx.trix.converter;

import co.xarx.trix.api.NotificationView;
import co.xarx.trix.config.multitenancy.TenantContextHolder;
import co.xarx.trix.domain.Network;
import co.xarx.trix.domain.Notification;
import co.xarx.trix.persistence.NetworkRepository;
import co.xarx.trix.persistence.NotificationRepository;
import co.xarx.trix.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NotificationConverter extends AbstractConverter<Notification, NotificationView> {

	@Autowired
	NetworkRepository networkRepository;

	@Autowired NotificationRepository notificationRepository;
	@Autowired PostConverter postConverter;

	@Override
	public Notification convertFrom(NotificationView notificationView) {
		return notificationRepository.findOne(notificationView.id);
	}

	@Override
	public NotificationView convertTo(Notification notification) {
		NotificationView notificationView = new NotificationView();
		String tenantId = TenantContextHolder.getCurrentTenantId();
		Network network = networkRepository.findByTenantId(tenantId);

		notificationView.id = notification.id;
		notificationView.hash = notification.hash;
		notificationView.message = notification.message;
		notificationView.networkId = network != null ? network.id : null;
		notificationView.post = notification.post != null ? postConverter.convertTo(notification.post) : null;
		notificationView.postId = notification.post != null ? notification.post.id : null;
		notificationView.postTitle = notification.post != null ? notification.post.title : null;
		notificationView.postSnippet = notification.post != null ? StringUtil.simpleSnippet(notification.post.body) : null;
		notificationView.type = notification.type;
		
		return notificationView;
	}
}