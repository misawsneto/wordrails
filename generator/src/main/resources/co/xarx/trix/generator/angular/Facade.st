Facade(facade, entities) ::=

<<$entities:{entity | $Dto(entity)$}; separator="\n\n"$
var TRIX_BACKEND = null;
var trix = angular.module('trix', [])
.provider('trix', function trixProvider(\$httpProvider) {

	var username = null;

	var _config = {}
	_config.url = null;
	var provider = {};

	provider.setUrl = function(u){
		_config.url = TRIX_BACKEND = u;
	}

	provider.getConfig = function(){
		return _config;
	}

	provider.setConfig = function(c){
		_config = c
		provider.setUrl(c.url);
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
				if(!response.data.id && Object.keys(response.data._embedded).length > 0)
				   response.data = response.data._embedded;
			    else{
			       response.data._embedded = null; response.data._links = null;
		        }

				return response
			}else if (_config.url && response.config.method === "GET" &&
				response.config.url.indexOf(_config.url + "/api") > -1 &&
				response.data && response.data.content){
					if(response.data.totalElements)
                       response.config.totalElements = response.data.totalElements
					response.data = response.data.content;
					return response
			}else if(response.config.method === "PUT"){
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
			if(username)
                config.headers['x-username'] = username;
		  return config;
		}
		};
		return requestInterceptor;
	})

	delete \$httpProvider.defaults.headers.common['X-Requested-With'];

  var Trix = function(\$http){

    this.setUsername = function(uname){
        username = uname;
    }

    this.resetUsername = function(){
        username = null;
    }

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

	this.findPostsByTerm = function(termId, page, size, projection) {
        var config = {};
        config.params = {
            termId: termId,
            page: page,
            size: size,
        }
        config.params["projection"] = projection;
        return \$http.get(_config.url + "/api/terms/search/findPostsByTerm",  config)
    };

    this.createPerson = function(person, invitation) {
      var config = {};
      config.headers = {"Content-Type": "application/json"}
      config.params = {invitation: invitation}
      return \$http.post(_config.url + "/api/persons/create", person, config)
    }

	this.createNetwork = function(createNetwork) {
	  var config = {};
	  config.headers = {"Content-Type": "application/json"}
	  return \$http.post(_config.url + "/api/networks/createNetwork", createNetwork, config)
	}

	this.updateTheme = function(theme) {
		var config = {"headers": {"Content-Type": "application/json"}}
		return \$http.put(_config.url + "/api/networks/updateTheme", theme, config)
	};

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

	this.disablePerson = function(personId){
		var config = {"headers": {"Content-Type": "application/json"}}
		return \$http.put(_config.url + "/api/persons/" + personId + "/disable", {}, config)
	}

	this.updatePerson = function(person){
		var config = {"headers": {"Content-Type": "application/json"}}
		return \$http.put(_config.url + "/api/persons/update", person, config)
	}

	this.updatePersonAuthData = function(person){
        var config = {"headers": {"Content-Type": "application/json"}}
        return \$http.put(_config.url + "/api/persons/authDataUpdate", person, config)
    }

	this.enablePerson = function(personId){
		var config = {"headers": {"Content-Type": "application/json"}}
		return \$http.put(_config.url + "/api/persons/" + personId + "/enable", {}, config)
	}

	this.enablePersons = function(personIds){
		var config = {"headers": {"Content-Type": "application/json"}}
		params = {ids: []};
		if(personIds)
		personIds.forEach(function(id){
			if(id) {params.ids.push(id)}
		})
		return \$http.put(_config.url + "/api/persons/enable/all", params, config)
	}

	this.disablePersons = function(personIds){
		var config = {"headers": {"Content-Type": "application/json"}}
		params = {ids: []};
		if(personIds)
		personIds.forEach(function(id){
			if(id) {params.ids.push(id)}
		})
		return \$http.put(_config.url + "/api/persons/disable/all", params, config)
	}

	this.updateStationRoles = function(stationRoleUpdates){
        var config = {"headers": {"Content-Type": "application/json"}}
        return \$http.put(_config.url + "/api/permission/stations/update", stationRoleUpdates, config)
    }

    this.clearStationRoles = function(stationRoleUpdates){
        var config = {"headers": {"Content-Type": "application/json"}}
        return \$http.post(_config.url + "/api/permission/stations/clean", stationRoleUpdates, config)
    }

	this.putTermView = function (termView) {
		var config = {"headers": {"Content-Type": "application/json"}}
		return \$http.put(_config.url + "/api/perspectives/termPerspectiveDefinitions/" + termView.id , termView, config)
	}

	this.postTermView = function (termView) {
		var config = {"headers": {"Content-Type": "application/json"}}
		return \$http.post(_config.url + "/api/perspectives/termPerspectiveDefinitions" , termView, config)
	}

	this.countPersonsByNetwork = function(){
		return \$http.get(_config.url + "/api/persons/stats/count")
	}

	this.countRolesByStationIds = function(stationIds, q){
		var config = {
			"params": {
				"stationIds": stationIds
			}
		}
		if(q && q.length > 0)
			config.params.q = q
		return \$http.get(_config.url + "/api/stations/stats/roles/count", config)
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

	this.searchComments = function(query,authors,posts,stations,from,until,page,size,order,embed){
        var config = {
            "params": {
                "q": query,
                "authorId": authors,
                "postId": posts,
                "stationId": stations,
                "from": from,
                "until": until,
                "page": page,
                "size": size,
                "order": order,
                "embed": embed,
            }
        }
        return \$http.get(_config.url + "/api/v2/comments/search", config)
    }

    this.deleteComments = function(comments) {
        var config = {};
        config.headers = {"Content-Type": "application/json"}
        return \$http.put("/api/v2/comments", comments, config);
    }

     this.bulkUpdateState = function(updates){
        var config = {};
        config.headers = {"Content-Type": "application/json"}
        return \$http.put(_config.url + "/api/v2/posts/bulkUpdateStates", updates, config);
    }

	this.searchPosts = function(query,authors,stations,state,from,until,categories,tags,page,size,order,
    	embed, snippet){
        var config = {
            "params": {
                "q": query,
                "authorId": authors,
                "stationId": stations,
                "state": state,
                "from": from,
                "until": until,
                "categoryId": categories,
                "tags": tags,
                "page": page,
                "size": size,
                "order": order,
                "embed": embed,
                "snippet": snippet
            }
        }
        return \$http.get(_config.url + "/api/v2/posts/search", config)
	}

	this.getPostVersions = function(postId){
		var config = {
            "params": {
                "postId": postId
            }
        }
		return \$http.get(_config.url + "/api/v2/posts/getPostVersions", config);
	}

	this.getStationPermission = function(stationId){
	    return \$http.get(_config.url + "/api/v2/stations/"+stationId+"/permissions");
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

	this.authCredentials = function() {
        return \$http.get(_config.url + "/api/v2/auth/social/credentials")
    }

    this.saveAuthCredentials = function (authCredentials) {
        var config = {"headers": {"Content-Type": "application/json"}}
        return \$http.put(_config.url + "/api/v2/auth/social/credentials", authCredentials, config)
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
		var config = {"headers": {"Content-Type": "application/json"}}
		return \$http.put(_config.url + "/api/v2/bookmarks/toggle/" + postId, {}, config)
	};

	this.toggleRecommend = function(postId) {
        var config = {"headers": {"Content-Type": "application/json"}}
        return \$http.put(_config.url + "/api/v2/recommends/toggle/" + postId, {}, config)
    };

	this.setMainStation = function(stationId, value) {
		var config = {"headers": {"Content-Type": "application/x-www-form-urlencoded"}}
		return \$http.put(_config.url + "/api/stations/"+stationId+"/setMainStation", \$.param({"value": value}), config)
	};

	this.setDefaultPerspective = function(stationId, perspectiveId) {
		var config = {"headers": {"Content-Type": "application/x-www-form-urlencoded"}}
		return \$http.put(_config.url + "/api/stations/"+stationId+"/setDefaultPerspective", \$.param({"perspectiveId": perspectiveId}), config)
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

	this.getAllTerms = function(perspectiveId, taxonomyId) {
		var config = {
			"params": {
				"perspectiveId": perspectiveId,
				"taxonomyId": taxonomyId
			}
		}
	  return \$http.get(_config.url + "/api/terms/allTerms", config)
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

	this.updateImageCredits = function(image){
		var config = {"headers": {"Content-Type": "application/json"}}
        return \$http.put(_config.url + "/api/images/updateCredits", image, config)
	}

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

	this.findPostsByIds = function(ids){
		var config = {};
        config.params = {
            ids: ids
        }
        return \$http.get(_config.url + "/api/v2/posts/findPostsByIds",  config)
	}

	this.recoverPassword = function(email) {
	  var config = {};
	  config.headers = {"Content-Type": "application/x-www-form-urlencoded"}
	  return \$http.post(_config.url + "/api/auth/forgotPassword", \$.param({"email": email}), config)
	}

	this.invitePeople = function(invitation) {
      var config = {};
      config.headers = {"Content-Type": "application/json"}
      return \$http.post(_config.url + "/api/persons/invitePerson", invitation, config)
    }

	this.updatePassword = function(hash, password) {
	  var config = {};
	  config.headers = {"Content-Type": "application/x-www-form-urlencoded"}
	  return \$http.put(_config.url + "/api/auth/" + hash, \$.param({"password": password}), config)
	}

    this.getAllStationPages = function(stationId) {
        return \$http.get(_config.url + "/api/web/" + stationId + "/pages")
    }

    this.getInvitationTemplate = function() {
        return \$http.get(_config.url + "/api/networks/invitationTemplate")
    }

    this.forceDeleteStation = function(stationId) {
        return \$http.delete(_config.url + "/api/stations/" + stationId + "/force")
    }

    this.getImagesPerson = function(id, size, type) {
        return _config.url + "/api/images/persons/" + id + "?size=" + size + "&type=" + type;
    }

    this.getImagesPost = function(id) {
        return _config.url + "/api/images/posts/" + id + "?size=" + size;
    }

    this.updateAuthCredential = function (authCredential) {
        var config = {"headers": {"Content-Type": "application/json"}}
        return \$http.put(_config.url + "/api/authCredentials",
        authCredential, config)
    }

    this.getAuthCredential = function () {
        var config = {"headers": {"Content-Type": "application/json"}}
        return \$http.get(_config.url + "/api/authCredentials",config);
    }

    //@Path("/getMostPopular")
    this.getMostCommonTerm = function(startTime,endTime,field,page,size){
        var config = {};
        config.params = {
            startTime: startTime,
            endTime: endTime,
            field: field,
            page: page,
            size: size
        }
        config.headers = {"Content-Type": "application/json"}
        return \$http.get(_config.url + "/api/v2/statistics/getMostPopular", config)
    }

    //@Path("/popularNetworks")
    this.getPopularNetworks = function(page,size){
        var config = {};
        config.params = {
            page: page,
            size: size
        }
        config.headers = {"Content-Type": "application/json"}
        return \$http.get(_config.url + "/api/v2/statistics/popularNetworks", config)
    }

    	//@Path("/post")
    this.postStats = function(end, start, postId){
         var config = {};
         config.params = {
             end: end,
             start: start,
             postId: postId
         }
         config.headers = {"Content-Type": "application/json"}
         return \$http.get(_config.url + "/api/v2/statistics/post", config)
    }

    //@Path("/author")
    this.authorStats = function(end, start, authorId){
         var config = {};
         config.params = {
             end: end,
             start: start,
             authorId: authorId
         }
         config.headers = {"Content-Type": "application/json"}
         return \$http.get(_config.url + "/api/v2/statistics/author", config)
    }

    //@Path("/network")
    this.networkStats = function(end, start){
		 var config = {};
         config.params = {
             end: end,
             start: start
         }
         config.headers = {"Content-Type": "application/json"}
         return \$http.get(_config.url + "/api/v2/statistics/network", config)
    }

    	//@Path("/station")
    this.stationStats = function(end, start, stationId){
	     var config = {};
         config.params = {
             end: end,
             start: start,
             stationId: stationId
         }
         config.headers = {"Content-Type": "application/json"}
         return \$http.get(_config.url + "/api/v2/statistics/station", config)
    }

    	//@Path("/storage")
    this.getNetworkUsedSpace = function(){
	     var config = {};
         config.headers = {"Content-Type": "application/json"}
         return \$http.get(_config.url + "/api/v2/statistics/storage", config)
    }

    //	@Path("/countPostReads")
    this.countReadsByPostIds = function(postIds){
         var config = {};
         config.params = {
             postIds: postIds
         }
         config.headers = {"Content-Type": "application/json"}
         return \$http.get(_config.url + "/api/v2/statistics/countPostReads", config)
    }

    	//@Path("/countReadersByStation")
    this.countReadersByStation = function(stationId){
     var config = {};
     config.params = {
         stationId: stationId
     }
     config.headers = {"Content-Type": "application/json"}
     return \$http.get(_config.url + "/api/v2/statistics/countReadersByStation", config)
    }

    this.dashboardStats = function(){
        var config = {};
        config.headers = {"Content-Type": "application/json"}
        return \$http.get(_config.url + "/api/v2/statistics/dashboardStats", config)
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