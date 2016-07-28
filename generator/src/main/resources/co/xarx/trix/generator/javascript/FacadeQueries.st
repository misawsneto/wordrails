FacadeQueries(entity, query) ::=

<<
if (that.$query.name$) {
	console.log("$query.name$");
}
that.$query.name$ = function($query.parameters:{parameter | $parameter.name$, }$_success, _error, _complete, projection) {
	return that._ajax({
		url: _url + "/api/$entity.pluralLowercase$/search/$query.name$",
		data: {
			$query.parameters:{parameter | $parameter.name$: $parameter.name$,$\n$}$
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
};>>