package co.xarx.trix.domain;

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
public class VideoExternal extends Video{

	public VideoExternal(String identifier, String provider) {
		this.identifier= identifier;
		this.provider= provider;
	}

	public String identifier;
	public String provider;
}
