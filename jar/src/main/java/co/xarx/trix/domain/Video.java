package co.xarx.trix.domain;

import co.xarx.trix.annotation.SdkExclude;
import co.xarx.trix.annotation.SdkInclude;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;

@lombok.Getter
@lombok.Setter
@lombok.NoArgsConstructor
@lombok.EqualsAndHashCode(callSuper = false)
@Entity
public class Video extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 6417000117605453415L;

	public Video(File file) {
		file.original = true;
		this.file = file;
	}

	public Video(String identifier, String provider) {
		this.identifier= identifier;
		this.provider= provider;
	}

	public String identifier;
	public String provider;

	@Id
	@Setter(AccessLevel.NONE)
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer id;

	public String title;

	@NotNull
	@ManyToOne(cascade=CascadeType.MERGE)
	@SdkExclude
	public File file;

	@SdkInclude
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	public Image image;

	@SdkInclude
	public String getImageHash() {
		if (image != null) return image.getOriginalHash();

		return null;
	}
	public String url;
}
