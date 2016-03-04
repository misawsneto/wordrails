function AbstractStatementDto(id, exceptionIds, sorts) {
	return {
		id: id,
		exceptionIds: exceptionIds,
		sorts: sorts
	};
}

function AdDto(id, link) {
	return {
		id: id,
		link: link
	};
}

function CellDto(id, featured, index) {
	return {
		id: id,
		featured: featured,
		index: index
	};
}

function CommentDto(id, author, body, date, lastModificationDate, post) {
	return {
		id: id,
		author: author,
		body: body,
		date: date,
		lastModificationDate: lastModificationDate,
		post: post
	};
}

function ImageDto(id, caption, credits, hashs, largeHash, mediumHash, originalHash, smallHash, title, vertical) {
	return {
		id: id,
		caption: caption,
		credits: credits,
		hashs: hashs,
		largeHash: largeHash,
		mediumHash: mediumHash,
		originalHash: originalHash,
		smallHash: smallHash,
		title: title,
		vertical: vertical
	};
}

function MenuEntryDto(id, anonymousUrl, content, loggedInUrl, name) {
	return {
		id: id,
		anonymousUrl: anonymousUrl,
		content: content,
		loggedInUrl: loggedInUrl,
		name: name
	};
}

function NetworkDto(id, addStationRolesOnSignup, alertColors, allowSignup, allowSocialLogin, allowSponsors, appleStoreAddress, backgroundColor, backgroundColors, categoriesTaxonomyId, configured, defaultOrientationMode, defaultReadMode, domain, facebookAppID, facebookAppSecret, facebookLink, facebookLoginAllowed, faviconHash, flurryAppleKey, flurryKey, googleAppID, googleAppSecret, googleLoginAllowed, googlePlusLink, homeTabName, info, invitationMessage, loginFooterMessage, loginImageHash, loginImageSmallHash, mainColor, name, navbarColor, navbarSecondaryColor, networkCreationToken, newsFontSize, playStoreAddress, primaryColors, primaryFont, secondaryColors, secondaryFont, splashImageHash, stationMenuName, subdomain, titleFontSize, trackingId, twitterLink, webFooter, youtubeLink) {
	return {
		id: id,
		addStationRolesOnSignup: addStationRolesOnSignup,
		alertColors: alertColors,
		allowSignup: allowSignup,
		allowSocialLogin: allowSocialLogin,
		allowSponsors: allowSponsors,
		appleStoreAddress: appleStoreAddress,
		backgroundColor: backgroundColor,
		backgroundColors: backgroundColors,
		categoriesTaxonomyId: categoriesTaxonomyId,
		configured: configured,
		defaultOrientationMode: defaultOrientationMode,
		defaultReadMode: defaultReadMode,
		domain: domain,
		facebookAppID: facebookAppID,
		facebookAppSecret: facebookAppSecret,
		facebookLink: facebookLink,
		facebookLoginAllowed: facebookLoginAllowed,
		faviconHash: faviconHash,
		flurryAppleKey: flurryAppleKey,
		flurryKey: flurryKey,
		googleAppID: googleAppID,
		googleAppSecret: googleAppSecret,
		googleLoginAllowed: googleLoginAllowed,
		googlePlusLink: googlePlusLink,
		homeTabName: homeTabName,
		info: info,
		invitationMessage: invitationMessage,
		loginFooterMessage: loginFooterMessage,
		loginImageHash: loginImageHash,
		loginImageSmallHash: loginImageSmallHash,
		mainColor: mainColor,
		name: name,
		navbarColor: navbarColor,
		navbarSecondaryColor: navbarSecondaryColor,
		networkCreationToken: networkCreationToken,
		newsFontSize: newsFontSize,
		playStoreAddress: playStoreAddress,
		primaryColors: primaryColors,
		primaryFont: primaryFont,
		secondaryColors: secondaryColors,
		secondaryFont: secondaryFont,
		splashImageHash: splashImageHash,
		stationMenuName: stationMenuName,
		subdomain: subdomain,
		titleFontSize: titleFontSize,
		trackingId: trackingId,
		twitterLink: twitterLink,
		webFooter: webFooter,
		youtubeLink: youtubeLink
	};
}

function NetworkRoleDto(id, admin) {
	return {
		id: id,
		admin: admin
	};
}

function NotificationDto(id, deviceDeactivated, deviceType, errorCodeName, hash, message, regId, stackTrace, status, test, type) {
	return {
		id: id,
		deviceDeactivated: deviceDeactivated,
		deviceType: deviceType,
		errorCodeName: errorCodeName,
		hash: hash,
		message: message,
		regId: regId,
		stackTrace: stackTrace,
		status: status,
		test: test,
		type: type
	};
}

function PageDto(id, sections, title) {
	return {
		id: id,
		sections: sections,
		title: title
	};
}

function PasswordResetDto(id, hash) {
	return {
		id: id,
		hash: hash
	};
}

function PersonDto(id, bio, bookmarkPosts, coverHash, coverLargeHash, coverMediumHash, coverUrl, email, imageHash, imageLargeHash, imageMediumHash, imageSmallHash, imageUrl, lastLogin, name, twitterHandle, username) {
	return {
		id: id,
		bio: bio,
		bookmarkPosts: bookmarkPosts,
		coverHash: coverHash,
		coverLargeHash: coverLargeHash,
		coverMediumHash: coverMediumHash,
		coverUrl: coverUrl,
		email: email,
		imageHash: imageHash,
		imageLargeHash: imageLargeHash,
		imageMediumHash: imageMediumHash,
		imageSmallHash: imageSmallHash,
		imageUrl: imageUrl,
		lastLogin: lastLogin,
		name: name,
		twitterHandle: twitterHandle,
		username: username
	};
}

function PostDto(id, author, body, bookmarksCount, commentsCount, date, externalFeaturedImgUrl, externalVideoUrl, featuredAudioHash, featuredImage, featuredVideoHash, imageCaptionText, imageCreditsText, imageHash, imageLandscape, imageLargeHash, imageMediumHash, imageSmallHash, imageTitleText, lastModificationDate, lat, lng, notify, originalPostId, originalSlug, readTime, readsCount, recommendsCount, scheduledDate, slug, state, station, stationId, subheading, tags, title, topper) {
	return {
		id: id,
		author: author,
		body: body,
		bookmarksCount: bookmarksCount,
		commentsCount: commentsCount,
		date: date,
		externalFeaturedImgUrl: externalFeaturedImgUrl,
		externalVideoUrl: externalVideoUrl,
		featuredAudioHash: featuredAudioHash,
		featuredImage: featuredImage,
		featuredVideoHash: featuredVideoHash,
		imageCaptionText: imageCaptionText,
		imageCreditsText: imageCreditsText,
		imageHash: imageHash,
		imageLandscape: imageLandscape,
		imageLargeHash: imageLargeHash,
		imageMediumHash: imageMediumHash,
		imageSmallHash: imageSmallHash,
		imageTitleText: imageTitleText,
		lastModificationDate: lastModificationDate,
		lat: lat,
		lng: lng,
		notify: notify,
		originalPostId: originalPostId,
		originalSlug: originalSlug,
		readTime: readTime,
		readsCount: readsCount,
		recommendsCount: recommendsCount,
		scheduledDate: scheduledDate,
		slug: slug,
		state: state,
		station: station,
		stationId: stationId,
		subheading: subheading,
		tags: tags,
		title: title,
		topper: topper
	};
}

function PostReadDto(id, sessionid) {
	return {
		id: id,
		sessionid: sessionid
	};
}

function RecommendDto(id, person, post) {
	return {
		id: id,
		person: person,
		post: post
	};
}

function RowDto(id, index, maxPosts, type) {
	return {
		id: id,
		index: index,
		maxPosts: maxPosts,
		type: type
	};
}

function SponsorDto(id, keywords, link, name) {
	return {
		id: id,
		keywords: keywords,
		link: link,
		name: name
	};
}

function StationDto(id, allowComments, allowSocialShare, allowWritersToAddSponsors, allowWritersToNotify, backgroundColor, categoriesTaxonomyId, defaultPerspectiveId, logoHash, logoMediumHash, main, name, navbarColor, postsTitleSize, primaryColor, showAuthorData, showAuthorSocialData, sponsored, stationPerspectives, subheading, tagsTaxonomyId, topper, visibility, writable) {
	return {
		id: id,
		allowComments: allowComments,
		allowSocialShare: allowSocialShare,
		allowWritersToAddSponsors: allowWritersToAddSponsors,
		allowWritersToNotify: allowWritersToNotify,
		backgroundColor: backgroundColor,
		categoriesTaxonomyId: categoriesTaxonomyId,
		defaultPerspectiveId: defaultPerspectiveId,
		logoHash: logoHash,
		logoMediumHash: logoMediumHash,
		main: main,
		name: name,
		navbarColor: navbarColor,
		postsTitleSize: postsTitleSize,
		primaryColor: primaryColor,
		showAuthorData: showAuthorData,
		showAuthorSocialData: showAuthorSocialData,
		sponsored: sponsored,
		stationPerspectives: stationPerspectives,
		subheading: subheading,
		tagsTaxonomyId: tagsTaxonomyId,
		topper: topper,
		visibility: visibility,
		writable: writable
	};
}

function StationPerspectiveDto(id, name, stationId, taxonomyId, taxonomyName, taxonomyType) {
	return {
		id: id,
		name: name,
		stationId: stationId,
		taxonomyId: taxonomyId,
		taxonomyName: taxonomyName,
		taxonomyType: taxonomyType
	};
}

function StationRoleDto(id, admin, editor, writer) {
	return {
		id: id,
		admin: admin,
		editor: editor,
		writer: writer
	};
}

function TaxonomyDto(id, name, type) {
	return {
		id: id,
		name: name,
		type: type
	};
}

function TermDto(id, name, name_parent, taxonomyId, taxonomyName) {
	return {
		id: id,
		name: name,
		name_parent: name_parent,
		taxonomyId: taxonomyId,
		taxonomyName: taxonomyName
	};
}

function TermPerspectiveDto(id, defaultImageHash, showPopular, showRecent, stationId, taxonomyId) {
	return {
		id: id,
		defaultImageHash: defaultImageHash,
		showPopular: showPopular,
		showRecent: showRecent,
		stationId: stationId,
		taxonomyId: taxonomyId
	};
}

function UserDto(id, enabled, username) {
	return {
		id: id,
		enabled: enabled,
		username: username
	};
}

function VideoDto(id, duration) {
	return {
		id: id,
		duration: duration
	};
}
var trix = angular.module('trix', [])

