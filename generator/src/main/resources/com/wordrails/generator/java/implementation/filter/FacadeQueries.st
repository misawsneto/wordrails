FacadeQueries(entity, query) ::=

<<@GET
@Path("/$entity.pluralLowercase$/search/$query.name$")
public Response $query.name$($query.parameters:{parameter | @QueryParam("$parameter.name$") $parameter.type$ $parameter.name$}; separator=", "$) {
	if (is$query.nameUppercase$Authorized($query.parameters:{parameter | $parameter.name$}; separator=", "$)) {
		return forward();
	} else {
		return forbidden();
	}		
}

protected abstract boolean is$query.nameUppercase$Authorized($query.parameters:{parameter | $parameter.type$ $parameter.name$}; separator=", "$);>>