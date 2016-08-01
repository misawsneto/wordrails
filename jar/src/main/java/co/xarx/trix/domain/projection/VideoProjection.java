package co.xarx.trix.domain.projection;

import co.xarx.trix.domain.Video;
import org.springframework.data.rest.core.config.Projection;

/**
 * Created by misael on 01/08/2016.
 */
@Projection(types = Video.class)
public interface VideoProjection {
	Integer getId();

	String getTitle();

	Integer getDuration();
}
