package co.xarx.trix.domain.projection;

import co.xarx.trix.domain.Person;
import org.springframework.data.rest.core.config.Projection;


@Projection(types = Person.class)
public interface PersonProjection {

	Integer getId();

	String getName();

	String getUsername();

	String getEmail();

	String getCoverLargeHash();

	String getCoverMediumHash();

	String getImageMediumHash();

	String getImageSmallHash();

	String getImageOriginalHash();
	String getCoverOriginalHash();
}
