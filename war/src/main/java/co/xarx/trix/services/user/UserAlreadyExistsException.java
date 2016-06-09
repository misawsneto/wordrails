package co.xarx.trix.services.user;

public class UserAlreadyExistsException extends Exception {

	private static final String MESSAGE = "User already exists";

	public UserAlreadyExistsException() {
		super(MESSAGE);
	}

	public UserAlreadyExistsException(Throwable t) {
		super(MESSAGE, t);
	}
}