function AdDto(id, imageId, imageSmallId, imageMediumId, imageLargeId, link, updatedAt, createdAt) {
	return {
		id: id,
		imageId: imageId,
		imageSmallId: imageSmallId,
		imageMediumId: imageMediumId,
		imageLargeId: imageLargeId,
		link: link,
		updatedAt: updatedAt,
		createdAt: createdAt
	};
}

function AndroidAppDto(id, projectName, appName, keyAlias, packageSuffix, host, shortDescription, fullDescription, videoUrl, apkUrl, icon, updatedAt, createdAt, networkId) {
	return {
		id: id,
		projectName: projectName,
		appName: appName,
		keyAlias: keyAlias,
		packageSuffix: packageSuffix,
		host: host,
		shortDescription: shortDescription,
		fullDescription: fullDescription,
		videoUrl: videoUrl,
		apkUrl: apkUrl,
		icon: icon,
		updatedAt: updatedAt,
		createdAt: createdAt,
		networkId: networkId
	};
}

function BaseObjectQueryDto(id, sorts, exceptionIds, updatedAt, createdAt, networkId) {
	return {
		id: id,
		sorts: sorts,
		exceptionIds: exceptionIds,
		updatedAt: updatedAt,
		createdAt: createdAt,
		networkId: networkId
	};
}

function BaseSectionDto(id, title, properties, updatedAt, createdAt, networkId) {
	return {
		id: id,
		title: title,
		properties: properties,
		updatedAt: updatedAt,
		createdAt: createdAt,
		networkId: networkId
	};
}

function CellDto(id, index, featured, row, post) {
	return {
		id: id,
		index: index,
		featured: featured,
		row: row,
		post: post
	};
}

function CommentDto(id, date, lastModificationDate, body, author, post) {
	return {
		id: id,
		date: date,
		lastModificationDate: lastModificationDate,
		body: body,
		author: author,
		post: post
	};
}

function EventDto(id, sessionId, device, userId, eventType, updatedAt, createdAt, networkId) {
	return {
		id: id,
		sessionId: sessionId,
		device: device,
		userId: userId,
		eventType: eventType,
		updatedAt: updatedAt,
		createdAt: createdAt,
		networkId: networkId
	};
}

function FileDto(id, type, directory, mime, name, url, hash, size, updatedAt, createdAt, networkId) {
	return {
		id: id,
		type: type,
		directory: directory,
		mime: mime,
		name: name,
		url: url,
		hash: hash,
		size: size,
		updatedAt: updatedAt,
		createdAt: createdAt,
		networkId: networkId
	};
}

function FixedQueryDto(id, updatedAt, createdAt, networkId) {
	return {
		id: id,
		updatedAt: updatedAt,
		createdAt: createdAt,
		networkId: networkId
	};
}

function GlobalParameterDto(id, parameterName, value) {
	return {
		id: id,
		parameterName: parameterName,
		value: value
	};
}

function ImageDto(id, title, caption, credits, type, hashs, original, small, medium, large, originalHash, smallHash, mediumHash, largeHash, vertical, postId, updatedAt, createdAt, networkId) {
	return {
		id: id,
		title: title,
		caption: caption,
		credits: credits,
		type: type,
		hashs: hashs,
		original: original,
		small: small,
		medium: medium,
		large: large,
		originalHash: originalHash,
		smallHash: smallHash,
		mediumHash: mediumHash,
		largeHash: largeHash,
		vertical: vertical,
		postId: postId,
		updatedAt: updatedAt,
		createdAt: createdAt,
		networkId: networkId
	};
}

function NetworkDto(id, name, flurryKey, flurryAppleKey, trackingId, allowSignup, allowSocialLogin, facebookAppID, facebookAppSecret, googleAppID, facebookLink, youtubeLink, googlePlusLink, twitterLink, webFooter, googleAppSecret, allowSponsors, stationMenuName, homeTabName, domain, networkCreationToken, info, loginFooterMessage, backgroundColor, navbarColor, navbarSecondaryColor, mainColor, primaryFont, secondaryFont, titleFontSize, newsFontSize, subdomain, configured, logoId, logoSmallId, logoHash, logoSmallHash, faviconHash, splashImageHash, loginImageHash, loginImageSmallHash, faviconId, splashImageId, loginImageId, loginImageSmallId, defaultReadMode, defaultOrientationMode, categoriesTaxonomyId, invitationMessage, playStoreAddress, appleStoreAddress, addStationRolesOnSignup, primaryColors, secondaryColors, alertColors, backgroundColors, createdAt, updatedAt) {
	return {
		id: id,
		name: name,
		flurryKey: flurryKey,
		flurryAppleKey: flurryAppleKey,
		trackingId: trackingId,
		allowSignup: allowSignup,
		allowSocialLogin: allowSocialLogin,
		facebookAppID: facebookAppID,
		facebookAppSecret: facebookAppSecret,
		googleAppID: googleAppID,
		facebookLink: facebookLink,
		youtubeLink: youtubeLink,
		googlePlusLink: googlePlusLink,
		twitterLink: twitterLink,
		webFooter: webFooter,
		googleAppSecret: googleAppSecret,
		allowSponsors: allowSponsors,
		stationMenuName: stationMenuName,
		homeTabName: homeTabName,
		domain: domain,
		networkCreationToken: networkCreationToken,
		info: info,
		loginFooterMessage: loginFooterMessage,
		backgroundColor: backgroundColor,
		navbarColor: navbarColor,
		navbarSecondaryColor: navbarSecondaryColor,
		mainColor: mainColor,
		primaryFont: primaryFont,
		secondaryFont: secondaryFont,
		titleFontSize: titleFontSize,
		newsFontSize: newsFontSize,
		subdomain: subdomain,
		configured: configured,
		logoId: logoId,
		logoSmallId: logoSmallId,
		logoHash: logoHash,
		logoSmallHash: logoSmallHash,
		faviconHash: faviconHash,
		splashImageHash: splashImageHash,
		loginImageHash: loginImageHash,
		loginImageSmallHash: loginImageSmallHash,
		faviconId: faviconId,
		splashImageId: splashImageId,
		loginImageId: loginImageId,
		loginImageSmallId: loginImageSmallId,
		defaultReadMode: defaultReadMode,
		defaultOrientationMode: defaultOrientationMode,
		categoriesTaxonomyId: categoriesTaxonomyId,
		invitationMessage: invitationMessage,
		playStoreAddress: playStoreAddress,
		appleStoreAddress: appleStoreAddress,
		addStationRolesOnSignup: addStationRolesOnSignup,
		primaryColors: primaryColors,
		secondaryColors: secondaryColors,
		alertColors: alertColors,
		backgroundColors: backgroundColors,
		createdAt: createdAt,
		updatedAt: updatedAt
	};
}

function NetworkRoleDto(id, network, person, admin, updatedAt, createdAt, networkId) {
	return {
		id: id,
		network: network,
		person: person,
		admin: admin,
		updatedAt: updatedAt,
		createdAt: createdAt,
		networkId: networkId
	};
}

function NotificationDto(id, person, network, hash, postId, seen, test, message, type, createdAt, updatedAt) {
	return {
		id: id,
		person: person,
		network: network,
		hash: hash,
		postId: postId,
		seen: seen,
		test: test,
		message: message,
		type: type,
		createdAt: createdAt,
		updatedAt: updatedAt
	};
}

function PageDto(id, title, updatedAt, createdAt, networkId) {
	return {
		id: id,
		title: title,
		updatedAt: updatedAt,
		createdAt: createdAt,
		networkId: networkId
	};
}

function PageableQueryDto(id, updatedAt, createdAt, networkId) {
	return {
		id: id,
		updatedAt: updatedAt,
		createdAt: createdAt,
		networkId: networkId
	};
}

function PasswordResetDto(id, hash, email, personName, active, invite, networkSubdomain, networkName, createdAt, updatedAt) {
	return {
		id: id,
		hash: hash,
		email: email,
		personName: personName,
		active: active,
		invite: invite,
		networkSubdomain: networkSubdomain,
		networkName: networkName,
		createdAt: createdAt,
		updatedAt: updatedAt
	};
}

function PersonDto(id, name, username, bookmarkPosts, user, bio, email, imageUrl, coverUrl, lastLogin, imageId, imageSmallId, imageMediumId, imageLargeId, coverLargeId, coverId, imageHash, imageSmallHash, imageMediumHash, imageLargeHash, coverMediumHash, coverLargeHash, coverHash, passwordReseted, twitterHandle, coverMediumId, updatedAt, createdAt, networkId) {
	return {
		id: id,
		name: name,
		username: username,
		bookmarkPosts: bookmarkPosts,
		user: user,
		bio: bio,
		email: email,
		imageUrl: imageUrl,
		coverUrl: coverUrl,
		lastLogin: lastLogin,
		imageId: imageId,
		imageSmallId: imageSmallId,
		imageMediumId: imageMediumId,
		imageLargeId: imageLargeId,
		coverLargeId: coverLargeId,
		coverId: coverId,
		imageHash: imageHash,
		imageSmallHash: imageSmallHash,
		imageMediumHash: imageMediumHash,
		imageLargeHash: imageLargeHash,
		coverMediumHash: coverMediumHash,
		coverLargeHash: coverLargeHash,
		coverHash: coverHash,
		passwordReseted: passwordReseted,
		twitterHandle: twitterHandle,
		coverMediumId: coverMediumId,
		updatedAt: updatedAt,
		createdAt: createdAt,
		networkId: networkId
	};
}

