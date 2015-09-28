FacadeMethods(entity) ::=

<</*---------------------------------------------------------------------------*/	
	public List<$entity.name$Dto> get$entity.plural$(Integer _page, Integer _size, List<String> _sort) {
		return _facade.get$entity.plural$(_page, _size, _sort).content;
	}
	
	public void get$entity.plural$(Integer _page, Integer _size, List<String> _sort, final Callback<List<$entity.name$Dto\>\> _callback) {
		_facade.get$entity.plural$(_page, _size, _sort, new Callback<ContentResponse<List<$entity.name$Dto\>\>\>() {
			@Override
			public void success(ContentResponse<List<$entity.name$Dto\>\> _t, Response _response) {
				_callback.success(_t.content, _response);
			}

			@Override
			public void failure(RetrofitError _error) {
				_callback.failure(_error);
			}		
		});
	}	

	public $entity.id.type$ post$entity.name$($entity.name$Dto $entity.nameLowercase$) {
		Response _response = _facade.post$entity.name$($entity.nameLowercase$);
		int _status = _response.getStatus();
		if (_status != 201) {
			throw new RuntimeException();
		}
		for (Header _header : _response.getHeaders()) {
			String _name = _header.getName();
			if ("Location".equals(_name)) {
				String _value = _header.getValue();
				$entity.nameLowercase$.setSelf(_value);
				int _index = _value.lastIndexOf('/');
				String _suffix = _value.substring(_index + 1);
				$entity.id.type$ $entity.id.name$ = new $entity.id.type$(_suffix);
				$entity.nameLowercase$.$entity.id.name$ = $entity.id.name$;  
				return $entity.id.name$;
			}
		}
		throw new RuntimeException();
	}

	public void post$entity.name$($entity.name$Dto $entity.nameLowercase$, final Callback<$entity.id.type$\> _callback) {
		_facade.post$entity.name$($entity.nameLowercase$, new Callback<Void\>() {
			@Override
			public void success(Void _t, Response _response) {
				int _status = _response.getStatus();
				if (_status != 201) {
					RetrofitError _error = RetrofitError.unexpectedError(null, new RuntimeException());
					_callback.failure(_error);
				}
				for (Header _header : _response.getHeaders()) {
					String _name = _header.getName();
					if ("Location".equals(_name)) {
						String _value = _header.getValue();
						int _index = _value.lastIndexOf('/');
						String _suffix = _value.substring(_index + 1); 
						$entity.id.type$ $entity.id.name$ = new $entity.id.type$(_suffix); 
						_callback.success($entity.id.name$, _response);
						return;
					}
				}
				RetrofitError _error = RetrofitError.unexpectedError(null, new RuntimeException());
				_callback.failure(_error);				
			}

			@Override
			public void failure(RetrofitError error) {
				_callback.failure(error);
			}		
		});	
	}	
	
	public $entity.name$Dto get$entity.name$($entity.id.type$ $entity.id.name$) {
		return _facade.get$entity.name$($entity.id.name$);
	}
	
	public void get$entity.name$($entity.id.type$ $entity.id.name$, Callback<$entity.name$Dto\> _callback) {
		_facade.get$entity.name$($entity.id.name$, _callback);
	}	
	
	public void put$entity.name$($entity.name$Dto $entity.nameLowercase$) {
		Response _response = _facade.put$entity.name$($entity.nameLowercase$.$entity.id.name$, $entity.nameLowercase$);
		int _status = _response.getStatus();
		if (_status != 204) {
			throw new RuntimeException();
		}
	}
	
	public void put$entity.name$($entity.name$Dto $entity.nameLowercase$, final Callback<Void> _callback) {	
		_facade.put$entity.name$($entity.nameLowercase$.$entity.id.name$, $entity.nameLowercase$, new Callback<Void>() {
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
	
	public void delete$entity.name$($entity.id.type$ $entity.id.name$) {
		Response _response = _facade.delete$entity.name$($entity.id.name$);
		int _status = _response.getStatus();
		if (_status != 204) {
			throw new RuntimeException();
		}		
	}
	
	public void delete$entity.name$($entity.id.type$ $entity.id.name$, final Callback<Void> _callback) {
		_facade.delete$entity.name$($entity.id.name$, new Callback<Void>() {
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
	
	$entity.projections:{projection | $FacadeProjectionMethods(entity, projection)$}; separator="\n\n"$
	
	$entity.queries:{query | $FacadeQueries(entity, query)$}; separator="\n\n"$	
	
	$entity.relationships:{relationship | $FacadeRelationshipMethods(entity, relationship)$}; separator="\n\n"$
/*---------------------------------------------------------------------------*/>>