package co.xarx.trix.api;

import java.io.Serializable;
import java.util.List;

public class TermPerspectiveView implements Serializable {
	private static final long serialVersionUID = 1073381103300701505L;

	public Integer id;
	public Integer stationPerspectiveId;
	public Integer termId;
	public RowView splashedRow;
    public RowView homeRow;
	public List<RowView> ordinaryRows;
	public RowView featuredRow;
	public Integer stationId;
	public String termName;
	public Integer taxonomyId;
}