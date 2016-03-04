package co.xarx.trix.domain.projection;

import co.xarx.trix.domain.Image;
import org.springframework.data.rest.core.config.Projection;

@Projection(types=Image.class)
public interface ImageProjection {
	Integer getId();
	String getOriginalHash();
	FileProjection getSmall();
	FileProjection getMedium();
	FileProjection getLarge();
}