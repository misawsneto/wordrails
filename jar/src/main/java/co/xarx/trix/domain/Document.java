package co.xarx.trix.domain;

import co.xarx.trix.annotation.SdkExclude;
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
public class Document extends BaseEntity implements Serializable {

	public Document(File file) {
		file.original = true;
		this.file = file;
	}

	public Document(String identifier, String provider) {
		this.identifier= identifier;
		this.provider= provider;
	}

	public String identifier;
	public String provider;

	@NotNull
	@ManyToOne(cascade= CascadeType.MERGE)
	@SdkExclude
	public File file;

	private static final long serialVersionUID = -6407170001176054415L;

	public Document(String title) {
		this.title = title;
	}

	public String title;

	public String url;

	@Id
	@Setter(AccessLevel.NONE)
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer id;

	public String getHash() {
		return file != null ? file.hash : null;
	}
}
