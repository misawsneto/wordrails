package com.wordrails.api;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.wordrails.business.BadRequestException;
import com.wordrails.business.ConflictException;
import com.wordrails.business.NotImplementedException;
import com.wordrails.business.OperationNotSupportedException;
import com.wordrails.business.UnauthorizedException;
import com.wordrails.util.ErrorResource;
import com.wordrails.util.FieldErrorResource;

@ControllerAdvice
public class ExceptionHandlingControllerAdvice extends ResponseEntityExceptionHandler{
	@ExceptionHandler(UnauthorizedException.class)
	public ResponseEntity<String> handleUnauthorizedException(UnauthorizedException exception) {
		String stackTrace = ExceptionUtils.getStackTrace(exception);
		return new ResponseEntity<>(stackTrace, HttpStatus.FORBIDDEN); 
	}

	@ExceptionHandler(NotImplementedException.class)
	public ResponseEntity<String> handleNotImplementedException(NotImplementedException exception) {
		String stackTrace = ExceptionUtils.getStackTrace(exception);
		return new ResponseEntity<>(stackTrace, HttpStatus.NOT_IMPLEMENTED); 
	}

	@ExceptionHandler(OperationNotSupportedException.class)
	public ResponseEntity<String> handleOperationNotSupportedException(OperationNotSupportedException exception) {
		String stackTrace = ExceptionUtils.getStackTrace(exception);
		return new ResponseEntity<>(stackTrace, HttpStatus.METHOD_NOT_ALLOWED); 
	}

	@ExceptionHandler(ConflictException.class)
	public ResponseEntity<String> handleConflictException(ConflictException exception) {
		String stackTrace = ExceptionUtils.getStackTrace(exception);
		return new ResponseEntity<>(stackTrace, HttpStatus.CONFLICT); 
	}

	@ResponseStatus(value=HttpStatus.BAD_REQUEST, reason="IOException occured")
	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<Object> handleBadRequest(Exception e, WebRequest request){
		BadRequestException badRequest = (BadRequestException) e;
		List<FieldErrorResource> fieldErrorResources = new ArrayList<>();

		List<FieldError> fieldErrors = badRequest.errors;
		for (FieldError fieldError : fieldErrors) {
			FieldErrorResource fieldErrorResource = new FieldErrorResource();
			fieldErrorResource.setResource(fieldError.getObjectName());
			fieldErrorResource.setField(fieldError.getField());
			fieldErrorResource.setCode(fieldError.getCode());
			fieldErrorResource.setMessage(fieldError.getDefaultMessage());
			fieldErrorResources.add(fieldErrorResource);
		}

		ErrorResource error = new ErrorResource("InvalidRequest", badRequest.getMessage());
		error.setFieldErrors(fieldErrorResources);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		return handleExceptionInternal(e, error, headers, HttpStatus.BAD_REQUEST, request);
	}
}