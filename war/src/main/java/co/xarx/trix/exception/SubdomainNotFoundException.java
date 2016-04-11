package co.xarx.trix.exception;

public class SubdomainNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1152018155705427122L;
	private static final String MESSAGE = "Subdomain not found";

	public SubdomainNotFoundException() {
		super(MESSAGE);
	}

	public SubdomainNotFoundException(String msg) {
		super(msg);
	}

	public SubdomainNotFoundException(String msg, Throwable t) {
		super(msg, t);
	}
}
