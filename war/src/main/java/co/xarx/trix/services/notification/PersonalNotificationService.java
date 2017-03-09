package co.xarx.trix.services.notification;

import co.xarx.trix.api.v2.PersonalNotificationData;
import co.xarx.trix.domain.NotificationRequest;
import co.xarx.trix.domain.Person;
import co.xarx.trix.domain.PersonalNotification;
import co.xarx.trix.exception.NotificationException;
import co.xarx.trix.persistence.PersonalNotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PersonalNotificationService {

	private PersonalNotificationRepository personalNotificationRepository;

	@Autowired
	public PersonalNotificationService(PersonalNotificationRepository personalNotificationRepository) {
		this.personalNotificationRepository = personalNotificationRepository;
	}

	public List<PersonalNotificationData> getNotifications(Integer personId) {
		List<PersonalNotification> notifications = personalNotificationRepository.findByPersonId(personId);

		if(notifications == null)
			return null;

		return notifications.stream().map(PersonalNotificationData::of).collect(Collectors.toList());
	}

	@Transactional
	public List<PersonalNotification> sendNotifications(List<Person> persons, NotificationRequest request) throws
			NotificationException {
		Assert.notNull(request);
		Assert.notNull(persons);

		List<PersonalNotification> notifications = new ArrayList<>();
		for (Person person : persons) {
			PersonalNotification notification = new PersonalNotification();
			notification.setTitle(request.getTitle());
			notification.setMessage(request.getMessage());
			notification.setPerson(person);
			notification.setSentAt(new Date());
			notification.setRequest(request);
			notification.setType(request.getType());
			notification.setPostId(request.getEntityId());

			personalNotificationRepository.save(notification);

			notifications.add(notification);
		}

		return notifications;
	}
}
