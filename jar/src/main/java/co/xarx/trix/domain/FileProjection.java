package co.xarx.trix.domain;

import org.springframework.data.rest.core.config.Projection;

@Projection(types=File.class)
public interface FileProjection {
	Integer getId();
}