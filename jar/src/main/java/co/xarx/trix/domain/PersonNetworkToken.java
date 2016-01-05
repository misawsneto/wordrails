package co.xarx.trix.domain;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"network_id", "token"}))
public class PersonNetworkToken extends BaseEntity {

	@NotEmpty
	public String token;

	@ManyToOne
	@JoinColumn(name = "person_id")
	public Person person;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "network_id")
	public Network network;

	public Double lat;

	public Double lng;

	@Override
	public String toString() {
		if (token != null) return token.toString();
		else return super.toString();
	}
}

