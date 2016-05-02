package co.xarx.trix.domain;

import co.xarx.trix.util.Constants;
import lombok.AccessLevel;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.rest.core.annotation.RestResource;

import javax.persistence.*;

@lombok.Getter
@lombok.Setter
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"tenantId", "deviceCode"}))
public class MobileDevice extends BaseEntity {

	@Id
	@Setter(AccessLevel.NONE)
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer id;

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

	@RestResource(exported = false)
	public Constants.MobilePlatform type;

	@Override
	public String toString() {
		if(deviceCode != null)
			return deviceCode;
		else
			return super.toString();
	}
}

