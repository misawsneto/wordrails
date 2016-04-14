package co.xarx.trix.domain.page;

import co.xarx.trix.domain.Identifiable;
import lombok.Getter;

@Getter
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
}
