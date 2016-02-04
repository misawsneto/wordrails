package co.xarx.trix.exception;

public class NotificationException extends RuntimeException {

	private static final long serialVersionUID = -1178396940693261330L;

	private static final String MESSAGE = "Unable to send notification";

	public NotificationException() {
		super(MESSAGE);
	}

	public NotificationException(String msg) {
		super(msg);
	}

	public NotificationException(String msg, Throwable t) {
		super(msg, t);
	}

	public NotificationException(Throwable t) {
		super(MESSAGE, t);
	}
}