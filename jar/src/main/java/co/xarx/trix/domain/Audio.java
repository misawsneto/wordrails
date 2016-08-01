package co.xarx.trix.domain;

import co.xarx.trix.annotation.SdkExclude;
import co.xarx.trix.annotation.SdkInclude;
import lombok.AccessLevel;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@lombok.Getter
@lombok.Setter
@lombok.NoArgsConstructor
@lombok.EqualsAndHashCode(callSuper = false)
@Entity
public class Audio extends BaseEntity implements Serializable {

	public Audio(File file) {
		file.original = true;
		this.file = file;
	}

	private static final long serialVersionUID = -6407170001176054415L;

	@Id
	@Setter(AccessLevel.NONE)
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer id;

	public String title;

	@Column(columnDefinition = "int(11) DEFAULT 0")
	public Integer duration;

	@SdkInclude
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	public Image image;

	@SdkInclude
	public String getImageHash() {
		if (image != null) return image.getOriginalHash();

		return null;
	}

	@NotNull
	@SdkExclude
	@ManyToOne(cascade= CascadeType.MERGE)
	public File file;

	public String identifier;
	public String provider;
}
