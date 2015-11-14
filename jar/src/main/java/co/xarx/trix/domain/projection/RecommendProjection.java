package co.xarx.trix.domain.projection;

import java.util.Date;

import co.xarx.trix.domain.Person;
import co.xarx.trix.domain.Post;
import co.xarx.trix.domain.Recommend;
import org.springframework.data.rest.core.config.Projection;

import com.fasterxml.jackson.annotation.JsonFormat;

@Projection(types=Recommend.class)
public interface RecommendProjection {
	Integer getId();
	
	@JsonFormat(shape=JsonFormat.Shape.NUMBER)
	Date getCreatedAt();
	
	Post getPost();
	Person getPerson();
}