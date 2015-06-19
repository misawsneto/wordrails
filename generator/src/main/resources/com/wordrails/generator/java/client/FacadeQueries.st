FacadeQueries(entity, query) ::=

<<$if(query.collection)$public List<$entity.name$Dto\> $query.name$($query.parameters:{parameter | $parameter.type$ $parameter.name$}; separator=", "$) {
	return _facade.$query.name$($query.parameters:{parameter | $parameter.name$}; separator=", "$).content;
}

public void $query.name$($query.parameters:{parameter | $parameter.type$ $parameter.name$}; separator=", "$, final Callback<List<$entity.name$Dto\>\> _callback) {
	_facade.$query.name$($query.parameters:{parameter | $parameter.name$}; separator=", "$, new Callback<ContentResponse<List<$entity.name$Dto\>\>\>() {
		@Override
		public void success(ContentResponse<List<$entity.name$Dto\>\> _t, Response _response) {
			_callback.success(_t.content, _response);
		}

		@Override
		public void failure(RetrofitError _error) {
			_callback.failure(_error);
		}		
	});
}$else$public $entity.name$Dto $query.name$($query.parameters:{parameter | $parameter.type$ $parameter.name$}; separator=", "$) {
	List<$entity.name$Dto> $entity.pluralLowercase$ = _facade.$query.name$($query.parameters:{parameter | $parameter.name$}; separator=", "$).content;
	if ($entity.pluralLowercase$.isEmpty()) {
		return null;
	} else {
		return $entity.pluralLowercase$.get(0);		 
	} 
}

public void $query.name$($query.parameters:{parameter | $parameter.type$ $parameter.name$}; separator=", "$, final Callback<$entity.name$Dto\> _callback) {
	_facade.$query.name$($query.parameters:{parameter | $parameter.name$}; separator=", "$, new Callback<ContentResponse<List<$entity.name$Dto\>\>\>() {
		@Override
		public void success(ContentResponse<List<$entity.name$Dto\>\> _t, Response _response) {
			List<$entity.name$Dto> $entity.pluralLowercase$ = _t.content;
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
}$endif$

$entity.projections:{projection | $FacadeProjectionQueries(entity, query, projection)$}; separator="\n\n"$>>