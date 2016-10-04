package co.xarx.trix.domain.projection;

import co.xarx.trix.domain.Video;
import org.springframework.data.rest.core.config.Projection;

@Projection(types = Video.class)
public interface VideoProjection {
	Integer getId();

	String getTitle();

	Integer getDuration();
}
