package co.xarx.trix.domain.query;

import co.xarx.trix.domain.EventLog;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "event_log")
public class BookmarkEventLog extends EventLog {
	public BookmarkEventLog(){

	}
}
