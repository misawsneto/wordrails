package co.xarx.trix.api.v2;

import co.xarx.trix.domain.PersonalNotification;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;


@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PersonalNotificationData implements Serializable {

	public static PersonalNotificationData of(PersonalNotification notification) {
		PersonalNotificationData data = new PersonalNotificationData();

		data.setId(notification.getId());
		data.setHash(notification.getRequest().getHash());
		data.setMessage(notification.getMessage());
		data.setTitle(notification.getTitle());
		data.setType(notification.getType().toString());
		data.setPersonId(notification.getPerson().getId());

		return data;
	}

	public Integer id;
	public String hash;
	public String message;
	public String title;
	public String type;
	public Integer personId;
}