package co.xarx.trix.exception;

import org.springframework.core.NestedRuntimeException;

public class TargetLookupFailureException extends NestedRuntimeException {

	private static final long serialVersionUID = -1645374642832222666L;

	public TargetLookupFailureException(final String message) {
		super(message);
	}

	public TargetLookupFailureException(final String message, final Throwable cause) {
		super(message, cause);
	}

}