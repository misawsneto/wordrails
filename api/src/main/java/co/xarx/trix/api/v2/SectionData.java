package co.xarx.trix.api.v2;

import co.xarx.trix.domain.Identifiable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.Map;


@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SectionData implements Serializable, Identifiable {

	private Integer id;
	private String title;
	private String type;
	private String style;
	private Map<String, String> properties;
	private List<BlockData> blocks;
}