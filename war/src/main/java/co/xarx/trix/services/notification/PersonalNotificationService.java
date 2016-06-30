package co.xarx.trix.services.notification;

import co.xarx.trix.domain.NotificationRequest;
import co.xarx.trix.domain.Person;
import co.xarx.trix.domain.PersonalNotification;
import co.xarx.trix.exception.NotificationException;
import co.xarx.trix.persistence.PersonalNotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PersonalNotificationService {

	private PersonalNotificationRepository personalNotificationRepository;

	@Autowired
	public PersonalNotificationService(PersonalNotificationRepository personalNotificationRepository) {
		this.personalNotificationRepository = personalNotificationRepository;
	}

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

			personalNotificationRepository.save(notification);

			notifications.add(notification);
		}

		return notifications;
	}
}
