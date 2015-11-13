Dto(entity) ::=

<<function $entity.name$Dto($entity.id.name$, $entity.fields:{field | $field.name$}; separator=", "$) {
	return {
		$entity.id.name$: $entity.id.name$,
		$entity.fields:{field | $field.name$: $field.name$}; separator=",\n"$
	};
}>>