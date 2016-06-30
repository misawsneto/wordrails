package co.xarx.trix.api.v2;

import co.xarx.trix.domain.PersonalNotification;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;


@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class NotificationData implements Serializable {

	public static NotificationData of(PersonalNotification notification) {
		NotificationData data = new NotificationData();

		data.setId(notification.getId());
//		data.setHash(notification.getHash());
		data.setMessage(notification.getMessage());
		data.setTitle(notification.getTitle());
//		data.setScheduledDate(notification.getScheduledDate());
		data.setProperties(notification.getProperties());

		return data;
	}

	public Integer id;
	public String hash;
	public String message;
	public String title;
	public Date scheduledDate;
	@JsonUnwrapped
	private Map<String, String> properties;
}