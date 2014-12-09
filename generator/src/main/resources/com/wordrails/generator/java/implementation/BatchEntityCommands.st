BatchEntityCommands(facade, entity) ::=

<</*---------------------------------------------------------------------------*/
class Post$entity.name$ extends Command {
	public $entity.name$Dto $entity.nameLowercase$;

	@Override
	Response execute(List<Response> results, $facade$ facade) {
		$entity.fields:{field | $if(field.entity)$$entity.nameLowercase$.$field.name$ = replace($entity.nameLowercase$.$field.name$, results);$endif$}; separator="\n"$
		return facade.post$entity.name$($entity.nameLowercase$);	
	}
}

class Put$entity.name$ extends Command {
	public $entity.name$Dto $entity.nameLowercase$;
	
	@Override
	Response execute(List<Response> results, $facade$ facade) {
		return facade.put$entity.name$($entity.nameLowercase$.$entity.id.name$, $entity.nameLowercase$);
	}	
}

class Delete$entity.name$ extends Command {
	public $entity.id.type$ $entity.id.name$;

	@Override
	Response execute(List<Response> results, $facade$ facade) {
		return facade.delete$entity.name$($entity.id.name$);
	}	
}

$entity.relationships:{relationship | $BatchRelationshipCommands(facade, entity, relationship)$}; separator="\n\n"$
/*---------------------------------------------------------------------------*/>>