package co.xarx.trix.domain;


import lombok.AccessLevel;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@lombok.Getter @lombok.Setter @lombok.NoArgsConstructor
@Entity
@Table(name = "person_station_role", uniqueConstraints = {@UniqueConstraint(columnNames = {"person_id", "station_id"})})
public class StationRole extends BaseEntity {

	@Id
	@Setter(AccessLevel.NONE)
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer id;


	@NotNull
	@ManyToOne
	@JoinColumn(name = "station_id")
	public Station station;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "person_id")
	public Person person;

	@NotNull
	public boolean editor;

	@NotNull
	public boolean writer;

	@NotNull
	public boolean admin;
}