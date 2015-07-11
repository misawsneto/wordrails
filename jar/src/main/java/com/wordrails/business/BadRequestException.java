package com.wordrails.business;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ResponseStatus;

public class BadRequestException extends Exception {
	
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