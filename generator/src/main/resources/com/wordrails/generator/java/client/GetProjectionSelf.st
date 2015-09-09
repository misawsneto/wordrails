GetProjectionSelf(entity, projection) ::=

<<public String getSelf($projection.name$Dto $projection.nameLowercase$) {
	return _url + "/api/$entity.pluralLowercase$/" + $projection.nameLowercase$.$entity.id.name$;
}>>