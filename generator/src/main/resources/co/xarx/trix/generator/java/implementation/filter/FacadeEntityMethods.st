FacadeEntityMethods(entity) ::=

<</*---------------------------------------------------------------------------*/
	@GET
	@Path("/$entity.pluralLowercase$")
	public Response get$entity.plural$(@QueryParam("page") Integer page, @QueryParam("size") Integer size, @QueryParam("sort") List<String\> sort) {
		if (isGet$entity.plural$Authorized()) {
			return forward();
		} else {
			return forbidden();
		}
	}

	protected abstract boolean isGet$entity.plural$Authorized(); 

	@POST
	@Path("/$entity.pluralLowercase$")	
	public Response post$entity.name$() {
		return forward();
	}

	@GET
	@Path("/$entity.pluralLowercase$/{$entity.id.name$}")
	public Response get$entity.name$(@PathParam("$entity.id.name$") $entity.id.type$ $entity.id.name$) {
		if (isGet$entity.name$Authorized($entity.id.name$)) {
			return forward();
		} else {
			return forbidden();
		}
	}
	
	protected abstract boolean isGet$entity.name$Authorized($entity.id.type$ $entity.nameLowercase$$entity.id.nameUppercase$);
	
	@PUT
	@Path("/$entity.pluralLowercase$/{$entity.id.name$}")
	public Response put$entity.name$() {
		return forward();
	}	
	
	@DELETE
	@Path("/$entity.pluralLowercase$/{$entity.id.name$}")
	public Response delete$entity.name$() {
		return forward();
	}	
	
		
	$entity.queries:{query | $FacadeQueries(entity, query)$}; separator="\n\n"$

	$entity.relationships:{relationship | $FacadeRelationshipMethods(entity, relationship)$}; separator="\n\n"$
/*---------------------------------------------------------------------------*/>>