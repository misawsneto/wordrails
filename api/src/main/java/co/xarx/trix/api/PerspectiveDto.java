package co.xarx.trix.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * Created by misael on 4/25/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PerspectiveDto extends EntityDto {

	private static final long serialVersionUID = 1073381103300701505L;

	public Integer id;
	public String name;
	public RowView homeRow;
	public List<RowView> ordinaryRows;
	public RowView featuredRow;
	public Integer stationId;
	public Boolean defaultPerspective = false;

	//TODO procurar remover este get que está sendo usado
	//no mobile android
	public Integer getStationPerspectiveId()
	{
		return id;
	}

}