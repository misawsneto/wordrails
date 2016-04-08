package co.xarx.trix.domain.page;

import co.xarx.trix.domain.Identifiable;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class BlockImpl<T extends Identifiable> implements Block {

	private T object;
	private Class objectType;

	public BlockImpl(T object, Class objectType) {
		this.object = object;
		this.objectType = objectType;
	}

	public String getType() {
		return objectType.getSimpleName().toLowerCase();
	}

	@Override
	public T getObject() {
		return object;
	}

	@Override
	@JsonIgnore
	public Class getObjectType() {
		return objectType;
	}
}
