Facade(facade, entities) ::=

<<$entities:{entity | $Dto(entity)$}; separator="\n\n"$
var trix = angular.module('trix', [])

.config(function(\$httpProvider) { 
	\$httpProvider.interceptors.push('$facade$_httpInterceptor'); 
})

.factory('$facade$_httpInterceptor', function $facade$_httpInterceptor(\$q, \$rootScope) {
	var requestInterceptor = {
    // optional method
    response: function(response) {
    // do something on success
    	if(token){
    		if(!$rootScope.$facade$) $rootScope.$facade$ = {}
    		$rootScope.$facade$.token = token;
    	}
     	return response;
  	},
  	request: function(config) {
      if($rootScope.$facade$ && $rootScope.$facade$.token)
      	config.headers["X-OMEGA-TOKEN"] = $rootScope.$facade$.token;
      return config;
    }
	};
	return requestInterceptor;
})
>>