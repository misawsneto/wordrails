package co.xarx.trix.web.rest.resource.v2.form;

import lombok.Getter;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.Map;

@Getter
public class CreateNotificationForm {

	private CreateNotificationForm(){}

	public static CreateNotificationForm instant(Map<String, String> properties) {
		Assert.notNull(properties);

		CreateNotificationForm form = new CreateNotificationForm();
		form.message = properties.remove("message");
		form.title = properties.remove("title");

		try {
			form.personId =  Integer.valueOf(properties.remove("personId"));
		} catch (Exception ignored) {
		}

		Assert.hasText(form.message);
		Assert.hasText(form.title);

		form.properties = properties;

		return form;
	}

	public static CreateNotificationForm scheduled(Date scheduledDate, Map<String, String> properties) {
		Assert.notNull(properties);
		Assert.notNull(scheduledDate);

		CreateNotificationForm form = instant(properties);
		form.scheduledDate = scheduledDate;

		Assert.notNull(form.scheduledDate);
		if(form.scheduledDate.before(new Date()))
			throw new IllegalArgumentException("Date must be a future date");

		return form;
	}

	private String message;
	private String title;
	private Integer personId = 0;
	private Date scheduledDate;
	private Map<String, String> properties;
}
