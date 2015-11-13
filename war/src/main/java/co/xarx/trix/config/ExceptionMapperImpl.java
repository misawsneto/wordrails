package co.xarx.trix.config;

import co.xarx.trix.exception.ConflictException;
import co.xarx.trix.exception.OperationNotSupportedException;
import co.xarx.trix.exception.UnauthorizedException;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
@Component
public class ExceptionMapperImpl implements ExceptionMapper<Throwable> {
	@Override
	public Response toResponse(Throwable throwable) {
		Status status;
		if (throwable instanceof EntityNotFoundException) {
			status = Status.NOT_FOUND;
		} else if (throwable instanceof UnauthorizedException) {
			status = Status.FORBIDDEN;
		} else if (throwable instanceof OperationNotSupportedException) {
			status = Status.METHOD_NOT_ALLOWED;
		} else if (throwable instanceof ConflictException){
			status = Status.CONFLICT;
		} else {
			status = Status.INTERNAL_SERVER_ERROR;
		}
		String stackTrace = ExceptionUtils.getStackTrace(throwable);
		return Response.status(status).entity(stackTrace).build();
	}
}