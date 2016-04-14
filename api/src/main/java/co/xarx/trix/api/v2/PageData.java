package co.xarx.trix.api.v2;

import co.xarx.trix.domain.Identifiable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PageData implements Serializable, Identifiable {

	private Integer id;
	private String title;
	private List<SectionData> sections;
}