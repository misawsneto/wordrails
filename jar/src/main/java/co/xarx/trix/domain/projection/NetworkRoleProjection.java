package co.xarx.trix.domain.projection;

import co.xarx.trix.domain.NetworkRole;
import co.xarx.trix.domain.Person;
import org.springframework.data.rest.core.config.Projection;

@Projection(types=NetworkRole.class)
public interface NetworkRoleProjection {
	Integer getId();
	Person getPerson();
	Boolean getAdmin();
}