function PersonNetworkRegIdDto(id, regId, network, lat, lng, updatedAt, createdAt, networkId) {
	return {
		id: id,
		regId: regId,
		network: network,
		lat: lat,
		lng: lng,
		updatedAt: updatedAt,
		createdAt: createdAt,
		networkId: networkId
	};
}

function PersonNetworkTokenDto(id, token, network, createdAt, lat, lng, updatedAt) {
	return {
		id: id,
		token: token,
		network: network,
		createdAt: createdAt,
		lat: lat,
		lng: lng,
		updatedAt: updatedAt
	};
}

function PictureDto(id, height, width, sizeTag, file, updatedAt, createdAt, networkId) {
	return {
		id: id,
		height: height,
		width: width,
		sizeTag: sizeTag,
		file: file,
		updatedAt: updatedAt,
		createdAt: createdAt,
		networkId: networkId
	};
}

function PostDto(id, originalPostId, date, lastModificationDate, title, body, topper, subheading, originalSlug, scheduledDate, slug, state, author, station, stationId, readsCount, bookmarksCount, recommendsCount, commentsCount, tags, imageLandscape, externalFeaturedImgUrl, externalVideoUrl, readTime, notify, imageCaptionText, imageCreditsText, lat, lng, imageTitleText, imageId, imageSmallId, imageMediumId, imageLargeId, imageHash, imageSmallHash, imageMediumHash, imageLargeHash, featuredVideoHash, featuredAudioHash, updatedAt, createdAt, networkId) {
	return {
		id: id,
		originalPostId: originalPostId,
		date: date,
		lastModificationDate: lastModificationDate,
		title: title,
		body: body,
		topper: topper,
		subheading: subheading,
		originalSlug: originalSlug,
		scheduledDate: scheduledDate,
		slug: slug,
		state: state,
		author: author,
		station: station,
		stationId: stationId,
		readsCount: readsCount,
		bookmarksCount: bookmarksCount,
		recommendsCount: recommendsCount,
		commentsCount: commentsCount,
		tags: tags,
		imageLandscape: imageLandscape,
		externalFeaturedImgUrl: externalFeaturedImgUrl,
		externalVideoUrl: externalVideoUrl,
		readTime: readTime,
		notify: notify,
		imageCaptionText: imageCaptionText,
		imageCreditsText: imageCreditsText,
		lat: lat,
		lng: lng,
		imageTitleText: imageTitleText,
		imageId: imageId,
		imageSmallId: imageSmallId,
		imageMediumId: imageMediumId,
		imageLargeId: imageLargeId,
		imageHash: imageHash,
		imageSmallHash: imageSmallHash,
		imageMediumHash: imageMediumHash,
		imageLargeHash: imageLargeHash,
		featuredVideoHash: featuredVideoHash,
		featuredAudioHash: featuredAudioHash,
		updatedAt: updatedAt,
		createdAt: createdAt,
		networkId: networkId
	};
}

function PostReadDto(id, createdAt, updatedAt, sessionid, person_session) {
	return {
		id: id,
		createdAt: createdAt,
		updatedAt: updatedAt,
		sessionid: sessionid,
		person_session: person_session
	};
}

function RecommendDto(id, post, person, createdAt, updatedAt) {
	return {
		id: id,
		post: post,
		person: person,
		createdAt: createdAt,
		updatedAt: updatedAt
	};
}

function RowDto(id, type, maxPosts, index) {
	return {
		id: id,
		type: type,
		maxPosts: maxPosts,
		index: index
	};
}

function SectionDto(id, name, loggedInUrl, anonymousUrl, content) {
	return {
		id: id,
		name: name,
		loggedInUrl: loggedInUrl,
		anonymousUrl: anonymousUrl,
		content: content
	};
}

function SponsorDto(id, network, name, keywords, link, logoId, logoMediumId, logoLargeId, updatedAt, createdAt) {
	return {
		id: id,
		network: network,
		name: name,
		keywords: keywords,
		link: link,
		logoId: logoId,
		logoMediumId: logoMediumId,
		logoLargeId: logoLargeId,
		updatedAt: updatedAt,
		createdAt: createdAt
	};
}

function StationDto(id, name, writable, main, visibility, allowComments, allowSocialShare, allowWritersToNotify, allowWritersToAddSponsors, backgroundColor, navbarColor, primaryColor, stationPerspectives, categoriesTaxonomyId, tagsTaxonomyId, postsTitleSize, topper, subheading, sponsored, showAuthorSocialData, showAuthorData, logoId, logoMediumId, logoHash, logoMediumHash, defaultPerspectiveId, updatedAt, createdAt, networkId) {
	return {
		id: id,
		name: name,
		writable: writable,
		main: main,
		visibility: visibility,
		allowComments: allowComments,
		allowSocialShare: allowSocialShare,
		allowWritersToNotify: allowWritersToNotify,
		allowWritersToAddSponsors: allowWritersToAddSponsors,
		backgroundColor: backgroundColor,
		navbarColor: navbarColor,
		primaryColor: primaryColor,
		stationPerspectives: stationPerspectives,
		categoriesTaxonomyId: categoriesTaxonomyId,
		tagsTaxonomyId: tagsTaxonomyId,
		postsTitleSize: postsTitleSize,
		topper: topper,
		subheading: subheading,
		sponsored: sponsored,
		showAuthorSocialData: showAuthorSocialData,
		showAuthorData: showAuthorData,
		logoId: logoId,
		logoMediumId: logoMediumId,
		logoHash: logoHash,
		logoMediumHash: logoMediumHash,
		defaultPerspectiveId: defaultPerspectiveId,
		updatedAt: updatedAt,
		createdAt: createdAt,
		networkId: networkId
	};
}

function StationPerspectiveDto(id, name, station, stationId, taxonomyId, taxonomyName, taxonomyType) {
	return {
		id: id,
		name: name,
		station: station,
		stationId: stationId,
		taxonomyId: taxonomyId,
		taxonomyName: taxonomyName,
		taxonomyType: taxonomyType
	};
}

function StationRoleDto(id, station, person, editor, writer, admin) {
	return {
		id: id,
		station: station,
		person: person,
		editor: editor,
		writer: writer,
		admin: admin
	};
}

function TaxonomyDto(id, type, name, createdAt, updatedAt) {
	return {
		id: id,
		type: type,
		name: name,
		createdAt: createdAt,
		updatedAt: updatedAt
	};
}

function TermDto(id, name, name_parent, taxonomy, taxonomyId, taxonomyName, updatedAt, createdAt, networkId) {
	return {
		id: id,
		name: name,
		name_parent: name_parent,
		taxonomy: taxonomy,
		taxonomyId: taxonomyId,
		taxonomyName: taxonomyName,
		updatedAt: updatedAt,
		createdAt: createdAt,
		networkId: networkId
	};
}

function TermPerspectiveDto(id, showPopular, showRecent, perspective, taxonomyId, stationId, defaultImageHash) {
	return {
		id: id,
		showPopular: showPopular,
		showRecent: showRecent,
		perspective: perspective,
		taxonomyId: taxonomyId,
		stationId: stationId,
		defaultImageHash: defaultImageHash
	};
}

function UserDto(id, username, password, enabled, updatedAt, createdAt, networkId) {
	return {
		id: id,
		username: username,
		password: password,
		enabled: enabled,
		updatedAt: updatedAt,
		createdAt: createdAt,
		networkId: networkId
	};
}

function UserConnectionDto(id, accessToken, providerUserId, user, providerId, displayName, profileUrl, email, imageUrl, secret, refreshToken, expireTime) {
	return {
		id: id,
		accessToken: accessToken,
		providerUserId: providerUserId,
		user: user,
		providerId: providerId,
		displayName: displayName,
		profileUrl: profileUrl,
		email: email,
		imageUrl: imageUrl,
		secret: secret,
		refreshToken: refreshToken,
		expireTime: expireTime
	};
}

function UserGrantedAuthorityDto(id, authority, updatedAt, createdAt, networkId) {
	return {
		id: id,
		authority: authority,
		updatedAt: updatedAt,
		createdAt: createdAt,
		networkId: networkId
	};
}

