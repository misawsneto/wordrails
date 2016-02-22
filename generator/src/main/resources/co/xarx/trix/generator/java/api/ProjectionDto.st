ProjectionDto(package, entity) ::=

<<package $package$;

public class $entity.name$Dto extends EntityDto {
	$entity.fields:{field | public $field.type$ $field.name$;}; separator="\n"$

	@Override
	public String toString() {
		return "[" + $entity.fields:{field | $field.name$}; separator=" + \", \" + "$ + "]";
	}
}>>