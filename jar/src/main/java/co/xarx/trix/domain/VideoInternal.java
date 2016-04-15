package co.xarx.trix.domain;

import co.xarx.trix.annotation.SdkExclude;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.validation.constraints.NotNull;

@lombok.Getter
@lombok.Setter
@lombok.NoArgsConstructor
@lombok.EqualsAndHashCode(callSuper = false)
@Entity
public class VideoInternal extends Video{


	public VideoInternal(File file) {
		file.original = true;
		this.file = file;
	}

	@NotNull
	@ManyToOne(cascade=CascadeType.MERGE)
	@SdkExclude
	public File file;
}
