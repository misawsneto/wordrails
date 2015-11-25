package co.xarx.trix.exception;

import java.util.ArrayList;
import java.util.List;

import org.springframework.validation.FieldError;

public class BadRequestException extends RuntimeException{
	
	private static final long serialVersionUID = 3494941457809130700L;

	private static final String MESSAGE = "Invalid Data";
	
	public List<FieldError> errors = new ArrayList<FieldError>(); 
	
	public BadRequestException() {
		super(MESSAGE);
	}

	public BadRequestException(String msg) {
		super(msg);
	}
	
	public BadRequestException(String msg, Throwable t) {
		super(msg, t);
	}
}