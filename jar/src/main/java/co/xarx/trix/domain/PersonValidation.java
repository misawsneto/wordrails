package co.xarx.trix.domain;

import lombok.AccessLevel;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@lombok.Getter
@lombok.Setter
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"hash", "person_id"}))
public class PersonValidation extends BaseEntity {

	public PersonValidation(){
		this.hash = UUID.randomUUID().toString();
	}

	@Id
	@Setter(AccessLevel.NONE)
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer id;

	@NotNull
	@OneToOne
	@JoinColumn(name = "person_id")
	public Person person;

	@Column(columnDefinition = "boolean default false", nullable = false)
	public boolean validated;

	@NotNull
	@Column
	public String hash;
}
