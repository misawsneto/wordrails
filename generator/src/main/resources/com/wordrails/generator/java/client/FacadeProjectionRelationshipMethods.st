FacadeProjectionRelationshipMethods(entity, relationship, projection) ::=

<<$if(relationship.collection)$public $relationship.collectionType$<$projection.name$Dto\> get$entity.name$$relationship.nameUppercase$_$projection.name$($entity.id.type$ $entity.id.name$) {
	return _facade.get$entity.name$$relationship.nameUppercase$_$projection.name$($entity.id.name$, "$projection.nameLowercase$").content;
}

public void get$entity.name$$relationship.nameUppercase$_$projection.name$($entity.id.type$ $entity.id.name$, final Callback<$relationship.collectionType$<$projection.name$Dto\>\> _callback) {
	_facade.get$entity.name$$relationship.nameUppercase$_$projection.name$($entity.id.name$, "$projection.nameLowercase$", new Callback<ContentResponse<$relationship.collectionType$<$projection.name$Dto\>\>\>() {
		@Override
		public void success(ContentResponse<$relationship.collectionType$<$projection.name$Dto\>\> _t, Response _response) {
			_callback.success(_t.content, _response);
		}

		@Override
		public void failure(RetrofitError _error) {
			_callback.failure(_error);
		}		
	});
}$else$public $projection.name$Dto get$entity.name$$relationship.nameUppercase$_$projection.name$($entity.id.type$ $entity.id.name$) {
	return _facade.get$entity.name$$relationship.nameUppercase$_$projection.name$($entity.id.name$, "$projection.nameLowercase$");
}

public void get$entity.name$$relationship.nameUppercase$_$projection.name$($entity.id.type$ $entity.id.name$, Callback<$projection.name$Dto> _callback) {
	_facade.get$entity.name$$relationship.nameUppercase$_$projection.name$($entity.id.name$, "$projection.nameLowercase$", _callback);
}$endif$>>