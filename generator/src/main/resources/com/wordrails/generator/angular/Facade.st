Facade(facade, entities) ::=

<<$entities:{entity | $Dto(entity)$}; separator="\n\n"$
var trix = angular.module('trix', [])

.config(function(\$httpProvider) { 
	\$httpProvider.defaults.useXDomain = true;
	\$httpProvider.defaults.withCredentials = true;
  delete \$httpProvider.defaults.headers.common['X-Requested-With'];
})

.service('trix', function PulseClient(\$http) {

	$entities:{entity | $FacadeMethods(entity)$}; separator="\n\n"$
})
>>