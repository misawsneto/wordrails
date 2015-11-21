package co.xarx.trix.domain.page;

import org.springframework.hateoas.Identifiable;

public interface Block<T extends Identifiable> {

	T getObject();

	String getObjectName();
}
