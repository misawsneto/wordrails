package co.xarx.trix.config.exception;

import co.xarx.trix.api.v2.ErrorData;
import co.xarx.trix.exception.*;
import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Produces;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class SpringExceptionHandler extends ResponseEntityExceptionHandler {

	@Autowired
	@Qualifier("objectMapper")
	public ObjectMapper mapper;

	@ExceptionHandler(UnauthorizedException.class)
	public ResponseEntity<String> handleUnauthorizedException(UnauthorizedException exception) throws JsonProcessingException {
		String stackTrace = ExceptionUtils.getStackTrace(exception);

		String message = stackTrace != null && !stackTrace.isEmpty() ? stackTrace.replaceAll("\"", "\\\"") : "";

		String error = mapper.writeValueAsString(new ErrorData(exception.getClass() + " - " + message ));

		return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
	}

	@ExceptionHandler(AccessDeniedException.class)
	@Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	public ResponseEntity<String> accessDeniedException(AccessDeniedException exception) throws JsonProcessingException {
		String stackTrace = ExceptionUtils.getStackTrace(exception);
		String message = stackTrace != null && !stackTrace.isEmpty() ? stackTrace.replaceAll("\"", "\\\"") : "";

		String error = mapper.writeValueAsString(new ErrorData(exception.getClass() + " - " + message ));
		return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
	}

	@ExceptionHandler(NotImplementedException.class)
	public ResponseEntity<String> handleNotImplementedException(NotImplementedException exception) throws JsonProcessingException {
		String stackTrace = ExceptionUtils.getStackTrace(exception);

		String message = stackTrace != null && !stackTrace.isEmpty() ? stackTrace.replaceAll("\"", "\\\"") : "";

		String error = mapper.writeValueAsString(new ErrorData(exception.getClass() + " - " + message ));

		return new ResponseEntity<>(error, HttpStatus.NOT_IMPLEMENTED);
	}

	@ExceptionHandler(OperationNotSupportedException.class)
	public ResponseEntity<String> handleOperationNotSupportedException(OperationNotSupportedException exception) throws JsonProcessingException {
		String stackTrace = ExceptionUtils.getStackTrace(exception);

		String message = stackTrace != null && !stackTrace.isEmpty() ? stackTrace.replaceAll("\"", "\\\"") : "";

		String error = mapper.writeValueAsString(new ErrorData(exception.getClass() + " - " + message ));

		return new ResponseEntity<>(error, HttpStatus.METHOD_NOT_ALLOWED);
	}

	@ExceptionHandler(AmazonServiceException.class)
	public ResponseEntity<String> handleAmazonServiceException(AmazonServiceException exception) {
		String stackTrace = ExceptionUtils.getStackTrace(exception);
		System.out.println("Caught an AmazonServiceException, " +
				"which means your request made it " +
				"to Amazon S3, but was rejected with an error response " +
				"for some reason.");
		System.out.println("Error Message: " + exception.getMessage());
		System.out.println("HTTP  Code: "    + exception.getStatusCode());
		System.out.println("AWS Error Code:" + exception.getErrorCode());
		System.out.println("Error Type:    " + exception.getErrorType());
		System.out.println("Request ID:    " + exception.getRequestId());
		return new ResponseEntity<>(stackTrace, HttpStatus.METHOD_NOT_ALLOWED);
	}

	@ExceptionHandler(AmazonClientException.class)
	public ResponseEntity<String> handleAmazonClientException(AmazonClientException exception) {
		String stackTrace = ExceptionUtils.getStackTrace(exception);
		System.out.println("Caught an AmazonClientException, " +
				"which means the client encountered " +
				"an internal error while trying to communicate" +
				" with S3, " +
				"such as not being able to access the network.");
		System.out.println("Error Message: " + exception.getMessage());
		return new ResponseEntity<>(stackTrace, HttpStatus.METHOD_NOT_ALLOWED);
	}

	@ExceptionHandler(ConflictException.class)
	public ResponseEntity<String> handleConflictException(ConflictException exception) throws JsonProcessingException {
		String stackTrace = ExceptionUtils.getStackTrace(exception);

		String message = stackTrace != null && !stackTrace.isEmpty() ? stackTrace.replaceAll("\"", "\\\"") : "";

		String error = mapper.writeValueAsString(new ErrorData(exception.getClass() + " - " + message ));

		return new ResponseEntity<>(error, HttpStatus.CONFLICT);
	}

	@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "IOException occured")
	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<Object> handleBadRequest(Exception e, WebRequest request) {
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


	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleAnyException(HttpServletRequest req, WebRequest request, Exception exception) {
		logger.error("Request: " + req.getRequestURL() + " raised " + ExceptionUtils.getStackTrace(exception));

		ErrorResource error = new ErrorResource("Generic Exception", ExceptionUtils.getRootCauseMessage(exception));
		error.setStacktrace(ExceptionUtils.getStackTrace(exception));
		error.setType(exception.toString());

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		return handleExceptionInternal(exception, error, headers, HttpStatus.INTERNAL_SERVER_ERROR, request);
	}
}