package co.xarx.trix.domain;


import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "person_station_role", uniqueConstraints = {@UniqueConstraint(columnNames = {"person_id", "station_id"})})
public class StationRole extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer id;

	@Override
	public Integer getId() {
		return id;
	}


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