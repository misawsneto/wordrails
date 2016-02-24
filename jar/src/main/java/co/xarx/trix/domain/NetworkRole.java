package co.xarx.trix.domain;


import lombok.AccessLevel;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;


@lombok.Getter @lombok.Setter @lombok.NoArgsConstructor
@Entity
@Table(name = "person_network_role", uniqueConstraints = {@UniqueConstraint(columnNames = {"person_id", "network_id"})})
public class NetworkRole extends BaseEntity {

	@Id
	@Setter(AccessLevel.NONE)
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer id;

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