Facade(facade, entities) ::=

<<$entities:{entity | $Dto(entity)$}; separator="\n\n"$
var trix = angular.module('trix', [])

.provider('trix', function trixProvider(\$httpProvider) {

  var _config = {}
  _config.url = null;
  var provider = {};

  provider.setUrl = function(u){
 		_config.url = u;
  }

  provider.getConfig = function(){
 		return _config;
  }

  provider.setConfig = function(c){
  	_config = c
  }

  \$httpProvider.defaults.useXDomain = true;
	\$httpProvider.defaults.withCredentials = true;
	\$httpProvider.interceptors.push(function trix_httpInterceptor(\$q) {
		var requestInterceptor = {
	    // optional method
	    response: function(response) {
	    // do something on success
	    	if (_config.url && response.config.method === "GET" && 
	    		response.config.url.indexOf(_config.url + "/api") > -1 && 
	    		response.data && response.data._embedded){
	    			response.data = response.data._embedded;
		    		return response
	    	}else if (_config.url && response.config.method === "GET" && 
	    		response.config.url.indexOf(_config.url + "/api") > -1 && 
	    		response.data && response.data.content){
	    			response.data = response.data.content;
		    		return response
		    	}else
	     			return response;
	  	},
	  	request: function(config) {
	      return config;
	    }
		};
		return requestInterceptor;
	})
	
	delete \$httpProvider.defaults.headers.common['X-Requested-With'];

  var Trix = function(\$http){

  	this.login = function(username, password) {
      var config = {};
      config.headers = {"Content-Type": "application/x-www-form-urlencoded"}
      return \$http.post(_config.url + "/j_spring_security_check", \$.param({"j_username": username, "j_password": password}), config)
    }

    this.logout = function() {
    	return \$http.get(_config.url + "/j_spring_security_logout")
    }
    
    this.initData = function() {
      return \$http.get(_config.url + "/api/persons/init");
    }
    
    this.allInitData = function() {
      return \$http.get(_config.url + "/api/persons/allInit");
    }
    
    this.getCurrentPerson = function(){
    	return \$http.get(_config.url + "/api/persons/me")
    }

    this.updatePostTerms = function(postId, terms) {
    	var config = {"headers": {"Content-Type": "application/json"}}
		return \$http.put(_config.url + "/api/posts/" + postId + "/updatePostTerms", terms, config)
	}

	this.findPerspectiveView = function(stationPerspectiveId, termPerspectiveId, termId, _page, _size, _sort) {
		var config = {
			"params": {
				"stationPerspectiveId": stationPerspectiveId,
				"termPerspectiveId": termPerspectiveId,
				"termId": termId,
				"page": _page,
				"size": _size,
				"sort": _sort
			}
		}
      return \$http.get(_config.url + "/api/perspectives/termPerspectiveViews", config)
    }
    
     this.getPersonNetworkPosts = function(personId, networkId, _page, _size) {
		var config = {
			"params": {
				"networkId": networkId,
				"page": _page,
				"size": _size,
			}
		}
      return \$http.get(_config.url + "/api/persons/"+personId+"/posts", config)
    }
    
    this.getPersonNetworkRecommendations = function(personId, networkId, _page, _size) {
		var config = {
			"params": {
				"networkId": networkId,
				"page": _page,
				"size": _size,
			}
		}
      return \$http.get(_config.url + "/api/persons/"+personId+"/recommends", config)
    }
    
    this.searchPostsFromOrPromotedToStation = function(stationId, query, _page, _size) {
		var config = {
			"params": {
				"query": query,
				"page": _page,
				"size": _size,
			}
		}
      return \$http.get(_config.url + "/api/posts/"+stationId+"/searchPostsFromOrPromotedToStation", config)
    }
    
    this.getRowView = function(stationPerspectiveId, termPerspectiveId, childTermId, _page, _size){
    	var config = {
			"params": {
				"stationPerspectiveId": stationPerspectiveId,
				"termPerspectiveId": termPerspectiveId,
				"childTermId": childTermId,
				"page": _page,
				"size": _size,
			}
		}
      return \$http.get(_config.url + "/api/perspectives/rowViews", config)
    }
    
    this.getTermTree = function(perspectiveId, taxonomyId) {
		var config = {
			"params": {
				"perspectiveId": perspectiveId,
				"taxonomyId": taxonomyId
			}
		}
      return \$http.get(_config.url + "/api/terms/termTree", config)
    }
    
    this.getPostViewBySlug = function(slug, withBody) {
		var config = {
			"params": {
				"slug": slug,
				"withBody": withBody
			}
		}
      return \$http.get(_config.url + "/api/posts/getPostViewBySlug", config)
    }
    
    this.getPostViewById = function(postId, withBody) {
		var config = {
			"params": {
				"withBody": withBody
			}
		}
      return \$http.get(_config.url + "/api/posts/" + postId + "/getPostViewById", config)
    }
    
    this.findRecentPosts = function(stationId, _page, _size) {
		var config = {
			"params": {
				"page": _page,
				"size": _size
			}
		}
      return \$http.get(_config.url + "/api/posts/" + stationId + "/recent", config)
    }

    this.putPassword = function(oldPassword, newPassword, _success, _error, _complete) {
    	return \$http.put(_config.url + "/api/persons/me/password", \$.param({"oldPassword": oldPassword, "newPassword": newPassword}), config)
    };

	this.findPostsByStationIdAndAuthorIdAndState = function(stationId, authorId, state, _page, _size, _sort) {
		var config = {
			params: {
				"stationId": stationId,
				"authorId": authorId,
				"state": state,
				"page": _page,
				"size": _size,
				"sort": _sort
			}
		}
		return \$http.get(_config.url + "/api/posts/" + stationId + "/findPostsByStationIdAndAuthorIdAndState", config)
	}

  	$entities:{entity | $FacadeMethods(entity)$}; separator="\n\n"$
  }

  provider.\$get = function(\$http){
  	var instance = new Trix(\$http)
  	if(_config.username && _config.password)
  		instance.login(_config.username, _config.password)
  	return instance;
  }

  return provider;
});

>>