package co.xarx.trix.domain.projection;

import co.xarx.trix.domain.Person;
import co.xarx.trix.domain.Station;
import co.xarx.trix.domain.StationRole;
import org.springframework.data.rest.core.config.Projection;

@Projection(types=StationRole.class)
public interface  StationRoleProjection {
	Integer getId();
	Station getStation();
	Person getPerson();
	Boolean getEditor();
	Boolean getWriter();
	Boolean getAdmin();
}