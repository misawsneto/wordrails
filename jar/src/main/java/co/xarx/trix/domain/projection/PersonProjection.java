package co.xarx.trix.domain.projection;

import co.xarx.trix.domain.NetworkRole;
import co.xarx.trix.domain.Person;
import co.xarx.trix.domain.StationRole;
import co.xarx.trix.domain.User;
import org.springframework.data.rest.core.config.Projection;

import java.util.Set;

/**
 * Created by misael on 24/07/2015.
 */
@Projection(types=Person.class)
public interface PersonProjection {
    Integer getId();
    String getName();
    String getUsername();
    String getEmail();
    public Set<StationRole> getPersonsStationPermissions();
    public Set<NetworkRole> getPersonsNetworkRoles();
    public User getUser();

    Integer getCoverLargeId();
    Integer getCoverMediumId();

    Integer getImageLargeId();
    Integer getImageMediumId();
    Integer getImageSmallId();

    String getImageLargeHash();
    String getImageMediumHash();
    String getImageSmallHash();
}
