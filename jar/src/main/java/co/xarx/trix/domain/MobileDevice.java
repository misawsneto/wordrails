package co.xarx.trix.domain;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"tenantId", "deviceCode"}))
public class MobileDevice extends BaseEntity {

	public enum Type {
		ANDROID,
		APPLE
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer id;

	@Override
	public Integer getId() {
		return id;
	}

	@NotEmpty
	public String deviceCode;

	@ManyToOne
	@JoinColumn(name = "person_id")
	public Person person;

	@ManyToOne
	@JoinColumn(name = "last_person_id")
	public Person lastPersonLogged;

	public Double lat;

	public Double lng;

	public boolean active;

	public Type type;

	@Override
	public String toString() {
		if(deviceCode != null)
			return deviceCode;
		else
			return super.toString();
	}
}

