package co.xarx.trix.exception;

public class OperationNotSupportedException extends RuntimeException {
	private static final long serialVersionUID = 3494941457809130700L;

	private static final String MESSAGE = "Operação não permitida.";	
	
	public OperationNotSupportedException() {
		super(MESSAGE);
	}

	public OperationNotSupportedException(String msg) {
		super(msg);
	}
	
	public OperationNotSupportedException(String msg, Throwable t) {
		super(msg, t);
	}
}