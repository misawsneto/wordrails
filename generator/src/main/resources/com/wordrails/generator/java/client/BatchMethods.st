BatchMethods(entity) ::=

<</*---------------------------------------------------------------------------*/	
	public String post$entity.name$($entity.name$Dto $entity.nameLowercase$) {
		Post$entity.name$ post$entity.name$ = new Post$entity.name$();
		post$entity.name$.$entity.nameLowercase$ = $entity.nameLowercase$;
		commands.add(post$entity.name$);
		int index = commands.size() - 1;
		String href = "{" + index + "}";
		$entity.nameLowercase$.setSelf(href);
		return href;		
	}
	
	public void put$entity.name$($entity.name$Dto $entity.nameLowercase$) {
		Put$entity.name$ put$entity.name$ = new Put$entity.name$();
		put$entity.name$.$entity.nameLowercase$ = $entity.nameLowercase$;
		commands.add(put$entity.name$);	
	}

	public void delete$entity.name$($entity.id.type$ $entity.id.name$) {
		Delete$entity.name$ delete$entity.name$ = new Delete$entity.name$();
		delete$entity.name$.$entity.id.name$ = $entity.id.name$;
		commands.add(delete$entity.name$);	
	}
		
	$entity.relationships:{relationship | $BatchRelationshipMethods(entity, relationship)$}; separator="\n\n"$
/*---------------------------------------------------------------------------*/>>