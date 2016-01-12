package co.xarx.trix.domain;

public interface Loggable {

	Event build(String type, LogBuilder builder);
}
