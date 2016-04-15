package co.xarx.trix.api.v2;

import co.xarx.trix.domain.Identifiable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;


@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PersonData implements Serializable, Identifiable {

	private Integer id;
	private String name;
	private String username;

	private String email;
	private String twitter;

	private String coverHash;
	private ImageData cover;
	private String profilePictureHash;
	private ImageData profilePicture;
}