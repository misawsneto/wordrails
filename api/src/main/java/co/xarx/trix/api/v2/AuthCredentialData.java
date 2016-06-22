package co.xarx.trix.api.v2;

import co.xarx.trix.api.EntityDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthCredentialData extends EntityDto {

	public String facebookAppID;
	public String facebookAppSecret;

	public String googleAppID;
	public String googleAppSecret;
}