package co.xarx.trix.api.v2;

import co.xarx.trix.domain.Identifiable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;


@lombok.Getter @lombok.Setter @lombok.NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PersonData implements Serializable, Identifiable {

	private Integer id;
	private String name;
	private String username;

	private String email;
	private String twitter;

	private ImageData cover;
	private ImageData profilePicture;
}