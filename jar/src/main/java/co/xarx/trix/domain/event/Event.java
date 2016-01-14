package co.xarx.trix.domain.event;

import co.xarx.trix.domain.BaseEntity;

import javax.persistence.*;

@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn
		(
				name="discriminator",
				discriminatorType=DiscriminatorType.STRING
		)
public abstract class Event extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer id;

	@Override
	public Integer getId() {
		return id;
	}

	public static final String EVENT_SAVE = "SAVE";
	public static final String EVENT_DELETE = "DELETE";
	public static final String EVENT_READ = "READ";
	public static final String EVENT_COMMENT = "COMMENT";
	public static final String EVENT_UPDATE_COMMENT = "UPDATE_COMMENT";
	public static final String EVENT_REMOVE_COMMENT = "REMOVE_COMMENT";
	public static final String EVENT_RECOMMEND = "RECOMMEND";
	public static final String EVENT_UNRECOMMEND = "UNRECOMMEND";

	public static final String UNKOWN_DEVICE = "UNKOWN";
	public static final String ANDROID_DEVICE = "ANDROID";
	public static final String APPLE_DEVICE = "IOS";
	public static final String BROWSER_DEVICE = "BROWSER";

	public String sessionId;

	public String device;

	public Integer userId;

	public String eventType;

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public void setAuthor(Author author) {
		this.userId = author.userId;
		this.sessionId = author.sessionId;
		this.device = author.device;
	}
}
