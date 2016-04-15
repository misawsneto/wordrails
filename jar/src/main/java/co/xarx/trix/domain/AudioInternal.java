package co.xarx.trix.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@lombok.Getter
@lombok.Setter
@lombok.NoArgsConstructor
@lombok.EqualsAndHashCode(callSuper = false)
@Entity
public class AudioInternal extends Audio{

	public AudioInternal(File file) {
		this.file = file;
	}

	@NotNull
	@ManyToOne(cascade= CascadeType.MERGE)
	public File file;
}
