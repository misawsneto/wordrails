FacadeMethods(entity) ::=

<</*-------------------------- Methods -----------------------------------------------*/
	if (this.get$entity.plural$) {
		window.console && console.log("get$entity.plural$");
	}
    this.get$entity.plural$ = function(_page, _size, _sort, projection) {
        var config = {};
        config.params = {};
        config.params["page"] = _page;
        config.params["size"] = _size;
        config.params["sort"] = _sort;
        config.params["projection"] = projection;
		return \$http.get(_config.url + "/api/$entity.pluralLowercase$",  config)
	}

	if (this.post$entity.name$) {
		window.console && console.log("post$entity.name$");
	}
	this.post$entity.name$ = function($entity.nameLowercase$) {
		var config = {};
		config.headers = {};
		config.headers["Content-Type"] = "application/json"
		return \$http.post(_config.url + "/api/$entity.pluralLowercase$", $entity.nameLowercase$, config)
	}

	if (this.get$entity.name$) {
		window.console && console.log("get$entity.name$");
	}
	this.get$entity.name$ = function($entity.id.name$, projection) {
		var config = {};
		config.params = {};
		config.params["projection"] = projection;
		return \$http.get(_config.url + "/api/$entity.pluralLowercase$/" + $entity.id.name$,  config)
	}

	if (this.put$entity.name$) {
		console.log("put$entity.name$");
	}
	this.put$entity.name$ = function($entity.nameLowercase$) {
		var config = {};
		config.headers = {};
		config.headers["Content-Type"] = "application/json"
		return \$http.put(_config.url + "/api/$entity.pluralLowercase$/" + $entity.nameLowercase$.$entity.id.name$, $entity.nameLowercase$, config)
	}

	if (this.delete$entity.name$) {
		console.log("delete$entity.name$");
	}
	this.delete$entity.name$ = function($entity.id.name$) {
		return \$http.delete("/api/$entity.pluralLowercase$/" + $entity.id.name$);
	}

	$entity.queries:{query | $FacadeQueries(entity, query)$}; separator="\n\n"$

	$entity.relationships:{relationship | $FacadeRelationshipMethods(entity, relationship)$}; separator="\n\n"$

/*---------------------------------------------------------------------------*/>>