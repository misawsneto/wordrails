package co.xarx.trix.persistence.elasticsearch;

import co.xarx.trix.domain.Row;
import co.xarx.trix.domain.StationPerspective;
import co.xarx.trix.domain.Term;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.util.List;

public class PerspectiveIndexed {

	public Integer id;

	@JsonManagedReference
	public Row splashedRow;

	@JsonManagedReference
	public Row homeRow;

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