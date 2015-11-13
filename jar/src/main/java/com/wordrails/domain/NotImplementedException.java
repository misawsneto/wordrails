package com.wordrails.domain;

public class NotImplementedException extends RuntimeException {
	private static final long serialVersionUID = 3233326293313822599L;
	private static final String MESSAGE = "Funcionalidade não implementada";	
	
	public NotImplementedException() {
		super(MESSAGE);
	}

	public NotImplementedException(String msg) {
		super(msg);
	}
	
	public NotImplementedException(String msg, Throwable t) {
		super(msg, t);
	}
}
