package co.xarx.trix.domain;


import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "person_network_role", uniqueConstraints = {@UniqueConstraint(columnNames = {"person_id", "network_id"})})
public class NetworkRole extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer id;

	@Override
	public Integer getId() {
		return id;
	}
	@ManyToOne
	@NotNull
	@JoinColumn(name = "network_id")
	public Network network;

	@ManyToOne
	@NotNull
	@JoinColumn(name = "person_id")
	public Person person;

	@NotNull
	public boolean admin;
}