package co.xarx.trix.domain;

import co.xarx.trix.annotation.SdkExclude;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;

@SdkExclude
@lombok.Getter
@lombok.Setter
@lombok.NoArgsConstructor
@lombok.EqualsAndHashCode(callSuper = false)
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Video extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 6417000117605453415L;

	@Id
	@Setter(AccessLevel.NONE)
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer id;

	public String title;

	@SdkExclude
	@ManyToOne(cascade=CascadeType.MERGE)
	@JsonIgnore
	private Picture cover;

	public String url;
}