function VideoDto(id, duration, file, updatedAt, createdAt, networkId) {
	return {
		id: id,
		duration: duration,
		file: file,
		updatedAt: updatedAt,
		createdAt: createdAt,
		networkId: networkId
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

	this.recoverPassword = function(email) {
      var config = {};
      config.headers = {"Content-Type": "application/x-www-form-urlencoded"};
      return $http.post(_config.url + "/api/persons/sendPassword", $.param({"email": email}), config);
    }

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

  	/*---------------------------------------------------------------------------*/
  		if (this.getAds) {
  			window.console && console.log("getAds");
  		}
  	    this.getAds = function(_page, _size, _sort, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["page"] = page;
  	        config.params["size"] = size;
  	        config.params["sort"] = sort;
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

  	/*---------------------------------------------------------------------------*/

  	/*---------------------------------------------------------------------------*/
  		if (this.getAndroidApps) {
  			window.console && console.log("getAndroidApps");
  		}
  	    this.getAndroidApps = function(_page, _size, _sort, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["page"] = page;
  	        config.params["size"] = size;
  	        config.params["sort"] = sort;
  	        config.params["projection"] = projection;
  			return $http.get(_config.url + "/api/androidApps",  config)
  	    }

  	    if (this.postAndroidApp) {
  	        window.console && console.log("postAndroidApp");
  	    }
  	    this.postAndroidApp = function(androidApp) {
  	        var config = {};
  	        config.headers = {};
  	        config.headers["Content-Type"] = "application/json"
  	        return $http.post(_config.url + "/api/androidApps", androidApp, config)
  	    }

  	    if (this.getAndroidApp) {
  	        window.console && console.log("getAndroidApp");
  	    }
  	    this.getAndroidApp = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/androidApps/" + id,  config)
  	    }

  	    if (this.putAndroidApp) {
  	        console.log("putAndroidApp");
  	    }
  	    this.putAndroidApp = function(androidApp) {
  	        var config = {};
  	        config.headers = {};
  	        config.headers["Content-Type"] = "application/json"
  	        return $http.put(_config.url + "/api/androidApps/" + androidApp.id, androidApp, config)
  	    }

  	    if (this.deleteAndroidApp) {
  	        console.log("deleteAndroidApp");
  	    }
  	    this.deleteAndroidApp = function(id) {
  	        return $http.delete("/api/androidApps/" + id);
  	    }



  	    if (this.getAndroidAppIcon) {
  	    	console.log("getAndroidAppIcon");
  	    }
  	    this.getAndroidAppIcon = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/androidApps/" + id + "/icon",  config)
  	    };

  	    if (this.putAndroidAppIcon) {
  	    	window.console && console.log("putAndroidAppIcon");
  	    }
  	    this.putAndroidAppIcon = function(id, icon) {
  	    	if (icon === null) {
  	            return $http.delete(_config.url + "/api/androidApps/" + id + "/icon")
  	    	} else {
  	            var _data = "";
  	            for (var i = 0; i < icon.length; ++i) {
  	                _data += icon[i] + "\n";
  	            }
  	            var config = {};
  	            config.headers = {};
  	            config.headers['Content-Type'] = "text/uri-list";
  	            return $http.put(_config.url + "/api/androidApps/" + id + "/icon", icon, config)
  	    	}
  	    };

  	/*---------------------------------------------------------------------------*/

  	/*---------------------------------------------------------------------------*/
  		if (this.getBaseObjectQueries) {
  			window.console && console.log("getBaseObjectQueries");
  		}
  	    this.getBaseObjectQueries = function(_page, _size, _sort, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["page"] = page;
  	        config.params["size"] = size;
  	        config.params["sort"] = sort;
  	        config.params["projection"] = projection;
  			return $http.get(_config.url + "/api/baseObjectQueries",  config)
  	    }

  	    if (this.postBaseObjectQuery) {
  	        window.console && console.log("postBaseObjectQuery");
  	    }
  	    this.postBaseObjectQuery = function(baseObjectQuery) {
  	        var config = {};
  	        config.headers = {};
  	        config.headers["Content-Type"] = "application/json"
  	        return $http.post(_config.url + "/api/baseObjectQueries", baseObjectQuery, config)
  	    }

  	    if (this.getBaseObjectQuery) {
  	        window.console && console.log("getBaseObjectQuery");
  	    }
  	    this.getBaseObjectQuery = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/baseObjectQueries/" + id,  config)
  	    }

  	    if (this.putBaseObjectQuery) {
  	        console.log("putBaseObjectQuery");
  	    }
  	    this.putBaseObjectQuery = function(baseObjectQuery) {
  	        var config = {};
  	        config.headers = {};
  	        config.headers["Content-Type"] = "application/json"
  	        return $http.put(_config.url + "/api/baseObjectQueries/" + baseObjectQuery.id, baseObjectQuery, config)
  	    }

  	    if (this.deleteBaseObjectQuery) {
  	        console.log("deleteBaseObjectQuery");
  	    }
  	    this.deleteBaseObjectQuery = function(id) {
  	        return $http.delete("/api/baseObjectQueries/" + id);
  	    }



  	/*---------------------------------------------------------------------------*/

  	/*---------------------------------------------------------------------------*/
  		if (this.getBaseSections) {
  			window.console && console.log("getBaseSections");
  		}
  	    this.getBaseSections = function(_page, _size, _sort, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["page"] = page;
  	        config.params["size"] = size;
  	        config.params["sort"] = sort;
  	        config.params["projection"] = projection;
  			return $http.get(_config.url + "/api/baseSections",  config)
  	    }

  	    if (this.postBaseSection) {
  	        window.console && console.log("postBaseSection");
  	    }
  	    this.postBaseSection = function(baseSection) {
  	        var config = {};
  	        config.headers = {};
  	        config.headers["Content-Type"] = "application/json"
  	        return $http.post(_config.url + "/api/baseSections", baseSection, config)
  	    }

  	    if (this.getBaseSection) {
  	        window.console && console.log("getBaseSection");
  	    }
  	    this.getBaseSection = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/baseSections/" + id,  config)
  	    }

  	    if (this.putBaseSection) {
  	        console.log("putBaseSection");
  	    }
  	    this.putBaseSection = function(baseSection) {
  	        var config = {};
  	        config.headers = {};
  	        config.headers["Content-Type"] = "application/json"
  	        return $http.put(_config.url + "/api/baseSections/" + baseSection.id, baseSection, config)
  	    }

  	    if (this.deleteBaseSection) {
  	        console.log("deleteBaseSection");
  	    }
  	    this.deleteBaseSection = function(id) {
  	        return $http.delete("/api/baseSections/" + id);
  	    }



  	/*---------------------------------------------------------------------------*/

  	/*---------------------------------------------------------------------------*/
  		if (this.getCells) {
  			window.console && console.log("getCells");
  		}
  	    this.getCells = function(_page, _size, _sort, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["page"] = page;
  	        config.params["size"] = size;
  	        config.params["sort"] = sort;
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

  	/*---------------------------------------------------------------------------*/

  	/*---------------------------------------------------------------------------*/
  		if (this.getComments) {
  			window.console && console.log("getComments");
  		}
  	    this.getComments = function(_page, _size, _sort, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["page"] = page;
  	        config.params["size"] = size;
  	        config.params["sort"] = sort;
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
  		if (this.getEvents) {
  			window.console && console.log("getEvents");
  		}
  	    this.getEvents = function(_page, _size, _sort, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["page"] = page;
  	        config.params["size"] = size;
  	        config.params["sort"] = sort;
  	        config.params["projection"] = projection;
  			return $http.get(_config.url + "/api/events",  config)
  	    }

  	    if (this.postEvent) {
  	        window.console && console.log("postEvent");
  	    }
  	    this.postEvent = function(event) {
  	        var config = {};
  	        config.headers = {};
  	        config.headers["Content-Type"] = "application/json"
  	        return $http.post(_config.url + "/api/events", event, config)
  	    }

  	    if (this.getEvent) {
  	        window.console && console.log("getEvent");
  	    }
  	    this.getEvent = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/events/" + id,  config)
  	    }

  	    if (this.putEvent) {
  	        console.log("putEvent");
  	    }
  	    this.putEvent = function(event) {
  	        var config = {};
  	        config.headers = {};
  	        config.headers["Content-Type"] = "application/json"
  	        return $http.put(_config.url + "/api/events/" + event.id, event, config)
  	    }

  	    if (this.deleteEvent) {
  	        console.log("deleteEvent");
  	    }
  	    this.deleteEvent = function(id) {
  	        return $http.delete("/api/events/" + id);
  	    }



  	/*---------------------------------------------------------------------------*/

  	/*---------------------------------------------------------------------------*/
  		if (this.getFiles) {
  			window.console && console.log("getFiles");
  		}
  	    this.getFiles = function(_page, _size, _sort, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["page"] = page;
  	        config.params["size"] = size;
  	        config.params["sort"] = sort;
  	        config.params["projection"] = projection;
  			return $http.get(_config.url + "/api/files",  config)
  	    }

  	    if (this.postFile) {
  	        window.console && console.log("postFile");
  	    }
  	    this.postFile = function(file) {
  	        var config = {};
  	        config.headers = {};
  	        config.headers["Content-Type"] = "application/json"
  	        return $http.post(_config.url + "/api/files", file, config)
  	    }

  	    if (this.getFile) {
  	        window.console && console.log("getFile");
  	    }
  	    this.getFile = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/files/" + id,  config)
  	    }

  	    if (this.putFile) {
  	        console.log("putFile");
  	    }
  	    this.putFile = function(file) {
  	        var config = {};
  	        config.headers = {};
  	        config.headers["Content-Type"] = "application/json"
  	        return $http.put(_config.url + "/api/files/" + file.id, file, config)
  	    }

  	    if (this.deleteFile) {
  	        console.log("deleteFile");
  	    }
  	    this.deleteFile = function(id) {
  	        return $http.delete("/api/files/" + id);
  	    }



  	/*---------------------------------------------------------------------------*/

  	/*---------------------------------------------------------------------------*/
  		if (this.getFixedQueries) {
  			window.console && console.log("getFixedQueries");
  		}
  	    this.getFixedQueries = function(_page, _size, _sort, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["page"] = page;
  	        config.params["size"] = size;
  	        config.params["sort"] = sort;
  	        config.params["projection"] = projection;
  			return $http.get(_config.url + "/api/fixedQueries",  config)
  	    }

  	    if (this.postFixedQuery) {
  	        window.console && console.log("postFixedQuery");
  	    }
  	    this.postFixedQuery = function(fixedQuery) {
  	        var config = {};
  	        config.headers = {};
  	        config.headers["Content-Type"] = "application/json"
  	        return $http.post(_config.url + "/api/fixedQueries", fixedQuery, config)
  	    }

  	    if (this.getFixedQuery) {
  	        window.console && console.log("getFixedQuery");
  	    }
  	    this.getFixedQuery = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/fixedQueries/" + id,  config)
  	    }

  	    if (this.putFixedQuery) {
  	        console.log("putFixedQuery");
  	    }
  	    this.putFixedQuery = function(fixedQuery) {
  	        var config = {};
  	        config.headers = {};
  	        config.headers["Content-Type"] = "application/json"
  	        return $http.put(_config.url + "/api/fixedQueries/" + fixedQuery.id, fixedQuery, config)
  	    }

  	    if (this.deleteFixedQuery) {
  	        console.log("deleteFixedQuery");
  	    }
  	    this.deleteFixedQuery = function(id) {
  	        return $http.delete("/api/fixedQueries/" + id);
  	    }



  	    if (this.getFixedQueryObjectQuery) {
  	    	console.log("getFixedQueryObjectQuery");
  	    }
  	    this.getFixedQueryObjectQuery = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/fixedQueries/" + id + "/objectQuery",  config)
  	    };

  	    if (this.putFixedQueryObjectQuery) {
  	    	window.console && console.log("putFixedQueryObjectQuery");
  	    }
  	    this.putFixedQueryObjectQuery = function(id, objectQuery) {
  	    	if (objectQuery === null) {
  	            return $http.delete(_config.url + "/api/fixedQueries/" + id + "/objectQuery")
  	    	} else {
  	            var _data = "";
  	            for (var i = 0; i < objectQuery.length; ++i) {
  	                _data += objectQuery[i] + "\n";
  	            }
  	            var config = {};
  	            config.headers = {};
  	            config.headers['Content-Type'] = "text/uri-list";
  	            return $http.put(_config.url + "/api/fixedQueries/" + id + "/objectQuery", objectQuery, config)
  	    	}
  	    };

  	/*---------------------------------------------------------------------------*/

  	/*---------------------------------------------------------------------------*/
  		if (this.getGlobalParameters) {
  			window.console && console.log("getGlobalParameters");
  		}
  	    this.getGlobalParameters = function(_page, _size, _sort, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["page"] = page;
  	        config.params["size"] = size;
  	        config.params["sort"] = sort;
  	        config.params["projection"] = projection;
  			return $http.get(_config.url + "/api/globalParameters",  config)
  	    }

  	    if (this.postGlobalParameter) {
  	        window.console && console.log("postGlobalParameter");
  	    }
  	    this.postGlobalParameter = function(globalParameter) {
  	        var config = {};
  	        config.headers = {};
  	        config.headers["Content-Type"] = "application/json"
  	        return $http.post(_config.url + "/api/globalParameters", globalParameter, config)
  	    }

  	    if (this.getGlobalParameter) {
  	        window.console && console.log("getGlobalParameter");
  	    }
  	    this.getGlobalParameter = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/globalParameters/" + id,  config)
  	    }

  	    if (this.putGlobalParameter) {
  	        console.log("putGlobalParameter");
  	    }
  	    this.putGlobalParameter = function(globalParameter) {
  	        var config = {};
  	        config.headers = {};
  	        config.headers["Content-Type"] = "application/json"
  	        return $http.put(_config.url + "/api/globalParameters/" + globalParameter.id, globalParameter, config)
  	    }

  	    if (this.deleteGlobalParameter) {
  	        console.log("deleteGlobalParameter");
  	    }
  	    this.deleteGlobalParameter = function(id) {
  	        return $http.delete("/api/globalParameters/" + id);
  	    }



  	/*---------------------------------------------------------------------------*/

  	/*---------------------------------------------------------------------------*/
  		if (this.getImages) {
  			window.console && console.log("getImages");
  		}
  	    this.getImages = function(_page, _size, _sort, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["page"] = page;
  	        config.params["size"] = size;
  	        config.params["sort"] = sort;
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


  	    if (this.getImagePictures) {
  	    	console.log("getImagePictures");
  	    }
  	    this.getImagePictures = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/images/" + id + "/pictures",  config)
  	    };
  	    if (this.patchImagePictures) {
  	    	window.console && console.log("patchImagePictures");
  	    }
  	    this.patchImagePictures = function(id, pictures) {		
  	    	var _data = "";
  	    	for (var i = 0; i < pictures.length; ++i) {
  	    		_data += pictures[i] + "\n";
  	    	}
  	        var config = {};
  	        config.headers = {};
  	        config.headers['Content-Type'] = "text/uri-list";
  	        return $http.get(_config.url + "/api/images/" + id + "/pictures", _data, config)
  	    };

  	    if (this.putImagePictures) {
  	    	window.console && console.log("putImagePictures");
  	    }
  	    this.putImagePictures = function(id, pictures) {		
  	    	var _data = "";
  	        for (var i = 0; i < pictures.length; ++i) {
  	            _data += pictures[i] + "\n";
  	        }
  	        var config = {};
  	        config.headers = {};
  	        config.headers['Content-Type'] = "text/uri-list";
  	        return $http.put(_config.url + "/api/images/" + id + "/pictures", _data, config)
  	    };



  	    if (this.getImageOriginal) {
  	    	console.log("getImageOriginal");
  	    }
  	    this.getImageOriginal = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/images/" + id + "/original",  config)
  	    };

  	    if (this.putImageOriginal) {
  	    	window.console && console.log("putImageOriginal");
  	    }
  	    this.putImageOriginal = function(id, original) {
  	    	if (original === null) {
  	            return $http.delete(_config.url + "/api/images/" + id + "/original")
  	    	} else {
  	            var _data = "";
  	            for (var i = 0; i < original.length; ++i) {
  	                _data += original[i] + "\n";
  	            }
  	            var config = {};
  	            config.headers = {};
  	            config.headers['Content-Type'] = "text/uri-list";
  	            return $http.put(_config.url + "/api/images/" + id + "/original", original, config)
  	    	}
  	    };



  	    if (this.getImageSmall) {
  	    	console.log("getImageSmall");
  	    }
  	    this.getImageSmall = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/images/" + id + "/small",  config)
  	    };

  	    if (this.putImageSmall) {
  	    	window.console && console.log("putImageSmall");
  	    }
  	    this.putImageSmall = function(id, small) {
  	    	if (small === null) {
  	            return $http.delete(_config.url + "/api/images/" + id + "/small")
  	    	} else {
  	            var _data = "";
  	            for (var i = 0; i < small.length; ++i) {
  	                _data += small[i] + "\n";
  	            }
  	            var config = {};
  	            config.headers = {};
  	            config.headers['Content-Type'] = "text/uri-list";
  	            return $http.put(_config.url + "/api/images/" + id + "/small", small, config)
  	    	}
  	    };



  	    if (this.getImageMedium) {
  	    	console.log("getImageMedium");
  	    }
  	    this.getImageMedium = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/images/" + id + "/medium",  config)
  	    };

  	    if (this.putImageMedium) {
  	    	window.console && console.log("putImageMedium");
  	    }
  	    this.putImageMedium = function(id, medium) {
  	    	if (medium === null) {
  	            return $http.delete(_config.url + "/api/images/" + id + "/medium")
  	    	} else {
  	            var _data = "";
  	            for (var i = 0; i < medium.length; ++i) {
  	                _data += medium[i] + "\n";
  	            }
  	            var config = {};
  	            config.headers = {};
  	            config.headers['Content-Type'] = "text/uri-list";
  	            return $http.put(_config.url + "/api/images/" + id + "/medium", medium, config)
  	    	}
  	    };



  	    if (this.getImageLarge) {
  	    	console.log("getImageLarge");
  	    }
  	    this.getImageLarge = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/images/" + id + "/large",  config)
  	    };

  	    if (this.putImageLarge) {
  	    	window.console && console.log("putImageLarge");
  	    }
  	    this.putImageLarge = function(id, large) {
  	    	if (large === null) {
  	            return $http.delete(_config.url + "/api/images/" + id + "/large")
  	    	} else {
  	            var _data = "";
  	            for (var i = 0; i < large.length; ++i) {
  	                _data += large[i] + "\n";
  	            }
  	            var config = {};
  	            config.headers = {};
  	            config.headers['Content-Type'] = "text/uri-list";
  	            return $http.put(_config.url + "/api/images/" + id + "/large", large, config)
  	    	}
  	    };

  	/*---------------------------------------------------------------------------*/

  	/*---------------------------------------------------------------------------*/
  		if (this.getNetworks) {
  			window.console && console.log("getNetworks");
  		}
  	    this.getNetworks = function(_page, _size, _sort, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["page"] = page;
  	        config.params["size"] = size;
  	        config.params["sort"] = sort;
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

  	    if (this.findBySubdomain) {
  	    	window.console && console.log("findBySubdomain");
  	    }
  	    this.findBySubdomain = function(subdomain, projection) {
  	        var config = {};
  	        config.params = {
  	            subdomain: subdomain,

  	        }
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/networks/search/findBySubdomain",  config)
  	    };

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

  	    if (this.getNetworkPersonsNetworkRoles) {
  	    	console.log("getNetworkPersonsNetworkRoles");
  	    }
  	    this.getNetworkPersonsNetworkRoles = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/networks/" + id + "/personsNetworkRoles",  config)
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


  	    if (this.getNetworkSponsors) {
  	    	console.log("getNetworkSponsors");
  	    }
  	    this.getNetworkSponsors = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/networks/" + id + "/sponsors",  config)
  	    };


  	    if (this.getNetworkSections) {
  	    	console.log("getNetworkSections");
  	    }
  	    this.getNetworkSections = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/networks/" + id + "/sections",  config)
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



  	    if (this.getNetworkLogo) {
  	    	console.log("getNetworkLogo");
  	    }
  	    this.getNetworkLogo = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/networks/" + id + "/logo",  config)
  	    };

  	    if (this.putNetworkLogo) {
  	    	window.console && console.log("putNetworkLogo");
  	    }
  	    this.putNetworkLogo = function(id, logo) {
  	    	if (logo === null) {
  	            return $http.delete(_config.url + "/api/networks/" + id + "/logo")
  	    	} else {
  	            var _data = "";
  	            for (var i = 0; i < logo.length; ++i) {
  	                _data += logo[i] + "\n";
  	            }
  	            var config = {};
  	            config.headers = {};
  	            config.headers['Content-Type'] = "text/uri-list";
  	            return $http.put(_config.url + "/api/networks/" + id + "/logo", logo, config)
  	    	}
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

  	/*---------------------------------------------------------------------------*/

  	/*---------------------------------------------------------------------------*/
  		if (this.getNetworkRoles) {
  			window.console && console.log("getNetworkRoles");
  		}
  	    this.getNetworkRoles = function(_page, _size, _sort, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["page"] = page;
  	        config.params["size"] = size;
  	        config.params["sort"] = sort;
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
  	        config.params["page"] = page;
  	        config.params["size"] = size;
  	        config.params["sort"] = sort;
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


  	    if (this.getNotificationPerson) {
  	    	console.log("getNotificationPerson");
  	    }
  	    this.getNotificationPerson = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/notifications/" + id + "/person",  config)
  	    };

  	    if (this.putNotificationPerson) {
  	    	window.console && console.log("putNotificationPerson");
  	    }
  	    this.putNotificationPerson = function(id, person) {
  	    	if (person === null) {
  	            return $http.delete(_config.url + "/api/notifications/" + id + "/person")
  	    	} else {
  	            var _data = "";
  	            for (var i = 0; i < person.length; ++i) {
  	                _data += person[i] + "\n";
  	            }
  	            var config = {};
  	            config.headers = {};
  	            config.headers['Content-Type'] = "text/uri-list";
  	            return $http.put(_config.url + "/api/notifications/" + id + "/person", person, config)
  	    	}
  	    };



  	    if (this.getNotificationNetwork) {
  	    	console.log("getNotificationNetwork");
  	    }
  	    this.getNotificationNetwork = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/notifications/" + id + "/network",  config)
  	    };

  	    if (this.putNotificationNetwork) {
  	    	window.console && console.log("putNotificationNetwork");
  	    }
  	    this.putNotificationNetwork = function(id, network) {
  	    	if (network === null) {
  	            return $http.delete(_config.url + "/api/notifications/" + id + "/network")
  	    	} else {
  	            var _data = "";
  	            for (var i = 0; i < network.length; ++i) {
  	                _data += network[i] + "\n";
  	            }
  	            var config = {};
  	            config.headers = {};
  	            config.headers['Content-Type'] = "text/uri-list";
  	            return $http.put(_config.url + "/api/notifications/" + id + "/network", network, config)
  	    	}
  	    };



  	    if (this.getNotificationStation) {
  	    	console.log("getNotificationStation");
  	    }
  	    this.getNotificationStation = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/notifications/" + id + "/station",  config)
  	    };

  	    if (this.putNotificationStation) {
  	    	window.console && console.log("putNotificationStation");
  	    }
  	    this.putNotificationStation = function(id, station) {
  	    	if (station === null) {
  	            return $http.delete(_config.url + "/api/notifications/" + id + "/station")
  	    	} else {
  	            var _data = "";
  	            for (var i = 0; i < station.length; ++i) {
  	                _data += station[i] + "\n";
  	            }
  	            var config = {};
  	            config.headers = {};
  	            config.headers['Content-Type'] = "text/uri-list";
  	            return $http.put(_config.url + "/api/notifications/" + id + "/station", station, config)
  	    	}
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
  	        config.params["page"] = page;
  	        config.params["size"] = size;
  	        config.params["sort"] = sort;
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


  	    if (this.getPageSections) {
  	    	console.log("getPageSections");
  	    }
  	    this.getPageSections = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/pages/" + id + "/sections",  config)
  	    };
  	    if (this.patchPageSections) {
  	    	window.console && console.log("patchPageSections");
  	    }
  	    this.patchPageSections = function(id, sections) {		
  	    	var _data = "";
  	    	for (var i = 0; i < sections.length; ++i) {
  	    		_data += sections[i] + "\n";
  	    	}
  	        var config = {};
  	        config.headers = {};
  	        config.headers['Content-Type'] = "text/uri-list";
  	        return $http.get(_config.url + "/api/pages/" + id + "/sections", _data, config)
  	    };

  	    if (this.putPageSections) {
  	    	window.console && console.log("putPageSections");
  	    }
  	    this.putPageSections = function(id, sections) {		
  	    	var _data = "";
  	        for (var i = 0; i < sections.length; ++i) {
  	            _data += sections[i] + "\n";
  	        }
  	        var config = {};
  	        config.headers = {};
  	        config.headers['Content-Type'] = "text/uri-list";
  	        return $http.put(_config.url + "/api/pages/" + id + "/sections", _data, config)
  	    };



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
  		if (this.getPageableQueries) {
  			window.console && console.log("getPageableQueries");
  		}
  	    this.getPageableQueries = function(_page, _size, _sort, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["page"] = page;
  	        config.params["size"] = size;
  	        config.params["sort"] = sort;
  	        config.params["projection"] = projection;
  			return $http.get(_config.url + "/api/pageableQueries",  config)
  	    }

  	    if (this.postPageableQuery) {
  	        window.console && console.log("postPageableQuery");
  	    }
  	    this.postPageableQuery = function(pageableQuery) {
  	        var config = {};
  	        config.headers = {};
  	        config.headers["Content-Type"] = "application/json"
  	        return $http.post(_config.url + "/api/pageableQueries", pageableQuery, config)
  	    }

  	    if (this.getPageableQuery) {
  	        window.console && console.log("getPageableQuery");
  	    }
  	    this.getPageableQuery = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/pageableQueries/" + id,  config)
  	    }

  	    if (this.putPageableQuery) {
  	        console.log("putPageableQuery");
  	    }
  	    this.putPageableQuery = function(pageableQuery) {
  	        var config = {};
  	        config.headers = {};
  	        config.headers["Content-Type"] = "application/json"
  	        return $http.put(_config.url + "/api/pageableQueries/" + pageableQuery.id, pageableQuery, config)
  	    }

  	    if (this.deletePageableQuery) {
  	        console.log("deletePageableQuery");
  	    }
  	    this.deletePageableQuery = function(id) {
  	        return $http.delete("/api/pageableQueries/" + id);
  	    }



  	    if (this.getPageableQueryObjectQuery) {
  	    	console.log("getPageableQueryObjectQuery");
  	    }
  	    this.getPageableQueryObjectQuery = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/pageableQueries/" + id + "/objectQuery",  config)
  	    };

  	    if (this.putPageableQueryObjectQuery) {
  	    	window.console && console.log("putPageableQueryObjectQuery");
  	    }
  	    this.putPageableQueryObjectQuery = function(id, objectQuery) {
  	    	if (objectQuery === null) {
  	            return $http.delete(_config.url + "/api/pageableQueries/" + id + "/objectQuery")
  	    	} else {
  	            var _data = "";
  	            for (var i = 0; i < objectQuery.length; ++i) {
  	                _data += objectQuery[i] + "\n";
  	            }
  	            var config = {};
  	            config.headers = {};
  	            config.headers['Content-Type'] = "text/uri-list";
  	            return $http.put(_config.url + "/api/pageableQueries/" + id + "/objectQuery", objectQuery, config)
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
  	        config.params["page"] = page;
  	        config.params["size"] = size;
  	        config.params["sort"] = sort;
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


  	/*---------------------------------------------------------------------------*/

  	/*---------------------------------------------------------------------------*/
  		if (this.getPersons) {
  			window.console && console.log("getPersons");
  		}
  	    this.getPersons = function(_page, _size, _sort, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["page"] = page;
  	        config.params["size"] = size;
  	        config.params["sort"] = sort;
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

  	    if (this.findByEmail) {
  	    	window.console && console.log("findByEmail");
  	    }
  	    this.findByEmail = function(email, projection) {
  	        var config = {};
  	        config.params = {
  	            email: email,

  	        }
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/persons/search/findByEmail",  config)
  	    };

  	    if (this.findAllByNetwork) {
  	    	window.console && console.log("findAllByNetwork");
  	    }
  	    this.findAllByNetwork = function(networkId, page, size, sort, projection) {
  	        var config = {};
  	        config.params = {
  	            networkId: networkId,
  	            page: page,
  	            size: size,
  	            sort: sort,

  	        }
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/persons/search/findAllByNetwork",  config)
  	    };

  	    if (this.isAdmin) {
  	    	window.console && console.log("isAdmin");
  	    }
  	    this.isAdmin = function(personId, projection) {
  	        var config = {};
  	        config.params = {
  	            personId: personId,

  	        }
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/persons/search/isAdmin",  config)
  	    };

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

  	    if (this.findAllByNetworkAndQuery) {
  	    	window.console && console.log("findAllByNetworkAndQuery");
  	    }
  	    this.findAllByNetworkAndQuery = function(networkId, query, page, size, sort, projection) {
  	        var config = {};
  	        config.params = {
  	            networkId: networkId,
  	            query: query,
  	            page: page,
  	            size: size,
  	            sort: sort,

  	        }
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/persons/search/findAllByNetworkAndQuery",  config)
  	    };

  	    if (this.findAllByNetworkExcludingPerson) {
  	    	window.console && console.log("findAllByNetworkExcludingPerson");
  	    }
  	    this.findAllByNetworkExcludingPerson = function(networkId, personId, page, size, sort, projection) {
  	        var config = {};
  	        config.params = {
  	            networkId: networkId,
  	            personId: personId,
  	            page: page,
  	            size: size,
  	            sort: sort,

  	        }
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/persons/search/findAllByNetworkExcludingPerson",  config)
  	    };

  	    if (this.findByUsernameAndNetworkId) {
  	    	window.console && console.log("findByUsernameAndNetworkId");
  	    }
  	    this.findByUsernameAndNetworkId = function(username, networkId, projection) {
  	        var config = {};
  	        config.params = {
  	            username: username,
  	            networkId: networkId,

  	        }
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/persons/search/findByUsernameAndNetworkId",  config)
  	    };

  	    if (this.findAllByNetworkAndQueryExcludingPerson) {
  	    	window.console && console.log("findAllByNetworkAndQueryExcludingPerson");
  	    }
  	    this.findAllByNetworkAndQueryExcludingPerson = function(networkId, personId, query, page, size, sort, projection) {
  	        var config = {};
  	        config.params = {
  	            networkId: networkId,
  	            personId: personId,
  	            query: query,
  	            page: page,
  	            size: size,
  	            sort: sort,

  	        }
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/persons/search/findAllByNetworkAndQueryExcludingPerson",  config)
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

  	/*---------------------------------------------------------------------------*/

  	/*---------------------------------------------------------------------------*/
  		if (this.getPersonNetworkRegIds) {
  			window.console && console.log("getPersonNetworkRegIds");
  		}
  	    this.getPersonNetworkRegIds = function(_page, _size, _sort, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["page"] = page;
  	        config.params["size"] = size;
  	        config.params["sort"] = sort;
  	        config.params["projection"] = projection;
  			return $http.get(_config.url + "/api/personNetworkRegIds",  config)
  	    }

  	    if (this.postPersonNetworkRegId) {
  	        window.console && console.log("postPersonNetworkRegId");
  	    }
  	    this.postPersonNetworkRegId = function(personNetworkRegId) {
  	        var config = {};
  	        config.headers = {};
  	        config.headers["Content-Type"] = "application/json"
  	        return $http.post(_config.url + "/api/personNetworkRegIds", personNetworkRegId, config)
  	    }

  	    if (this.getPersonNetworkRegId) {
  	        window.console && console.log("getPersonNetworkRegId");
  	    }
  	    this.getPersonNetworkRegId = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/personNetworkRegIds/" + id,  config)
  	    }

  	    if (this.putPersonNetworkRegId) {
  	        console.log("putPersonNetworkRegId");
  	    }
  	    this.putPersonNetworkRegId = function(personNetworkRegId) {
  	        var config = {};
  	        config.headers = {};
  	        config.headers["Content-Type"] = "application/json"
  	        return $http.put(_config.url + "/api/personNetworkRegIds/" + personNetworkRegId.id, personNetworkRegId, config)
  	    }

  	    if (this.deletePersonNetworkRegId) {
  	        console.log("deletePersonNetworkRegId");
  	    }
  	    this.deletePersonNetworkRegId = function(id) {
  	        return $http.delete("/api/personNetworkRegIds/" + id);
  	    }



  	    if (this.getPersonNetworkRegIdPerson) {
  	    	console.log("getPersonNetworkRegIdPerson");
  	    }
  	    this.getPersonNetworkRegIdPerson = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/personNetworkRegIds/" + id + "/person",  config)
  	    };

  	    if (this.putPersonNetworkRegIdPerson) {
  	    	window.console && console.log("putPersonNetworkRegIdPerson");
  	    }
  	    this.putPersonNetworkRegIdPerson = function(id, person) {
  	    	if (person === null) {
  	            return $http.delete(_config.url + "/api/personNetworkRegIds/" + id + "/person")
  	    	} else {
  	            var _data = "";
  	            for (var i = 0; i < person.length; ++i) {
  	                _data += person[i] + "\n";
  	            }
  	            var config = {};
  	            config.headers = {};
  	            config.headers['Content-Type'] = "text/uri-list";
  	            return $http.put(_config.url + "/api/personNetworkRegIds/" + id + "/person", person, config)
  	    	}
  	    };



  	    if (this.getPersonNetworkRegIdNetwork) {
  	    	console.log("getPersonNetworkRegIdNetwork");
  	    }
  	    this.getPersonNetworkRegIdNetwork = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/personNetworkRegIds/" + id + "/network",  config)
  	    };

  	    if (this.putPersonNetworkRegIdNetwork) {
  	    	window.console && console.log("putPersonNetworkRegIdNetwork");
  	    }
  	    this.putPersonNetworkRegIdNetwork = function(id, network) {
  	    	if (network === null) {
  	            return $http.delete(_config.url + "/api/personNetworkRegIds/" + id + "/network")
  	    	} else {
  	            var _data = "";
  	            for (var i = 0; i < network.length; ++i) {
  	                _data += network[i] + "\n";
  	            }
  	            var config = {};
  	            config.headers = {};
  	            config.headers['Content-Type'] = "text/uri-list";
  	            return $http.put(_config.url + "/api/personNetworkRegIds/" + id + "/network", network, config)
  	    	}
  	    };

  	/*---------------------------------------------------------------------------*/

  	/*---------------------------------------------------------------------------*/
  		if (this.getPersonNetworkTokens) {
  			window.console && console.log("getPersonNetworkTokens");
  		}
  	    this.getPersonNetworkTokens = function(_page, _size, _sort, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["page"] = page;
  	        config.params["size"] = size;
  	        config.params["sort"] = sort;
  	        config.params["projection"] = projection;
  			return $http.get(_config.url + "/api/personNetworkTokens",  config)
  	    }

  	    if (this.postPersonNetworkToken) {
  	        window.console && console.log("postPersonNetworkToken");
  	    }
  	    this.postPersonNetworkToken = function(personNetworkToken) {
  	        var config = {};
  	        config.headers = {};
  	        config.headers["Content-Type"] = "application/json"
  	        return $http.post(_config.url + "/api/personNetworkTokens", personNetworkToken, config)
  	    }

  	    if (this.getPersonNetworkToken) {
  	        window.console && console.log("getPersonNetworkToken");
  	    }
  	    this.getPersonNetworkToken = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/personNetworkTokens/" + id,  config)
  	    }

  	    if (this.putPersonNetworkToken) {
  	        console.log("putPersonNetworkToken");
  	    }
  	    this.putPersonNetworkToken = function(personNetworkToken) {
  	        var config = {};
  	        config.headers = {};
  	        config.headers["Content-Type"] = "application/json"
  	        return $http.put(_config.url + "/api/personNetworkTokens/" + personNetworkToken.id, personNetworkToken, config)
  	    }

  	    if (this.deletePersonNetworkToken) {
  	        console.log("deletePersonNetworkToken");
  	    }
  	    this.deletePersonNetworkToken = function(id) {
  	        return $http.delete("/api/personNetworkTokens/" + id);
  	    }



  	    if (this.getPersonNetworkTokenPerson) {
  	    	console.log("getPersonNetworkTokenPerson");
  	    }
  	    this.getPersonNetworkTokenPerson = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/personNetworkTokens/" + id + "/person",  config)
  	    };

  	    if (this.putPersonNetworkTokenPerson) {
  	    	window.console && console.log("putPersonNetworkTokenPerson");
  	    }
  	    this.putPersonNetworkTokenPerson = function(id, person) {
  	    	if (person === null) {
  	            return $http.delete(_config.url + "/api/personNetworkTokens/" + id + "/person")
  	    	} else {
  	            var _data = "";
  	            for (var i = 0; i < person.length; ++i) {
  	                _data += person[i] + "\n";
  	            }
  	            var config = {};
  	            config.headers = {};
  	            config.headers['Content-Type'] = "text/uri-list";
  	            return $http.put(_config.url + "/api/personNetworkTokens/" + id + "/person", person, config)
  	    	}
  	    };



  	    if (this.getPersonNetworkTokenNetwork) {
  	    	console.log("getPersonNetworkTokenNetwork");
  	    }
  	    this.getPersonNetworkTokenNetwork = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/personNetworkTokens/" + id + "/network",  config)
  	    };

  	    if (this.putPersonNetworkTokenNetwork) {
  	    	window.console && console.log("putPersonNetworkTokenNetwork");
  	    }
  	    this.putPersonNetworkTokenNetwork = function(id, network) {
  	    	if (network === null) {
  	            return $http.delete(_config.url + "/api/personNetworkTokens/" + id + "/network")
  	    	} else {
  	            var _data = "";
  	            for (var i = 0; i < network.length; ++i) {
  	                _data += network[i] + "\n";
  	            }
  	            var config = {};
  	            config.headers = {};
  	            config.headers['Content-Type'] = "text/uri-list";
  	            return $http.put(_config.url + "/api/personNetworkTokens/" + id + "/network", network, config)
  	    	}
  	    };

  	/*---------------------------------------------------------------------------*/

  	/*---------------------------------------------------------------------------*/
  		if (this.getPictures) {
  			window.console && console.log("getPictures");
  		}
  	    this.getPictures = function(_page, _size, _sort, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["page"] = page;
  	        config.params["size"] = size;
  	        config.params["sort"] = sort;
  	        config.params["projection"] = projection;
  			return $http.get(_config.url + "/api/pictures",  config)
  	    }

  	    if (this.postPicture) {
  	        window.console && console.log("postPicture");
  	    }
  	    this.postPicture = function(picture) {
  	        var config = {};
  	        config.headers = {};
  	        config.headers["Content-Type"] = "application/json"
  	        return $http.post(_config.url + "/api/pictures", picture, config)
  	    }

  	    if (this.getPicture) {
  	        window.console && console.log("getPicture");
  	    }
  	    this.getPicture = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/pictures/" + id,  config)
  	    }

  	    if (this.putPicture) {
  	        console.log("putPicture");
  	    }
  	    this.putPicture = function(picture) {
  	        var config = {};
  	        config.headers = {};
  	        config.headers["Content-Type"] = "application/json"
  	        return $http.put(_config.url + "/api/pictures/" + picture.id, picture, config)
  	    }

  	    if (this.deletePicture) {
  	        console.log("deletePicture");
  	    }
  	    this.deletePicture = function(id) {
  	        return $http.delete("/api/pictures/" + id);
  	    }



  	    if (this.getPictureFile) {
  	    	console.log("getPictureFile");
  	    }
  	    this.getPictureFile = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/pictures/" + id + "/file",  config)
  	    };

  	    if (this.putPictureFile) {
  	    	window.console && console.log("putPictureFile");
  	    }
  	    this.putPictureFile = function(id, file) {
  	    	if (file === null) {
  	            return $http.delete(_config.url + "/api/pictures/" + id + "/file")
  	    	} else {
  	            var _data = "";
  	            for (var i = 0; i < file.length; ++i) {
  	                _data += file[i] + "\n";
  	            }
  	            var config = {};
  	            config.headers = {};
  	            config.headers['Content-Type'] = "text/uri-list";
  	            return $http.put(_config.url + "/api/pictures/" + id + "/file", file, config)
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
  	        config.params["page"] = page;
  	        config.params["size"] = size;
  	        config.params["sort"] = sort;
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


  	    if (this.getPostImages) {
  	    	console.log("getPostImages");
  	    }
  	    this.getPostImages = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/posts/" + id + "/images",  config)
  	    };
  	    if (this.patchPostImages) {
  	    	window.console && console.log("patchPostImages");
  	    }
  	    this.patchPostImages = function(id, images) {		
  	    	var _data = "";
  	    	for (var i = 0; i < images.length; ++i) {
  	    		_data += images[i] + "\n";
  	    	}
  	        var config = {};
  	        config.headers = {};
  	        config.headers['Content-Type'] = "text/uri-list";
  	        return $http.get(_config.url + "/api/posts/" + id + "/images", _data, config)
  	    };

  	    if (this.putPostImages) {
  	    	window.console && console.log("putPostImages");
  	    }
  	    this.putPostImages = function(id, images) {		
  	    	var _data = "";
  	        for (var i = 0; i < images.length; ++i) {
  	            _data += images[i] + "\n";
  	        }
  	        var config = {};
  	        config.headers = {};
  	        config.headers['Content-Type'] = "text/uri-list";
  	        return $http.put(_config.url + "/api/posts/" + id + "/images", _data, config)
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

  	/*---------------------------------------------------------------------------*/

  	/*---------------------------------------------------------------------------*/
  		if (this.getPostReads) {
  			window.console && console.log("getPostReads");
  		}
  	    this.getPostReads = function(_page, _size, _sort, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["page"] = page;
  	        config.params["size"] = size;
  	        config.params["sort"] = sort;
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
  	        config.params["page"] = page;
  	        config.params["size"] = size;
  	        config.params["sort"] = sort;
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

  	/*---------------------------------------------------------------------------*/

  	/*---------------------------------------------------------------------------*/
  		if (this.getRows) {
  			window.console && console.log("getRows");
  		}
  	    this.getRows = function(_page, _size, _sort, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["page"] = page;
  	        config.params["size"] = size;
  	        config.params["sort"] = sort;
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

  	/*---------------------------------------------------------------------------*/

  	/*---------------------------------------------------------------------------*/
  		if (this.getSections) {
  			window.console && console.log("getSections");
  		}
  	    this.getSections = function(_page, _size, _sort, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["page"] = page;
  	        config.params["size"] = size;
  	        config.params["sort"] = sort;
  	        config.params["projection"] = projection;
  			return $http.get(_config.url + "/api/sections",  config)
  	    }

  	    if (this.postSection) {
  	        window.console && console.log("postSection");
  	    }
  	    this.postSection = function(section) {
  	        var config = {};
  	        config.headers = {};
  	        config.headers["Content-Type"] = "application/json"
  	        return $http.post(_config.url + "/api/sections", section, config)
  	    }

  	    if (this.getSection) {
  	        window.console && console.log("getSection");
  	    }
  	    this.getSection = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/sections/" + id,  config)
  	    }

  	    if (this.putSection) {
  	        console.log("putSection");
  	    }
  	    this.putSection = function(section) {
  	        var config = {};
  	        config.headers = {};
  	        config.headers["Content-Type"] = "application/json"
  	        return $http.put(_config.url + "/api/sections/" + section.id, section, config)
  	    }

  	    if (this.deleteSection) {
  	        console.log("deleteSection");
  	    }
  	    this.deleteSection = function(id) {
  	        return $http.delete("/api/sections/" + id);
  	    }



  	    if (this.getSectionNetwork) {
  	    	console.log("getSectionNetwork");
  	    }
  	    this.getSectionNetwork = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/sections/" + id + "/network",  config)
  	    };

  	    if (this.putSectionNetwork) {
  	    	window.console && console.log("putSectionNetwork");
  	    }
  	    this.putSectionNetwork = function(id, network) {
  	    	if (network === null) {
  	            return $http.delete(_config.url + "/api/sections/" + id + "/network")
  	    	} else {
  	            var _data = "";
  	            for (var i = 0; i < network.length; ++i) {
  	                _data += network[i] + "\n";
  	            }
  	            var config = {};
  	            config.headers = {};
  	            config.headers['Content-Type'] = "text/uri-list";
  	            return $http.put(_config.url + "/api/sections/" + id + "/network", network, config)
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
  	        config.params["page"] = page;
  	        config.params["size"] = size;
  	        config.params["sort"] = sort;
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

  	/*---------------------------------------------------------------------------*/

  	/*---------------------------------------------------------------------------*/
  		if (this.getStations) {
  			window.console && console.log("getStations");
  		}
  	    this.getStations = function(_page, _size, _sort, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["page"] = page;
  	        config.params["size"] = size;
  	        config.params["sort"] = sort;
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

  	    if (this.findByName) {
  	    	window.console && console.log("findByName");
  	    }
  	    this.findByName = function(name, projection) {
  	        var config = {};
  	        config.params = {
  	            name: name,

  	        }
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/stations/search/findByName",  config)
  	    };

  	    if (this.findByPersonIdAndNetworkId) {
  	    	window.console && console.log("findByPersonIdAndNetworkId");
  	    }
  	    this.findByPersonIdAndNetworkId = function(personId, networkId, projection) {
  	        var config = {};
  	        config.params = {
  	            personId: personId,
  	            networkId: networkId,

  	        }
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/stations/search/findByPersonIdAndNetworkId",  config)
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


  	    if (this.getStationPages) {
  	    	console.log("getStationPages");
  	    }
  	    this.getStationPages = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/stations/" + id + "/pages",  config)
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


  	    if (this.getStationOwnedTaxonomies) {
  	    	console.log("getStationOwnedTaxonomies");
  	    }
  	    this.getStationOwnedTaxonomies = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/stations/" + id + "/ownedTaxonomies",  config)
  	    };



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

  	/*---------------------------------------------------------------------------*/

  	/*---------------------------------------------------------------------------*/
  		if (this.getStationPerspectives) {
  			window.console && console.log("getStationPerspectives");
  		}
  	    this.getStationPerspectives = function(_page, _size, _sort, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["page"] = page;
  	        config.params["size"] = size;
  	        config.params["sort"] = sort;
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


  	    if (this.getStationPerspectivePerspectives) {
  	    	console.log("getStationPerspectivePerspectives");
  	    }
  	    this.getStationPerspectivePerspectives = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/stationPerspectives/" + id + "/perspectives",  config)
  	    };

  	/*---------------------------------------------------------------------------*/

  	/*---------------------------------------------------------------------------*/
  		if (this.getStationRoles) {
  			window.console && console.log("getStationRoles");
  		}
  	    this.getStationRoles = function(_page, _size, _sort, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["page"] = page;
  	        config.params["size"] = size;
  	        config.params["sort"] = sort;
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

  	/*---------------------------------------------------------------------------*/

  	/*---------------------------------------------------------------------------*/
  		if (this.getTaxonomies) {
  			window.console && console.log("getTaxonomies");
  		}
  	    this.getTaxonomies = function(_page, _size, _sort, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["page"] = page;
  	        config.params["size"] = size;
  	        config.params["sort"] = sort;
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

  	    if (this.getTaxonomyNetworks) {
  	    	console.log("getTaxonomyNetworks");
  	    }
  	    this.getTaxonomyNetworks = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/taxonomies/" + id + "/networks",  config)
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

  	/*---------------------------------------------------------------------------*/

  	/*---------------------------------------------------------------------------*/
  		if (this.getTerms) {
  			window.console && console.log("getTerms");
  		}
  	    this.getTerms = function(_page, _size, _sort, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["page"] = page;
  	        config.params["size"] = size;
  	        config.params["sort"] = sort;
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

  	    if (this.getTermCells) {
  	    	console.log("getTermCells");
  	    }
  	    this.getTermCells = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/terms/" + id + "/cells",  config)
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


  	    if (this.getTermChildren) {
  	    	console.log("getTermChildren");
  	    }
  	    this.getTermChildren = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/terms/" + id + "/children",  config)
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
  	        config.params["page"] = page;
  	        config.params["size"] = size;
  	        config.params["sort"] = sort;
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


  	    if (this.getTermPerspectiveRows) {
  	    	console.log("getTermPerspectiveRows");
  	    }
  	    this.getTermPerspectiveRows = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/termPerspectives/" + id + "/rows",  config)
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
  	        config.params["page"] = page;
  	        config.params["size"] = size;
  	        config.params["sort"] = sort;
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

  	    if (this.getUserAuthorities) {
  	    	console.log("getUserAuthorities");
  	    }
  	    this.getUserAuthorities = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/users/" + id + "/authorities",  config)
  	    };


  	    if (this.getUserUserConnections) {
  	    	console.log("getUserUserConnections");
  	    }
  	    this.getUserUserConnections = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/users/" + id + "/userConnections",  config)
  	    };

  	/*---------------------------------------------------------------------------*/

  	/*---------------------------------------------------------------------------*/
  		if (this.getUserConnections) {
  			window.console && console.log("getUserConnections");
  		}
  	    this.getUserConnections = function(_page, _size, _sort, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["page"] = page;
  	        config.params["size"] = size;
  	        config.params["sort"] = sort;
  	        config.params["projection"] = projection;
  			return $http.get(_config.url + "/api/userConnections",  config)
  	    }

  	    if (this.postUserConnection) {
  	        window.console && console.log("postUserConnection");
  	    }
  	    this.postUserConnection = function(userConnection) {
  	        var config = {};
  	        config.headers = {};
  	        config.headers["Content-Type"] = "application/json"
  	        return $http.post(_config.url + "/api/userConnections", userConnection, config)
  	    }

  	    if (this.getUserConnection) {
  	        window.console && console.log("getUserConnection");
  	    }
  	    this.getUserConnection = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/userConnections/" + id,  config)
  	    }

  	    if (this.putUserConnection) {
  	        console.log("putUserConnection");
  	    }
  	    this.putUserConnection = function(userConnection) {
  	        var config = {};
  	        config.headers = {};
  	        config.headers["Content-Type"] = "application/json"
  	        return $http.put(_config.url + "/api/userConnections/" + userConnection.id, userConnection, config)
  	    }

  	    if (this.deleteUserConnection) {
  	        console.log("deleteUserConnection");
  	    }
  	    this.deleteUserConnection = function(id) {
  	        return $http.delete("/api/userConnections/" + id);
  	    }



  	    if (this.getUserConnectionUser) {
  	    	console.log("getUserConnectionUser");
  	    }
  	    this.getUserConnectionUser = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/userConnections/" + id + "/user",  config)
  	    };

  	    if (this.putUserConnectionUser) {
  	    	window.console && console.log("putUserConnectionUser");
  	    }
  	    this.putUserConnectionUser = function(id, user) {
  	    	if (user === null) {
  	            return $http.delete(_config.url + "/api/userConnections/" + id + "/user")
  	    	} else {
  	            var _data = "";
  	            for (var i = 0; i < user.length; ++i) {
  	                _data += user[i] + "\n";
  	            }
  	            var config = {};
  	            config.headers = {};
  	            config.headers['Content-Type'] = "text/uri-list";
  	            return $http.put(_config.url + "/api/userConnections/" + id + "/user", user, config)
  	    	}
  	    };

  	/*---------------------------------------------------------------------------*/

  	/*---------------------------------------------------------------------------*/
  		if (this.getUserGrantedAuthorities) {
  			window.console && console.log("getUserGrantedAuthorities");
  		}
  	    this.getUserGrantedAuthorities = function(_page, _size, _sort, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["page"] = page;
  	        config.params["size"] = size;
  	        config.params["sort"] = sort;
  	        config.params["projection"] = projection;
  			return $http.get(_config.url + "/api/userGrantedAuthorities",  config)
  	    }

  	    if (this.postUserGrantedAuthority) {
  	        window.console && console.log("postUserGrantedAuthority");
  	    }
  	    this.postUserGrantedAuthority = function(userGrantedAuthority) {
  	        var config = {};
  	        config.headers = {};
  	        config.headers["Content-Type"] = "application/json"
  	        return $http.post(_config.url + "/api/userGrantedAuthorities", userGrantedAuthority, config)
  	    }

  	    if (this.getUserGrantedAuthority) {
  	        window.console && console.log("getUserGrantedAuthority");
  	    }
  	    this.getUserGrantedAuthority = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/userGrantedAuthorities/" + id,  config)
  	    }

  	    if (this.putUserGrantedAuthority) {
  	        console.log("putUserGrantedAuthority");
  	    }
  	    this.putUserGrantedAuthority = function(userGrantedAuthority) {
  	        var config = {};
  	        config.headers = {};
  	        config.headers["Content-Type"] = "application/json"
  	        return $http.put(_config.url + "/api/userGrantedAuthorities/" + userGrantedAuthority.id, userGrantedAuthority, config)
  	    }

  	    if (this.deleteUserGrantedAuthority) {
  	        console.log("deleteUserGrantedAuthority");
  	    }
  	    this.deleteUserGrantedAuthority = function(id) {
  	        return $http.delete("/api/userGrantedAuthorities/" + id);
  	    }



  	    if (this.getUserGrantedAuthorityUser) {
  	    	console.log("getUserGrantedAuthorityUser");
  	    }
  	    this.getUserGrantedAuthorityUser = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/userGrantedAuthorities/" + id + "/user",  config)
  	    };

  	    if (this.putUserGrantedAuthorityUser) {
  	    	window.console && console.log("putUserGrantedAuthorityUser");
  	    }
  	    this.putUserGrantedAuthorityUser = function(id, user) {
  	    	if (user === null) {
  	            return $http.delete(_config.url + "/api/userGrantedAuthorities/" + id + "/user")
  	    	} else {
  	            var _data = "";
  	            for (var i = 0; i < user.length; ++i) {
  	                _data += user[i] + "\n";
  	            }
  	            var config = {};
  	            config.headers = {};
  	            config.headers['Content-Type'] = "text/uri-list";
  	            return $http.put(_config.url + "/api/userGrantedAuthorities/" + id + "/user", user, config)
  	    	}
  	    };



  	    if (this.getUserGrantedAuthorityStation) {
  	    	console.log("getUserGrantedAuthorityStation");
  	    }
  	    this.getUserGrantedAuthorityStation = function(id, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["projection"] = projection;
  	        return $http.get(_config.url + "/api/userGrantedAuthorities/" + id + "/station",  config)
  	    };

  	    if (this.putUserGrantedAuthorityStation) {
  	    	window.console && console.log("putUserGrantedAuthorityStation");
  	    }
  	    this.putUserGrantedAuthorityStation = function(id, station) {
  	    	if (station === null) {
  	            return $http.delete(_config.url + "/api/userGrantedAuthorities/" + id + "/station")
  	    	} else {
  	            var _data = "";
  	            for (var i = 0; i < station.length; ++i) {
  	                _data += station[i] + "\n";
  	            }
  	            var config = {};
  	            config.headers = {};
  	            config.headers['Content-Type'] = "text/uri-list";
  	            return $http.put(_config.url + "/api/userGrantedAuthorities/" + id + "/station", station, config)
  	    	}
  	    };

  	/*---------------------------------------------------------------------------*/

  	/*---------------------------------------------------------------------------*/
  		if (this.getVideos) {
  			window.console && console.log("getVideos");
  		}
  	    this.getVideos = function(_page, _size, _sort, projection) {
  	        var config = {};
  	        config.params = {};
  	        config.params["page"] = page;
  	        config.params["size"] = size;
  	        config.params["sort"] = sort;
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
