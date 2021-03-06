package co.xarx.trix.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Email;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by misael on 10/14/2016.
 */
@Entity
@Getter
@Setter
public class NetworkCreate extends BaseEntity implements Serializable, Identifiable {

	private static final long serialVersionUID = 7723825542358685233L;

	@Id
	@Setter(AccessLevel.NONE)
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer id;

	@NotNull
	public String name;
	@Email
	public String email;

	public Boolean contacted = false;

}
