FacadeMethods(entity) ::=

<</*---------------------------------------------------------------------------*/
	if (that.get$entity.plural$) {
		console.log("get$entity.plural$");
	}
	that.get$entity.plural$ = function(_page, _size, _sort, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/$entity.pluralLowercase$",
			data: {
				page: _page,
				size: _size,
				sort: _sort,
				projection: projection
			},
			success: function(_data, _textStatus, _jqXHR) {
				if (_success) {
					_success(_data.content, _textStatus, _jqXHR);
				}
			},
			error: _error,
			complete: _complete
		});
	};

	if (that.post$entity.name$) {
		console.log("post$entity.name$");
	}
	that.post$entity.name$ = function($entity.nameLowercase$, _success, _error, _complete) {
		return that._ajax({
			type: "POST",
			url: _url + "/api/$entity.pluralLowercase$",
			contentType: "application/json",
			data: JSON.stringify($entity.nameLowercase$),
			success: function(_data, _textStatus, _jqXHR) {
				if (_success) {
					var _value = _jqXHR.getResponseHeader("Location");
					if (_value) {
						var _index = _value.lastIndexOf("/");
						var _suffix = _value.substring(_index + 1);
						var id = _suffix;
						_success(id, _textStatus, _jqXHR);
					}
				}
			},
			error: _error,
			complete: _complete
		});
	};

	if (that.get$entity.name$) {
		console.log("get$entity.name$");
	}
	that.get$entity.name$ = function($entity.id.name$, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/$entity.pluralLowercase$/" + $entity.id.name$,
			data: {
				projection: projection
			},
			success: _success,
			error: _error,
			complete: _complete
		});
	};

	if (that.put$entity.name$) {
		console.log("put$entity.name$");
	}
	that.put$entity.name$ = function($entity.nameLowercase$, _success, _error, _complete) {
		return that._ajax({
			type: "PUT",
			url: _url + "/api/$entity.pluralLowercase$/" + $entity.nameLowercase$.$entity.id.name$,
			contentType: "application/json",
			data: JSON.stringify($entity.nameLowercase$),
			success: _success,
			error: _error,
			complete: _complete
		});
	};

	if (that.delete$entity.name$) {
		console.log("delete$entity.name$");
	}
	that.delete$entity.name$ = function($entity.id.name$, _success, _error, _complete) {
		return that._ajax({
			type: "DELETE",
			url: _url + "/api/$entity.pluralLowercase$/" + $entity.id.name$,
			success: _success,
			error: _error,
			complete: _complete
		});
	};

	$entity.queries:{query | $FacadeQueries(entity, query)$}; separator="\n\n"$

	$entity.relationships:{relationship | $FacadeRelationshipMethods(entity, relationship)$}; separator="\n\n"$
/*---------------------------------------------------------------------------*/>>