package co.xarx.trix.domain;

public class ConflictException extends Exception{

	private static final long serialVersionUID = -7025727186094626101L;

	private static final String MESSAGE = "Operação não permitida. Conflito";	
	
	public ConflictException() {
		super(MESSAGE);
	}

	public ConflictException(String msg) {
		super(msg);
	}
	
	public ConflictException(String msg, Throwable t) {
		super(msg, t);
	}
}