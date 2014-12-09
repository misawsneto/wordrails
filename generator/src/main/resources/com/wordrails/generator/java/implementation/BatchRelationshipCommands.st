BatchRelationshipCommands(facade, entity, relationship) ::=

<<$if(!relationship.mappedBy)$class Put$entity.name$$relationship.nameUppercase$ extends Command {
	public String $entity.nameLowercase$;
	public $if(relationship.collection)$List<String>$else$String$endif$ $relationship.name$;

	@Override
	Response execute(List<Response> results, $facade$ facade) {
		String uri = toUriList($entity.nameLowercase$, results);
		int index = uri.lastIndexOf('/');
		String suffix = $entity.nameLowercase$.substring(index + 1); 
		$entity.id.type$ $entity.id.name$ = new $entity.id.type$(suffix); 
		return facade.put$entity.name$$relationship.nameUppercase$($entity.id.name$, toUriList($relationship.name$, results));
	}	
}

class Delete$entity.name$$relationship.nameUppercase$ extends Command {
	public String $entity.nameLowercase$;
	$if(relationship.collection)$public List<String> $relationship.name$;$endif$

	@Override
	Response execute(List<Response> results, $facade$ facade) {
		String uri = toUriList($entity.nameLowercase$, results);
		int index = uri.lastIndexOf('/');
		String suffix = $entity.nameLowercase$.substring(index + 1); 
		$entity.id.type$ $entity.id.name$ = new $entity.id.type$(suffix); 	
		return facade.delete$entity.name$$relationship.nameUppercase$($entity.id.name$$if(relationship.collection)$, toUriList($relationship.name$, results)$endif$);
	}
}$endif$>>