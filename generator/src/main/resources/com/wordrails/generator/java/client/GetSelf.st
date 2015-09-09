GetSelf(entity) ::=

<<public String getSelf($entity.name$Dto $entity.nameLowercase$) {
	return _url + "/api/$entity.pluralLowercase$/" + $entity.nameLowercase$.$entity.id.name$;
}

$entity.projections:{projection | $GetProjectionSelf(entity, projection)$}; separator="\n\n"$>>