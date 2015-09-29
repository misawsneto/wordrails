package com.wordrails.business;

import org.springframework.data.rest.core.config.Projection;

import java.util.Set;

/**
 * Created by misael on 15/09/2015.
 */
@Projection(types=StationPerspective.class)
public interface StationPerspectiveProjection {
    Integer getId();

    String getName();

    Station getStation();

    Taxonomy getTaxonomy();

    Set<TermPerspective> getPerspectives();

    Integer getStationId();
    Integer getTaxonomyId();
    String getTaxonomyName();
    String getTaxonomyType();

}

