package co.xarx.trix.domain;


import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;


@Data
@EqualsAndHashCode(callSuper = true)
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