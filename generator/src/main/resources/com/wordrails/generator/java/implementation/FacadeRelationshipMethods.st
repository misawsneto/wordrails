FacadeRelationshipMethods(entity, relationship) ::=

<<$if(relationship.collection)$
@GET
@Path("/$entity.pluralLowercase$/{$entity.id.name$}/$relationship.name$")
@Produces(MediaType.APPLICATION_JSON)
public ContentResponse<$relationship.type$\> get$entity.name$$relationship.nameUppercase$(@PathParam("$entity.id.name$") $entity.id.type$ $entity.id.name$) {
	foward();
	return null;
}
$else$
@GET
@Path("/$entity.pluralLowercase$/{$entity.id.name$}/$relationship.name$")
@Produces(MediaType.APPLICATION_JSON)
public $relationship.type$ get$entity.name$$relationship.nameUppercase$(@PathParam("$entity.id.name$") $entity.id.type$ $entity.id.name$) {
	foward();
	return null;
}
$endif$
$if(!relationship.mappedBy)$
	$if(relationship.collection)$

@PUT
@Path("/$entity.pluralLowercase$/{$entity.id.name$}/$relationship.name$")
@Consumes("text/uri-list")
public Response put$entity.name$$relationship.nameUppercase$(@PathParam("$entity.id.name$") $entity.id.type$ $entity.nameLowercase$$entity.id.nameUppercase$, String $relationship.entity.nameLowercase$Uris) {
	List<$relationship.entity.name$> $relationship.name$ = new ArrayList<$relationship.entity.name$>();
	StringTokenizer tokenizer = new StringTokenizer($relationship.entity.nameLowercase$Uris, "\n");
	while (tokenizer.hasMoreTokens()) {
		String uri = tokenizer.nextToken();
		int index = uri.lastIndexOf("/");
		String suffix = uri.substring(index + 1);
		$relationship.entity.id.type$ $relationship.entity.nameLowercase$$relationship.entity.id.nameUppercase$ = new $relationship.entity.id.type$(suffix);
		$relationship.entity.name$ $relationship.entity.nameLowercase$ = $relationship.entity.nameLowercase$Dao.findOne($relationship.entity.nameLowercase$$relationship.entity.id.nameUppercase$);
		if ($relationship.entity.nameLowercase$ == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		$relationship.name$.add($relationship.entity.nameLowercase$);
	}

	$entity.name$ $entity.nameLowercase$ = $entity.nameLowercase$Dao.findOne($entity.nameLowercase$$entity.id.nameUppercase$);
	if ($entity.nameLowercase$ == null) {
		return Response.status(Status.NOT_FOUND).build();
	}
	$entity.nameLowercase$.$relationship.name$.clear();
	$entity.nameLowercase$.$relationship.name$.addAll($relationship.name$);
	return Response.status(Status.NO_CONTENT).build();
}

@DELETE
@Path("/$entity.pluralLowercase$/{$entity.id.name$}/$relationship.name$")
public Response delete$entity.name$$relationship.nameUppercase$(@PathParam("$entity.id.name$") $entity.id.type$ $entity.nameLowercase$$entity.id.nameUppercase$, String $relationship.entity.nameLowercase$Uris) {
	List<$relationship.entity.name$> $relationship.name$ = new ArrayList<$relationship.entity.name$>();
	StringTokenizer tokenizer = new StringTokenizer($relationship.entity.nameLowercase$Uris, "\n");
	while (tokenizer.hasMoreTokens()) {
		String uri = tokenizer.nextToken();
		int index = uri.lastIndexOf("/");
		String suffix = uri.substring(index + 1);
		$relationship.entity.id.type$ $relationship.entity.nameLowercase$$relationship.entity.id.nameUppercase$ = new $relationship.entity.id.type$(suffix);
		$relationship.entity.name$ $relationship.entity.nameLowercase$ = $relationship.entity.nameLowercase$Dao.findOne($relationship.entity.nameLowercase$$relationship.entity.id.nameUppercase$);
		if ($relationship.entity.nameLowercase$ == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		$relationship.name$.add($relationship.entity.nameLowercase$);
	}

	$entity.name$ $entity.nameLowercase$ = $entity.nameLowercase$Dao.findOne($entity.nameLowercase$$entity.id.nameUppercase$);
	if ($entity.nameLowercase$ == null) {
		return Response.status(Status.NOT_FOUND).build();
	}
	$entity.nameLowercase$.$relationship.name$.removeAll($relationship.name$);
	return Response.status(Status.NO_CONTENT).build();
}
	$else$
	
@PUT
@Path("/$entity.pluralLowercase$/{$entity.id.name$}/$relationship.name$")
@Consumes("text/uri-list")
public Response put$entity.name$$relationship.nameUppercase$(@PathParam("$entity.id.name$") $entity.id.type$ $entity.nameLowercase$$entity.id.nameUppercase$, String $relationship.name$Uri) {
	int index = $relationship.name$Uri.lastIndexOf("/");
	String suffix = $relationship.name$Uri.substring(index + 1);
	$relationship.entity.id.type$ $relationship.name$$relationship.entity.id.nameUppercase$ = new $relationship.entity.id.type$(suffix);
	$relationship.entity.name$ $relationship.name$ = $relationship.entity.nameLowercase$Dao.findOne($relationship.name$$relationship.entity.id.nameUppercase$);
	if ($relationship.name$ == null) {
		return Response.status(Status.NOT_FOUND).build();
	}			
	$entity.name$ $entity.nameLowercase$ = $entity.nameLowercase$Dao.findOne($entity.nameLowercase$$entity.id.nameUppercase$);
	if ($entity.nameLowercase$ == null) {
		return Response.status(Status.NOT_FOUND).build();
	}
	$entity.nameLowercase$.$relationship.name$ = $relationship.name$;
	return Response.status(Status.NO_CONTENT).build();
}

@DELETE
@Path("/$entity.pluralLowercase$/{$entity.id.name$}/$relationship.name$")
public Response delete$entity.name$$relationship.nameUppercase$(@PathParam("$entity.id.name$") $entity.id.type$ $entity.id.name$) {
	$entity.name$ $entity.nameLowercase$ = $entity.nameLowercase$Dao.findOne($entity.id.name$);
	if ($entity.nameLowercase$ == null) {
		return Response.status(Status.NOT_FOUND).build();
	}
	$entity.nameLowercase$.$relationship.name$ = null;
	$entity.nameLowercase$Dao.save($entity.nameLowercase$);
	return Response.status(Status.NO_CONTENT).build();			
}
	$endif$	
$endif$
>>