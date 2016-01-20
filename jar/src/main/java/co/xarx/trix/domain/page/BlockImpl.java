package co.xarx.trix.domain.page;

import co.xarx.trix.domain.Identifiable;

public class BlockImpl<T extends Identifiable> implements Block {

	private T object;
	private String objectType;

	public BlockImpl(T object, String objectType) {
		this.object = object;
		this.objectType = objectType;
	}

	@Override
	public T getObject() {
		return object;
	}

	@Override
	public String getObjectType() {
		return objectType;
	}
}
