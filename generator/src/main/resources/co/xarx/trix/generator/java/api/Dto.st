Dto(package, entity) ::=

<<package $package$;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class $entity.name$Dto extends EntityDto {
	public $entity.id.type$ $entity.id.name$;
	$entity.fields:{field | public $field.type$ $field.name$;}; separator="\n"$

	@Override
	public String toString() {
		return "[" + $entity.id.name$ + ", " + $entity.fields:{field | $field.name$}; separator=" + \", \" + "$ + "]";
	}
}>>