package co.xarx.trix.persistence.elasticsearch;

/**
 * Created by misael on 05/10/2015.
 */

import com.fasterxml.jackson.annotation.JsonManagedReference;
import co.xarx.trix.domain.Row;
import co.xarx.trix.domain.StationPerspective;
import co.xarx.trix.domain.Term;

import java.util.List;

public class PerspectiveIndexed {
    public Integer id;

    @JsonManagedReference
    public Row splashedRow;

    @JsonManagedReference
    public List<Row> homeRows;

    @JsonManagedReference
    public Row featuredRow;

    @JsonManagedReference
    public List<Row> rows;

    public boolean showPopular;

    public boolean showRecent;

    @JsonManagedReference
    public StationPerspective perspective;

    @JsonManagedReference
    public Term term;

    public Integer taxonomyId;

    public Integer stationId;
}