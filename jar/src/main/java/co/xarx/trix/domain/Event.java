package co.xarx.trix.domain;

import javax.persistence.*;

@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn
		(
				name="class",
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

	@Column(name = "session_id")
	public String sessionId;

	@Column(name = "device")
	public String device;

	@Column(name = "user_id")
	public Integer userId;

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getDevice() {
		return device;
	}

	public void setDevice(String device) {
		this.device = device;
	}

	@Column(name = "type")
	public String typeEvent;

	public String getTypeEvent() {
		return typeEvent;
	}

	public void setTypeEvent(String typeEvent) {
		this.typeEvent = typeEvent;
	}
}
