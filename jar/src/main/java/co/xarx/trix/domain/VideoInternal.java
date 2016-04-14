package co.xarx.trix.domain;

import javax.persistence.CascadeType;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

/**
 * Created by misael on 4/14/2016.
 */
@lombok.Getter
@lombok.Setter
@lombok.NoArgsConstructor
@lombok.EqualsAndHashCode(callSuper = false)
public class VideoInternal extends Video{


	public VideoInternal(File file) {
		this.file = file;
	}

	@NotNull
	@ManyToOne(cascade=CascadeType.MERGE)
	public File file;

	@Override
	public String getUrl() {
		return null;
	}
}
