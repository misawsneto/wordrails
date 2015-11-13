package co.xarx.trix.domain;

import java.util.Date;

import org.springframework.data.rest.core.config.Projection;

import com.fasterxml.jackson.annotation.JsonFormat;

@Projection(types=Bookmark.class)
public interface BookmarkProjection {
	Integer getId();
	
	@JsonFormat(shape=JsonFormat.Shape.NUMBER)
	Date getCreatedAt();
	
	Post getPost();
	Person getPerson();
}