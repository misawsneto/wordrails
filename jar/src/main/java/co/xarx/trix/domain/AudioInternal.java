package co.xarx.trix.domain;

import co.xarx.trix.annotation.SdkExclude;
import lombok.AccessLevel;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@lombok.Getter
@lombok.Setter
@lombok.NoArgsConstructor
@lombok.EqualsAndHashCode(callSuper = false)
@Entity
public class AudioInternal extends Audio{

	public AudioInternal(File file) {
		file.original = true;
		this.file = file;
	}

	@NotNull
	@SdkExclude
	@ManyToOne(cascade= CascadeType.MERGE)
	public File file;
}
