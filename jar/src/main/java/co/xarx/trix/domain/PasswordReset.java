package co.xarx.trix.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Calendar;
import java.util.Date;


@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class PasswordReset extends BaseEntity {

	public PasswordReset(){
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_YEAR, 1);

		expiresAt = calendar.getTime();
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
	@OneToOne
	public User user;

	public Date expiresAt;
}
