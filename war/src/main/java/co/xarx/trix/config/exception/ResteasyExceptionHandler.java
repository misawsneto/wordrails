package co.xarx.trix.config.exception;

import co.xarx.trix.api.v2.ErrorData;
import co.xarx.trix.config.multitenancy.TenantContextHolder;
import co.xarx.trix.exception.BadRequestException;
import co.xarx.trix.exception.ConflictException;
import co.xarx.trix.exception.OperationNotSupportedException;
import co.xarx.trix.exception.UnauthorizedException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.acls.model.AlreadyExistsException;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
@Component
public class ResteasyExceptionHandler implements ExceptionMapper<Throwable> {

	static Logger log = Logger.getLogger(ResteasyExceptionHandler.class.getName());

	@Autowired
	@Qualifier("objectMapper")
	private ObjectMapper mapper;

	@Autowired
	private HttpServletRequest request;

//	@Autowired
//	private SlackBot slackBot;

	@Override
	public Response toResponse(Throwable throwable) {
		Status status;


		if (throwable instanceof EntityNotFoundException) {
			status = Status.NOT_FOUND;
		} else if (throwable instanceof AccessDeniedException) {
			status = Status.FORBIDDEN;
		} else if (throwable instanceof UnauthorizedException) {
			status = Status.FORBIDDEN;
		} else if (throwable instanceof OperationNotSupportedException) {
			status = Status.METHOD_NOT_ALLOWED;
		} else if (throwable instanceof ConflictException) {
			status = Status.CONFLICT;
		} else if (throwable instanceof BadRequestException) {
			status = Status.BAD_REQUEST;
		}else if (throwable instanceof AlreadyExistsException){
			status = Status.CONFLICT;
		} else if (throwable instanceof ClientErrorException) {
			status = Status.fromStatusCode(((ClientErrorException) throwable).getResponse().getStatus());
		} else {
			status = Status.INTERNAL_SERVER_ERROR;
		}

		if (status.equals(Status.FORBIDDEN)) {
			log.info("Access denied for url " + request.getRequestURL() + " - Session ID: " + request
					.getRequestedSessionId() + " - User-Agent: " + request.getHeader("User-Agent"));
		} else {
			logError("LOG FATAL ERROR\n" +
							"NETWORK: " + TenantContextHolder.getCurrentTenantId() + "\n" +
							"MESSAGE: " + throwable.getMessage() + "\n" +
							"URL: " + request.getRequestURL() + "\n" +
							"METHOD :" + request.getMethod() + "\n" +
							"User-Agent: " + request.getHeader("User-Agent") + "\n",
					throwable);
		}

//		String stackTrace = ExceptionUtils.getStackTrace(throwable);
		String message = throwable.getMessage();
		message = message != null && !message.isEmpty() ? message.replaceAll("\"", "\\\"") : "";

		try {
			return Response.status(status).entity(mapper.writeValueAsString(new ErrorData(throwable.getClass() + " - " + message ))).type(MediaType.APPLICATION_JSON).build();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return null;
		}
	}

	private void logError(String message, Throwable throwable){
		String tenant = TenantContextHolder.getCurrentTenantId();
		String logMessage = message + "\nUser-Agent: " + request.getHeader("User-Agent");
		log.error(logMessage, throwable);

		//POG -- This is bad code. Don't repeat that at home, kids
		if(tenant.equals("pd")) return;

//		slackBot.logError(logMessage);
	}
}