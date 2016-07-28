FacadeQueries(entity, query) ::=

<</*-------------------------- Queries -----------------------------------------------*/
if (this.$query.name$) {
	window.console && console.log("$query.name$");
}
this.$query.name$ = function($query.parameters:{parameter | $parameter.name$, }$projection) {
	var config = {};
	config.params = {
		$query.parameters:{parameter | $parameter.name$: $parameter.name$,$\n$}$
	}
	config.params["projection"] = projection;
	return \$http.get(_config.url + "/api/$entity.pluralLowercase$/search/$query.name$",  config)
};>>