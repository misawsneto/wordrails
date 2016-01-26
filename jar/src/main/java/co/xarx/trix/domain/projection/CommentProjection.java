package co.xarx.trix.domain.projection;

import java.util.Date;
import java.util.List;

import co.xarx.trix.domain.Comment;
import co.xarx.trix.domain.Person;
import org.springframework.data.rest.core.config.Projection;

import com.fasterxml.jackson.annotation.JsonFormat;

@Projection(types=Comment.class)
public interface CommentProjection {
	Integer getId();
	
	@JsonFormat(shape=JsonFormat.Shape.NUMBER)
	Date getDate();
	
	String getBody();
	Person getAuthor();
}