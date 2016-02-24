package co.xarx.trix.domain;

import lombok.AccessLevel;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;


@lombok.Getter @lombok.Setter
@Entity
public class PasswordReset extends BaseEntity {

	@Id
	@Setter(AccessLevel.NONE)
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer id;

	@NotNull
	public String hash;

	@NotNull
	@OneToOne
	public User user;
}
