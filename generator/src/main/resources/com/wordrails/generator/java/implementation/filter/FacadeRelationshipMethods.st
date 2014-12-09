FacadeRelationshipMethods(entity, relationship) ::=

<<@GET
@Path("/$entity.pluralLowercase$/{$entity.id.name$}/$relationship.name$")
public Response get$entity.name$$relationship.nameUppercase$(@PathParam("$entity.id.name$") $entity.id.type$ $entity.id.name$) {
	if (isGet$entity.name$$relationship.nameUppercase$Authorized($entity.id.name$)) {
		return forward();
	} else {
		return forbidden();
	}
}

protected abstract boolean isGet$entity.name$$relationship.nameUppercase$Authorized($entity.id.type$ $entity.nameLowercase$$entity.id.nameUppercase$);

@PATCH
@Path("/$entity.pluralLowercase$/{$entity.id.name$}/$relationship.name$")
public Response patch$entity.name$$relationship.nameUppercase$() {
	return forward();
}

@PUT
@Path("/$entity.pluralLowercase$/{$entity.id.name$}/$relationship.name$")
public Response put$entity.name$$relationship.nameUppercase$() {
	return forward();
}

@DELETE
@Path("/$entity.pluralLowercase$/{$entity.id.name$}/$relationship.name$")
public Response delete$entity.name$$relationship.nameUppercase$(@PathParam("$entity.id.name$") $entity.id.type$ $entity.id.name$) {
	return forward();
}>>