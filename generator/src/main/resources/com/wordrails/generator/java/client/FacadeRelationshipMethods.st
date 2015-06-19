FacadeRelationshipMethods(entity, relationship) ::=

<<$if(relationship.collection)$public $relationship.type$ get$entity.name$$relationship.nameUppercase$($entity.id.type$ $entity.id.name$) {
	return _facade.get$entity.name$$relationship.nameUppercase$($entity.id.name$).content;
}

public void get$entity.name$$relationship.nameUppercase$($entity.id.type$ $entity.id.name$, final Callback<$relationship.type$> _callback) {
	_facade.get$entity.name$$relationship.nameUppercase$($entity.id.name$, new Callback<ContentResponse<$relationship.type$\>\>() {
		@Override
		public void success(ContentResponse<$relationship.type$\> _t, Response _response) {
			_callback.success(_t.content, _response);
		}

		@Override
		public void failure(RetrofitError _error) {
			_callback.failure(_error);
		}		
	});
}$else$public $relationship.type$ get$entity.name$$relationship.nameUppercase$($entity.id.type$ $entity.id.name$) {
	return _facade.get$entity.name$$relationship.nameUppercase$($entity.id.name$);
}

public void get$entity.name$$relationship.nameUppercase$($entity.id.type$ $entity.id.name$, Callback<$relationship.type$> _callback) {
	_facade.get$entity.name$$relationship.nameUppercase$($entity.id.name$, _callback);
}$endif$$if(!relationship.mappedBy)$

$if(relationship.collection)$public void patch$entity.name$$relationship.nameUppercase$($entity.id.type$ $entity.id.name$, List<String> $relationship.name$) {
	StringBuilder _uris = new StringBuilder();
	for (String _uri : $relationship.name$) {
		_uris.append(_uri);
		_uris.append('\n');
	}
	Response _response = _facade.patch$entity.name$$relationship.nameUppercase$($entity.id.name$, new TypedString(_uris.toString()));
	int status = _response.getStatus();
	if (status != 204) {
		throw new RuntimeException();
	}		
}

public void patch$entity.name$$relationship.nameUppercase$($entity.id.type$ $entity.id.name$, List<String> $relationship.name$, final Callback<Void> _callback) {
	StringBuilder _uris = new StringBuilder();
	for (String _uri : $relationship.name$) {
		_uris.append(_uri);
		_uris.append('\n');
	}
	_facade.patch$entity.name$$relationship.nameUppercase$($entity.id.name$, new TypedString(_uris.toString()), new Callback<Void>() {
		@Override
		public void success(Void _t, Response _response) {
			int _status = _response.getStatus();
			if (_status != 204) {
				RetrofitError _error = RetrofitError.unexpectedError(null, new RuntimeException());
				_callback.failure(_error);
				return;
			}			
			_callback.success(null, _response);
		}

		@Override
		public void failure(RetrofitError error) {
			_callback.failure(error);
		}	
	});
}

public void put$entity.name$$relationship.nameUppercase$($entity.id.type$ $entity.id.name$, List<String> $relationship.name$) {
	StringBuilder _uris = new StringBuilder();
	for (String _uri : $relationship.name$) {
		_uris.append(_uri);
		_uris.append('\n');
	}
	Response _response = _facade.put$entity.name$$relationship.nameUppercase$($entity.id.name$, new TypedString(_uris.toString()));
	int status = _response.getStatus();
	if (status != 204) {
		throw new RuntimeException();
	}		
}

public void put$entity.name$$relationship.nameUppercase$($entity.id.type$ $entity.id.name$, List<String> $relationship.name$, final Callback<Void> _callback) {
	StringBuilder _uris = new StringBuilder();
	for (String _uri : $relationship.name$) {
		_uris.append(_uri);
		_uris.append('\n');
	}
	_facade.put$entity.name$$relationship.nameUppercase$($entity.id.name$, new TypedString(_uris.toString()), new Callback<Void>() {
		@Override
		public void success(Void _t, Response _response) {
			int _status = _response.getStatus();
			if (_status != 204) {
				RetrofitError _error = RetrofitError.unexpectedError(null, new RuntimeException());
				_callback.failure(_error);
				return;
			}			
			_callback.success(null, _response);
		}

		@Override
		public void failure(RetrofitError error) {
			_callback.failure(error);
		}	
	});
}$else$public void put$entity.name$$relationship.nameUppercase$($entity.id.type$ $entity.id.name$, String $relationship.name$) {
	Response _response;
	if ($relationship.name$ == null) {
		_response = _facade.delete$entity.name$$relationship.nameUppercase$($entity.id.name$);
	} else {
		_response = _facade.put$entity.name$$relationship.nameUppercase$($entity.id.name$, new TypedString($relationship.name$));		
	}	
	int _status = _response.getStatus();
	if (_status != 204) {
		throw new RuntimeException();
	}
}

public void put$entity.name$$relationship.nameUppercase$($entity.id.type$ $entity.id.name$, String $relationship.name$, final Callback<Void> _callback) {
	Callback<Void> _callback2 = new Callback<Void>() {
		@Override
		public void success(Void _t, Response _response) {
			int _status = _response.getStatus();
			if (_status != 204) {
				RetrofitError _error = RetrofitError.unexpectedError(null, new RuntimeException());
				_callback.failure(_error);
				return;
			}			
			_callback.success(null, _response);
		}

		@Override
		public void failure(RetrofitError error) {
			_callback.failure(error);
		}
	};
	if ($relationship.name$ == null) {
		_facade.delete$entity.name$$relationship.nameUppercase$($entity.id.name$, _callback2);
	} else {
		_facade.put$entity.name$$relationship.nameUppercase$($entity.id.name$, new TypedString($relationship.name$), _callback2);
	}	
}$endif$$endif$

$relationship.entity.projections:{projection | $FacadeProjectionRelationshipMethods(entity, relationship, projection)$}; separator="\n\n"$>>