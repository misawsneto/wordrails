package co.xarx.trix.domain;

import javax.persistence.*;

@Entity
public abstract class Event extends BaseEntity {

	public static final String EVENT_CREATE = "CREATE";
	public static final String EVENT_UPDATE = "UPDATE";
	public static final String EVENT_DELETE = "DELETE";

	public String typeEvent;

	@Lob
	public String rawData;

	public String getTypeEvent() {
		return typeEvent;
	}

	public void setTypeEvent(String typeEvent) {
		this.typeEvent = typeEvent;
	}

	public String getRawData() {
		return rawData;
	}

	public void setRawData(String rawData) {
		this.rawData = rawData;
	}

	public abstract Event build();
}
