package co.xarx.trix.domain.page;

import co.xarx.trix.domain.Identifiable;

public class BlockImpl<T extends Identifiable> implements Block {

	private T object;
	private Class objectType;

	public BlockImpl(T object, Class objectType) {
		this.object = object;
		this.objectType = objectType;
	}

	@Override
	public T getObject() {
		return object;
	}

	@Override
	public Class getObjectType() {
		return objectType;
	}
}
