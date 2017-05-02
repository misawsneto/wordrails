package co.xarx.trix.api;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StationPostDto {
	private static final long serialVersionUID = -278630211043976635L;
	public Integer stationId;
	public List<Integer> posts;
}
