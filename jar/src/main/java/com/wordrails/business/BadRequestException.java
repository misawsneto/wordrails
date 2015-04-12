package com.wordrails.business;

public class BadRequestException extends RuntimeException {
	private static final long serialVersionUID = 3494941457809130700L;

	private static final String MESSAGE = "Acesso negado. Você não tem permissão para efetuar esta operação.";	
	
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