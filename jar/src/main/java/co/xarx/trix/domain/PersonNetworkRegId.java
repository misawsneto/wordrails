package co.xarx.trix.domain;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"tenantId", "regId"}))
public class PersonNetworkRegId extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer id;

	@Override
	public Integer getId() {
		return id;
	}

	@NotEmpty
	public String regId;
	
	@ManyToOne
	@JoinColumn(name = "person_id")
	public Person person;

	public Double lat;

	public Double lng;

	@Override
	public String toString() {
		if(regId != null)
			return regId;
		else
			return super.toString();
	}
}

