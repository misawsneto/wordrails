package co.xarx.trix.domain.projection;

import co.xarx.trix.domain.Audio;
import org.springframework.data.rest.core.config.Projection;

@Projection(types = Audio.class)
public interface AudioProjection {

	Integer getId();

	String getTitle();

	Integer getDuration();
}
