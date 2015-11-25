package co.xarx.trix.exception;

public class UnauthorizedException extends RuntimeException {
	private static final long serialVersionUID = 3494941457809130700L;

	private static final String MESSAGE = "Acesso negado. Você não tem permissão para efetuar esta operação.";	
	
	public UnauthorizedException() {
		super(MESSAGE);
	}

	public UnauthorizedException(String msg) {
		super(msg);
	}
	
	public UnauthorizedException(String msg, Throwable t) {
		super(msg, t);
	}
}