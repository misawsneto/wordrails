package co.xarx.trix.generator.domain;

import javax.persistence.Id;
import java.lang.reflect.Field;

public class TrixField {

	private Field field;

	private boolean isId;

	public TrixField(Field field) {
		this.field = field;
	}

	boolean isId() {
		if(field != null) {
			return field.isAnnotationPresent(Id.class);
		}

		return isId;
	}
}
