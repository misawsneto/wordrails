package co.xarx.trix.generator.domain;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TrixField extends AbstractField {

	public boolean isList;
	public boolean isMap;
	public boolean isIncludeAsReference;
	public Class<?> type;
	public Class<?> genericType;
	public String name;
	public String parameterizedGenericTypeName;

	@Override
	public boolean isId() {
		return false;
	}

	@Override
	public boolean isMappedBy() {
		return false;
	}


}
