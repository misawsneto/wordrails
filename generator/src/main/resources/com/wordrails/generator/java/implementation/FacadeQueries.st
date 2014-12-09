FacadeQueries(entity, query) ::=

<<@GET
@Path("/$entity.pluralLowercase$/search/$query.name$")
@Produces(MediaType.APPLICATION_JSON)
public ContentResponse<List<$entity.name$Dto\>\> $query.name$($query.parameters:{parameter | @QueryParam("$parameter.name$") $parameter.type$ $parameter.name$}; separator=", "$) {
	foward();
	return null;
}>>