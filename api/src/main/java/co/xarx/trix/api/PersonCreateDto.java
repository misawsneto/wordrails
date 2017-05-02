package co.xarx.trix.api;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class PersonCreateDto {
    public String firstName;
    public String lastName;
    public String user_login;
	public String email;
    public String password;
}