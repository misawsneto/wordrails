package co.xarx.trix.domain;

import lombok.AccessLevel;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
@lombok.Getter @lombok.Setter @lombok.NoArgsConstructor
public class GeoPoint extends BaseEntity{

	@Override
	public Serializable getId() {
		return null;
	}

	@Id
	@Setter(AccessLevel.NONE)
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer id;

}
