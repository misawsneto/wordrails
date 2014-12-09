RelationshipCommands(entity, relationship) ::=

<<$if(!relationship.mappedBy)$class Put$entity.name$$relationship.nameUppercase$ extends Command {
	String $entity.nameLowercase$;
	$if(relationship.collection)$List<String>$else$String$endif$ $relationship.name$;
}

class Delete$entity.name$$relationship.nameUppercase$ extends Command {
	String $entity.nameLowercase$;
	$if(relationship.collection)$List<String> $relationship.name$;$endif$
}$endif$>>