.provider('trix', function trixProvider($httpProvider) {

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

  $httpProvider.defaults.useXDomain = true;
	$httpProvider.defaults.withCredentials = true;
	$httpProvider.interceptors.push(function trix_httpInterceptor($q, $rootScope) {
		var requestInterceptor = {
		responseError: function(rejection){
			$rootScope.$broadcast('HTTP_ERROR',rejection);
			return $q.reject(rejection);
		},
	    // optional method
	    response: function(response) {
	    // do something on success
	    	if (_config.url && response.config.method === "GET" &&
	    		response.config.url.indexOf(_config.url + "/api") > -1 &&
	    		response.data && response.data._embedded){
                if(Object.keys(response.data._embedded).length > 0)
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

	delete $httpProvider.defaults.headers.common['X-Requested-With'];

  var Trix = function($http){

  	this.login = function(username, password) {
      var config = {};
      config.headers = {"Content-Type": "application/x-www-form-urlencoded"}
      return $http.post(_config.url + "/api/persons/login", $.param({"username": username, "password": password}), config)
    }

    this.tokenSignin = function(token) {
	  var config = {};
	  config.headers = {"Content-Type": "application/x-www-form-urlencoded"}
	  return $http.post(_config.url + "/api/persons/tokenSignin", $.param({"token": token}), config)
	}

    this.socialLogin = function(userId, accessToken, provider) {
	  var config = {};
	  config.headers = {"Content-Type": "application/x-www-form-urlencoded"}
	  return $http.post(_config.url + "/api/auth/signin", $.param({"userId": userId, "accessToken": accessToken, "provider": provider}), config)
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
		return $http.get(_config.url + "/api/terms/search/findPostsByTagAndStationId",  config)
	};

    this.createPerson = function(person) {
      var config = {};
      config.headers = {"Content-Type": "application/json"}
      return $http.post(_config.url + "/api/persons/create", person, config)
    }

    this.createNetwork = function(createNetwork) {
	  var config = {};
	  config.headers = {"Content-Type": "application/json"}
	  return $http.post(_config.url + "/api/networks/createNetwork", createNetwork, config)
	}

    this.updateTheme = function(theme) {
        var config = {"headers": {"Content-Type": "application/json"}}
        return $http.put(_config.url + "/api/networks/updateTheme", theme, config)
    };

    this.logout = function() {
    	return $http.get(_config.url + "/j_spring_security_logout")
    }

    this.initData = function() {
      return $http.get(_config.url + "/api/persons/init");
    }

    this.allInitData = function() {
      return $http.get(_config.url + "/api/persons/allInit");
    }

    this.getCurrentPerson = function(){
    	return $http.get(_config.url + "/api/persons/me")
    }

    this.deletePersons = function(personIds) {
		var config = {"headers": {"Content-Type": "application/json"}}
		return $http.put(_config.url + "/api/persons/deleteMany/network", personIds, config)
	};

	this.disablePerson = function(personId){
		var config = {"headers": {"Content-Type": "application/json"}}
		return $http.put(_config.url + "/api/persons/" + personId + "/disable", {}, config)
	}

	this.updatePerson = function(person){
        var config = {"headers": {"Content-Type": "application/json"}}
        return $http.put(_config.url + "/api/persons/update", person, config)
    }

	this.enablePerson = function(personId){
		var config = {"headers": {"Content-Type": "application/json"}}
		return $http.put(_config.url + "/api/persons/" + personId + "/enable", {}, config)
	}

	this.enablePersons = function(personIds){
	    var config = {"headers": {"Content-Type": "application/json"}}
	    params = {ids: []};
	    if(personIds)
	    personIds.forEach(function(id){
	        if(id) {params.ids.push(id)}
	    })
        return $http.put(_config.url + "/api/persons/enable/all", params, config)
	}

	this.disablePersons = function(personIds){
        var config = {"headers": {"Content-Type": "application/json"}}
        params = {ids: []};
        if(personIds)
        personIds.forEach(function(id){
            if(id) {params.ids.push(id)}
        })
        return $http.put(_config.url + "/api/persons/disable/all", params, config)
    }

    this.updateStationRoles = function(stationRoleUpdates){
        var config = {"headers": {"Content-Type": "application/json"}}
        return $http.put(_config.url + "/api/persons/updateStationRoles", stationRoleUpdates, config)
    }

	this.putTermView = function (termView) {
		var config = {"headers": {"Content-Type": "application/json"}}
    	return $http.put(_config.url + "/api/perspectives/termPerspectiveDefinitions/" + termView.id , termView, config)
	}

	this.postTermView = function (termView) {
		var config = {"headers": {"Content-Type": "application/json"}}
		return $http.post(_config.url + "/api/perspectives/termPerspectiveDefinitions" , termView, config)
	}

    this.countPersonsByNetwork = function(){
    	return $http.get(_config.url + "/api/persons/stats/count")
    }

    this.countRolesByStationIds = function(stationIds, q){
        var config = {
            "params": {
                "stationIds": stationIds
            }
        }
        if(q && q.length > 0)
            config.params.q = q
        return $http.get(_config.url + "/api/stations/stats/roles/count", config)
    }

    this.updatePostTerms = function(postId, terms) {
    	var config = {"headers": {"Content-Type": "application/json"}}
		return $http.put(_config.url + "/api/posts/" + postId + "/updatePostTerms", terms, config)
	}

	this.deletePersonStationRoles = function(stationRolesIds) {
    	var config = {"headers": {"Content-Type": "application/json"}}
		return $http.put(_config.url + "/api/persons/deletePersonStationRoles", stationRolesIds, config)
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
      return $http.get(_config.url + "/api/perspectives/termPerspectiveViews", config)
    }

     this.getPersonNetworkPosts = function(personId, networkId, _page, _size) {
		var config = {
			"params": {
				"networkId": networkId,
				"page": _page,
				"size": _size,
			}
		}
      return $http.get(_config.url + "/api/persons/"+personId+"/posts", config)
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
        return $http.get(_config.url + "/api/persons/getPostsByState", config)
    }

    this.getPersonStats = function(date, postId) {
		var config = {
			"params": {
				"date": date,
				"postId": postId,
			}
		}
	  return $http.get(_config.url + "/api/persons/me/stats", config)
	}

	this.getNetworkStats = function(date, postId) {
		var config = {
			"params": {
				"date": date,
				"postId": postId,
			}
		}
	  return $http.get(_config.url + "/api/networks/stats", config)
	}

	this.getNetworkPublicationsCount = function() {
	  return $http.get(_config.url + "/api/networks/publicationsCount")
	}

	this.getPersonPublicationsCount = function(personId) {
		var config = {
			"params": {
				"personId": personId,
			}
		}
	  return $http.get(_config.url + "/api/persons/me/publicationsCount", config)
	}

    this.getPersonNetworkRecommendations = function(personId, networkId, _page, _size) {
		var config = {
			"params": {
				"networkId": networkId,
				"page": _page,
				"size": _size,
			}
		}
      return $http.get(_config.url + "/api/persons/"+personId+"/recommends", config)
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

      return $http.get(_config.url + "/api/posts/search/networkPosts", config)
    }

    this.searchBookmarks = function(query, _page, _size) {
    	var config = {
    	"params": {
				"query": query,
				"page": _page,
				"size": _size,
			}
		}
      return $http.get(_config.url + "/api/bookmarks/searchBookmarks", config)
    }

    this.searchNotifications = function(query, _page, _size) {
    	var config = {
    	"params": {
				"query": query,
				"page": _page,
				"size": _size,
			}
		}
      return $http.get(_config.url + "/api/notifications/searchNotifications", config)
    }

    this.checkBookmarkedRecommendedByMe = function(postId) {
    	var config = {
    	"params": {
				"postId": postId,
			}
		}
      return $http.get(_config.url + "/api/me/bookmarkedRecommended", config)
    }

    this.toggleBookmark = function(postId) {
    	var config = {"headers": {"Content-Type": "application/x-www-form-urlencoded"}}
    	return $http.put(_config.url + "/api/bookmarks/toggleBookmark", $.param({"postId": postId}), config)
    };

    this.setMainStation = function(stationId, value) {
    	var config = {"headers": {"Content-Type": "application/x-www-form-urlencoded"}}
    	return $http.put(_config.url + "/api/stations/"+stationId+"/setMainStation", $.param({"value": value}), config)
    };

    this.setDefaultPerspective = function(stationId, perspectiveId) {
		var config = {"headers": {"Content-Type": "application/x-www-form-urlencoded"}}
		return $http.put(_config.url + "/api/stations/"+stationId+"/setDefaultPerspective", $.param({"perspectiveId": perspectiveId}), config)
	};

    this.toggleRecommend = function(postId) {
    	var config = {"headers": {"Content-Type": "application/x-www-form-urlencoded"}}
    	return $http.put(_config.url + "/api/recommends/toggleRecommend", $.param({"postId": postId}), config)
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
      return $http.get(_config.url + "/api/perspectives/rowViews", config)
    }

    this.getTermTree = function(perspectiveId, taxonomyId) {
		var config = {
			"params": {
				"perspectiveId": perspectiveId,
				"taxonomyId": taxonomyId
			}
		}
      return $http.get(_config.url + "/api/terms/termTree", config)
    }

    this.getAllTerms = function(perspectiveId, taxonomyId) {
		var config = {
			"params": {
				"perspectiveId": perspectiveId,
				"taxonomyId": taxonomyId
			}
		}
	  return $http.get(_config.url + "/api/terms/allTerms", config)
	}

    this.getPostViewBySlug = function(slug, withBody) {
		var config = {
			"params": {
				"slug": slug,
				"withBody": withBody
			}
		}
      return $http.get(_config.url + "/api/posts/getPostViewBySlug", config)
    }

    this.getPostViewById = function(postId, withBody) {
		var config = {
			"params": {
				"withBody": withBody
			}
		}
      return $http.get(_config.url + "/api/posts/" + postId + "/getPostViewById", config)
    }

    this.findPopularPosts = function(stationId, _page, _size) {
		var config = {
			"params": {
				"page": _page,
				"size": _size
			}
		}
      return $http.get(_config.url + "/api/posts/" + stationId + "/popular", config)
    }

    this.findRecentPosts = function(stationId, _page, _size) {
		var config = {
			"params": {
				"page": _page,
				"size": _size
			}
		}
      return $http.get(_config.url + "/api/posts/" + stationId + "/recent", config)
    }

    this.putPassword = function(oldPassword, newPassword) {
    	var config = {"headers": {"Content-Type": "application/x-www-form-urlencoded"}}
    	return $http.put(_config.url + "/api/persons/me/password", $.param({"oldPassword": oldPassword, "newPassword": newPassword}), config)
    };

    this.convertPost = function(postId, state){
    	var config = {"headers": {"Content-Type": "application/x-www-form-urlencoded"}}
    	return $http.put(_config.url + "/api/posts/"+postId+"/convert", $.param({"state": state}), config)
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
		return $http.get(_config.url + "/api/posts/" + stationId + "/findPostsByStationIdAndAuthorIdAndState", config)
	}

	this.findAllCategories = function(stationId) {
    	var config = {};
        config.params = {
    	    stationId: stationId
        }
        return $http.get(_config.url + "/api/taxonomies/allCategories",  config)
    };

    this.recoverPassword = function(email) {
      var config = {};
      config.headers = {"Content-Type": "application/x-www-form-urlencoded"}
      return $http.post(_config.url + "/api/auth/forgotPassword", $.param({"email": email}), config)
    }

    this.updatePassword = function(hash, password) {
      var config = {};
      config.headers = {"Content-Type": "application/x-www-form-urlencoded"}
      return $http.put(_config.url + "/api/auth/" + hash, $.param({"password": password}), config)
    }


  	/*---------------------------------------------------------------------------*/
  		if (this.getAbstractStatements) {
  			window.console && console.log("getAbstractStatements");
  		}
  	    this.getAbstractStatements = function(_page, _size, _sort, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["page"] = _page;
  	        config.params["size"] = _size;
  	        config.params["sort"] = _sort;
  	        config.params["projection"] = projection;
  			return $http.get(_config.url + "/api/abstractStatements",  config)
  	    }

  	    if (this.postAbstractStatement) {
  	        window.console && console.log("postAbstractStatement");
  	    }
  	    this.postAbstractStatement = function(abstractStatement) {
  	        var config = {};
  	        config.headers = {};
  	        config.headers["Content-Type"] = "application/json"
  	        return $http.post(_config.url + "/api/abstractStatements", abstractStatement, config)
  	    }

  	    if (this.getAbstractStatement) {
  	        window.console && console.log("getAbstractStatement");
  	    }
  	    this.getAbstractStatement = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/abstractStatements/" + id,  config)
  	    }

  	    if (this.putAbstractStatement) {
  	        console.log("putAbstractStatement");
  	    }
  	    this.putAbstractStatement = function(abstractStatement) {
  	        var config = {};
  	        config.headers = {};
  	        config.headers["Content-Type"] = "application/json"
  	        return $http.put(_config.url + "/api/abstractStatements/" + abstractStatement.id, abstractStatement, config)
  	    }

  	    if (this.deleteAbstractStatement) {
  	        console.log("deleteAbstractStatement");
  	    }
  	    this.deleteAbstractStatement = function(id) {
  	        return $http.delete("/api/abstractStatements/" + id);
  	    }


  	    if (this.getAbstractStatementExceptionIds) {
  	    	console.log("getAbstractStatementExceptionIds");
  	    }
  	    this.getAbstractStatementExceptionIds = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/abstractStatements/" + id + "/exceptionIds",  config)
  	    };
  	    if (this.patchAbstractStatementExceptionIds) {
  	    	window.console && console.log("patchAbstractStatementExceptionIds");
  	    }
  	    this.patchAbstractStatementExceptionIds = function(id, exceptionIds) {		
  	    	var _data = "";
  	    	for (var i = 0; i < exceptionIds.length; ++i) {
  	    		_data += exceptionIds[i] + "\n";
  	    	}
  	        var config = {};
  	        config.headers = {};
  	        config.headers['Content-Type'] = "text/uri-list";
  	        return $http.get(_config.url + "/api/abstractStatements/" + id + "/exceptionIds", _data, config)
  	    };

  	    if (this.putAbstractStatementExceptionIds) {
  	    	window.console && console.log("putAbstractStatementExceptionIds");
  	    }
  	    this.putAbstractStatementExceptionIds = function(id, exceptionIds) {		
  	    	var _data = "";
  	        for (var i = 0; i < exceptionIds.length; ++i) {
  	            _data += exceptionIds[i] + "\n";
  	        }
  	        var config = {};
  	        config.headers = {};
  	        config.headers['Content-Type'] = "text/uri-list";
  	        return $http.put(_config.url + "/api/abstractStatements/" + id + "/exceptionIds", _data, config)
  	    };

  	/*---------------------------------------------------------------------------*/

  	/*---------------------------------------------------------------------------*/
  		if (this.getAds) {
  			window.console && console.log("getAds");
  		}
  	    this.getAds = function(_page, _size, _sort, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["page"] = _page;
  	        config.params["size"] = _size;
  	        config.params["sort"] = _sort;
  	        config.params["projection"] = projection;
  			return $http.get(_config.url + "/api/ads",  config)
  	    }

  	    if (this.postAd) {
  	        window.console && console.log("postAd");
  	    }
  	    this.postAd = function(ad) {
  	        var config = {};
  	        config.headers = {};
  	        config.headers["Content-Type"] = "application/json"
  	        return $http.post(_config.url + "/api/ads", ad, config)
  	    }

  	    if (this.getAd) {
  	        window.console && console.log("getAd");
  	    }
  	    this.getAd = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/ads/" + id,  config)
  	    }

  	    if (this.putAd) {
  	        console.log("putAd");
  	    }
  	    this.putAd = function(ad) {
  	        var config = {};
  	        config.headers = {};
  	        config.headers["Content-Type"] = "application/json"
  	        return $http.put(_config.url + "/api/ads/" + ad.id, ad, config)
  	    }

  	    if (this.deleteAd) {
  	        console.log("deleteAd");
  	    }
  	    this.deleteAd = function(id) {
  	        return $http.delete("/api/ads/" + id);
  	    }



  	    if (this.getAdImage) {
  	    	console.log("getAdImage");
  	    }
  	    this.getAdImage = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/ads/" + id + "/image",  config)
  	    };

  	    if (this.putAdImage) {
  	    	window.console && console.log("putAdImage");
  	    }
  	    this.putAdImage = function(id, image) {
  	    	if (image === null) {
  	            return $http.delete(_config.url + "/api/ads/" + id + "/image")
  	    	} else {
  	            var _data = "";
  	            for (var i = 0; i < image.length; ++i) {
  	                _data += image[i] + "\n";
  	            }
  	            var config = {};
  	            config.headers = {};
  	            config.headers['Content-Type'] = "text/uri-list";
  	            return $http.put(_config.url + "/api/ads/" + id + "/image", image, config)
  	    	}
  	    };



  	    if (this.getAdSponsor) {
  	    	console.log("getAdSponsor");
  	    }
  	    this.getAdSponsor = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/ads/" + id + "/sponsor",  config)
  	    };

  	    if (this.putAdSponsor) {
  	    	window.console && console.log("putAdSponsor");
  	    }
  	    this.putAdSponsor = function(id, sponsor) {
  	    	if (sponsor === null) {
  	            return $http.delete(_config.url + "/api/ads/" + id + "/sponsor")
  	    	} else {
  	            var _data = "";
  	            for (var i = 0; i < sponsor.length; ++i) {
  	                _data += sponsor[i] + "\n";
  	            }
  	            var config = {};
  	            config.headers = {};
  	            config.headers['Content-Type'] = "text/uri-list";
  	            return $http.put(_config.url + "/api/ads/" + id + "/sponsor", sponsor, config)
  	    	}
  	    };

  	/*---------------------------------------------------------------------------*/

  	/*---------------------------------------------------------------------------*/
  		if (this.getCells) {
  			window.console && console.log("getCells");
  		}
  	    this.getCells = function(_page, _size, _sort, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["page"] = _page;
  	        config.params["size"] = _size;
  	        config.params["sort"] = _sort;
  	        config.params["projection"] = projection;
  			return $http.get(_config.url + "/api/cells",  config)
  	    }

  	    if (this.postCell) {
  	        window.console && console.log("postCell");
  	    }
  	    this.postCell = function(cell) {
  	        var config = {};
  	        config.headers = {};
  	        config.headers["Content-Type"] = "application/json"
  	        return $http.post(_config.url + "/api/cells", cell, config)
  	    }

  	    if (this.getCell) {
  	        window.console && console.log("getCell");
  	    }
  	    this.getCell = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/cells/" + id,  config)
  	    }

  	    if (this.putCell) {
  	        console.log("putCell");
  	    }
  	    this.putCell = function(cell) {
  	        var config = {};
  	        config.headers = {};
  	        config.headers["Content-Type"] = "application/json"
  	        return $http.put(_config.url + "/api/cells/" + cell.id, cell, config)
  	    }

  	    if (this.deleteCell) {
  	        console.log("deleteCell");
  	    }
  	    this.deleteCell = function(id) {
  	        return $http.delete("/api/cells/" + id);
  	    }



  	    if (this.getCellPost) {
  	    	console.log("getCellPost");
  	    }
  	    this.getCellPost = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/cells/" + id + "/post",  config)
  	    };

  	    if (this.putCellPost) {
  	    	window.console && console.log("putCellPost");
  	    }
  	    this.putCellPost = function(id, post) {
  	    	if (post === null) {
  	            return $http.delete(_config.url + "/api/cells/" + id + "/post")
  	    	} else {
  	            var _data = "";
  	            for (var i = 0; i < post.length; ++i) {
  	                _data += post[i] + "\n";
  	            }
  	            var config = {};
  	            config.headers = {};
  	            config.headers['Content-Type'] = "text/uri-list";
  	            return $http.put(_config.url + "/api/cells/" + id + "/post", post, config)
  	    	}
  	    };



  	    if (this.getCellRow) {
  	    	console.log("getCellRow");
  	    }
  	    this.getCellRow = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/cells/" + id + "/row",  config)
  	    };

  	    if (this.putCellRow) {
  	    	window.console && console.log("putCellRow");
  	    }
  	    this.putCellRow = function(id, row) {
  	    	if (row === null) {
  	            return $http.delete(_config.url + "/api/cells/" + id + "/row")
  	    	} else {
  	            var _data = "";
  	            for (var i = 0; i < row.length; ++i) {
  	                _data += row[i] + "\n";
  	            }
  	            var config = {};
  	            config.headers = {};
  	            config.headers['Content-Type'] = "text/uri-list";
  	            return $http.put(_config.url + "/api/cells/" + id + "/row", row, config)
  	    	}
  	    };



  	    if (this.getCellTerm) {
  	    	console.log("getCellTerm");
  	    }
  	    this.getCellTerm = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/cells/" + id + "/term",  config)
  	    };

  	    if (this.putCellTerm) {
  	    	window.console && console.log("putCellTerm");
  	    }
  	    this.putCellTerm = function(id, term) {
  	    	if (term === null) {
  	            return $http.delete(_config.url + "/api/cells/" + id + "/term")
  	    	} else {
  	            var _data = "";
  	            for (var i = 0; i < term.length; ++i) {
  	                _data += term[i] + "\n";
  	            }
  	            var config = {};
  	            config.headers = {};
  	            config.headers['Content-Type'] = "text/uri-list";
  	            return $http.put(_config.url + "/api/cells/" + id + "/term", term, config)
  	    	}
  	    };

  	/*---------------------------------------------------------------------------*/

  	/*---------------------------------------------------------------------------*/
  		if (this.getComments) {
  			window.console && console.log("getComments");
  		}
  	    this.getComments = function(_page, _size, _sort, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["page"] = _page;
  	        config.params["size"] = _size;
  	        config.params["sort"] = _sort;
  	        config.params["projection"] = projection;
  			return $http.get(_config.url + "/api/comments",  config)
  	    }

  	    if (this.postComment) {
  	        window.console && console.log("postComment");
  	    }
  	    this.postComment = function(comment) {
  	        var config = {};
  	        config.headers = {};
  	        config.headers["Content-Type"] = "application/json"
  	        return $http.post(_config.url + "/api/comments", comment, config)
  	    }

  	    if (this.getComment) {
  	        window.console && console.log("getComment");
  	    }
  	    this.getComment = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/comments/" + id,  config)
  	    }

  	    if (this.putComment) {
  	        console.log("putComment");
  	    }
  	    this.putComment = function(comment) {
  	        var config = {};
  	        config.headers = {};
  	        config.headers["Content-Type"] = "application/json"
  	        return $http.put(_config.url + "/api/comments/" + comment.id, comment, config)
  	    }

  	    if (this.deleteComment) {
  	        console.log("deleteComment");
  	    }
  	    this.deleteComment = function(id) {
  	        return $http.delete("/api/comments/" + id);
  	    }

  	    if (this.findPostCommentsOrderByDate) {
  	    	window.console && console.log("findPostCommentsOrderByDate");
  	    }
  	    this.findPostCommentsOrderByDate = function(postId, page, size, sort, projection) {
  	        var config = {};
  	        config.params = {
  	            postId: postId,
  	            page: page,
  	            size: size,
  	            sort: sort,

  	        }
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/comments/search/findPostCommentsOrderByDate",  config)
  	    };


  	    if (this.getCommentAuthor) {
  	    	console.log("getCommentAuthor");
  	    }
  	    this.getCommentAuthor = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/comments/" + id + "/author",  config)
  	    };

  	    if (this.putCommentAuthor) {
  	    	window.console && console.log("putCommentAuthor");
  	    }
  	    this.putCommentAuthor = function(id, author) {
  	    	if (author === null) {
  	            return $http.delete(_config.url + "/api/comments/" + id + "/author")
  	    	} else {
  	            var _data = "";
  	            for (var i = 0; i < author.length; ++i) {
  	                _data += author[i] + "\n";
  	            }
  	            var config = {};
  	            config.headers = {};
  	            config.headers['Content-Type'] = "text/uri-list";
  	            return $http.put(_config.url + "/api/comments/" + id + "/author", author, config)
  	    	}
  	    };



  	    if (this.getCommentPost) {
  	    	console.log("getCommentPost");
  	    }
  	    this.getCommentPost = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/comments/" + id + "/post",  config)
  	    };

  	    if (this.putCommentPost) {
  	    	window.console && console.log("putCommentPost");
  	    }
  	    this.putCommentPost = function(id, post) {
  	    	if (post === null) {
  	            return $http.delete(_config.url + "/api/comments/" + id + "/post")
  	    	} else {
  	            var _data = "";
  	            for (var i = 0; i < post.length; ++i) {
  	                _data += post[i] + "\n";
  	            }
  	            var config = {};
  	            config.headers = {};
  	            config.headers['Content-Type'] = "text/uri-list";
  	            return $http.put(_config.url + "/api/comments/" + id + "/post", post, config)
  	    	}
  	    };

  	/*---------------------------------------------------------------------------*/

  	/*---------------------------------------------------------------------------*/
  		if (this.getImages) {
  			window.console && console.log("getImages");
  		}
  	    this.getImages = function(_page, _size, _sort, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["page"] = _page;
  	        config.params["size"] = _size;
  	        config.params["sort"] = _sort;
  	        config.params["projection"] = projection;
  			return $http.get(_config.url + "/api/images",  config)
  	    }

  	    if (this.postImage) {
  	        window.console && console.log("postImage");
  	    }
  	    this.postImage = function(image) {
  	        var config = {};
  	        config.headers = {};
  	        config.headers["Content-Type"] = "application/json"
  	        return $http.post(_config.url + "/api/images", image, config)
  	    }

  	    if (this.getImage) {
  	        window.console && console.log("getImage");
  	    }
  	    this.getImage = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/images/" + id,  config)
  	    }

  	    if (this.putImage) {
  	        console.log("putImage");
  	    }
  	    this.putImage = function(image) {
  	        var config = {};
  	        config.headers = {};
  	        config.headers["Content-Type"] = "application/json"
  	        return $http.put(_config.url + "/api/images/" + image.id, image, config)
  	    }

  	    if (this.deleteImage) {
  	        console.log("deleteImage");
  	    }
  	    this.deleteImage = function(id) {
  	        return $http.delete("/api/images/" + id);
  	    }



  	/*---------------------------------------------------------------------------*/

  	/*---------------------------------------------------------------------------*/
  		if (this.getMenuEntries) {
  			window.console && console.log("getMenuEntries");
  		}
  	    this.getMenuEntries = function(_page, _size, _sort, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["page"] = _page;
  	        config.params["size"] = _size;
  	        config.params["sort"] = _sort;
  	        config.params["projection"] = projection;
  			return $http.get(_config.url + "/api/menuEntries",  config)
  	    }

  	    if (this.postMenuEntry) {
  	        window.console && console.log("postMenuEntry");
  	    }
  	    this.postMenuEntry = function(menuEntry) {
  	        var config = {};
  	        config.headers = {};
  	        config.headers["Content-Type"] = "application/json"
  	        return $http.post(_config.url + "/api/menuEntries", menuEntry, config)
  	    }

  	    if (this.getMenuEntry) {
  	        window.console && console.log("getMenuEntry");
  	    }
  	    this.getMenuEntry = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/menuEntries/" + id,  config)
  	    }

  	    if (this.putMenuEntry) {
  	        console.log("putMenuEntry");
  	    }
  	    this.putMenuEntry = function(menuEntry) {
  	        var config = {};
  	        config.headers = {};
  	        config.headers["Content-Type"] = "application/json"
  	        return $http.put(_config.url + "/api/menuEntries/" + menuEntry.id, menuEntry, config)
  	    }

  	    if (this.deleteMenuEntry) {
  	        console.log("deleteMenuEntry");
  	    }
  	    this.deleteMenuEntry = function(id) {
  	        return $http.delete("/api/menuEntries/" + id);
  	    }



  	/*---------------------------------------------------------------------------*/

  	/*---------------------------------------------------------------------------*/
  		if (this.getNetworks) {
  			window.console && console.log("getNetworks");
  		}
  	    this.getNetworks = function(_page, _size, _sort, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["page"] = _page;
  	        config.params["size"] = _size;
  	        config.params["sort"] = _sort;
  	        config.params["projection"] = projection;
  			return $http.get(_config.url + "/api/networks",  config)
  	    }

  	    if (this.postNetwork) {
  	        window.console && console.log("postNetwork");
  	    }
  	    this.postNetwork = function(network) {
  	        var config = {};
  	        config.headers = {};
  	        config.headers["Content-Type"] = "application/json"
  	        return $http.post(_config.url + "/api/networks", network, config)
  	    }

  	    if (this.getNetwork) {
  	        window.console && console.log("getNetwork");
  	    }
  	    this.getNetwork = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/networks/" + id,  config)
  	    }

  	    if (this.putNetwork) {
  	        console.log("putNetwork");
  	    }
  	    this.putNetwork = function(network) {
  	        var config = {};
  	        config.headers = {};
  	        config.headers["Content-Type"] = "application/json"
  	        return $http.put(_config.url + "/api/networks/" + network.id, network, config)
  	    }

  	    if (this.deleteNetwork) {
  	        console.log("deleteNetwork");
  	    }
  	    this.deleteNetwork = function(id) {
  	        return $http.delete("/api/networks/" + id);
  	    }

  	    if (this.findNetworksOrderDesc) {
  	    	window.console && console.log("findNetworksOrderDesc");
  	    }
  	    this.findNetworksOrderDesc = function(id, projection) {
  	        var config = {};
  	        config.params = {
  	            id: id,

  	        }
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/networks/search/findNetworksOrderDesc",  config)
  	    };


  	    if (this.getNetworkFavicon) {
  	    	console.log("getNetworkFavicon");
  	    }
  	    this.getNetworkFavicon = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/networks/" + id + "/favicon",  config)
  	    };

  	    if (this.putNetworkFavicon) {
  	    	window.console && console.log("putNetworkFavicon");
  	    }
  	    this.putNetworkFavicon = function(id, favicon) {
  	    	if (favicon === null) {
  	            return $http.delete(_config.url + "/api/networks/" + id + "/favicon")
  	    	} else {
  	            var _data = "";
  	            for (var i = 0; i < favicon.length; ++i) {
  	                _data += favicon[i] + "\n";
  	            }
  	            var config = {};
  	            config.headers = {};
  	            config.headers['Content-Type'] = "text/uri-list";
  	            return $http.put(_config.url + "/api/networks/" + id + "/favicon", favicon, config)
  	    	}
  	    };



  	    if (this.getNetworkLoginImage) {
  	    	console.log("getNetworkLoginImage");
  	    }
  	    this.getNetworkLoginImage = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/networks/" + id + "/loginImage",  config)
  	    };

  	    if (this.putNetworkLoginImage) {
  	    	window.console && console.log("putNetworkLoginImage");
  	    }
  	    this.putNetworkLoginImage = function(id, loginImage) {
  	    	if (loginImage === null) {
  	            return $http.delete(_config.url + "/api/networks/" + id + "/loginImage")
  	    	} else {
  	            var _data = "";
  	            for (var i = 0; i < loginImage.length; ++i) {
  	                _data += loginImage[i] + "\n";
  	            }
  	            var config = {};
  	            config.headers = {};
  	            config.headers['Content-Type'] = "text/uri-list";
  	            return $http.put(_config.url + "/api/networks/" + id + "/loginImage", loginImage, config)
  	    	}
  	    };


  	    if (this.getNetworkOwnedTaxonomies) {
  	    	console.log("getNetworkOwnedTaxonomies");
  	    }
  	    this.getNetworkOwnedTaxonomies = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/networks/" + id + "/ownedTaxonomies",  config)
  	    };


  	    if (this.getNetworkPersonsNetworkRoles) {
  	    	console.log("getNetworkPersonsNetworkRoles");
  	    }
  	    this.getNetworkPersonsNetworkRoles = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/networks/" + id + "/personsNetworkRoles",  config)
  	    };



  	    if (this.getNetworkSplashImage) {
  	    	console.log("getNetworkSplashImage");
  	    }
  	    this.getNetworkSplashImage = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/networks/" + id + "/splashImage",  config)
  	    };

  	    if (this.putNetworkSplashImage) {
  	    	window.console && console.log("putNetworkSplashImage");
  	    }
  	    this.putNetworkSplashImage = function(id, splashImage) {
  	    	if (splashImage === null) {
  	            return $http.delete(_config.url + "/api/networks/" + id + "/splashImage")
  	    	} else {
  	            var _data = "";
  	            for (var i = 0; i < splashImage.length; ++i) {
  	                _data += splashImage[i] + "\n";
  	            }
  	            var config = {};
  	            config.headers = {};
  	            config.headers['Content-Type'] = "text/uri-list";
  	            return $http.put(_config.url + "/api/networks/" + id + "/splashImage", splashImage, config)
  	    	}
  	    };


  	    if (this.getNetworkSponsors) {
  	    	console.log("getNetworkSponsors");
  	    }
  	    this.getNetworkSponsors = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/networks/" + id + "/sponsors",  config)
  	    };


  	    if (this.getNetworkStations) {
  	    	console.log("getNetworkStations");
  	    }
  	    this.getNetworkStations = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/networks/" + id + "/stations",  config)
  	    };


  	    if (this.getNetworkTaxonomies) {
  	    	console.log("getNetworkTaxonomies");
  	    }
  	    this.getNetworkTaxonomies = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/networks/" + id + "/taxonomies",  config)
  	    };
  	    if (this.patchNetworkTaxonomies) {
  	    	window.console && console.log("patchNetworkTaxonomies");
  	    }
  	    this.patchNetworkTaxonomies = function(id, taxonomies) {		
  	    	var _data = "";
  	    	for (var i = 0; i < taxonomies.length; ++i) {
  	    		_data += taxonomies[i] + "\n";
  	    	}
  	        var config = {};
  	        config.headers = {};
  	        config.headers['Content-Type'] = "text/uri-list";
  	        return $http.get(_config.url + "/api/networks/" + id + "/taxonomies", _data, config)
  	    };

  	    if (this.putNetworkTaxonomies) {
  	    	window.console && console.log("putNetworkTaxonomies");
  	    }
  	    this.putNetworkTaxonomies = function(id, taxonomies) {		
  	    	var _data = "";
  	        for (var i = 0; i < taxonomies.length; ++i) {
  	            _data += taxonomies[i] + "\n";
  	        }
  	        var config = {};
  	        config.headers = {};
  	        config.headers['Content-Type'] = "text/uri-list";
  	        return $http.put(_config.url + "/api/networks/" + id + "/taxonomies", _data, config)
  	    };

  	/*---------------------------------------------------------------------------*/

  	/*---------------------------------------------------------------------------*/
  		if (this.getNetworkRoles) {
  			window.console && console.log("getNetworkRoles");
  		}
  	    this.getNetworkRoles = function(_page, _size, _sort, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["page"] = _page;
  	        config.params["size"] = _size;
  	        config.params["sort"] = _sort;
  	        config.params["projection"] = projection;
  			return $http.get(_config.url + "/api/networkRoles",  config)
  	    }

  	    if (this.postNetworkRole) {
  	        window.console && console.log("postNetworkRole");
  	    }
  	    this.postNetworkRole = function(networkRole) {
  	        var config = {};
  	        config.headers = {};
  	        config.headers["Content-Type"] = "application/json"
  	        return $http.post(_config.url + "/api/networkRoles", networkRole, config)
  	    }

  	    if (this.getNetworkRole) {
  	        window.console && console.log("getNetworkRole");
  	    }
  	    this.getNetworkRole = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/networkRoles/" + id,  config)
  	    }

  	    if (this.putNetworkRole) {
  	        console.log("putNetworkRole");
  	    }
  	    this.putNetworkRole = function(networkRole) {
  	        var config = {};
  	        config.headers = {};
  	        config.headers["Content-Type"] = "application/json"
  	        return $http.put(_config.url + "/api/networkRoles/" + networkRole.id, networkRole, config)
  	    }

  	    if (this.deleteNetworkRole) {
  	        console.log("deleteNetworkRole");
  	    }
  	    this.deleteNetworkRole = function(id) {
  	        return $http.delete("/api/networkRoles/" + id);
  	    }



  	    if (this.getNetworkRoleNetwork) {
  	    	console.log("getNetworkRoleNetwork");
  	    }
  	    this.getNetworkRoleNetwork = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/networkRoles/" + id + "/network",  config)
  	    };

  	    if (this.putNetworkRoleNetwork) {
  	    	window.console && console.log("putNetworkRoleNetwork");
  	    }
  	    this.putNetworkRoleNetwork = function(id, network) {
  	    	if (network === null) {
  	            return $http.delete(_config.url + "/api/networkRoles/" + id + "/network")
  	    	} else {
  	            var _data = "";
  	            for (var i = 0; i < network.length; ++i) {
  	                _data += network[i] + "\n";
  	            }
  	            var config = {};
  	            config.headers = {};
  	            config.headers['Content-Type'] = "text/uri-list";
  	            return $http.put(_config.url + "/api/networkRoles/" + id + "/network", network, config)
  	    	}
  	    };



  	    if (this.getNetworkRolePerson) {
  	    	console.log("getNetworkRolePerson");
  	    }
  	    this.getNetworkRolePerson = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/networkRoles/" + id + "/person",  config)
  	    };

  	    if (this.putNetworkRolePerson) {
  	    	window.console && console.log("putNetworkRolePerson");
  	    }
  	    this.putNetworkRolePerson = function(id, person) {
  	    	if (person === null) {
  	            return $http.delete(_config.url + "/api/networkRoles/" + id + "/person")
  	    	} else {
  	            var _data = "";
  	            for (var i = 0; i < person.length; ++i) {
  	                _data += person[i] + "\n";
  	            }
  	            var config = {};
  	            config.headers = {};
  	            config.headers['Content-Type'] = "text/uri-list";
  	            return $http.put(_config.url + "/api/networkRoles/" + id + "/person", person, config)
  	    	}
  	    };

  	/*---------------------------------------------------------------------------*/

  	/*---------------------------------------------------------------------------*/
  		if (this.getNotifications) {
  			window.console && console.log("getNotifications");
  		}
  	    this.getNotifications = function(_page, _size, _sort, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["page"] = _page;
  	        config.params["size"] = _size;
  	        config.params["sort"] = _sort;
  	        config.params["projection"] = projection;
  			return $http.get(_config.url + "/api/notifications",  config)
  	    }

  	    if (this.postNotification) {
  	        window.console && console.log("postNotification");
  	    }
  	    this.postNotification = function(notification) {
  	        var config = {};
  	        config.headers = {};
  	        config.headers["Content-Type"] = "application/json"
  	        return $http.post(_config.url + "/api/notifications", notification, config)
  	    }

  	    if (this.getNotification) {
  	        window.console && console.log("getNotification");
  	    }
  	    this.getNotification = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/notifications/" + id,  config)
  	    }

  	    if (this.putNotification) {
  	        console.log("putNotification");
  	    }
  	    this.putNotification = function(notification) {
  	        var config = {};
  	        config.headers = {};
  	        config.headers["Content-Type"] = "application/json"
  	        return $http.put(_config.url + "/api/notifications/" + notification.id, notification, config)
  	    }

  	    if (this.deleteNotification) {
  	        console.log("deleteNotification");
  	    }
  	    this.deleteNotification = function(id) {
  	        return $http.delete("/api/notifications/" + id);
  	    }

  	    if (this.findNotificationsByPersonIdOrderByDate) {
  	    	window.console && console.log("findNotificationsByPersonIdOrderByDate");
  	    }
  	    this.findNotificationsByPersonIdOrderByDate = function(personId, page, size, sort, projection) {
  	        var config = {};
  	        config.params = {
  	            personId: personId,
  	            page: page,
  	            size: size,
  	            sort: sort,

  	        }
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/notifications/search/findNotificationsByPersonIdOrderByDate",  config)
  	    };


  	    if (this.getNotificationPost) {
  	    	console.log("getNotificationPost");
  	    }
  	    this.getNotificationPost = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/notifications/" + id + "/post",  config)
  	    };

  	    if (this.putNotificationPost) {
  	    	window.console && console.log("putNotificationPost");
  	    }
  	    this.putNotificationPost = function(id, post) {
  	    	if (post === null) {
  	            return $http.delete(_config.url + "/api/notifications/" + id + "/post")
  	    	} else {
  	            var _data = "";
  	            for (var i = 0; i < post.length; ++i) {
  	                _data += post[i] + "\n";
  	            }
  	            var config = {};
  	            config.headers = {};
  	            config.headers['Content-Type'] = "text/uri-list";
  	            return $http.put(_config.url + "/api/notifications/" + id + "/post", post, config)
  	    	}
  	    };

  	/*---------------------------------------------------------------------------*/

  	/*---------------------------------------------------------------------------*/
  		if (this.getPages) {
  			window.console && console.log("getPages");
  		}
  	    this.getPages = function(_page, _size, _sort, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["page"] = _page;
  	        config.params["size"] = _size;
  	        config.params["sort"] = _sort;
  	        config.params["projection"] = projection;
  			return $http.get(_config.url + "/api/pages",  config)
  	    }

  	    if (this.postPage) {
  	        window.console && console.log("postPage");
  	    }
  	    this.postPage = function(page) {
  	        var config = {};
  	        config.headers = {};
  	        config.headers["Content-Type"] = "application/json"
  	        return $http.post(_config.url + "/api/pages", page, config)
  	    }

  	    if (this.getPage) {
  	        window.console && console.log("getPage");
  	    }
  	    this.getPage = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/pages/" + id,  config)
  	    }

  	    if (this.putPage) {
  	        console.log("putPage");
  	    }
  	    this.putPage = function(page) {
  	        var config = {};
  	        config.headers = {};
  	        config.headers["Content-Type"] = "application/json"
  	        return $http.put(_config.url + "/api/pages/" + page.id, page, config)
  	    }

  	    if (this.deletePage) {
  	        console.log("deletePage");
  	    }
  	    this.deletePage = function(id) {
  	        return $http.delete("/api/pages/" + id);
  	    }



  	    if (this.getPageStation) {
  	    	console.log("getPageStation");
  	    }
  	    this.getPageStation = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/pages/" + id + "/station",  config)
  	    };

  	    if (this.putPageStation) {
  	    	window.console && console.log("putPageStation");
  	    }
  	    this.putPageStation = function(id, station) {
  	    	if (station === null) {
  	            return $http.delete(_config.url + "/api/pages/" + id + "/station")
  	    	} else {
  	            var _data = "";
  	            for (var i = 0; i < station.length; ++i) {
  	                _data += station[i] + "\n";
  	            }
  	            var config = {};
  	            config.headers = {};
  	            config.headers['Content-Type'] = "text/uri-list";
  	            return $http.put(_config.url + "/api/pages/" + id + "/station", station, config)
  	    	}
  	    };

  	/*---------------------------------------------------------------------------*/

  	/*---------------------------------------------------------------------------*/
  		if (this.getPasswordResets) {
  			window.console && console.log("getPasswordResets");
  		}
  	    this.getPasswordResets = function(_page, _size, _sort, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["page"] = _page;
  	        config.params["size"] = _size;
  	        config.params["sort"] = _sort;
  	        config.params["projection"] = projection;
  			return $http.get(_config.url + "/api/passwordResets",  config)
  	    }

  	    if (this.postPasswordReset) {
  	        window.console && console.log("postPasswordReset");
  	    }
  	    this.postPasswordReset = function(passwordReset) {
  	        var config = {};
  	        config.headers = {};
  	        config.headers["Content-Type"] = "application/json"
  	        return $http.post(_config.url + "/api/passwordResets", passwordReset, config)
  	    }

  	    if (this.getPasswordReset) {
  	        window.console && console.log("getPasswordReset");
  	    }
  	    this.getPasswordReset = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/passwordResets/" + id,  config)
  	    }

  	    if (this.putPasswordReset) {
  	        console.log("putPasswordReset");
  	    }
  	    this.putPasswordReset = function(passwordReset) {
  	        var config = {};
  	        config.headers = {};
  	        config.headers["Content-Type"] = "application/json"
  	        return $http.put(_config.url + "/api/passwordResets/" + passwordReset.id, passwordReset, config)
  	    }

  	    if (this.deletePasswordReset) {
  	        console.log("deletePasswordReset");
  	    }
  	    this.deletePasswordReset = function(id) {
  	        return $http.delete("/api/passwordResets/" + id);
  	    }

  	    if (this.findByHash) {
  	    	window.console && console.log("findByHash");
  	    }
  	    this.findByHash = function(hash, projection) {
  	        var config = {};
  	        config.params = {
  	            hash: hash,

  	        }
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/passwordResets/search/findByHash",  config)
  	    };


  	    if (this.getPasswordResetUser) {
  	    	console.log("getPasswordResetUser");
  	    }
  	    this.getPasswordResetUser = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/passwordResets/" + id + "/user",  config)
  	    };

  	    if (this.putPasswordResetUser) {
  	    	window.console && console.log("putPasswordResetUser");
  	    }
  	    this.putPasswordResetUser = function(id, user) {
  	    	if (user === null) {
  	            return $http.delete(_config.url + "/api/passwordResets/" + id + "/user")
  	    	} else {
  	            var _data = "";
  	            for (var i = 0; i < user.length; ++i) {
  	                _data += user[i] + "\n";
  	            }
  	            var config = {};
  	            config.headers = {};
  	            config.headers['Content-Type'] = "text/uri-list";
  	            return $http.put(_config.url + "/api/passwordResets/" + id + "/user", user, config)
  	    	}
  	    };

  	/*---------------------------------------------------------------------------*/

  	/*---------------------------------------------------------------------------*/
  		if (this.getPersons) {
  			window.console && console.log("getPersons");
  		}
  	    this.getPersons = function(_page, _size, _sort, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["page"] = _page;
  	        config.params["size"] = _size;
  	        config.params["sort"] = _sort;
  	        config.params["projection"] = projection;
  			return $http.get(_config.url + "/api/persons",  config)
  	    }

  	    if (this.postPerson) {
  	        window.console && console.log("postPerson");
  	    }
  	    this.postPerson = function(person) {
  	        var config = {};
  	        config.headers = {};
  	        config.headers["Content-Type"] = "application/json"
  	        return $http.post(_config.url + "/api/persons", person, config)
  	    }

  	    if (this.getPerson) {
  	        window.console && console.log("getPerson");
  	    }
  	    this.getPerson = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/persons/" + id,  config)
  	    }

  	    if (this.putPerson) {
  	        console.log("putPerson");
  	    }
  	    this.putPerson = function(person) {
  	        var config = {};
  	        config.headers = {};
  	        config.headers["Content-Type"] = "application/json"
  	        return $http.put(_config.url + "/api/persons/" + person.id, person, config)
  	    }

  	    if (this.deletePerson) {
  	        console.log("deletePerson");
  	    }
  	    this.deletePerson = function(id) {
  	        return $http.delete("/api/persons/" + id);
  	    }

  	    if (this.findByUsername) {
  	    	window.console && console.log("findByUsername");
  	    }
  	    this.findByUsername = function(username, projection) {
  	        var config = {};
  	        config.params = {
  	            username: username,

  	        }
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/persons/search/findByUsername",  config)
  	    };

  	    if (this.getPersonBookmarkPosts) {
  	    	console.log("getPersonBookmarkPosts");
  	    }
  	    this.getPersonBookmarkPosts = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/persons/" + id + "/bookmarkPosts",  config)
  	    };
  	    if (this.patchPersonBookmarkPosts) {
  	    	window.console && console.log("patchPersonBookmarkPosts");
  	    }
  	    this.patchPersonBookmarkPosts = function(id, bookmarkPosts) {		
  	    	var _data = "";
  	    	for (var i = 0; i < bookmarkPosts.length; ++i) {
  	    		_data += bookmarkPosts[i] + "\n";
  	    	}
  	        var config = {};
  	        config.headers = {};
  	        config.headers['Content-Type'] = "text/uri-list";
  	        return $http.get(_config.url + "/api/persons/" + id + "/bookmarkPosts", _data, config)
  	    };

  	    if (this.putPersonBookmarkPosts) {
  	    	window.console && console.log("putPersonBookmarkPosts");
  	    }
  	    this.putPersonBookmarkPosts = function(id, bookmarkPosts) {		
  	    	var _data = "";
  	        for (var i = 0; i < bookmarkPosts.length; ++i) {
  	            _data += bookmarkPosts[i] + "\n";
  	        }
  	        var config = {};
  	        config.headers = {};
  	        config.headers['Content-Type'] = "text/uri-list";
  	        return $http.put(_config.url + "/api/persons/" + id + "/bookmarkPosts", _data, config)
  	    };



  	    if (this.getPersonCover) {
  	    	console.log("getPersonCover");
  	    }
  	    this.getPersonCover = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/persons/" + id + "/cover",  config)
  	    };

  	    if (this.putPersonCover) {
  	    	window.console && console.log("putPersonCover");
  	    }
  	    this.putPersonCover = function(id, cover) {
  	    	if (cover === null) {
  	            return $http.delete(_config.url + "/api/persons/" + id + "/cover")
  	    	} else {
  	            var _data = "";
  	            for (var i = 0; i < cover.length; ++i) {
  	                _data += cover[i] + "\n";
  	            }
  	            var config = {};
  	            config.headers = {};
  	            config.headers['Content-Type'] = "text/uri-list";
  	            return $http.put(_config.url + "/api/persons/" + id + "/cover", cover, config)
  	    	}
  	    };



  	    if (this.getPersonImage) {
  	    	console.log("getPersonImage");
  	    }
  	    this.getPersonImage = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/persons/" + id + "/image",  config)
  	    };

  	    if (this.putPersonImage) {
  	    	window.console && console.log("putPersonImage");
  	    }
  	    this.putPersonImage = function(id, image) {
  	    	if (image === null) {
  	            return $http.delete(_config.url + "/api/persons/" + id + "/image")
  	    	} else {
  	            var _data = "";
  	            for (var i = 0; i < image.length; ++i) {
  	                _data += image[i] + "\n";
  	            }
  	            var config = {};
  	            config.headers = {};
  	            config.headers['Content-Type'] = "text/uri-list";
  	            return $http.put(_config.url + "/api/persons/" + id + "/image", image, config)
  	    	}
  	    };



  	    if (this.getPersonUser) {
  	    	console.log("getPersonUser");
  	    }
  	    this.getPersonUser = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/persons/" + id + "/user",  config)
  	    };

  	    if (this.putPersonUser) {
  	    	window.console && console.log("putPersonUser");
  	    }
  	    this.putPersonUser = function(id, user) {
  	    	if (user === null) {
  	            return $http.delete(_config.url + "/api/persons/" + id + "/user")
  	    	} else {
  	            var _data = "";
  	            for (var i = 0; i < user.length; ++i) {
  	                _data += user[i] + "\n";
  	            }
  	            var config = {};
  	            config.headers = {};
  	            config.headers['Content-Type'] = "text/uri-list";
  	            return $http.put(_config.url + "/api/persons/" + id + "/user", user, config)
  	    	}
  	    };

  	/*---------------------------------------------------------------------------*/

  	/*---------------------------------------------------------------------------*/
  		if (this.getPosts) {
  			window.console && console.log("getPosts");
  		}
  	    this.getPosts = function(_page, _size, _sort, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["page"] = _page;
  	        config.params["size"] = _size;
  	        config.params["sort"] = _sort;
  	        config.params["projection"] = projection;
  			return $http.get(_config.url + "/api/posts",  config)
  	    }

  	    if (this.postPost) {
  	        window.console && console.log("postPost");
  	    }
  	    this.postPost = function(post) {
  	        var config = {};
  	        config.headers = {};
  	        config.headers["Content-Type"] = "application/json"
  	        return $http.post(_config.url + "/api/posts", post, config)
  	    }

  	    if (this.getPost) {
  	        window.console && console.log("getPost");
  	    }
  	    this.getPost = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/posts/" + id,  config)
  	    }

  	    if (this.putPost) {
  	        console.log("putPost");
  	    }
  	    this.putPost = function(post) {
  	        var config = {};
  	        config.headers = {};
  	        config.headers["Content-Type"] = "application/json"
  	        return $http.put(_config.url + "/api/posts/" + post.id, post, config)
  	    }

  	    if (this.deletePost) {
  	        console.log("deletePost");
  	    }
  	    this.deletePost = function(id) {
  	        return $http.delete("/api/posts/" + id);
  	    }

  	    if (this.findByOriginalPostId) {
  	    	window.console && console.log("findByOriginalPostId");
  	    }
  	    this.findByOriginalPostId = function(originalPostId, projection) {
  	        var config = {};
  	        config.params = {
  	            originalPostId: originalPostId,

  	        }
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/posts/search/findByOriginalPostId",  config)
  	    };

  	    if (this.findPostsFromOrPromotedToStation) {
  	    	window.console && console.log("findPostsFromOrPromotedToStation");
  	    }
  	    this.findPostsFromOrPromotedToStation = function(stationId, page, size, sort, projection) {
  	        var config = {};
  	        config.params = {
  	            stationId: stationId,
  	            page: page,
  	            size: size,
  	            sort: sort,

  	        }
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/posts/search/findPostsFromOrPromotedToStation",  config)
  	    };

  	    if (this.findPostsOrderByDateDesc) {
  	    	window.console && console.log("findPostsOrderByDateDesc");
  	    }
  	    this.findPostsOrderByDateDesc = function(stationId, page, size, sort, projection) {
  	        var config = {};
  	        config.params = {
  	            stationId: stationId,
  	            page: page,
  	            size: size,
  	            sort: sort,

  	        }
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/posts/search/findPostsOrderByDateDesc",  config)
  	    };

  	    if (this.findPostReadByPerson) {
  	    	window.console && console.log("findPostReadByPerson");
  	    }
  	    this.findPostReadByPerson = function(personId, page, size, sort, projection) {
  	        var config = {};
  	        config.params = {
  	            personId: personId,
  	            page: page,
  	            size: size,
  	            sort: sort,

  	        }
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/posts/search/findPostReadByPerson",  config)
  	    };

  	    if (this.findPostsNotPositioned) {
  	    	window.console && console.log("findPostsNotPositioned");
  	    }
  	    this.findPostsNotPositioned = function(stationId, termsIds, idsToExclude, page, size, sort, projection) {
  	        var config = {};
  	        config.params = {
  	            stationId: stationId,
  	            termsIds: termsIds,
  	            idsToExclude: idsToExclude,
  	            page: page,
  	            size: size,
  	            sort: sort,

  	        }
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/posts/search/findPostsNotPositioned",  config)
  	    };

  	    if (this.findPostsPublished) {
  	    	window.console && console.log("findPostsPublished");
  	    }
  	    this.findPostsPublished = function(stationId, termsIds, page, size, sort, projection) {
  	        var config = {};
  	        config.params = {
  	            stationId: stationId,
  	            termsIds: termsIds,
  	            page: page,
  	            size: size,
  	            sort: sort,

  	        }
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/posts/search/findPostsPublished",  config)
  	    };

  	    if (this.findPostBySlug) {
  	    	window.console && console.log("findPostBySlug");
  	    }
  	    this.findPostBySlug = function(slug, projection) {
  	        var config = {};
  	        config.params = {
  	            slug: slug,

  	        }
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/posts/search/findPostBySlug",  config)
  	    };

  	    if (this.findBySlug) {
  	    	window.console && console.log("findBySlug");
  	    }
  	    this.findBySlug = function(slug, projection) {
  	        var config = {};
  	        config.params = {
  	            slug: slug,

  	        }
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/posts/search/findBySlug",  config)
  	    };


  	    if (this.getPostAuthor) {
  	    	console.log("getPostAuthor");
  	    }
  	    this.getPostAuthor = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/posts/" + id + "/author",  config)
  	    };

  	    if (this.putPostAuthor) {
  	    	window.console && console.log("putPostAuthor");
  	    }
  	    this.putPostAuthor = function(id, author) {
  	    	if (author === null) {
  	            return $http.delete(_config.url + "/api/posts/" + id + "/author")
  	    	} else {
  	            var _data = "";
  	            for (var i = 0; i < author.length; ++i) {
  	                _data += author[i] + "\n";
  	            }
  	            var config = {};
  	            config.headers = {};
  	            config.headers['Content-Type'] = "text/uri-list";
  	            return $http.put(_config.url + "/api/posts/" + id + "/author", author, config)
  	    	}
  	    };


  	    if (this.getPostComments) {
  	    	console.log("getPostComments");
  	    }
  	    this.getPostComments = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/posts/" + id + "/comments",  config)
  	    };



  	    if (this.getPostFeaturedImage) {
  	    	console.log("getPostFeaturedImage");
  	    }
  	    this.getPostFeaturedImage = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/posts/" + id + "/featuredImage",  config)
  	    };

  	    if (this.putPostFeaturedImage) {
  	    	window.console && console.log("putPostFeaturedImage");
  	    }
  	    this.putPostFeaturedImage = function(id, featuredImage) {
  	    	if (featuredImage === null) {
  	            return $http.delete(_config.url + "/api/posts/" + id + "/featuredImage")
  	    	} else {
  	            var _data = "";
  	            for (var i = 0; i < featuredImage.length; ++i) {
  	                _data += featuredImage[i] + "\n";
  	            }
  	            var config = {};
  	            config.headers = {};
  	            config.headers['Content-Type'] = "text/uri-list";
  	            return $http.put(_config.url + "/api/posts/" + id + "/featuredImage", featuredImage, config)
  	    	}
  	    };



  	    if (this.getPostSponsor) {
  	    	console.log("getPostSponsor");
  	    }
  	    this.getPostSponsor = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/posts/" + id + "/sponsor",  config)
  	    };

  	    if (this.putPostSponsor) {
  	    	window.console && console.log("putPostSponsor");
  	    }
  	    this.putPostSponsor = function(id, sponsor) {
  	    	if (sponsor === null) {
  	            return $http.delete(_config.url + "/api/posts/" + id + "/sponsor")
  	    	} else {
  	            var _data = "";
  	            for (var i = 0; i < sponsor.length; ++i) {
  	                _data += sponsor[i] + "\n";
  	            }
  	            var config = {};
  	            config.headers = {};
  	            config.headers['Content-Type'] = "text/uri-list";
  	            return $http.put(_config.url + "/api/posts/" + id + "/sponsor", sponsor, config)
  	    	}
  	    };



  	    if (this.getPostStation) {
  	    	console.log("getPostStation");
  	    }
  	    this.getPostStation = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/posts/" + id + "/station",  config)
  	    };

  	    if (this.putPostStation) {
  	    	window.console && console.log("putPostStation");
  	    }
  	    this.putPostStation = function(id, station) {
  	    	if (station === null) {
  	            return $http.delete(_config.url + "/api/posts/" + id + "/station")
  	    	} else {
  	            var _data = "";
  	            for (var i = 0; i < station.length; ++i) {
  	                _data += station[i] + "\n";
  	            }
  	            var config = {};
  	            config.headers = {};
  	            config.headers['Content-Type'] = "text/uri-list";
  	            return $http.put(_config.url + "/api/posts/" + id + "/station", station, config)
  	    	}
  	    };


  	    if (this.getPostTags) {
  	    	console.log("getPostTags");
  	    }
  	    this.getPostTags = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/posts/" + id + "/tags",  config)
  	    };
  	    if (this.patchPostTags) {
  	    	window.console && console.log("patchPostTags");
  	    }
  	    this.patchPostTags = function(id, tags) {		
  	    	var _data = "";
  	    	for (var i = 0; i < tags.length; ++i) {
  	    		_data += tags[i] + "\n";
  	    	}
  	        var config = {};
  	        config.headers = {};
  	        config.headers['Content-Type'] = "text/uri-list";
  	        return $http.get(_config.url + "/api/posts/" + id + "/tags", _data, config)
  	    };

  	    if (this.putPostTags) {
  	    	window.console && console.log("putPostTags");
  	    }
  	    this.putPostTags = function(id, tags) {		
  	    	var _data = "";
  	        for (var i = 0; i < tags.length; ++i) {
  	            _data += tags[i] + "\n";
  	        }
  	        var config = {};
  	        config.headers = {};
  	        config.headers['Content-Type'] = "text/uri-list";
  	        return $http.put(_config.url + "/api/posts/" + id + "/tags", _data, config)
  	    };


  	    if (this.getPostTerms) {
  	    	console.log("getPostTerms");
  	    }
  	    this.getPostTerms = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/posts/" + id + "/terms",  config)
  	    };
  	    if (this.patchPostTerms) {
  	    	window.console && console.log("patchPostTerms");
  	    }
  	    this.patchPostTerms = function(id, terms) {		
  	    	var _data = "";
  	    	for (var i = 0; i < terms.length; ++i) {
  	    		_data += terms[i] + "\n";
  	    	}
  	        var config = {};
  	        config.headers = {};
  	        config.headers['Content-Type'] = "text/uri-list";
  	        return $http.get(_config.url + "/api/posts/" + id + "/terms", _data, config)
  	    };

  	    if (this.putPostTerms) {
  	    	window.console && console.log("putPostTerms");
  	    }
  	    this.putPostTerms = function(id, terms) {		
  	    	var _data = "";
  	        for (var i = 0; i < terms.length; ++i) {
  	            _data += terms[i] + "\n";
  	        }
  	        var config = {};
  	        config.headers = {};
  	        config.headers['Content-Type'] = "text/uri-list";
  	        return $http.put(_config.url + "/api/posts/" + id + "/terms", _data, config)
  	    };


  	    if (this.getPostVideos) {
  	    	console.log("getPostVideos");
  	    }
  	    this.getPostVideos = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/posts/" + id + "/videos",  config)
  	    };
  	    if (this.patchPostVideos) {
  	    	window.console && console.log("patchPostVideos");
  	    }
  	    this.patchPostVideos = function(id, videos) {		
  	    	var _data = "";
  	    	for (var i = 0; i < videos.length; ++i) {
  	    		_data += videos[i] + "\n";
  	    	}
  	        var config = {};
  	        config.headers = {};
  	        config.headers['Content-Type'] = "text/uri-list";
  	        return $http.get(_config.url + "/api/posts/" + id + "/videos", _data, config)
  	    };

  	    if (this.putPostVideos) {
  	    	window.console && console.log("putPostVideos");
  	    }
  	    this.putPostVideos = function(id, videos) {		
  	    	var _data = "";
  	        for (var i = 0; i < videos.length; ++i) {
  	            _data += videos[i] + "\n";
  	        }
  	        var config = {};
  	        config.headers = {};
  	        config.headers['Content-Type'] = "text/uri-list";
  	        return $http.put(_config.url + "/api/posts/" + id + "/videos", _data, config)
  	    };

  	/*---------------------------------------------------------------------------*/

  	/*---------------------------------------------------------------------------*/
  		if (this.getPostReads) {
  			window.console && console.log("getPostReads");
  		}
  	    this.getPostReads = function(_page, _size, _sort, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["page"] = _page;
  	        config.params["size"] = _size;
  	        config.params["sort"] = _sort;
  	        config.params["projection"] = projection;
  			return $http.get(_config.url + "/api/postReads",  config)
  	    }

  	    if (this.postPostRead) {
  	        window.console && console.log("postPostRead");
  	    }
  	    this.postPostRead = function(postRead) {
  	        var config = {};
  	        config.headers = {};
  	        config.headers["Content-Type"] = "application/json"
  	        return $http.post(_config.url + "/api/postReads", postRead, config)
  	    }

  	    if (this.getPostRead) {
  	        window.console && console.log("getPostRead");
  	    }
  	    this.getPostRead = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/postReads/" + id,  config)
  	    }

  	    if (this.putPostRead) {
  	        console.log("putPostRead");
  	    }
  	    this.putPostRead = function(postRead) {
  	        var config = {};
  	        config.headers = {};
  	        config.headers["Content-Type"] = "application/json"
  	        return $http.put(_config.url + "/api/postReads/" + postRead.id, postRead, config)
  	    }

  	    if (this.deletePostRead) {
  	        console.log("deletePostRead");
  	    }
  	    this.deletePostRead = function(id) {
  	        return $http.delete("/api/postReads/" + id);
  	    }

  	    if (this.countByDistinctSessionid) {
  	    	window.console && console.log("countByDistinctSessionid");
  	    }
  	    this.countByDistinctSessionid = function(dateIni, dateEnd, projection) {
  	        var config = {};
  	        config.params = {
  	            dateIni: dateIni,
  	            dateEnd: dateEnd,

  	        }
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/postReads/search/countByDistinctSessionid",  config)
  	    };

  	    if (this.findPostReadByPersonIdOrderByDatePaginated) {
  	    	window.console && console.log("findPostReadByPersonIdOrderByDatePaginated");
  	    }
  	    this.findPostReadByPersonIdOrderByDatePaginated = function(personId, page, size, sort, projection) {
  	        var config = {};
  	        config.params = {
  	            personId: personId,
  	            page: page,
  	            size: size,
  	            sort: sort,

  	        }
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/postReads/search/findPostReadByPersonIdOrderByDatePaginated",  config)
  	    };

  	    if (this.findPostReadByPersonIdOrderByDate) {
  	    	window.console && console.log("findPostReadByPersonIdOrderByDate");
  	    }
  	    this.findPostReadByPersonIdOrderByDate = function(personId, projection) {
  	        var config = {};
  	        config.params = {
  	            personId: personId,

  	        }
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/postReads/search/findPostReadByPersonIdOrderByDate",  config)
  	    };


  	    if (this.getPostReadPerson) {
  	    	console.log("getPostReadPerson");
  	    }
  	    this.getPostReadPerson = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/postReads/" + id + "/person",  config)
  	    };

  	    if (this.putPostReadPerson) {
  	    	window.console && console.log("putPostReadPerson");
  	    }
  	    this.putPostReadPerson = function(id, person) {
  	    	if (person === null) {
  	            return $http.delete(_config.url + "/api/postReads/" + id + "/person")
  	    	} else {
  	            var _data = "";
  	            for (var i = 0; i < person.length; ++i) {
  	                _data += person[i] + "\n";
  	            }
  	            var config = {};
  	            config.headers = {};
  	            config.headers['Content-Type'] = "text/uri-list";
  	            return $http.put(_config.url + "/api/postReads/" + id + "/person", person, config)
  	    	}
  	    };



  	    if (this.getPostReadPost) {
  	    	console.log("getPostReadPost");
  	    }
  	    this.getPostReadPost = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/postReads/" + id + "/post",  config)
  	    };

  	    if (this.putPostReadPost) {
  	    	window.console && console.log("putPostReadPost");
  	    }
  	    this.putPostReadPost = function(id, post) {
  	    	if (post === null) {
  	            return $http.delete(_config.url + "/api/postReads/" + id + "/post")
  	    	} else {
  	            var _data = "";
  	            for (var i = 0; i < post.length; ++i) {
  	                _data += post[i] + "\n";
  	            }
  	            var config = {};
  	            config.headers = {};
  	            config.headers['Content-Type'] = "text/uri-list";
  	            return $http.put(_config.url + "/api/postReads/" + id + "/post", post, config)
  	    	}
  	    };

  	/*---------------------------------------------------------------------------*/

  	/*---------------------------------------------------------------------------*/
  		if (this.getRecommends) {
  			window.console && console.log("getRecommends");
  		}
  	    this.getRecommends = function(_page, _size, _sort, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["page"] = _page;
  	        config.params["size"] = _size;
  	        config.params["sort"] = _sort;
  	        config.params["projection"] = projection;
  			return $http.get(_config.url + "/api/recommends",  config)
  	    }

  	    if (this.postRecommend) {
  	        window.console && console.log("postRecommend");
  	    }
  	    this.postRecommend = function(recommend) {
  	        var config = {};
  	        config.headers = {};
  	        config.headers["Content-Type"] = "application/json"
  	        return $http.post(_config.url + "/api/recommends", recommend, config)
  	    }

  	    if (this.getRecommend) {
  	        window.console && console.log("getRecommend");
  	    }
  	    this.getRecommend = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/recommends/" + id,  config)
  	    }

  	    if (this.putRecommend) {
  	        console.log("putRecommend");
  	    }
  	    this.putRecommend = function(recommend) {
  	        var config = {};
  	        config.headers = {};
  	        config.headers["Content-Type"] = "application/json"
  	        return $http.put(_config.url + "/api/recommends/" + recommend.id, recommend, config)
  	    }

  	    if (this.deleteRecommend) {
  	        console.log("deleteRecommend");
  	    }
  	    this.deleteRecommend = function(id) {
  	        return $http.delete("/api/recommends/" + id);
  	    }

  	    if (this.findRecommendByPerson) {
  	    	window.console && console.log("findRecommendByPerson");
  	    }
  	    this.findRecommendByPerson = function(personId, page, size, sort, projection) {
  	        var config = {};
  	        config.params = {
  	            personId: personId,
  	            page: page,
  	            size: size,
  	            sort: sort,

  	        }
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/recommends/search/findRecommendByPerson",  config)
  	    };

  	    if (this.findRecommendsByPostId) {
  	    	window.console && console.log("findRecommendsByPostId");
  	    }
  	    this.findRecommendsByPostId = function(postId, projection) {
  	        var config = {};
  	        config.params = {
  	            postId: postId,

  	        }
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/recommends/search/findRecommendsByPostId",  config)
  	    };

  	    if (this.findRecommendByPersonIdAndPostId) {
  	    	window.console && console.log("findRecommendByPersonIdAndPostId");
  	    }
  	    this.findRecommendByPersonIdAndPostId = function(personId, postId, projection) {
  	        var config = {};
  	        config.params = {
  	            personId: personId,
  	            postId: postId,

  	        }
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/recommends/search/findRecommendByPersonIdAndPostId",  config)
  	    };

  	    if (this.findRecommendsByPersonIdOrderByDate) {
  	    	window.console && console.log("findRecommendsByPersonIdOrderByDate");
  	    }
  	    this.findRecommendsByPersonIdOrderByDate = function(personId, page, size, sort, projection) {
  	        var config = {};
  	        config.params = {
  	            personId: personId,
  	            page: page,
  	            size: size,
  	            sort: sort,

  	        }
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/recommends/search/findRecommendsByPersonIdOrderByDate",  config)
  	    };


  	    if (this.getRecommendPerson) {
  	    	console.log("getRecommendPerson");
  	    }
  	    this.getRecommendPerson = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/recommends/" + id + "/person",  config)
  	    };

  	    if (this.putRecommendPerson) {
  	    	window.console && console.log("putRecommendPerson");
  	    }
  	    this.putRecommendPerson = function(id, person) {
  	    	if (person === null) {
  	            return $http.delete(_config.url + "/api/recommends/" + id + "/person")
  	    	} else {
  	            var _data = "";
  	            for (var i = 0; i < person.length; ++i) {
  	                _data += person[i] + "\n";
  	            }
  	            var config = {};
  	            config.headers = {};
  	            config.headers['Content-Type'] = "text/uri-list";
  	            return $http.put(_config.url + "/api/recommends/" + id + "/person", person, config)
  	    	}
  	    };



  	    if (this.getRecommendPost) {
  	    	console.log("getRecommendPost");
  	    }
  	    this.getRecommendPost = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/recommends/" + id + "/post",  config)
  	    };

  	    if (this.putRecommendPost) {
  	    	window.console && console.log("putRecommendPost");
  	    }
  	    this.putRecommendPost = function(id, post) {
  	    	if (post === null) {
  	            return $http.delete(_config.url + "/api/recommends/" + id + "/post")
  	    	} else {
  	            var _data = "";
  	            for (var i = 0; i < post.length; ++i) {
  	                _data += post[i] + "\n";
  	            }
  	            var config = {};
  	            config.headers = {};
  	            config.headers['Content-Type'] = "text/uri-list";
  	            return $http.put(_config.url + "/api/recommends/" + id + "/post", post, config)
  	    	}
  	    };

  	/*---------------------------------------------------------------------------*/

  	/*---------------------------------------------------------------------------*/
  		if (this.getRows) {
  			window.console && console.log("getRows");
  		}
  	    this.getRows = function(_page, _size, _sort, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["page"] = _page;
  	        config.params["size"] = _size;
  	        config.params["sort"] = _sort;
  	        config.params["projection"] = projection;
  			return $http.get(_config.url + "/api/rows",  config)
  	    }

  	    if (this.postRow) {
  	        window.console && console.log("postRow");
  	    }
  	    this.postRow = function(row) {
  	        var config = {};
  	        config.headers = {};
  	        config.headers["Content-Type"] = "application/json"
  	        return $http.post(_config.url + "/api/rows", row, config)
  	    }

  	    if (this.getRow) {
  	        window.console && console.log("getRow");
  	    }
  	    this.getRow = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/rows/" + id,  config)
  	    }

  	    if (this.putRow) {
  	        console.log("putRow");
  	    }
  	    this.putRow = function(row) {
  	        var config = {};
  	        config.headers = {};
  	        config.headers["Content-Type"] = "application/json"
  	        return $http.put(_config.url + "/api/rows/" + row.id, row, config)
  	    }

  	    if (this.deleteRow) {
  	        console.log("deleteRow");
  	    }
  	    this.deleteRow = function(id) {
  	        return $http.delete("/api/rows/" + id);
  	    }


  	    if (this.getRowCells) {
  	    	console.log("getRowCells");
  	    }
  	    this.getRowCells = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/rows/" + id + "/cells",  config)
  	    };



  	    if (this.getRowFeaturingPerspective) {
  	    	console.log("getRowFeaturingPerspective");
  	    }
  	    this.getRowFeaturingPerspective = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/rows/" + id + "/featuringPerspective",  config)
  	    };

  	    if (this.putRowFeaturingPerspective) {
  	    	window.console && console.log("putRowFeaturingPerspective");
  	    }
  	    this.putRowFeaturingPerspective = function(id, featuringPerspective) {
  	    	if (featuringPerspective === null) {
  	            return $http.delete(_config.url + "/api/rows/" + id + "/featuringPerspective")
  	    	} else {
  	            var _data = "";
  	            for (var i = 0; i < featuringPerspective.length; ++i) {
  	                _data += featuringPerspective[i] + "\n";
  	            }
  	            var config = {};
  	            config.headers = {};
  	            config.headers['Content-Type'] = "text/uri-list";
  	            return $http.put(_config.url + "/api/rows/" + id + "/featuringPerspective", featuringPerspective, config)
  	    	}
  	    };



  	    if (this.getRowHomePerspective) {
  	    	console.log("getRowHomePerspective");
  	    }
  	    this.getRowHomePerspective = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/rows/" + id + "/homePerspective",  config)
  	    };

  	    if (this.putRowHomePerspective) {
  	    	window.console && console.log("putRowHomePerspective");
  	    }
  	    this.putRowHomePerspective = function(id, homePerspective) {
  	    	if (homePerspective === null) {
  	            return $http.delete(_config.url + "/api/rows/" + id + "/homePerspective")
  	    	} else {
  	            var _data = "";
  	            for (var i = 0; i < homePerspective.length; ++i) {
  	                _data += homePerspective[i] + "\n";
  	            }
  	            var config = {};
  	            config.headers = {};
  	            config.headers['Content-Type'] = "text/uri-list";
  	            return $http.put(_config.url + "/api/rows/" + id + "/homePerspective", homePerspective, config)
  	    	}
  	    };



  	    if (this.getRowPerspective) {
  	    	console.log("getRowPerspective");
  	    }
  	    this.getRowPerspective = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/rows/" + id + "/perspective",  config)
  	    };

  	    if (this.putRowPerspective) {
  	    	window.console && console.log("putRowPerspective");
  	    }
  	    this.putRowPerspective = function(id, perspective) {
  	    	if (perspective === null) {
  	            return $http.delete(_config.url + "/api/rows/" + id + "/perspective")
  	    	} else {
  	            var _data = "";
  	            for (var i = 0; i < perspective.length; ++i) {
  	                _data += perspective[i] + "\n";
  	            }
  	            var config = {};
  	            config.headers = {};
  	            config.headers['Content-Type'] = "text/uri-list";
  	            return $http.put(_config.url + "/api/rows/" + id + "/perspective", perspective, config)
  	    	}
  	    };



  	    if (this.getRowSplashedPerspective) {
  	    	console.log("getRowSplashedPerspective");
  	    }
  	    this.getRowSplashedPerspective = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/rows/" + id + "/splashedPerspective",  config)
  	    };

  	    if (this.putRowSplashedPerspective) {
  	    	window.console && console.log("putRowSplashedPerspective");
  	    }
  	    this.putRowSplashedPerspective = function(id, splashedPerspective) {
  	    	if (splashedPerspective === null) {
  	            return $http.delete(_config.url + "/api/rows/" + id + "/splashedPerspective")
  	    	} else {
  	            var _data = "";
  	            for (var i = 0; i < splashedPerspective.length; ++i) {
  	                _data += splashedPerspective[i] + "\n";
  	            }
  	            var config = {};
  	            config.headers = {};
  	            config.headers['Content-Type'] = "text/uri-list";
  	            return $http.put(_config.url + "/api/rows/" + id + "/splashedPerspective", splashedPerspective, config)
  	    	}
  	    };



  	    if (this.getRowTerm) {
  	    	console.log("getRowTerm");
  	    }
  	    this.getRowTerm = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/rows/" + id + "/term",  config)
  	    };

  	    if (this.putRowTerm) {
  	    	window.console && console.log("putRowTerm");
  	    }
  	    this.putRowTerm = function(id, term) {
  	    	if (term === null) {
  	            return $http.delete(_config.url + "/api/rows/" + id + "/term")
  	    	} else {
  	            var _data = "";
  	            for (var i = 0; i < term.length; ++i) {
  	                _data += term[i] + "\n";
  	            }
  	            var config = {};
  	            config.headers = {};
  	            config.headers['Content-Type'] = "text/uri-list";
  	            return $http.put(_config.url + "/api/rows/" + id + "/term", term, config)
  	    	}
  	    };

  	/*---------------------------------------------------------------------------*/

  	/*---------------------------------------------------------------------------*/
  		if (this.getSponsors) {
  			window.console && console.log("getSponsors");
  		}
  	    this.getSponsors = function(_page, _size, _sort, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["page"] = _page;
  	        config.params["size"] = _size;
  	        config.params["sort"] = _sort;
  	        config.params["projection"] = projection;
  			return $http.get(_config.url + "/api/sponsors",  config)
  	    }

  	    if (this.postSponsor) {
  	        window.console && console.log("postSponsor");
  	    }
  	    this.postSponsor = function(sponsor) {
  	        var config = {};
  	        config.headers = {};
  	        config.headers["Content-Type"] = "application/json"
  	        return $http.post(_config.url + "/api/sponsors", sponsor, config)
  	    }

  	    if (this.getSponsor) {
  	        window.console && console.log("getSponsor");
  	    }
  	    this.getSponsor = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/sponsors/" + id,  config)
  	    }

  	    if (this.putSponsor) {
  	        console.log("putSponsor");
  	    }
  	    this.putSponsor = function(sponsor) {
  	        var config = {};
  	        config.headers = {};
  	        config.headers["Content-Type"] = "application/json"
  	        return $http.put(_config.url + "/api/sponsors/" + sponsor.id, sponsor, config)
  	    }

  	    if (this.deleteSponsor) {
  	        console.log("deleteSponsor");
  	    }
  	    this.deleteSponsor = function(id) {
  	        return $http.delete("/api/sponsors/" + id);
  	    }

  	    if (this.findSponsorByNetworkId) {
  	    	window.console && console.log("findSponsorByNetworkId");
  	    }
  	    this.findSponsorByNetworkId = function(networkId, projection) {
  	        var config = {};
  	        config.params = {
  	            networkId: networkId,

  	        }
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/sponsors/search/findSponsorByNetworkId",  config)
  	    };

  	    if (this.getSponsorAds) {
  	    	console.log("getSponsorAds");
  	    }
  	    this.getSponsorAds = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/sponsors/" + id + "/ads",  config)
  	    };
  	    if (this.patchSponsorAds) {
  	    	window.console && console.log("patchSponsorAds");
  	    }
  	    this.patchSponsorAds = function(id, ads) {		
  	    	var _data = "";
  	    	for (var i = 0; i < ads.length; ++i) {
  	    		_data += ads[i] + "\n";
  	    	}
  	        var config = {};
  	        config.headers = {};
  	        config.headers['Content-Type'] = "text/uri-list";
  	        return $http.get(_config.url + "/api/sponsors/" + id + "/ads", _data, config)
  	    };

  	    if (this.putSponsorAds) {
  	    	window.console && console.log("putSponsorAds");
  	    }
  	    this.putSponsorAds = function(id, ads) {		
  	    	var _data = "";
  	        for (var i = 0; i < ads.length; ++i) {
  	            _data += ads[i] + "\n";
  	        }
  	        var config = {};
  	        config.headers = {};
  	        config.headers['Content-Type'] = "text/uri-list";
  	        return $http.put(_config.url + "/api/sponsors/" + id + "/ads", _data, config)
  	    };



  	    if (this.getSponsorLogo) {
  	    	console.log("getSponsorLogo");
  	    }
  	    this.getSponsorLogo = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/sponsors/" + id + "/logo",  config)
  	    };

  	    if (this.putSponsorLogo) {
  	    	window.console && console.log("putSponsorLogo");
  	    }
  	    this.putSponsorLogo = function(id, logo) {
  	    	if (logo === null) {
  	            return $http.delete(_config.url + "/api/sponsors/" + id + "/logo")
  	    	} else {
  	            var _data = "";
  	            for (var i = 0; i < logo.length; ++i) {
  	                _data += logo[i] + "\n";
  	            }
  	            var config = {};
  	            config.headers = {};
  	            config.headers['Content-Type'] = "text/uri-list";
  	            return $http.put(_config.url + "/api/sponsors/" + id + "/logo", logo, config)
  	    	}
  	    };



  	    if (this.getSponsorNetwork) {
  	    	console.log("getSponsorNetwork");
  	    }
  	    this.getSponsorNetwork = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/sponsors/" + id + "/network",  config)
  	    };

  	    if (this.putSponsorNetwork) {
  	    	window.console && console.log("putSponsorNetwork");
  	    }
  	    this.putSponsorNetwork = function(id, network) {
  	    	if (network === null) {
  	            return $http.delete(_config.url + "/api/sponsors/" + id + "/network")
  	    	} else {
  	            var _data = "";
  	            for (var i = 0; i < network.length; ++i) {
  	                _data += network[i] + "\n";
  	            }
  	            var config = {};
  	            config.headers = {};
  	            config.headers['Content-Type'] = "text/uri-list";
  	            return $http.put(_config.url + "/api/sponsors/" + id + "/network", network, config)
  	    	}
  	    };

  	/*---------------------------------------------------------------------------*/

  	/*---------------------------------------------------------------------------*/
  		if (this.getStations) {
  			window.console && console.log("getStations");
  		}
  	    this.getStations = function(_page, _size, _sort, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["page"] = _page;
  	        config.params["size"] = _size;
  	        config.params["sort"] = _sort;
  	        config.params["projection"] = projection;
  			return $http.get(_config.url + "/api/stations",  config)
  	    }

  	    if (this.postStation) {
  	        window.console && console.log("postStation");
  	    }
  	    this.postStation = function(station) {
  	        var config = {};
  	        config.headers = {};
  	        config.headers["Content-Type"] = "application/json"
  	        return $http.post(_config.url + "/api/stations", station, config)
  	    }

  	    if (this.getStation) {
  	        window.console && console.log("getStation");
  	    }
  	    this.getStation = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/stations/" + id,  config)
  	    }

  	    if (this.putStation) {
  	        console.log("putStation");
  	    }
  	    this.putStation = function(station) {
  	        var config = {};
  	        config.headers = {};
  	        config.headers["Content-Type"] = "application/json"
  	        return $http.put(_config.url + "/api/stations/" + station.id, station, config)
  	    }

  	    if (this.deleteStation) {
  	        console.log("deleteStation");
  	    }
  	    this.deleteStation = function(id) {
  	        return $http.delete("/api/stations/" + id);
  	    }



  	    if (this.getStationLogo) {
  	    	console.log("getStationLogo");
  	    }
  	    this.getStationLogo = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/stations/" + id + "/logo",  config)
  	    };

  	    if (this.putStationLogo) {
  	    	window.console && console.log("putStationLogo");
  	    }
  	    this.putStationLogo = function(id, logo) {
  	    	if (logo === null) {
  	            return $http.delete(_config.url + "/api/stations/" + id + "/logo")
  	    	} else {
  	            var _data = "";
  	            for (var i = 0; i < logo.length; ++i) {
  	                _data += logo[i] + "\n";
  	            }
  	            var config = {};
  	            config.headers = {};
  	            config.headers['Content-Type'] = "text/uri-list";
  	            return $http.put(_config.url + "/api/stations/" + id + "/logo", logo, config)
  	    	}
  	    };



  	    if (this.getStationNetwork) {
  	    	console.log("getStationNetwork");
  	    }
  	    this.getStationNetwork = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/stations/" + id + "/network",  config)
  	    };

  	    if (this.putStationNetwork) {
  	    	window.console && console.log("putStationNetwork");
  	    }
  	    this.putStationNetwork = function(id, network) {
  	    	if (network === null) {
  	            return $http.delete(_config.url + "/api/stations/" + id + "/network")
  	    	} else {
  	            var _data = "";
  	            for (var i = 0; i < network.length; ++i) {
  	                _data += network[i] + "\n";
  	            }
  	            var config = {};
  	            config.headers = {};
  	            config.headers['Content-Type'] = "text/uri-list";
  	            return $http.put(_config.url + "/api/stations/" + id + "/network", network, config)
  	    	}
  	    };


  	    if (this.getStationOwnedTaxonomies) {
  	    	console.log("getStationOwnedTaxonomies");
  	    }
  	    this.getStationOwnedTaxonomies = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/stations/" + id + "/ownedTaxonomies",  config)
  	    };


  	    if (this.getStationPages) {
  	    	console.log("getStationPages");
  	    }
  	    this.getStationPages = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/stations/" + id + "/pages",  config)
  	    };


  	    if (this.getStationPersonsStationRoles) {
  	    	console.log("getStationPersonsStationRoles");
  	    }
  	    this.getStationPersonsStationRoles = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/stations/" + id + "/personsStationRoles",  config)
  	    };


  	    if (this.getStationPosts) {
  	    	console.log("getStationPosts");
  	    }
  	    this.getStationPosts = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/stations/" + id + "/posts",  config)
  	    };


  	    if (this.getStationStationPerspectives) {
  	    	console.log("getStationStationPerspectives");
  	    }
  	    this.getStationStationPerspectives = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/stations/" + id + "/stationPerspectives",  config)
  	    };

  	/*---------------------------------------------------------------------------*/

  	/*---------------------------------------------------------------------------*/
  		if (this.getStationPerspectives) {
  			window.console && console.log("getStationPerspectives");
  		}
  	    this.getStationPerspectives = function(_page, _size, _sort, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["page"] = _page;
  	        config.params["size"] = _size;
  	        config.params["sort"] = _sort;
  	        config.params["projection"] = projection;
  			return $http.get(_config.url + "/api/stationPerspectives",  config)
  	    }

  	    if (this.postStationPerspective) {
  	        window.console && console.log("postStationPerspective");
  	    }
  	    this.postStationPerspective = function(stationPerspective) {
  	        var config = {};
  	        config.headers = {};
  	        config.headers["Content-Type"] = "application/json"
  	        return $http.post(_config.url + "/api/stationPerspectives", stationPerspective, config)
  	    }

  	    if (this.getStationPerspective) {
  	        window.console && console.log("getStationPerspective");
  	    }
  	    this.getStationPerspective = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/stationPerspectives/" + id,  config)
  	    }

  	    if (this.putStationPerspective) {
  	        console.log("putStationPerspective");
  	    }
  	    this.putStationPerspective = function(stationPerspective) {
  	        var config = {};
  	        config.headers = {};
  	        config.headers["Content-Type"] = "application/json"
  	        return $http.put(_config.url + "/api/stationPerspectives/" + stationPerspective.id, stationPerspective, config)
  	    }

  	    if (this.deleteStationPerspective) {
  	        console.log("deleteStationPerspective");
  	    }
  	    this.deleteStationPerspective = function(id) {
  	        return $http.delete("/api/stationPerspectives/" + id);
  	    }

  	    if (this.findStationPerspectivesByStation) {
  	    	window.console && console.log("findStationPerspectivesByStation");
  	    }
  	    this.findStationPerspectivesByStation = function(stationId, projection) {
  	        var config = {};
  	        config.params = {
  	            stationId: stationId,

  	        }
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/stationPerspectives/search/findStationPerspectivesByStation",  config)
  	    };

  	    if (this.getStationPerspectivePerspectives) {
  	    	console.log("getStationPerspectivePerspectives");
  	    }
  	    this.getStationPerspectivePerspectives = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/stationPerspectives/" + id + "/perspectives",  config)
  	    };



  	    if (this.getStationPerspectiveStation) {
  	    	console.log("getStationPerspectiveStation");
  	    }
  	    this.getStationPerspectiveStation = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/stationPerspectives/" + id + "/station",  config)
  	    };

  	    if (this.putStationPerspectiveStation) {
  	    	window.console && console.log("putStationPerspectiveStation");
  	    }
  	    this.putStationPerspectiveStation = function(id, station) {
  	    	if (station === null) {
  	            return $http.delete(_config.url + "/api/stationPerspectives/" + id + "/station")
  	    	} else {
  	            var _data = "";
  	            for (var i = 0; i < station.length; ++i) {
  	                _data += station[i] + "\n";
  	            }
  	            var config = {};
  	            config.headers = {};
  	            config.headers['Content-Type'] = "text/uri-list";
  	            return $http.put(_config.url + "/api/stationPerspectives/" + id + "/station", station, config)
  	    	}
  	    };



  	    if (this.getStationPerspectiveTaxonomy) {
  	    	console.log("getStationPerspectiveTaxonomy");
  	    }
  	    this.getStationPerspectiveTaxonomy = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/stationPerspectives/" + id + "/taxonomy",  config)
  	    };

  	    if (this.putStationPerspectiveTaxonomy) {
  	    	window.console && console.log("putStationPerspectiveTaxonomy");
  	    }
  	    this.putStationPerspectiveTaxonomy = function(id, taxonomy) {
  	    	if (taxonomy === null) {
  	            return $http.delete(_config.url + "/api/stationPerspectives/" + id + "/taxonomy")
  	    	} else {
  	            var _data = "";
  	            for (var i = 0; i < taxonomy.length; ++i) {
  	                _data += taxonomy[i] + "\n";
  	            }
  	            var config = {};
  	            config.headers = {};
  	            config.headers['Content-Type'] = "text/uri-list";
  	            return $http.put(_config.url + "/api/stationPerspectives/" + id + "/taxonomy", taxonomy, config)
  	    	}
  	    };

  	/*---------------------------------------------------------------------------*/

  	/*---------------------------------------------------------------------------*/
  		if (this.getStationRoles) {
  			window.console && console.log("getStationRoles");
  		}
  	    this.getStationRoles = function(_page, _size, _sort, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["page"] = _page;
  	        config.params["size"] = _size;
  	        config.params["sort"] = _sort;
  	        config.params["projection"] = projection;
  			return $http.get(_config.url + "/api/stationRoles",  config)
  	    }

  	    if (this.postStationRole) {
  	        window.console && console.log("postStationRole");
  	    }
  	    this.postStationRole = function(stationRole) {
  	        var config = {};
  	        config.headers = {};
  	        config.headers["Content-Type"] = "application/json"
  	        return $http.post(_config.url + "/api/stationRoles", stationRole, config)
  	    }

  	    if (this.getStationRole) {
  	        window.console && console.log("getStationRole");
  	    }
  	    this.getStationRole = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/stationRoles/" + id,  config)
  	    }

  	    if (this.putStationRole) {
  	        console.log("putStationRole");
  	    }
  	    this.putStationRole = function(stationRole) {
  	        var config = {};
  	        config.headers = {};
  	        config.headers["Content-Type"] = "application/json"
  	        return $http.put(_config.url + "/api/stationRoles/" + stationRole.id, stationRole, config)
  	    }

  	    if (this.deleteStationRole) {
  	        console.log("deleteStationRole");
  	    }
  	    this.deleteStationRole = function(id) {
  	        return $http.delete("/api/stationRoles/" + id);
  	    }

  	    if (this.findRolesByStationIds) {
  	    	window.console && console.log("findRolesByStationIds");
  	    }
  	    this.findRolesByStationIds = function(stationIds, page, size, sort, projection) {
  	        var config = {};
  	        config.params = {
  	            stationIds: stationIds,
  	            page: page,
  	            size: size,
  	            sort: sort,

  	        }
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/stationRoles/search/findRolesByStationIds",  config)
  	    };

  	    if (this.findByStationIdAndPersonId) {
  	    	window.console && console.log("findByStationIdAndPersonId");
  	    }
  	    this.findByStationIdAndPersonId = function(stationId, personId, projection) {
  	        var config = {};
  	        config.params = {
  	            stationId: stationId,
  	            personId: personId,

  	        }
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/stationRoles/search/findByStationIdAndPersonId",  config)
  	    };

  	    if (this.findRolesByStationIdsAndNameOrUsernameOrEmail) {
  	    	window.console && console.log("findRolesByStationIdsAndNameOrUsernameOrEmail");
  	    }
  	    this.findRolesByStationIdsAndNameOrUsernameOrEmail = function(stationIds, nameOrUsernameOrEmail, page, size, sort, projection) {
  	        var config = {};
  	        config.params = {
  	            stationIds: stationIds,
  	            nameOrUsernameOrEmail: nameOrUsernameOrEmail,
  	            page: page,
  	            size: size,
  	            sort: sort,

  	        }
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/stationRoles/search/findRolesByStationIdsAndNameOrUsernameOrEmail",  config)
  	    };


  	    if (this.getStationRolePerson) {
  	    	console.log("getStationRolePerson");
  	    }
  	    this.getStationRolePerson = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/stationRoles/" + id + "/person",  config)
  	    };

  	    if (this.putStationRolePerson) {
  	    	window.console && console.log("putStationRolePerson");
  	    }
  	    this.putStationRolePerson = function(id, person) {
  	    	if (person === null) {
  	            return $http.delete(_config.url + "/api/stationRoles/" + id + "/person")
  	    	} else {
  	            var _data = "";
  	            for (var i = 0; i < person.length; ++i) {
  	                _data += person[i] + "\n";
  	            }
  	            var config = {};
  	            config.headers = {};
  	            config.headers['Content-Type'] = "text/uri-list";
  	            return $http.put(_config.url + "/api/stationRoles/" + id + "/person", person, config)
  	    	}
  	    };



  	    if (this.getStationRoleStation) {
  	    	console.log("getStationRoleStation");
  	    }
  	    this.getStationRoleStation = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/stationRoles/" + id + "/station",  config)
  	    };

  	    if (this.putStationRoleStation) {
  	    	window.console && console.log("putStationRoleStation");
  	    }
  	    this.putStationRoleStation = function(id, station) {
  	    	if (station === null) {
  	            return $http.delete(_config.url + "/api/stationRoles/" + id + "/station")
  	    	} else {
  	            var _data = "";
  	            for (var i = 0; i < station.length; ++i) {
  	                _data += station[i] + "\n";
  	            }
  	            var config = {};
  	            config.headers = {};
  	            config.headers['Content-Type'] = "text/uri-list";
  	            return $http.put(_config.url + "/api/stationRoles/" + id + "/station", station, config)
  	    	}
  	    };

  	/*---------------------------------------------------------------------------*/

  	/*---------------------------------------------------------------------------*/
  		if (this.getTaxonomies) {
  			window.console && console.log("getTaxonomies");
  		}
  	    this.getTaxonomies = function(_page, _size, _sort, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["page"] = _page;
  	        config.params["size"] = _size;
  	        config.params["sort"] = _sort;
  	        config.params["projection"] = projection;
  			return $http.get(_config.url + "/api/taxonomies",  config)
  	    }

  	    if (this.postTaxonomy) {
  	        window.console && console.log("postTaxonomy");
  	    }
  	    this.postTaxonomy = function(taxonomy) {
  	        var config = {};
  	        config.headers = {};
  	        config.headers["Content-Type"] = "application/json"
  	        return $http.post(_config.url + "/api/taxonomies", taxonomy, config)
  	    }

  	    if (this.getTaxonomy) {
  	        window.console && console.log("getTaxonomy");
  	    }
  	    this.getTaxonomy = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/taxonomies/" + id,  config)
  	    }

  	    if (this.putTaxonomy) {
  	        console.log("putTaxonomy");
  	    }
  	    this.putTaxonomy = function(taxonomy) {
  	        var config = {};
  	        config.headers = {};
  	        config.headers["Content-Type"] = "application/json"
  	        return $http.put(_config.url + "/api/taxonomies/" + taxonomy.id, taxonomy, config)
  	    }

  	    if (this.deleteTaxonomy) {
  	        console.log("deleteTaxonomy");
  	    }
  	    this.deleteTaxonomy = function(id) {
  	        return $http.delete("/api/taxonomies/" + id);
  	    }

  	    if (this.findNetworkOrStationTaxonomies) {
  	    	window.console && console.log("findNetworkOrStationTaxonomies");
  	    }
  	    this.findNetworkOrStationTaxonomies = function(networkId, projection) {
  	        var config = {};
  	        config.params = {
  	            networkId: networkId,

  	        }
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/taxonomies/search/findNetworkOrStationTaxonomies",  config)
  	    };

  	    if (this.findNetworkCategories) {
  	    	window.console && console.log("findNetworkCategories");
  	    }
  	    this.findNetworkCategories = function(networkId, projection) {
  	        var config = {};
  	        config.params = {
  	            networkId: networkId,

  	        }
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/taxonomies/search/findNetworkCategories",  config)
  	    };

  	    if (this.findByTypeAndName) {
  	    	window.console && console.log("findByTypeAndName");
  	    }
  	    this.findByTypeAndName = function(type, name, projection) {
  	        var config = {};
  	        config.params = {
  	            type: type,
  	            name: name,

  	        }
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/taxonomies/search/findByTypeAndName",  config)
  	    };

  	    if (this.findStationTaxonomy) {
  	    	window.console && console.log("findStationTaxonomy");
  	    }
  	    this.findStationTaxonomy = function(stationId, projection) {
  	        var config = {};
  	        config.params = {
  	            stationId: stationId,

  	        }
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/taxonomies/search/findStationTaxonomy",  config)
  	    };

  	    if (this.findStationTags) {
  	    	window.console && console.log("findStationTags");
  	    }
  	    this.findStationTags = function(stationId, projection) {
  	        var config = {};
  	        config.params = {
  	            stationId: stationId,

  	        }
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/taxonomies/search/findStationTags",  config)
  	    };

  	    if (this.getTaxonomyNetworks) {
  	    	console.log("getTaxonomyNetworks");
  	    }
  	    this.getTaxonomyNetworks = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/taxonomies/" + id + "/networks",  config)
  	    };



  	    if (this.getTaxonomyOwningNetwork) {
  	    	console.log("getTaxonomyOwningNetwork");
  	    }
  	    this.getTaxonomyOwningNetwork = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/taxonomies/" + id + "/owningNetwork",  config)
  	    };

  	    if (this.putTaxonomyOwningNetwork) {
  	    	window.console && console.log("putTaxonomyOwningNetwork");
  	    }
  	    this.putTaxonomyOwningNetwork = function(id, owningNetwork) {
  	    	if (owningNetwork === null) {
  	            return $http.delete(_config.url + "/api/taxonomies/" + id + "/owningNetwork")
  	    	} else {
  	            var _data = "";
  	            for (var i = 0; i < owningNetwork.length; ++i) {
  	                _data += owningNetwork[i] + "\n";
  	            }
  	            var config = {};
  	            config.headers = {};
  	            config.headers['Content-Type'] = "text/uri-list";
  	            return $http.put(_config.url + "/api/taxonomies/" + id + "/owningNetwork", owningNetwork, config)
  	    	}
  	    };



  	    if (this.getTaxonomyOwningStation) {
  	    	console.log("getTaxonomyOwningStation");
  	    }
  	    this.getTaxonomyOwningStation = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/taxonomies/" + id + "/owningStation",  config)
  	    };

  	    if (this.putTaxonomyOwningStation) {
  	    	window.console && console.log("putTaxonomyOwningStation");
  	    }
  	    this.putTaxonomyOwningStation = function(id, owningStation) {
  	    	if (owningStation === null) {
  	            return $http.delete(_config.url + "/api/taxonomies/" + id + "/owningStation")
  	    	} else {
  	            var _data = "";
  	            for (var i = 0; i < owningStation.length; ++i) {
  	                _data += owningStation[i] + "\n";
  	            }
  	            var config = {};
  	            config.headers = {};
  	            config.headers['Content-Type'] = "text/uri-list";
  	            return $http.put(_config.url + "/api/taxonomies/" + id + "/owningStation", owningStation, config)
  	    	}
  	    };


  	    if (this.getTaxonomyTerms) {
  	    	console.log("getTaxonomyTerms");
  	    }
  	    this.getTaxonomyTerms = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/taxonomies/" + id + "/terms",  config)
  	    };

  	/*---------------------------------------------------------------------------*/

  	/*---------------------------------------------------------------------------*/
  		if (this.getTerms) {
  			window.console && console.log("getTerms");
  		}
  	    this.getTerms = function(_page, _size, _sort, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["page"] = _page;
  	        config.params["size"] = _size;
  	        config.params["sort"] = _sort;
  	        config.params["projection"] = projection;
  			return $http.get(_config.url + "/api/terms",  config)
  	    }

  	    if (this.postTerm) {
  	        window.console && console.log("postTerm");
  	    }
  	    this.postTerm = function(term) {
  	        var config = {};
  	        config.headers = {};
  	        config.headers["Content-Type"] = "application/json"
  	        return $http.post(_config.url + "/api/terms", term, config)
  	    }

  	    if (this.getTerm) {
  	        window.console && console.log("getTerm");
  	    }
  	    this.getTerm = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/terms/" + id,  config)
  	    }

  	    if (this.putTerm) {
  	        console.log("putTerm");
  	    }
  	    this.putTerm = function(term) {
  	        var config = {};
  	        config.headers = {};
  	        config.headers["Content-Type"] = "application/json"
  	        return $http.put(_config.url + "/api/terms/" + term.id, term, config)
  	    }

  	    if (this.deleteTerm) {
  	        console.log("deleteTerm");
  	    }
  	    this.deleteTerm = function(id) {
  	        return $http.delete("/api/terms/" + id);
  	    }

  	    if (this.findByPerspectiveId) {
  	    	window.console && console.log("findByPerspectiveId");
  	    }
  	    this.findByPerspectiveId = function(perspectiveId, projection) {
  	        var config = {};
  	        config.params = {
  	            perspectiveId: perspectiveId,

  	        }
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/terms/search/findByPerspectiveId",  config)
  	    };

  	    if (this.findTermsByPostId) {
  	    	window.console && console.log("findTermsByPostId");
  	    }
  	    this.findTermsByPostId = function(postId, projection) {
  	        var config = {};
  	        config.params = {
  	            postId: postId,

  	        }
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/terms/search/findTermsByPostId",  config)
  	    };

  	    if (this.findTermsByPostSlug) {
  	    	window.console && console.log("findTermsByPostSlug");
  	    }
  	    this.findTermsByPostSlug = function(slug, projection) {
  	        var config = {};
  	        config.params = {
  	            slug: slug,

  	        }
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/terms/search/findTermsByPostSlug",  config)
  	    };

  	    if (this.findTermsByParentId) {
  	    	window.console && console.log("findTermsByParentId");
  	    }
  	    this.findTermsByParentId = function(termId, page, size, sort, projection) {
  	        var config = {};
  	        config.params = {
  	            termId: termId,
  	            page: page,
  	            size: size,
  	            sort: sort,

  	        }
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/terms/search/findTermsByParentId",  config)
  	    };

  	    if (this.findByTaxonomyId) {
  	    	window.console && console.log("findByTaxonomyId");
  	    }
  	    this.findByTaxonomyId = function(taxonomyId, projection) {
  	        var config = {};
  	        config.params = {
  	            taxonomyId: taxonomyId,

  	        }
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/terms/search/findByTaxonomyId",  config)
  	    };

  	    if (this.findRoots) {
  	    	window.console && console.log("findRoots");
  	    }
  	    this.findRoots = function(taxonomyId, projection) {
  	        var config = {};
  	        config.params = {
  	            taxonomyId: taxonomyId,

  	        }
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/terms/search/findRoots",  config)
  	    };

  	    if (this.findRootsPage) {
  	    	window.console && console.log("findRootsPage");
  	    }
  	    this.findRootsPage = function(taxonomyId, page, size, sort, projection) {
  	        var config = {};
  	        config.params = {
  	            taxonomyId: taxonomyId,
  	            page: page,
  	            size: size,
  	            sort: sort,

  	        }
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/terms/search/findRootsPage",  config)
  	    };

  	    if (this.countTerms) {
  	    	window.console && console.log("countTerms");
  	    }
  	    this.countTerms = function(termsIds, projection) {
  	        var config = {};
  	        config.params = {
  	            termsIds: termsIds,

  	        }
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/terms/search/countTerms",  config)
  	    };

  	    if (this.getTermCells) {
  	    	console.log("getTermCells");
  	    }
  	    this.getTermCells = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/terms/" + id + "/cells",  config)
  	    };


  	    if (this.getTermChildren) {
  	    	console.log("getTermChildren");
  	    }
  	    this.getTermChildren = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/terms/" + id + "/children",  config)
  	    };



  	    if (this.getTermParent) {
  	    	console.log("getTermParent");
  	    }
  	    this.getTermParent = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/terms/" + id + "/parent",  config)
  	    };

  	    if (this.putTermParent) {
  	    	window.console && console.log("putTermParent");
  	    }
  	    this.putTermParent = function(id, parent) {
  	    	if (parent === null) {
  	            return $http.delete(_config.url + "/api/terms/" + id + "/parent")
  	    	} else {
  	            var _data = "";
  	            for (var i = 0; i < parent.length; ++i) {
  	                _data += parent[i] + "\n";
  	            }
  	            var config = {};
  	            config.headers = {};
  	            config.headers['Content-Type'] = "text/uri-list";
  	            return $http.put(_config.url + "/api/terms/" + id + "/parent", parent, config)
  	    	}
  	    };


  	    if (this.getTermPosts) {
  	    	console.log("getTermPosts");
  	    }
  	    this.getTermPosts = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/terms/" + id + "/posts",  config)
  	    };


  	    if (this.getTermRows) {
  	    	console.log("getTermRows");
  	    }
  	    this.getTermRows = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/terms/" + id + "/rows",  config)
  	    };



  	    if (this.getTermTaxonomy) {
  	    	console.log("getTermTaxonomy");
  	    }
  	    this.getTermTaxonomy = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/terms/" + id + "/taxonomy",  config)
  	    };

  	    if (this.putTermTaxonomy) {
  	    	window.console && console.log("putTermTaxonomy");
  	    }
  	    this.putTermTaxonomy = function(id, taxonomy) {
  	    	if (taxonomy === null) {
  	            return $http.delete(_config.url + "/api/terms/" + id + "/taxonomy")
  	    	} else {
  	            var _data = "";
  	            for (var i = 0; i < taxonomy.length; ++i) {
  	                _data += taxonomy[i] + "\n";
  	            }
  	            var config = {};
  	            config.headers = {};
  	            config.headers['Content-Type'] = "text/uri-list";
  	            return $http.put(_config.url + "/api/terms/" + id + "/taxonomy", taxonomy, config)
  	    	}
  	    };


  	    if (this.getTermTermPerspectives) {
  	    	console.log("getTermTermPerspectives");
  	    }
  	    this.getTermTermPerspectives = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/terms/" + id + "/termPerspectives",  config)
  	    };

  	/*---------------------------------------------------------------------------*/

  	/*---------------------------------------------------------------------------*/
  		if (this.getTermPerspectives) {
  			window.console && console.log("getTermPerspectives");
  		}
  	    this.getTermPerspectives = function(_page, _size, _sort, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["page"] = _page;
  	        config.params["size"] = _size;
  	        config.params["sort"] = _sort;
  	        config.params["projection"] = projection;
  			return $http.get(_config.url + "/api/termPerspectives",  config)
  	    }

  	    if (this.postTermPerspective) {
  	        window.console && console.log("postTermPerspective");
  	    }
  	    this.postTermPerspective = function(termPerspective) {
  	        var config = {};
  	        config.headers = {};
  	        config.headers["Content-Type"] = "application/json"
  	        return $http.post(_config.url + "/api/termPerspectives", termPerspective, config)
  	    }

  	    if (this.getTermPerspective) {
  	        window.console && console.log("getTermPerspective");
  	    }
  	    this.getTermPerspective = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/termPerspectives/" + id,  config)
  	    }

  	    if (this.putTermPerspective) {
  	        console.log("putTermPerspective");
  	    }
  	    this.putTermPerspective = function(termPerspective) {
  	        var config = {};
  	        config.headers = {};
  	        config.headers["Content-Type"] = "application/json"
  	        return $http.put(_config.url + "/api/termPerspectives/" + termPerspective.id, termPerspective, config)
  	    }

  	    if (this.deleteTermPerspective) {
  	        console.log("deleteTermPerspective");
  	    }
  	    this.deleteTermPerspective = function(id) {
  	        return $http.delete("/api/termPerspectives/" + id);
  	    }



  	    if (this.getTermPerspectiveFeaturedRow) {
  	    	console.log("getTermPerspectiveFeaturedRow");
  	    }
  	    this.getTermPerspectiveFeaturedRow = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/termPerspectives/" + id + "/featuredRow",  config)
  	    };

  	    if (this.putTermPerspectiveFeaturedRow) {
  	    	window.console && console.log("putTermPerspectiveFeaturedRow");
  	    }
  	    this.putTermPerspectiveFeaturedRow = function(id, featuredRow) {
  	    	if (featuredRow === null) {
  	            return $http.delete(_config.url + "/api/termPerspectives/" + id + "/featuredRow")
  	    	} else {
  	            var _data = "";
  	            for (var i = 0; i < featuredRow.length; ++i) {
  	                _data += featuredRow[i] + "\n";
  	            }
  	            var config = {};
  	            config.headers = {};
  	            config.headers['Content-Type'] = "text/uri-list";
  	            return $http.put(_config.url + "/api/termPerspectives/" + id + "/featuredRow", featuredRow, config)
  	    	}
  	    };



  	    if (this.getTermPerspectiveHomeRow) {
  	    	console.log("getTermPerspectiveHomeRow");
  	    }
  	    this.getTermPerspectiveHomeRow = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/termPerspectives/" + id + "/homeRow",  config)
  	    };

  	    if (this.putTermPerspectiveHomeRow) {
  	    	window.console && console.log("putTermPerspectiveHomeRow");
  	    }
  	    this.putTermPerspectiveHomeRow = function(id, homeRow) {
  	    	if (homeRow === null) {
  	            return $http.delete(_config.url + "/api/termPerspectives/" + id + "/homeRow")
  	    	} else {
  	            var _data = "";
  	            for (var i = 0; i < homeRow.length; ++i) {
  	                _data += homeRow[i] + "\n";
  	            }
  	            var config = {};
  	            config.headers = {};
  	            config.headers['Content-Type'] = "text/uri-list";
  	            return $http.put(_config.url + "/api/termPerspectives/" + id + "/homeRow", homeRow, config)
  	    	}
  	    };



  	    if (this.getTermPerspectivePerspective) {
  	    	console.log("getTermPerspectivePerspective");
  	    }
  	    this.getTermPerspectivePerspective = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/termPerspectives/" + id + "/perspective",  config)
  	    };

  	    if (this.putTermPerspectivePerspective) {
  	    	window.console && console.log("putTermPerspectivePerspective");
  	    }
  	    this.putTermPerspectivePerspective = function(id, perspective) {
  	    	if (perspective === null) {
  	            return $http.delete(_config.url + "/api/termPerspectives/" + id + "/perspective")
  	    	} else {
  	            var _data = "";
  	            for (var i = 0; i < perspective.length; ++i) {
  	                _data += perspective[i] + "\n";
  	            }
  	            var config = {};
  	            config.headers = {};
  	            config.headers['Content-Type'] = "text/uri-list";
  	            return $http.put(_config.url + "/api/termPerspectives/" + id + "/perspective", perspective, config)
  	    	}
  	    };


  	    if (this.getTermPerspectiveRows) {
  	    	console.log("getTermPerspectiveRows");
  	    }
  	    this.getTermPerspectiveRows = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/termPerspectives/" + id + "/rows",  config)
  	    };



  	    if (this.getTermPerspectiveSplashedRow) {
  	    	console.log("getTermPerspectiveSplashedRow");
  	    }
  	    this.getTermPerspectiveSplashedRow = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/termPerspectives/" + id + "/splashedRow",  config)
  	    };

  	    if (this.putTermPerspectiveSplashedRow) {
  	    	window.console && console.log("putTermPerspectiveSplashedRow");
  	    }
  	    this.putTermPerspectiveSplashedRow = function(id, splashedRow) {
  	    	if (splashedRow === null) {
  	            return $http.delete(_config.url + "/api/termPerspectives/" + id + "/splashedRow")
  	    	} else {
  	            var _data = "";
  	            for (var i = 0; i < splashedRow.length; ++i) {
  	                _data += splashedRow[i] + "\n";
  	            }
  	            var config = {};
  	            config.headers = {};
  	            config.headers['Content-Type'] = "text/uri-list";
  	            return $http.put(_config.url + "/api/termPerspectives/" + id + "/splashedRow", splashedRow, config)
  	    	}
  	    };



  	    if (this.getTermPerspectiveTerm) {
  	    	console.log("getTermPerspectiveTerm");
  	    }
  	    this.getTermPerspectiveTerm = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/termPerspectives/" + id + "/term",  config)
  	    };

  	    if (this.putTermPerspectiveTerm) {
  	    	window.console && console.log("putTermPerspectiveTerm");
  	    }
  	    this.putTermPerspectiveTerm = function(id, term) {
  	    	if (term === null) {
  	            return $http.delete(_config.url + "/api/termPerspectives/" + id + "/term")
  	    	} else {
  	            var _data = "";
  	            for (var i = 0; i < term.length; ++i) {
  	                _data += term[i] + "\n";
  	            }
  	            var config = {};
  	            config.headers = {};
  	            config.headers['Content-Type'] = "text/uri-list";
  	            return $http.put(_config.url + "/api/termPerspectives/" + id + "/term", term, config)
  	    	}
  	    };

  	/*---------------------------------------------------------------------------*/

  	/*---------------------------------------------------------------------------*/
  		if (this.getUsers) {
  			window.console && console.log("getUsers");
  		}
  	    this.getUsers = function(_page, _size, _sort, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["page"] = _page;
  	        config.params["size"] = _size;
  	        config.params["sort"] = _sort;
  	        config.params["projection"] = projection;
  			return $http.get(_config.url + "/api/users",  config)
  	    }

  	    if (this.postUser) {
  	        window.console && console.log("postUser");
  	    }
  	    this.postUser = function(user) {
  	        var config = {};
  	        config.headers = {};
  	        config.headers["Content-Type"] = "application/json"
  	        return $http.post(_config.url + "/api/users", user, config)
  	    }

  	    if (this.getUser) {
  	        window.console && console.log("getUser");
  	    }
  	    this.getUser = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/users/" + id,  config)
  	    }

  	    if (this.putUser) {
  	        console.log("putUser");
  	    }
  	    this.putUser = function(user) {
  	        var config = {};
  	        config.headers = {};
  	        config.headers["Content-Type"] = "application/json"
  	        return $http.put(_config.url + "/api/users/" + user.id, user, config)
  	    }

  	    if (this.deleteUser) {
  	        console.log("deleteUser");
  	    }
  	    this.deleteUser = function(id) {
  	        return $http.delete("/api/users/" + id);
  	    }

  	    if (this.existsByUsername) {
  	    	window.console && console.log("existsByUsername");
  	    }
  	    this.existsByUsername = function(username, projection) {
  	        var config = {};
  	        config.params = {
  	            username: username,

  	        }
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/users/search/existsByUsername",  config)
  	    };


  	/*---------------------------------------------------------------------------*/

  	/*---------------------------------------------------------------------------*/
  		if (this.getVideos) {
  			window.console && console.log("getVideos");
  		}
  	    this.getVideos = function(_page, _size, _sort, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["page"] = _page;
  	        config.params["size"] = _size;
  	        config.params["sort"] = _sort;
  	        config.params["projection"] = projection;
  			return $http.get(_config.url + "/api/videos",  config)
  	    }

  	    if (this.postVideo) {
  	        window.console && console.log("postVideo");
  	    }
  	    this.postVideo = function(video) {
  	        var config = {};
  	        config.headers = {};
  	        config.headers["Content-Type"] = "application/json"
  	        return $http.post(_config.url + "/api/videos", video, config)
  	    }

  	    if (this.getVideo) {
  	        window.console && console.log("getVideo");
  	    }
  	    this.getVideo = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/videos/" + id,  config)
  	    }

  	    if (this.putVideo) {
  	        console.log("putVideo");
  	    }
  	    this.putVideo = function(video) {
  	        var config = {};
  	        config.headers = {};
  	        config.headers["Content-Type"] = "application/json"
  	        return $http.put(_config.url + "/api/videos/" + video.id, video, config)
  	    }

  	    if (this.deleteVideo) {
  	        console.log("deleteVideo");
  	    }
  	    this.deleteVideo = function(id) {
  	        return $http.delete("/api/videos/" + id);
  	    }



  	/*---------------------------------------------------------------------------*/
  }

  provider.$get = function($http){
  	var instance = new Trix($http)
  	if(_config.username && _config.password)
  		instance.login(_config.username, _config.password)
  	return instance;
  }

  return provider;
});
