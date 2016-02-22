package co.xarx.trix.domain;

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
	public String hash;

	@NotNull
	@OneToOne
	public User user;
}
