FacadeEntityMethods(entity) ::=

<</*---------------------------------------------------------------------------*/
	@GET
	@Path("/$entity.pluralLowercase$")
	@Produces(MediaType.APPLICATION_JSON) 	
	public ContentResponse<List<$entity.name$Dto\>\> get$entity.plural$(@QueryParam("page") Integer page, @QueryParam("size") Integer size, @QueryParam("sort") List<String\> sort) {
		foward();
		return null;
	}

	@POST
	@Path("/$entity.pluralLowercase$")
	@Consumes(MediaType.APPLICATION_JSON)	
	public Response post$entity.name$($entity.name$Dto $entity.nameLowercase$Dto) {
		$entity.name$ $entity.nameLowercase$ = new $entity.name$();
		copy($entity.nameLowercase$, $entity.nameLowercase$Dto);
		$entity.nameLowercase$ = $entity.nameLowercase$Dao.save($entity.nameLowercase$);
		URI location = uriInfo.getBaseUriBuilder().path("/$entity.pluralLowercase$/" + $entity.nameLowercase$.$entity.id.name$).build();		
		return Response.status(Status.CREATED).location(location).build();
	}

	private void copy($entity.name$ $entity.nameLowercase$, $entity.name$Dto $entity.nameLowercase$Dto) {		
		$entity.fields:{field | $entity.nameLowercase$.$field.name$ = $if(field.entity)$find$field.entity.name$($entity.nameLowercase$Dto.$field.name$)$else$$entity.nameLowercase$Dto.$field.name$$endif$;}; separator="\n"$
	}

	private $entity.name$ find$entity.name$(String $entity.nameLowercase$Uri) {		
		int index = $entity.nameLowercase$Uri.lastIndexOf("/");
		String suffix = $entity.nameLowercase$Uri.substring(index + 1);
		Integer $entity.nameLowercase$$entity.id.nameUppercase$ = new $entity.id.type$(suffix);
		return $entity.nameLowercase$Dao.findOne($entity.nameLowercase$$entity.id.nameUppercase$);		
	}
	
	private Set<$entity.name$> find$entity.name$(Set<String> $entity.nameLowercase$Uris) {
		Set<$entity.name$> $entity.pluralLowercase$ = new HashSet<$entity.name$>();
		for (String $entity.nameLowercase$Uri : $entity.nameLowercase$Uris) {
			$entity.name$ $entity.nameLowercase$ = find$entity.name$($entity.nameLowercase$Uri);
			$entity.pluralLowercase$.add($entity.nameLowercase$);
		}
		return $entity.pluralLowercase$;
	}

	@GET
	@Path("/$entity.pluralLowercase$/{$entity.id.name$}")
	@Produces(MediaType.APPLICATION_JSON)
	public $entity.name$Dto get$entity.name$(@PathParam("$entity.id.name$") $entity.id.type$ $entity.id.name$) {
		foward();
		return null;
	}
	
	@PUT
	@Path("/$entity.pluralLowercase$/{$entity.id.name$}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response put$entity.name$(@PathParam("$entity.id.name$") $entity.id.type$ $entity.id.name$, $entity.name$Dto $entity.nameLowercase$Dto) {
		$entity.name$ $entity.nameLowercase$ = $entity.nameLowercase$Dao.findOne($entity.id.name$);
		if ($entity.nameLowercase$ == null) {
			return Response.status(Status.NOT_FOUND).build();
		} else {
			copy($entity.nameLowercase$, $entity.nameLowercase$Dto);
			$entity.nameLowercase$ = $entity.nameLowercase$Dao.save($entity.nameLowercase$);
			return Response.status(Status.NO_CONTENT).build();			
		}
	}	
	
	@DELETE
	@Path("/$entity.pluralLowercase$/{$entity.id.name$}")
	public Response delete$entity.name$(@PathParam("$entity.id.name$") $entity.id.type$ $entity.id.name$) {
		$entity.name$ $entity.nameLowercase$ = $entity.nameLowercase$Dao.findOne($entity.id.name$);
		if ($entity.nameLowercase$ == null) {
			return Response.status(Status.NOT_FOUND).build();
		} else {
			$entity.nameLowercase$Dao.delete($entity.nameLowercase$);
			return Response.status(Status.NO_CONTENT).build();			
		}	
	}	

	$entity.queries:{query | $FacadeQueries(entity, query)$}; separator="\n\n"$
	$entity.relationships:{relationship | $FacadeRelationshipMethods(entity, relationship)$}; separator="\n\n"$
/*---------------------------------------------------------------------------*/>>