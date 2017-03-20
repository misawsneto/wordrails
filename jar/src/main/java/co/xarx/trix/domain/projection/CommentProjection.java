package co.xarx.trix.domain.projection;

import co.xarx.trix.domain.Comment;
import co.xarx.trix.domain.Person;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.rest.core.config.Projection;

import java.util.Date;

@Projection(types=Comment.class)
public interface CommentProjection {

	Integer getId();
	
	@JsonFormat(shape=JsonFormat.Shape.NUMBER)
	Date getDate();
	
	String getBody();

	Person getAuthor();

	Integer stationId();
}