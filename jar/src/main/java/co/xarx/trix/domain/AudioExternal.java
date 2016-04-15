package co.xarx.trix.domain;

import lombok.AccessLevel;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@lombok.Getter
@lombok.Setter
@lombok.NoArgsConstructor
@lombok.EqualsAndHashCode(callSuper = false)
@Entity
public class AudioExternal extends Audio{
	public AudioExternal(String identifier, String provider) {
		this.identifier= identifier;
		this.provider= provider;
	}

	public String identifier;
	public String provider;
}
