package co.xarx.trix.api;

import lombok.Data;

@Data
public class PersonCreateDto {
    public String user_login;
	public String email;
    public String password;
}