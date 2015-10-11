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
	\$httpProvider.interceptors.push(function trix_httpInterceptor(\$q, \$rootScope) {
		var requestInterceptor = {
		responseError: function(rejection){
			\$rootScope.\$broadcast('HTTP_ERROR',rejection);
			return \$q.reject(rejection);
		},
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
		    }else if(response.config.method === "POST" || response.config.method === "PUT"){
		    	var _value = response.headers("Location");
                if (_value) {
                    var _index = _value.lastIndexOf("/");
                    var _suffix = _value.substring(_index + 1);
                    var id = _suffix;
                    response.data = id
                }
                return response;
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
      return \$http.post(_config.url + "/api/persons/login", \$.param({"username": username, "password": password}), config)
    }

    this.tokenSignin = function(token) {
	  var config = {};
	  config.headers = {"Content-Type": "application/x-www-form-urlencoded"}
	  return \$http.post(_config.url + "/api/persons/tokenSignin", \$.param({"token": token}), config)
	}

    this.socialLogin = function(userId, accessToken, provider) {
	  var config = {};
	  config.headers = {"Content-Type": "application/x-www-form-urlencoded"}
	  return \$http.post(_config.url + "/api/auth/signin", \$.param({"userId": userId, "accessToken": accessToken, "provider": provider}), config)
	}

	this.findPostsByTagAndStationId = function(tagName, stationId, page, size, projection) {
		var config = {};
		config.params = {
			tagName: tagName,
			stationId: stationId,
			page: page,
			size: size,
		}
		config.params["projection"] = projection;
		return \$http.get(_config.url + "/api/terms/search/findPostsByTagAndStationId",  config)
	};

    this.createPerson = function(person) {
      var config = {};
      config.headers = {"Content-Type": "application/json"}
      return \$http.post(_config.url + "/api/persons/create", person, config)
    }

    this.createNetwork = function(createNetwork) {
	  var config = {};
	  config.headers = {"Content-Type": "application/json"}
	  return \$http.post(_config.url + "/api/networks/createNetwork", createNetwork, config)
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

    this.deletePersons = function(personIds) {
		var config = {"headers": {"Content-Type": "application/json"}}
		return \$http.put(_config.url + "/api/persons/deleteMany/network", personIds, config)
	};

	this.putTermView = function (termView) {
		var config = {"headers": {"Content-Type": "application/json"}}
    	return \$http.put(_config.url + "/api/perspectives/termPerspectiveDefinitions/" + termView.id , termView, config)
	}

	this.postTermView = function (termView) {
		var config = {"headers": {"Content-Type": "application/json"}}
		return \$http.post(_config.url + "/api/perspectives/termPerspectiveDefinitions" , termView, config)
	}

    this.countPersonsByNetwork = function(networkId){
    	var config = {
			"params": {
				"networkId": networkId
			}
		}
    	return \$http.get(_config.url + "/api/persons/count", config)
    }

    this.updatePostTerms = function(postId, terms) {
    	var config = {"headers": {"Content-Type": "application/json"}}
		return \$http.put(_config.url + "/api/posts/" + postId + "/updatePostTerms", terms, config)
	}

	this.deletePersonStationRoles = function(stationRolesIds) {
    	var config = {"headers": {"Content-Type": "application/json"}}
		return \$http.put(_config.url + "/api/persons/deletePersonStationRoles", stationRolesIds, config)
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

    this.getPersonNetworkPostsByState = function(personId, state, _page, _size){
    	var config = {
    		"params": {
    			"state": state,
				"personId": personId,
				"page": _page,
				"size": _size,
			}
    	}
        return \$http.get(_config.url + "/api/persons/getPostsByState", config)
    }

    this.getPersonStats = function(date, postId) {
		var config = {
			"params": {
				"date": date,
				"postId": postId,
			}
		}
	  return \$http.get(_config.url + "/api/persons/me/stats", config)
	}

	this.getNetworkStats = function(date, postId) {
		var config = {
			"params": {
				"date": date,
				"postId": postId,
			}
		}
	  return \$http.get(_config.url + "/api/networks/stats", config)
	}

	this.getNetworkPublicationsCount = function() {
	  return \$http.get(_config.url + "/api/networks/publicationsCount")
	}

	this.getPersonPublicationsCount = function(personId) {
		var config = {
			"params": {
				"personId": personId,
			}
		}
	  return \$http.get(_config.url + "/api/persons/me/publicationsCount", config)
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

    this.searchPosts = function(query, _page, _size, params) {
		var config = {
			"params": {
				"query": query,
				"page": _page,
				"size": _size,
			}
		}

		if(params && angular)
			config.params = angular.extend(config.params, params);

      return \$http.get(_config.url + "/api/posts/search/networkPosts", config)
    }

    this.searchBookmarks = function(query, _page, _size) {
    	var config = {
    	"params": {
				"query": query,
				"page": _page,
				"size": _size,
			}
		}
      return \$http.get(_config.url + "/api/bookmarks/searchBookmarks", config)
    }

    this.searchNotifications = function(query, _page, _size) {
    	var config = {
    	"params": {
				"query": query,
				"page": _page,
				"size": _size,
			}
		}
      return \$http.get(_config.url + "/api/notifications/searchNotifications", config)
    }

    this.checkBookmarkedRecommendedByMe = function(postId) {
    	var config = {
    	"params": {
				"postId": postId,
			}
		}
      return \$http.get(_config.url + "/api/me/bookmarkedRecommended", config)
    }

    this.toggleBookmark = function(postId) {
    	var config = {"headers": {"Content-Type": "application/x-www-form-urlencoded"}}
    	return \$http.put(_config.url + "/api/bookmarks/toggleBookmark", \$.param({"postId": postId}), config)
    };

    this.setMainStation = function(stationId, value) {
    	var config = {"headers": {"Content-Type": "application/x-www-form-urlencoded"}}
    	return \$http.put(_config.url + "/api/stations/"+stationId+"/setMainStation", \$.param({"value": value}), config)
    };

    this.setDefaultPerspective = function(stationId, perspectiveId) {
		var config = {"headers": {"Content-Type": "application/x-www-form-urlencoded"}}
		return \$http.put(_config.url + "/api/stations/"+stationId+"/setDefaultPerspective", \$.param({"perspectiveId": perspectiveId}), config)
	};

    this.toggleRecommend = function(postId) {
    	var config = {"headers": {"Content-Type": "application/x-www-form-urlencoded"}}
    	return \$http.put(_config.url + "/api/recommends/toggleRecommend", \$.param({"postId": postId}), config)
    };

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

    this.findPopularPosts = function(stationId, _page, _size) {
		var config = {
			"params": {
				"page": _page,
				"size": _size
			}
		}
      return \$http.get(_config.url + "/api/posts/" + stationId + "/popular", config)
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

    this.putPassword = function(oldPassword, newPassword) {
    	var config = {"headers": {"Content-Type": "application/x-www-form-urlencoded"}}
    	return \$http.put(_config.url + "/api/persons/me/password", \$.param({"oldPassword": oldPassword, "newPassword": newPassword}), config)
    };

    this.convertPost = function(postId, state){
    	var config = {"headers": {"Content-Type": "application/x-www-form-urlencoded"}}
    	return \$http.put(_config.url + "/api/posts/"+postId+"/convert", \$.param({"state": state}), config)
    }

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

	this.findAllCategories = function(stationId) {
    	var config = {};
        config.params = {
    	    stationId: stationId
        }
        return \$http.get(_config.url + "/api/taxonomies/allCategories",  config)
    };

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