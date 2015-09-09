FacadeProjectionQueries(entity, query, projection) ::=

<<$if(query.collection)$public List<$projection.name$Dto\> $query.name$_$projection.name$($query.parameters:{parameter | $parameter.type$ $parameter.name$}; separator=", "$) {
	return _facade.$query.name$_$projection.name$($query.parameters:{parameter | $parameter.name$, }$"$projection.nameLowercase$").content;
}

public void $query.name$_$projection.name$($query.parameters:{parameter | $parameter.type$ $parameter.name$}; separator=", "$, final Callback<List<$projection.name$Dto\>\> _callback) {
	_facade.$query.name$_$projection.name$($query.parameters:{parameter | $parameter.name$, }$"$projection.nameLowercase$", new Callback<ContentResponse<List<$projection.name$Dto\>\>\>() {
		@Override
		public void success(ContentResponse<List<$projection.name$Dto\>\> _t, Response _response) {
			_callback.success(_t.content, _response);
		}

		@Override
		public void failure(RetrofitError _error) {
			_callback.failure(_error);
		}		
	});
}$else$public $projection.name$Dto $query.name$_$projection.name$($query.parameters:{parameter | $parameter.type$ $parameter.name$}; separator=", "$) {
	List<$projection.name$Dto> $entity.pluralLowercase$ = _facade.$query.name$_$projection.name$($query.parameters:{parameter | $parameter.name$, }$"$projection.nameLowercase$").content;
	if ($entity.pluralLowercase$.isEmpty()) {
		return null;
	} else {
		return $entity.pluralLowercase$.get(0);		 
	} 
}

public void $query.name$_$projection.name$($query.parameters:{parameter | $parameter.type$ $parameter.name$}; separator=", "$, final Callback<$projection.name$Dto\> _callback) {
	_facade.$query.name$_$projection.name$($query.parameters:{parameter | $parameter.name$, }$"$projection.nameLowercase$", new Callback<ContentResponse<List<$projection.name$Dto\>\>\>() {
		@Override
		public void success(ContentResponse<List<$projection.name$Dto\>\> _t, Response _response) {
			List<$projection.name$Dto> $entity.pluralLowercase$ = _t.content;
			if ($entity.pluralLowercase$.isEmpty()) {
				_callback.success(null, _response);
			} else {
				_callback.success($entity.pluralLowercase$.get(0), _response);		 
			} 
		}

		@Override
		public void failure(RetrofitError _error) {
			_callback.failure(_error);
		}		
	});
}$endif$>>