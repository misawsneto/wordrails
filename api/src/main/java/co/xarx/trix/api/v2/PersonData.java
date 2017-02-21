package co.xarx.trix.api.v2;

import co.xarx.trix.domain.Identifiable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;


@Getter
@Setter
@ToString
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PersonData implements Serializable, Identifiable {

	private Integer id;
	private String name;
	private String username;

	private String email;
	private String twitter;

	private String coverHash;
	private String imageHash;
	private Date lastLogin;

	public String coverUrl;
	public String imageUrl;
}