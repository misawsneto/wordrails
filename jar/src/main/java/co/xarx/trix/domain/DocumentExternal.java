package co.xarx.trix.domain;

import javax.persistence.Entity;

@lombok.Getter
@lombok.Setter
@lombok.NoArgsConstructor
@lombok.EqualsAndHashCode(callSuper = false)
@Entity
public class DocumentExternal extends Document{
	public DocumentExternal(String identifier, String provider) {
		this.identifier= identifier;
		this.provider= provider;
	}

	public String identifier;
	public String provider;
}
