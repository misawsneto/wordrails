package co.xarx.trix.domain;

import org.hibernate.validator.constraints.Email;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class PasswordReset extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer id;

	@Override
	public Integer getId() {
		return id;
	}

	@NotNull
	@Column(unique=true)
	public String hash;
	
	@NotNull
	@Email
	public String email;
	
	public String personName;
	
	public boolean active = true;
	
	public boolean invite = false;
	
	public String networkName;
}
