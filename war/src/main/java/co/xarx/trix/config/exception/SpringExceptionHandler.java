package co.xarx.trix.config.exception;

import co.xarx.trix.api.v2.ErrorData;
import co.xarx.trix.config.multitenancy.TenantContextHolder;
import co.xarx.trix.exception.*;
import co.xarx.trix.services.analytics.RequestWrapper;
import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
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

	static Logger log = Logger.getLogger(ResteasyExceptionHandler.class.getName());

	@Autowired
	@Qualifier("objectMapper")
	public ObjectMapper mapper;

	@Autowired
	private HttpServletRequest request;

//	@Autowired
//	private SlackBot slackBot;

	@ExceptionHandler(SubdomainNotFoundException.class)
	public ResponseEntity<Object> handleSubdomainNotFoundException(SubdomainNotFoundException e, WebRequest req) throws
			JsonProcessingException {
		String stackTrace = ExceptionUtils.getStackTrace(e);

		String message = stackTrace != null && !stackTrace.isEmpty() ? stackTrace.replaceAll("\"", "\\\"") : "";

		String error = mapper.writeValueAsString(new ErrorData(e.getClass() + " - " + message));

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		logError(e);
		return handleExceptionInternal(e, error, headers, HttpStatus.NOT_FOUND, req);
	}

	@ExceptionHandler(UnauthorizedException.class)
	public ResponseEntity<Object> handleUnauthorizedException(UnauthorizedException e, WebRequest req) throws
			JsonProcessingException {
		String stackTrace = ExceptionUtils.getStackTrace(e);

		String message = stackTrace != null && !stackTrace.isEmpty() ? stackTrace.replaceAll("\"", "\\\"") : "";

		String error = mapper.writeValueAsString(new ErrorData(e.getClass() + " - " + message));

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		logError(e);
		return handleExceptionInternal(e, error, headers, HttpStatus.METHOD_NOT_ALLOWED, req);
	}

	@ExceptionHandler(AccessDeniedException.class)
	@Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	public ResponseEntity<Object> accessDeniedException(AccessDeniedException e, WebRequest req) throws
			JsonProcessingException {
		String stackTrace = ExceptionUtils.getStackTrace(e);
		String message = stackTrace != null && !stackTrace.isEmpty() ? stackTrace.replaceAll("\"", "\\\"") : "";

		String error = mapper.writeValueAsString(new ErrorData(e.getClass() + " - " + message));

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		logError(e);
		return handleExceptionInternal(e, error, headers, HttpStatus.FORBIDDEN, req);
	}

	@ExceptionHandler(NotImplementedException.class)
	public ResponseEntity<Object> handleNotImplementedException(NotImplementedException e, WebRequest req) throws
			JsonProcessingException {
		String stackTrace = ExceptionUtils.getStackTrace(e);

		String message = stackTrace != null && !stackTrace.isEmpty() ? stackTrace.replaceAll("\"", "\\\"") : "";

		String error = mapper.writeValueAsString(new ErrorData(e.getClass() + " - " + message));

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		logError(e);
		return handleExceptionInternal(e, error, headers, HttpStatus.NOT_IMPLEMENTED, req);
	}

	@ExceptionHandler(OperationNotSupportedException.class)
	public ResponseEntity<Object> handleOperationNotSupportedException(OperationNotSupportedException e, WebRequest req)
			throws
			JsonProcessingException {
		String stackTrace = ExceptionUtils.getStackTrace(e);

		String message = stackTrace != null && !stackTrace.isEmpty() ? stackTrace.replaceAll("\"", "\\\"") : "";

		String error = mapper.writeValueAsString(new ErrorData(e.getClass() + " - " + message));

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		logError(e);
		return handleExceptionInternal(e, error, headers, HttpStatus.METHOD_NOT_ALLOWED, req);
	}

	@ExceptionHandler(AmazonServiceException.class)
	public ResponseEntity<Object> handleAmazonServiceException(AmazonServiceException e, WebRequest req) throws JsonProcessingException {
		String stackTrace = ExceptionUtils.getStackTrace(e);
		System.out.println("Caught an AmazonServiceException, " +
				"which means your request made it " +
				"to Amazon S3, but was rejected with an error response " +
				"for some reason.");
		System.out.println("Error Message: " + e.getMessage());
		System.out.println("HTTP  Code: " + e.getStatusCode());
		System.out.println("AWS Error Code:" + e.getErrorCode());
		System.out.println("Error Type:    " + e.getErrorType());
		System.out.println("Request ID:    " + e.getRequestId());
		String message = stackTrace != null && !stackTrace.isEmpty() ? stackTrace.replaceAll("\"", "\\\"") : "";

		String error = mapper.writeValueAsString(new ErrorData(e.getClass() + " - " + message));

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		logError(e);
		return handleExceptionInternal(e, error, headers, HttpStatus.METHOD_NOT_ALLOWED, req);
	}

	@ExceptionHandler(AmazonClientException.class)
	public ResponseEntity<Object> handleAmazonClientException(AmazonClientException e, WebRequest req) throws JsonProcessingException {
		String stackTrace = ExceptionUtils.getStackTrace(e);
		System.out.println("Caught an AmazonClientException, " +
				"which means the client encountered " +
				"an internal error while trying to communicate" +
				" with S3, " +
				"such as not being able to access the network.");
		System.out.println("Error Message: " + e.getMessage());

		String message = stackTrace != null && !stackTrace.isEmpty() ? stackTrace.replaceAll("\"", "\\\"") : "";

		String error = mapper.writeValueAsString(new ErrorData(e.getClass() + " - " + message));

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		logError(e);
		return handleExceptionInternal(e, error, headers, HttpStatus.METHOD_NOT_ALLOWED, req);
	}

	@ExceptionHandler(ConflictException.class)
	public ResponseEntity<Object> handleConflictException(ConflictException e, WebRequest req) throws
			JsonProcessingException {
		String stackTrace = ExceptionUtils.getStackTrace(e);

		String message = stackTrace != null && !stackTrace.isEmpty() ? stackTrace.replaceAll("\"", "\\\"") : "";

		String error = mapper.writeValueAsString(new ErrorData(e.getClass() + " - " + message));

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		logError(e);
		return handleExceptionInternal(e, error, headers, HttpStatus.CONFLICT, req);
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

		logError(e);
		return handleExceptionInternal(e, error, headers, HttpStatus.BAD_REQUEST, request);
	}


	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleAnyException(HttpServletRequest req, WebRequest request, Exception e) throws JsonProcessingException {


		Throwable t = e.getCause();

		logError(e);

		if (t instanceof ConflictException)
			return handleConflictException((ConflictException) t, request);

		if (t instanceof BadRequestException)
			return handleBadRequest((BadRequestException) t, request);

		if (t instanceof co.xarx.trix.exception.UnauthorizedException)
			return handleUnauthorizedException((UnauthorizedException) t, request);

		if (t instanceof co.xarx.trix.exception.SubdomainNotFoundException)
			return handleSubdomainNotFoundException((SubdomainNotFoundException) t, request);

		if (t instanceof co.xarx.trix.exception.OperationNotSupportedException)
			return handleOperationNotSupportedException((OperationNotSupportedException) t, request);

		if (t instanceof AmazonClientException)
			return handleAmazonClientException((AmazonClientException) t, request);

		if (t instanceof AmazonServiceException)
			return handleAmazonServiceException((AmazonServiceException) t, request);

		if (t instanceof NotImplementedException)
			return handleNotImplementedException((NotImplementedException) t, request);

		ErrorResource error = new ErrorResource("Generic Exception", ExceptionUtils.getRootCauseMessage(e));
		error.setStacktrace(ExceptionUtils.getStackTrace(e));
		error.setType(e.toString());

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		if (e instanceof java.net.SocketException) {
			return handleExceptionInternal(e, ExceptionUtils.getRootCauseMessage(e), headers, HttpStatus
					.INTERNAL_SERVER_ERROR, request);
		}

		return handleExceptionInternal(e, error, headers, HttpStatus.INTERNAL_SERVER_ERROR, request);
	}

	private void logError(Throwable e) {
		String message = "LOG FATAL ERROR\n" +
				"NETWORK: " + TenantContextHolder.getCurrentTenantId() + "\n" +
				"MESSAGE: " + e.getMessage() + "\n" +
				"METHOD :" + request.getMethod() +
				"URL: " + request.getRequestURL() + "\n" +
				"User-Agent: " + request.getHeader("User-Agent") + "\n";

		if(e instanceof IllegalArgumentException) message += new RequestWrapper(request).getAllHeaders();

		log.error(message, e);
		if(TenantContextHolder.getCurrentTenantId().equals("pd")) return;
//		slackBot.logError(message);
	}
}