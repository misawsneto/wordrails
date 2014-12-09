package com.wordrails.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.wordrails.business.ConflictException;
import com.wordrails.business.NotImplementedException;
import com.wordrails.business.OperationNotSupportedException;
import com.wordrails.business.UnauthorizedException;

@ControllerAdvice
public class ExceptionHandlingControllerAdvice {
	@ExceptionHandler(UnauthorizedException.class)
	public ResponseEntity<String> handleUnauthorizedException(UnauthorizedException exception) {
		String stackTrace = ExceptionUtils.getStackTrace(exception);
		return new ResponseEntity<String>(stackTrace, HttpStatus.FORBIDDEN); 
	}

	@ExceptionHandler(NotImplementedException.class)
	public ResponseEntity<String> handleNotImplementedException(NotImplementedException exception) {
		String stackTrace = ExceptionUtils.getStackTrace(exception);
		return new ResponseEntity<String>(stackTrace, HttpStatus.NOT_IMPLEMENTED); 
	}
	
	@ExceptionHandler(OperationNotSupportedException.class)
	public ResponseEntity<String> handleOperationNotSupportedException(OperationNotSupportedException exception) {
		String stackTrace = ExceptionUtils.getStackTrace(exception);
		return new ResponseEntity<String>(stackTrace, HttpStatus.METHOD_NOT_ALLOWED); 
	}
	
	@ExceptionHandler(ConflictException.class)
	public ResponseEntity<String> handleConflictException(ConflictException exception) {
		String stackTrace = ExceptionUtils.getStackTrace(exception);
		return new ResponseEntity<String>(stackTrace, HttpStatus.CONFLICT); 
	}
}