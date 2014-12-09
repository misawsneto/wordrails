FacadeProjectionMethods(entity, projection) ::=

<<public List<$projection.name$Dto> get$entity.plural$_$projection.name$(Integer _page, Integer _size, List<String> _sort) {
	return _facade.get$entity.plural$_$projection.name$(_page, _size, _sort, "$projection.nameLowercase$").content;
}

public void get$entity.plural$_$projection.name$(Integer _page, Integer _size, List<String> _sort, final Callback<List<$projection.name$Dto\>\> _callback) {
	_facade.get$entity.plural$_$projection.name$(_page, _size, _sort, "$projection.nameLowercase$", new Callback<ContentResponse<List<$projection.name$Dto\>\>\>() {
		@Override
		public void success(ContentResponse<List<$projection.name$Dto\>\> _t, Response _response) {
			_callback.success(_t.content, _response);
		}

		@Override
		public void failure(RetrofitError _error) {
			_callback.failure(_error);
		}		
	});
}	

public $projection.name$Dto get$entity.name$_$projection.name$($entity.id.type$ $entity.id.name$) {
	return _facade.get$entity.name$_$projection.name$($entity.id.name$, "$projection.nameLowercase$");
}

public void get$entity.name$_$projection.name$($entity.id.type$ $entity.id.name$, Callback<$projection.name$Dto\> _callback) {
	_facade.get$entity.name$_$projection.name$($entity.id.name$, "$projection.nameLowercase$", _callback);
}>>