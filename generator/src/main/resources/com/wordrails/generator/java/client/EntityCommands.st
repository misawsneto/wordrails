EntityCommands(entity) ::=

<</*---------------------------------------------------------------------------*/
class Post$entity.name$ extends Command {
	$entity.name$Dto $entity.nameLowercase$;
}

class Put$entity.name$ extends Command {
	$entity.name$Dto $entity.nameLowercase$;
}

class Delete$entity.name$ extends Command {
	$entity.id.type$ $entity.id.name$;
}

$entity.relationships:{relationship | $RelationshipCommands(entity, relationship)$}; separator="\n\n"$
/*---------------------------------------------------------------------------*/>>