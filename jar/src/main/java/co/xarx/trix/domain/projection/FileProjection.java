package co.xarx.trix.domain.projection;

import co.xarx.trix.domain.File;
import org.springframework.data.rest.core.config.Projection;

/**
 * Created by misael on 3/4/2016.
 */
@Projection(types = File.class)
public interface FileProjection {
	public Integer getId();
}
