package co.xarx.trix.converter;

import co.xarx.trix.api.NotificationView;
import co.xarx.trix.util.TrixUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.xarx.trix.domain.Notification;
import co.xarx.trix.persistence.NotificationRepository;

@Component
public class NotificationConverter extends AbstractConverter<Notification, NotificationView> {

	@Autowired NotificationRepository notificationRepository;
	@Autowired PostConverter postConverter;

	@Override
	public Notification convertFrom(NotificationView notificationView) {
		return notificationRepository.findOne(notificationView.id);
	}

	@Override
	public NotificationView convertTo(Notification notification) {
		NotificationView notificationView = new NotificationView();

		if(notification == null) return null;

		notificationView.id = notification.id;
		notificationView.hash = notification.hash;
		notificationView.imageMediumId = notification.post != null ? notification.post.imageMediumHash : null;
		notificationView.imageSmallId = notification.post != null ? notification.post.imageSmallHash : null;
		notificationView.message = notification.message;
		notificationView.networkId = notification.network != null ? notification.network.id : null;
		notificationView.networkName = notification.network != null ? notification.network.name : null;
		notificationView.personId = notification.person != null ? notification.person.id : null;
		notificationView.post = notification.post != null ? postConverter.convertTo(notification.post) : null;
		notificationView.postId = notification.post != null ? notification.post.id : null;
		notificationView.postTitle = notification.post != null ? notification.post.title : null;
		notificationView.postSnippet = notification.post != null ? TrixUtil.simpleSnippet(notification.post.body, 100) : null;
		notificationView.seen = notification.seen;
		notificationView.stationId = notification != null ? notification.station.id : null;
		notificationView.stationName = notification.station != null ? notification.station.name : null;
		notificationView.type = notification.type;
		
		return notificationView;
	}
}