FacadeRelationshipMethods(entity, relationship) ::=

<</*-------------------------- Relationship -----------------------------------------------*/
$if(relationship.collection)$
if (this.get$entity.name$$relationship.nameUppercase$) {
	console.log("get$entity.name$$relationship.nameUppercase$");
}
this.get$entity.name$$relationship.nameUppercase$ = function($entity.id.name$, projection) {
	var config = {};
	config.params = {};
	config.params["projection"] = projection;
	return \$http.get(_config.url + "/api/$entity.pluralLowercase$/" + $entity.id.name$ + "/$relationship.name$",  config)
};$else$

if (this.get$entity.name$$relationship.nameUppercase$) {
	console.log("get$entity.name$$relationship.nameUppercase$");
}
this.get$entity.name$$relationship.nameUppercase$ = function($entity.id.name$, projection) {
	var config = {};
	config.params = {};
	config.params["projection"] = projection;
	return \$http.get(_config.url + "/api/$entity.pluralLowercase$/" + $entity.id.name$ + "/$relationship.name$",  config)
};
$endif$$if(!relationship.mappedBy)$$if(relationship.collection)$
if (this.patch$entity.name$$relationship.nameUppercase$) {
	window.console && console.log("patch$entity.name$$relationship.nameUppercase$");
}
this.patch$entity.name$$relationship.nameUppercase$ = function($entity.id.name$, $relationship.name$) {		
	var _data = "";
	for (var i = 0; i < $relationship.name$.length; ++i) {
		_data += $relationship.name$[i] + "\n";
	}
	var config = {};
	config.headers = {};
	config.headers['Content-Type'] = "text/uri-list";
	return \$http.get(_config.url + "/api/$entity.pluralLowercase$/" + $entity.id.name$ + "/$relationship.name$", _data, config)
};

if (this.put$entity.name$$relationship.nameUppercase$) {
	window.console && console.log("put$entity.name$$relationship.nameUppercase$");
}
this.put$entity.name$$relationship.nameUppercase$ = function($entity.id.name$, $relationship.name$) {		
	var _data = "";
	for (var i = 0; i < $relationship.name$.length; ++i) {
		_data += $relationship.name$[i] + "\n";
	}
	var config = {};
	config.headers = {};
	config.headers['Content-Type'] = "text/uri-list";
	return \$http.put(_config.url + "/api/$entity.pluralLowercase$/" + $entity.id.name$ + "/$relationship.name$", _data, config)
};$else$

if (this.put$entity.name$$relationship.nameUppercase$) {
	window.console && console.log("put$entity.name$$relationship.nameUppercase$");
}
this.put$entity.name$$relationship.nameUppercase$ = function($entity.id.name$, $relationship.name$) {
	if ($relationship.name$ === null) {
		return \$http.delete(_config.url + "/api/$entity.pluralLowercase$/" + $entity.id.name$ + "/$relationship.name$")
	} else {
		var _data = "";
		for (var i = 0; i < $relationship.name$.length; ++i) {
			_data += $relationship.name$[i] + "\n";
		}
		var config = {};
		config.headers = {};
		config.headers['Content-Type'] = "text/uri-list";
		return \$http.put(_config.url + "/api/$entity.pluralLowercase$/" + $entity.id.name$ + "/$relationship.name$", $relationship.name$, config)
	}
};$endif$$endif$>>