package co.xarx.trix.domain;

import co.xarx.trix.annotation.SdkExclude;
import lombok.AccessLevel;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@SdkExclude
@lombok.Getter
@Setter
@lombok.NoArgsConstructor
@lombok.EqualsAndHashCode(callSuper = false)
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Document extends BaseEntity implements Serializable {

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
}
