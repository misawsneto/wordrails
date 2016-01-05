package co.xarx.trix.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@MappedSuperclass
public abstract class EventLog {

	public static final String EVENT_CREATE = "CREATE";
	public static final String EVENT_UPDATE = "UPDATE";
	public static final String EVENT_DELETE = "DELETE";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer id;

	@NotNull
	@Column
	public String entityName;

	@NotNull
	@Column
	public String eventName;

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}
}
