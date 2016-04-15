package co.xarx.trix.domain;

import co.xarx.trix.annotation.SdkExclude;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

	@SdkExclude
	@ManyToOne(cascade=CascadeType.MERGE)
	@JsonIgnore
	private Picture cover;

	@NotNull
	@SdkExclude
	@ManyToOne(cascade= CascadeType.MERGE)
	public File file;

	public String identifier;
	public String provider;
}
