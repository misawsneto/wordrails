package co.xarx.trix.domain.projection;

import co.xarx.trix.domain.Station;
import co.xarx.trix.domain.StationPerspective;
import co.xarx.trix.domain.Taxonomy;
import co.xarx.trix.domain.TermPerspective;
import org.springframework.data.rest.core.config.Projection;

import java.util.Set;

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

