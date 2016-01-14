package co.xarx.trix.domain;

import co.xarx.trix.domain.event.Event;

public interface Loggable {

	Event build(String type, LogBuilder builder);
}
