package co.xarx.trix.domain.page;

import co.xarx.trix.domain.BaseEntity;
import lombok.AccessLevel;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;


@lombok.Getter @lombok.Setter
@Entity
public class LinkItem extends BaseEntity implements Serializable {
	private static final long serialVersionUID = -741871893047249401L;

	public enum Type{
		DIVIDER,PAGE,CATEGORY,STATION,POST
	};

	@Id
	@Setter(AccessLevel.NONE)
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer id;

	@Lob
	public String text;

	public Integer referenceId;

	@Lob
	String externalLink;
}
