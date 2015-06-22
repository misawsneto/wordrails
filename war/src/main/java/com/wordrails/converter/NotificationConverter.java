package com.wordrails.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wordrails.api.NotificationView;
import com.wordrails.business.Notification;
import com.wordrails.persistence.NotificationRepository;
import com.wordrails.util.WordrailsUtil;

@Component
public class NotificationConverter extends AbstractConverter<Notification, NotificationView> {

	@Autowired NotificationRepository notificationRepository;
	@Autowired PostConverter postConverter;

	@Override
	public Notification convertToEntity(NotificationView notificationView) {
		return notificationRepository.findOne(notificationView.id);
	}

	@Override
	public NotificationView convertToView(Notification notification) {
		NotificationView notificationView = new NotificationView();
		notificationView.id = notification.id;
		notificationView.hash = notification.hash;
		notificationView.imageMediumId = notification.post.imageMediumId;
		notificationView.imageSmallId = notification.post.imageSmallId;
		notificationView.message = notification.message;
		notificationView.networkId = notification.network != null ? notification.network.id : null; 
		notificationView.networkName = notification.network != null ? notification.network.name : null;
		notificationView.personId = notification.person != null ? notification.person.id : null;
		notificationView.post = notification.post != null ? postConverter.convertToView(notification.post) : null;
		notificationView.postId = notification.post.id;
		notificationView.postTitle = notification.post.title;
		notificationView.postSnippet = WordrailsUtil.simpleSnippet(notification.post.body, 100);
		notificationView.seen = notification.seen;
		notificationView.stationId = notification != null ? notification.station.id : null;
		notificationView.stationName = notification.station != null ? notification.station.name : null;
		notificationView.type = notification.type;
		
		return notificationView;
	}
}