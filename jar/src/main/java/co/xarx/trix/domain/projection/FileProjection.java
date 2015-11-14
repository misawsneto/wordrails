package co.xarx.trix.domain.projection;

import co.xarx.trix.domain.File;
import org.springframework.data.rest.core.config.Projection;

@Projection(types=File.class)
public interface FileProjection {
	Integer getId();
}