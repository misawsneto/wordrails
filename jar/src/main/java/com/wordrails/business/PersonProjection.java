package com.wordrails.business;

import org.springframework.data.rest.core.config.Projection;

import javax.persistence.OneToMany;
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

    Integer getCoverLargeId();
    Integer getCoverMediumId();

    Integer getImageLargeId();
    Integer getImageMediumId();
    Integer getImageSmallId();

    String getImageLargeHash();
    String getImageMediumHash();
    String getImageSmallHash();
}
