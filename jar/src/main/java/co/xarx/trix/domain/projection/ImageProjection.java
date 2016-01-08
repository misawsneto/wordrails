package co.xarx.trix.domain.projection;

import co.xarx.trix.domain.Image;
import org.springframework.data.rest.core.config.Projection;

import java.util.Map;

@Projection(types=Image.class)
public interface ImageProjection {
	Integer getId();
	FileProjection getSmall();
	FileProjection getMedium();
	FileProjection getLarge();
	Integer getPostId();
}