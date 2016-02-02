package co.xarx.trix.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Date;

@Entity
public class PasswordReset extends BaseEntity {

	public PasswordReset(){
		LocalDate daysFromNow = LocalDate.now().plusDays(1);
		expiresAt = new Date(daysFromNow.toEpochDay());
	}

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
	public User user;

	public Date expiresAt;
}
