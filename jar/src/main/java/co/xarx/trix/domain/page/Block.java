package co.xarx.trix.domain.page;

import org.springframework.hateoas.Identifiable;

import java.io.Serializable;

public interface Block<T extends Identifiable> extends Identifiable {

	default Serializable getId() {
		return getObject().getId();
	}

	T getObject();

	String getObjectName();
}
