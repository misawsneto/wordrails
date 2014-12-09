BatchRelationshipMethods(entity, relationship) ::=

<<$if(!relationship.mappedBy)$
public void put$entity.name$$relationship.nameUppercase$(String $entity.nameLowercase$, $if(relationship.collection)$List<String>$else$String$endif$ $relationship.name$) {
	Put$entity.name$$relationship.nameUppercase$ put$entity.name$$relationship.nameUppercase$ = new Put$entity.name$$relationship.nameUppercase$();
	put$entity.name$$relationship.nameUppercase$.$entity.nameLowercase$ = $entity.nameLowercase$;
	put$entity.name$$relationship.nameUppercase$.$relationship.name$ = $relationship.name$;
	commands.add(put$entity.name$$relationship.nameUppercase$); 
}
$endif$>>