FacadeRelationshipMethods(entity, relationship) ::=

<<$if(relationship.collection)$
if (that.get$entity.name$$relationship.nameUppercase$) {
	console.log("get$entity.name$$relationship.nameUppercase$");
}
that.get$entity.name$$relationship.nameUppercase$ = function($entity.id.name$, _success, _error, _complete, projection) {
	return that._ajax({
		url: _url + "/api/$entity.pluralLowercase$/" + $entity.id.name$ + "/$relationship.name$",
		data: {
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
};$else$
if (that.get$entity.name$$relationship.nameUppercase$) {
	console.log("get$entity.name$$relationship.nameUppercase$");
}
that.get$entity.name$$relationship.nameUppercase$ = function($entity.id.name$, _success, _error, _complete, projection) {
	return that._ajax({
		url: _url + "/api/$entity.pluralLowercase$/" + $entity.id.name$ + "/$relationship.name$",
		data: {
			projection: projection
		},
		success: _success,
		error: _error,
		complete: _complete
	});
};
$endif$$if(!relationship.mappedBy)$$if(relationship.collection)$
if (that.patch$entity.name$$relationship.nameUppercase$) {
	console.log("patch$entity.name$$relationship.nameUppercase$");
}
that.patch$entity.name$$relationship.nameUppercase$ = function($entity.id.name$, $relationship.name$, _success, _error, _complete) {		
	var _data = "";
	for (var i = 0; i < $relationship.name$.length; ++i) {
		_data += $relationship.name$[i] + "\n";
	}
	return that._ajax({
		type: "PATCH",
		url: _url + "/api/$entity.pluralLowercase$/" + $entity.id.name$ + "/$relationship.name$",
		contentType: "text/uri-list",
		data: _data,
		success: _success,
		error: _error,
		complete: _complete
	});
};

if (that.put$entity.name$$relationship.nameUppercase$) {
	console.log("put$entity.name$$relationship.nameUppercase$");
}
that.put$entity.name$$relationship.nameUppercase$ = function($entity.id.name$, $relationship.name$, _success, _error, _complete) {		
	var _data = "";
	for (var i = 0; i < $relationship.name$.length; ++i) {
		_data += $relationship.name$[i] + "\n";
	}
	return that._ajax({
		type: "PUT",
		url: _url + "/api/$entity.pluralLowercase$/" + $entity.id.name$ + "/$relationship.name$",
		contentType: "text/uri-list",
		data: _data,
		success: _success,
		error: _error,
		complete: _complete
	});
};$else$

if (that.put$entity.name$$relationship.nameUppercase$) {
	console.log("put$entity.name$$relationship.nameUppercase$");
}
that.put$entity.name$$relationship.nameUppercase$ = function($entity.id.name$, $relationship.name$, _success, _error, _complete) {
	if ($relationship.name$ === null) {
		return that._ajax({
			type: "DELETE",
			url: _url + "/api/$entity.pluralLowercase$/" + $entity.id.name$ + "/$relationship.name$",
			success: _success,
			error: _error,
			complete: _complete
		});
	} else {
		return that._ajax({
			type: "PUT",
			url: _url + "/api/$entity.pluralLowercase$/" + $entity.id.name$ + "/$relationship.name$",
			contentType: "text/uri-list",
			data: $relationship.name$,
			success: _success,
			error: _error,
			complete: _complete
		});
	}
};$endif$$endif$>>