package co.xarx.trix.converter;

import co.xarx.trix.api.NotificationView;
import co.xarx.trix.config.multitenancy.TenantContextHolder;
import co.xarx.trix.domain.MobileNotification;
import co.xarx.trix.persistence.MobileNotificationRepository;
import co.xarx.trix.persistence.NetworkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NotificationConverter extends AbstractConverter<MobileNotification, NotificationView> {

	@Autowired
	NetworkRepository networkRepository;

	@Autowired
	MobileNotificationRepository mobileNotificationRepository;
	@Autowired PostConverter postConverter;

	@Override
	public MobileNotification convertFrom(NotificationView notificationView) {
		return mobileNotificationRepository.findOne(notificationView.id);
	}

	@Override
	public NotificationView convertTo(MobileNotification mobileNotification) {
		NotificationView notificationView = new NotificationView(mobileNotification.message, mobileNotification.message, mobileNotification.hash, mobileNotification.test);
		String tenantId = TenantContextHolder.getCurrentTenantId();

		notificationView.id = mobileNotification.id;
		notificationView.hash = mobileNotification.hash;
		notificationView.message = mobileNotification.message;
//		notificationView.post = mobileNotification.post != null ? postConverter.convertTo(mobileNotification.post) : null;
		notificationView.postId = mobileNotification.postId;
//		notificationView.postTitle = mobileNotification.post != null ? mobileNotification.post.title : null;
//		notificationView.postSnippet = mobileNotification.post != null ? StringUtil.simpleSnippet(mobileNotification.post.body) : null;
		notificationView.type = mobileNotification.type;
		
		return notificationView;
	}
}