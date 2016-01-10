package co.xarx.trix.domain;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class Event extends BaseEntity {

	public static final String EVENT_CREATE = "CREATE";
	public static final String EVENT_UPDATE = "UPDATE";
	public static final String EVENT_DELETE = "DELETE";
	public static final String EVENT_READ = "READ";
	public static final String EVENT_COMMENT = "COMMENT";
	public static final String EVENT_UPDATE_COMMENT = "UPDATE_COMMENT";
	public static final String EVENT_REMOVE_COMMENT = "REMOVE_COMMENT";

	public static final String UNKOWN_DEVICE = "UNKOWN";


	@Column(name = "type")
	public String typeEvent;

	@Column(name = "station_id")
	public Integer stationId;

	public Integer getStationId() {
		return stationId;
	}

	public void setStationId(Integer stationId) {
		this.stationId = stationId;
	}

	public String getTypeEvent() {
		return typeEvent;
	}

	public void setTypeEvent(String typeEvent) {
		this.typeEvent = typeEvent;
	}
}
