package co.xarx.trix.domain;

import lombok.AccessLevel;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Calendar;
import java.util.Date;


@lombok.Getter @lombok.Setter
@Entity
public class PasswordReset extends BaseEntity {

	public PasswordReset(){
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_YEAR, 1);

		expiresAt = calendar.getTime();
	}

	@Id
	@Setter(AccessLevel.NONE)
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer id;

	@NotNull
	public String hash;

	@NotNull
	@OneToOne
	public User user;

	public Date expiresAt;
}
