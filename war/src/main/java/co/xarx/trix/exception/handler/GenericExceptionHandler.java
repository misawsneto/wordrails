package co.xarx.trix.exception.handler;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

//@ControllerAdvice(basePackageClasses = RepositoryRestExceptionHandler.class)
public class GenericExceptionHandler {

	@ExceptionHandler
	ResponseEntity handle(Exception e) {
		return new ResponseEntity("Some message", new HttpHeaders(), HttpStatus.BAD_REQUEST);
	}
}