package co.xarx.trix.api;

import co.xarx.trix.domain.Identifiable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@lombok.Getter @lombok.Setter @lombok.NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthorView implements Serializable, Identifiable {
	@Override
	public Serializable getId() {
		return null;
	}
}