package co.xarx.trix.services.person;

public class PersonAlreadyExistsException extends Exception {

	private static final String MESSAGE = "User already exists";

	public PersonAlreadyExistsException() {
		super(MESSAGE);
	}

	public PersonAlreadyExistsException(String s) {
		super(s);
	}

	public PersonAlreadyExistsException(Throwable t) {
		super(MESSAGE, t);
	}
}