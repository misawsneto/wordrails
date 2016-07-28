Dto(entity) ::=

<<function $entity.name$Dto($entity.id.name$, $entity.fields:{field | $field.name$}; separator=", "$) {
	return {
		$entity.id.name$: $entity.id.name$,
		$entity.fields:{field | $field.name$: $field.name$}; separator=",\n"$
	};
}
$entity.name$Dto.getSelf = function(object){
	var id = object.id ? object.id : object.$entity.nameLowercase$Id;
	return TRIX_BACKEND + "/api/$entity.pluralLowercase$/" + id;
}>>