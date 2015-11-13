package co.xarx.trix.domain;

import org.springframework.data.rest.core.config.Projection;

@Projection(types=NetworkRole.class)
public interface NetworkRoleProjection {
	Integer getId();
	Person getPerson();
	Boolean getAdmin();
}