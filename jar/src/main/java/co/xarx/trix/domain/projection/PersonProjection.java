package co.xarx.trix.domain.projection;

import co.xarx.trix.domain.NetworkRole;
import co.xarx.trix.domain.Person;
import co.xarx.trix.domain.StationRole;
import co.xarx.trix.domain.User;
import org.springframework.data.rest.core.config.Projection;

import java.util.Set;


@Projection(types = Person.class)
public interface PersonProjection {
	Integer getId();

	String getName();

	String getUsername();

	String getEmail();

	Set<StationRole> getPersonsStationPermissions();

	Set<NetworkRole> getPersonsNetworkRoles();

	User getUser();

	Integer getCoverLargeId();

	Integer getCoverMediumId();

	Integer getImageLargeId();

	Integer getImageMediumId();

	Integer getImageSmallId();

	String getImageLargeHash();

	String getImageMediumHash();

	String getImageSmallHash();
}
