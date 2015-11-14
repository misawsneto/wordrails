package co.xarx.trix.domain.projection;

import java.util.Date;

import co.xarx.trix.domain.Bookmark;
import co.xarx.trix.domain.Person;
import co.xarx.trix.domain.Post;
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