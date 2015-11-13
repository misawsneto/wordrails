package co.xarx.trix.domain;

import java.util.Date;
import java.util.List;

import org.springframework.data.rest.core.config.Projection;

import com.fasterxml.jackson.annotation.JsonFormat;

@Projection(types=Comment.class)
public interface CommentProjection {
	Integer getId();
	
	@JsonFormat(shape=JsonFormat.Shape.NUMBER)
	Date getDate();
	
	String getTitle();
	String getBody();
	Person getAuthor();
	List<ImageProjection> getImages();
}