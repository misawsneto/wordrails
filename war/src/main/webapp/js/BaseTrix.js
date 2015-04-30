function CellDto(id, index, row, post) {
	return {
		id: id,
		index: index,
		row: row,
		post: post
	};
}

function CommentDto(id, date, lastModificationDate, title, body, author, post) {
	return {
		id: id,
		date: date,
		lastModificationDate: lastModificationDate,
		title: title,
		body: body,
		author: author,
		post: post
	};
}

function FavoriteDto(id, post, person, createdAt, updatedAt) {
	return {
		id: id,
		post: post,
		person: person,
		createdAt: createdAt,
		updatedAt: updatedAt
	};
}

function FileDto(id, type, mime, name, url) {
	return {
		id: id,
		type: type,
		mime: mime,
		name: name,
		url: url
	};
}

function ImageDto(id, title, caption, credits, original, small, medium, large) {
	return {
		id: id,
		title: title,
		caption: caption,
		credits: credits,
		original: original,
		small: small,
		medium: medium,
		large: large
	};
}

function NetworkDto(id, NIGHT_READ_MODE, DAY_READ_MODE, VERTICAL_ORIENTATION_MODE, HORIZONTAL_ORIENTATION_MODE, name, trackingId, defaultTaxonomy, allowSignup, allowComments, domain, backgroundColor, navbarColor, navbarSecondaryColor, mainColor, primaryFont, secondaryFont, titleFontSize, newsFontSize, subdomain, configured, logoId, defaultReadMode, defaultOrientationMode, createdAt, updatedAt) {
	return {
		id: id,
		NIGHT_READ_MODE: NIGHT_READ_MODE,
		DAY_READ_MODE: DAY_READ_MODE,
		VERTICAL_ORIENTATION_MODE: VERTICAL_ORIENTATION_MODE,
		HORIZONTAL_ORIENTATION_MODE: HORIZONTAL_ORIENTATION_MODE,
		name: name,
		trackingId: trackingId,
		defaultTaxonomy: defaultTaxonomy,
		allowSignup: allowSignup,
		allowComments: allowComments,
		domain: domain,
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
		defaultReadMode: defaultReadMode,
		defaultOrientationMode: defaultOrientationMode,
		createdAt: createdAt,
		updatedAt: updatedAt
	};
}

function NetworkRoleDto(id, network, person, admin) {
	return {
		id: id,
		network: network,
		person: person,
		admin: admin
	};
}

function NotificationDto(id, person, network, seen, message, type, createdAt, updatedAt) {
	return {
		id: id,
		person: person,
		network: network,
		seen: seen,
		message: message,
		type: type,
		createdAt: createdAt,
		updatedAt: updatedAt
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

function PersonDto(id, name, username, bio, email, createdAt, updatedAt, imageId, imageSmallId, imageMediumId, imageLargeId, passwordReseted, twitterHandle) {
	return {
		id: id,
		name: name,
		username: username,
		bio: bio,
		email: email,
		createdAt: createdAt,
		updatedAt: updatedAt,
		imageId: imageId,
		imageSmallId: imageSmallId,
		imageMediumId: imageMediumId,
		imageLargeId: imageLargeId,
		passwordReseted: passwordReseted,
		twitterHandle: twitterHandle
	};
}

function PostDto(id, wordpressId, date, lastModificationDate, title, body, topper, state, originalSlug, slug, author, station, readsCount, favoritesCount, imageLandscape, updatedAt, imageId, imageSmallId, imageMediumId, imageLargeId) {
	return {
		id: id,
		wordpressId: wordpressId,
		date: date,
		lastModificationDate: lastModificationDate,
		title: title,
		body: body,
		topper: topper,
		state: state,
		originalSlug: originalSlug,
		slug: slug,
		author: author,
		station: station,
		readsCount: readsCount,
		favoritesCount: favoritesCount,
		imageLandscape: imageLandscape,
		updatedAt: updatedAt,
		imageId: imageId,
		imageSmallId: imageSmallId,
		imageMediumId: imageMediumId,
		imageLargeId: imageLargeId
	};
}

function PostReadDto(id, person, createdAt, updatedAt) {
	return {
		id: id,
		person: person,
		createdAt: createdAt,
		updatedAt: updatedAt
	};
}

function PromotionDto(id, date, post, promoter, station, comment) {
	return {
		id: id,
		date: date,
		post: post,
		promoter: promoter,
		station: station,
		comment: comment
	};
}

function RowDto(id, type, index) {
	return {
		id: id,
		type: type,
		index: index
	};
}

function SponsorDto(id, network, name, keywords) {
	return {
		id: id,
		network: network,
		name: name,
		keywords: keywords
	};
}

function StationDto(id, name, writable, main, visibility, networks, stationPerspectives, postsTitleSize, topper, sponsored, social, logoId, defaultPerspectiveId, createdAt, updatedAt) {
	return {
		id: id,
		name: name,
		writable: writable,
		main: main,
		visibility: visibility,
		networks: networks,
		stationPerspectives: stationPerspectives,
		postsTitleSize: postsTitleSize,
		topper: topper,
		sponsored: sponsored,
		social: social,
		logoId: logoId,
		defaultPerspectiveId: defaultPerspectiveId,
		createdAt: createdAt,
		updatedAt: updatedAt
	};
}

function StationPerspectiveDto(id, name, station, stationId) {
	return {
		id: id,
		name: name,
		station: station,
		stationId: stationId
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

function TaxonomyDto(id, type, name) {
	return {
		id: id,
		type: type,
		name: name
	};
}

function TermDto(id, name, taxonomy, taxonomyId, taxonomyName) {
	return {
		id: id,
		name: name,
		taxonomy: taxonomy,
		taxonomyId: taxonomyId,
		taxonomyName: taxonomyName
	};
}

function TermPerspectiveDto(id, perspective, stationId) {
	return {
		id: id,
		perspective: perspective,
		stationId: stationId
	};
}

function UserDto(username, password, enabled, authorities) {
	return {
		username: username,
		password: password,
		enabled: enabled,
		authorities: authorities
	};
}

function WordpressDto(id, domain, username, password, token) {
	return {
		id: id,
		domain: domain,
		username: username,
		password: password,
		token: token
	};
}
var trix = angular.module('trix', [])

.config(function($httpProvider) { 
	$httpProvider.defaults.useXDomain = true;
	$httpProvider.defaults.withCredentials = true;
  delete $httpProvider.defaults.headers.common['X-Requested-With'];
})

.service('trix', function PulseClient($http) {

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
			return $http.get(_url + "/api/cells",  config)
	    }

	    if (this.postCell) {
	        window.console && console.log("postCell");
	    }
	    this.postCell = function(cell) {
	        var config = {};
	        config.headers = {};
	        config.headers["Content-Type"] = "application/json"
	        return $http.post(_url + "/api/cells/", cell, config)
	    }

	    if (this.getCell) {
	        window.console && console.log("getCell");
	    }
	    this.getCell = function(id, projection) {
	        var config = {};
	        config.params = {};
	        config.params["projection"] = projection;
	        return $http.get(_url + "/api/cells/" + id,  config)
	    }

	    if (this.putCell) {
	        console.log("putCell");
	    }
	    this.putCell = function(cell) {
	        var config = {};
	        config.headers = {};
	        config.headers["Content-Type"] = "application/json"
	        return $http.put(_url + "/api/cells/" + cell.id, cell, config)
	    }

	    if (this.deleteCell) {
	        console.log("deleteCell");
	    }
	    this.deleteCell = function(id) {
	        return $http.delete("/api/cells/" + id);
	    }


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
			return $http.get(_url + "/api/comments",  config)
	    }

	    if (this.postComment) {
	        window.console && console.log("postComment");
	    }
	    this.postComment = function(comment) {
	        var config = {};
	        config.headers = {};
	        config.headers["Content-Type"] = "application/json"
	        return $http.post(_url + "/api/comments/", comment, config)
	    }

	    if (this.getComment) {
	        window.console && console.log("getComment");
	    }
	    this.getComment = function(id, projection) {
	        var config = {};
	        config.params = {};
	        config.params["projection"] = projection;
	        return $http.get(_url + "/api/comments/" + id,  config)
	    }

	    if (this.putComment) {
	        console.log("putComment");
	    }
	    this.putComment = function(comment) {
	        var config = {};
	        config.headers = {};
	        config.headers["Content-Type"] = "application/json"
	        return $http.put(_url + "/api/comments/" + comment.id, comment, config)
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
	        return $http.get(_url + "/api/comments/search/findPostCommentsOrderByDate",  config)
	    };

	/*---------------------------------------------------------------------------*/

	/*---------------------------------------------------------------------------*/
		if (this.getFavorites) {
			window.console && console.log("getFavorites");
		}
	    this.getFavorites = function(_page, _size, _sort, projection) {
	        var config = {};
	        config.params = {};
	        config.params["page"] = page;
	        config.params["size"] = size;
	        config.params["sort"] = sort;
	        config.params["projection"] = projection;
			return $http.get(_url + "/api/favorites",  config)
	    }

	    if (this.postFavorite) {
	        window.console && console.log("postFavorite");
	    }
	    this.postFavorite = function(favorite) {
	        var config = {};
	        config.headers = {};
	        config.headers["Content-Type"] = "application/json"
	        return $http.post(_url + "/api/favorites/", favorite, config)
	    }

	    if (this.getFavorite) {
	        window.console && console.log("getFavorite");
	    }
	    this.getFavorite = function(id, projection) {
	        var config = {};
	        config.params = {};
	        config.params["projection"] = projection;
	        return $http.get(_url + "/api/favorites/" + id,  config)
	    }

	    if (this.putFavorite) {
	        console.log("putFavorite");
	    }
	    this.putFavorite = function(favorite) {
	        var config = {};
	        config.headers = {};
	        config.headers["Content-Type"] = "application/json"
	        return $http.put(_url + "/api/favorites/" + favorite.id, favorite, config)
	    }

	    if (this.deleteFavorite) {
	        console.log("deleteFavorite");
	    }
	    this.deleteFavorite = function(id) {
	        return $http.delete("/api/favorites/" + id);
	    }

	    if (this.findFavoritesByPersonId) {
	    	window.console && console.log("findFavoritesByPersonId");
	    }
	    this.findFavoritesByPersonId = function(personId, projection) {
	        var config = {};
	        config.params = {
	            personId: personId,

	        }
	        config.params["projection"] = projection;
	        return $http.get(_url + "/api/favorites/search/findFavoritesByPersonId",  config)
	    };

	    if (this.findFavoritesByPostId) {
	    	window.console && console.log("findFavoritesByPostId");
	    }
	    this.findFavoritesByPostId = function(postId, projection) {
	        var config = {};
	        config.params = {
	            postId: postId,

	        }
	        config.params["projection"] = projection;
	        return $http.get(_url + "/api/favorites/search/findFavoritesByPostId",  config)
	    };

	    if (this.findFavoritesByPersonIdOrderByDate) {
	    	window.console && console.log("findFavoritesByPersonIdOrderByDate");
	    }
	    this.findFavoritesByPersonIdOrderByDate = function(personId, page, size, sort, projection) {
	        var config = {};
	        config.params = {
	            personId: personId,
	            page: page,
	            size: size,
	            sort: sort,

	        }
	        config.params["projection"] = projection;
	        return $http.get(_url + "/api/favorites/search/findFavoritesByPersonIdOrderByDate",  config)
	    };

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
			return $http.get(_url + "/api/files",  config)
	    }

	    if (this.postFile) {
	        window.console && console.log("postFile");
	    }
	    this.postFile = function(file) {
	        var config = {};
	        config.headers = {};
	        config.headers["Content-Type"] = "application/json"
	        return $http.post(_url + "/api/files/", file, config)
	    }

	    if (this.getFile) {
	        window.console && console.log("getFile");
	    }
	    this.getFile = function(id, projection) {
	        var config = {};
	        config.params = {};
	        config.params["projection"] = projection;
	        return $http.get(_url + "/api/files/" + id,  config)
	    }

	    if (this.putFile) {
	        console.log("putFile");
	    }
	    this.putFile = function(file) {
	        var config = {};
	        config.headers = {};
	        config.headers["Content-Type"] = "application/json"
	        return $http.put(_url + "/api/files/" + file.id, file, config)
	    }

	    if (this.deleteFile) {
	        console.log("deleteFile");
	    }
	    this.deleteFile = function(id) {
	        return $http.delete("/api/files/" + id);
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
			return $http.get(_url + "/api/images",  config)
	    }

	    if (this.postImage) {
	        window.console && console.log("postImage");
	    }
	    this.postImage = function(image) {
	        var config = {};
	        config.headers = {};
	        config.headers["Content-Type"] = "application/json"
	        return $http.post(_url + "/api/images/", image, config)
	    }

	    if (this.getImage) {
	        window.console && console.log("getImage");
	    }
	    this.getImage = function(id, projection) {
	        var config = {};
	        config.params = {};
	        config.params["projection"] = projection;
	        return $http.get(_url + "/api/images/" + id,  config)
	    }

	    if (this.putImage) {
	        console.log("putImage");
	    }
	    this.putImage = function(image) {
	        var config = {};
	        config.headers = {};
	        config.headers["Content-Type"] = "application/json"
	        return $http.put(_url + "/api/images/" + image.id, image, config)
	    }

	    if (this.deleteImage) {
	        console.log("deleteImage");
	    }
	    this.deleteImage = function(id) {
	        return $http.delete("/api/images/" + id);
	    }


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
			return $http.get(_url + "/api/networks",  config)
	    }

	    if (this.postNetwork) {
	        window.console && console.log("postNetwork");
	    }
	    this.postNetwork = function(network) {
	        var config = {};
	        config.headers = {};
	        config.headers["Content-Type"] = "application/json"
	        return $http.post(_url + "/api/networks/", network, config)
	    }

	    if (this.getNetwork) {
	        window.console && console.log("getNetwork");
	    }
	    this.getNetwork = function(id, projection) {
	        var config = {};
	        config.params = {};
	        config.params["projection"] = projection;
	        return $http.get(_url + "/api/networks/" + id,  config)
	    }

	    if (this.putNetwork) {
	        console.log("putNetwork");
	    }
	    this.putNetwork = function(network) {
	        var config = {};
	        config.headers = {};
	        config.headers["Content-Type"] = "application/json"
	        return $http.put(_url + "/api/networks/" + network.id, network, config)
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
	        return $http.get(_url + "/api/networks/search/findBySubdomain",  config)
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
			return $http.get(_url + "/api/networkRoles",  config)
	    }

	    if (this.postNetworkRole) {
	        window.console && console.log("postNetworkRole");
	    }
	    this.postNetworkRole = function(networkRole) {
	        var config = {};
	        config.headers = {};
	        config.headers["Content-Type"] = "application/json"
	        return $http.post(_url + "/api/networkRoles/", networkRole, config)
	    }

	    if (this.getNetworkRole) {
	        window.console && console.log("getNetworkRole");
	    }
	    this.getNetworkRole = function(id, projection) {
	        var config = {};
	        config.params = {};
	        config.params["projection"] = projection;
	        return $http.get(_url + "/api/networkRoles/" + id,  config)
	    }

	    if (this.putNetworkRole) {
	        console.log("putNetworkRole");
	    }
	    this.putNetworkRole = function(networkRole) {
	        var config = {};
	        config.headers = {};
	        config.headers["Content-Type"] = "application/json"
	        return $http.put(_url + "/api/networkRoles/" + networkRole.id, networkRole, config)
	    }

	    if (this.deleteNetworkRole) {
	        console.log("deleteNetworkRole");
	    }
	    this.deleteNetworkRole = function(id) {
	        return $http.delete("/api/networkRoles/" + id);
	    }


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
			return $http.get(_url + "/api/notifications",  config)
	    }

	    if (this.postNotification) {
	        window.console && console.log("postNotification");
	    }
	    this.postNotification = function(notification) {
	        var config = {};
	        config.headers = {};
	        config.headers["Content-Type"] = "application/json"
	        return $http.post(_url + "/api/notifications/", notification, config)
	    }

	    if (this.getNotification) {
	        window.console && console.log("getNotification");
	    }
	    this.getNotification = function(id, projection) {
	        var config = {};
	        config.params = {};
	        config.params["projection"] = projection;
	        return $http.get(_url + "/api/notifications/" + id,  config)
	    }

	    if (this.putNotification) {
	        console.log("putNotification");
	    }
	    this.putNotification = function(notification) {
	        var config = {};
	        config.headers = {};
	        config.headers["Content-Type"] = "application/json"
	        return $http.put(_url + "/api/notifications/" + notification.id, notification, config)
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
	        return $http.get(_url + "/api/notifications/search/findNotificationsByPersonIdOrderByDate",  config)
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
			return $http.get(_url + "/api/passwordResets",  config)
	    }

	    if (this.postPasswordReset) {
	        window.console && console.log("postPasswordReset");
	    }
	    this.postPasswordReset = function(passwordReset) {
	        var config = {};
	        config.headers = {};
	        config.headers["Content-Type"] = "application/json"
	        return $http.post(_url + "/api/passwordResets/", passwordReset, config)
	    }

	    if (this.getPasswordReset) {
	        window.console && console.log("getPasswordReset");
	    }
	    this.getPasswordReset = function(id, projection) {
	        var config = {};
	        config.params = {};
	        config.params["projection"] = projection;
	        return $http.get(_url + "/api/passwordResets/" + id,  config)
	    }

	    if (this.putPasswordReset) {
	        console.log("putPasswordReset");
	    }
	    this.putPasswordReset = function(passwordReset) {
	        var config = {};
	        config.headers = {};
	        config.headers["Content-Type"] = "application/json"
	        return $http.put(_url + "/api/passwordResets/" + passwordReset.id, passwordReset, config)
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
	        return $http.get(_url + "/api/passwordResets/search/findByHash",  config)
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
			return $http.get(_url + "/api/persons",  config)
	    }

	    if (this.postPerson) {
	        window.console && console.log("postPerson");
	    }
	    this.postPerson = function(person) {
	        var config = {};
	        config.headers = {};
	        config.headers["Content-Type"] = "application/json"
	        return $http.post(_url + "/api/persons/", person, config)
	    }

	    if (this.getPerson) {
	        window.console && console.log("getPerson");
	    }
	    this.getPerson = function(id, projection) {
	        var config = {};
	        config.params = {};
	        config.params["projection"] = projection;
	        return $http.get(_url + "/api/persons/" + id,  config)
	    }

	    if (this.putPerson) {
	        console.log("putPerson");
	    }
	    this.putPerson = function(person) {
	        var config = {};
	        config.headers = {};
	        config.headers["Content-Type"] = "application/json"
	        return $http.put(_url + "/api/persons/" + person.id, person, config)
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
	        return $http.get(_url + "/api/persons/search/findByUsername",  config)
	    };

	    if (this.findByEmail) {
	    	window.console && console.log("findByEmail");
	    }
	    this.findByEmail = function(email, projection) {
	        var config = {};
	        config.params = {
	            email: email,

	        }
	        config.params["projection"] = projection;
	        return $http.get(_url + "/api/persons/search/findByEmail",  config)
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
			return $http.get(_url + "/api/posts",  config)
	    }

	    if (this.postPost) {
	        window.console && console.log("postPost");
	    }
	    this.postPost = function(post) {
	        var config = {};
	        config.headers = {};
	        config.headers["Content-Type"] = "application/json"
	        return $http.post(_url + "/api/posts/", post, config)
	    }

	    if (this.getPost) {
	        window.console && console.log("getPost");
	    }
	    this.getPost = function(id, projection) {
	        var config = {};
	        config.params = {};
	        config.params["projection"] = projection;
	        return $http.get(_url + "/api/posts/" + id,  config)
	    }

	    if (this.putPost) {
	        console.log("putPost");
	    }
	    this.putPost = function(post) {
	        var config = {};
	        config.headers = {};
	        config.headers["Content-Type"] = "application/json"
	        return $http.put(_url + "/api/posts/" + post.id, post, config)
	    }

	    if (this.deletePost) {
	        console.log("deletePost");
	    }
	    this.deletePost = function(id) {
	        return $http.delete("/api/posts/" + id);
	    }

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
	        return $http.get(_url + "/api/posts/search/findPostsFromOrPromotedToStation",  config)
	    };

	    if (this.findPostsAndPostsPromoted) {
	    	window.console && console.log("findPostsAndPostsPromoted");
	    }
	    this.findPostsAndPostsPromoted = function(stationId, termsIds, page, size, sort, projection) {
	        var config = {};
	        config.params = {
	            stationId: stationId,
	            termsIds: termsIds,
	            page: page,
	            size: size,
	            sort: sort,

	        }
	        config.params["projection"] = projection;
	        return $http.get(_url + "/api/posts/search/findPostsAndPostsPromoted",  config)
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
	        return $http.get(_url + "/api/posts/search/findPostsNotPositioned",  config)
	    };

	    if (this.findUnreadByStationAndPerson) {
	    	window.console && console.log("findUnreadByStationAndPerson");
	    }
	    this.findUnreadByStationAndPerson = function(stationId, personId, projection) {
	        var config = {};
	        config.params = {
	            stationId: stationId,
	            personId: personId,

	        }
	        config.params["projection"] = projection;
	        return $http.get(_url + "/api/posts/search/findUnreadByStationAndPerson",  config)
	    };

	    if (this.findPosts) {
	    	window.console && console.log("findPosts");
	    }
	    this.findPosts = function(stationId, termId, page, size, sort, projection) {
	        var config = {};
	        config.params = {
	            stationId: stationId,
	            termId: termId,
	            page: page,
	            size: size,
	            sort: sort,

	        }
	        config.params["projection"] = projection;
	        return $http.get(_url + "/api/posts/search/findPosts",  config)
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
			return $http.get(_url + "/api/postReads",  config)
	    }

	    if (this.postPostRead) {
	        window.console && console.log("postPostRead");
	    }
	    this.postPostRead = function(postRead) {
	        var config = {};
	        config.headers = {};
	        config.headers["Content-Type"] = "application/json"
	        return $http.post(_url + "/api/postReads/", postRead, config)
	    }

	    if (this.getPostRead) {
	        window.console && console.log("getPostRead");
	    }
	    this.getPostRead = function(id, projection) {
	        var config = {};
	        config.params = {};
	        config.params["projection"] = projection;
	        return $http.get(_url + "/api/postReads/" + id,  config)
	    }

	    if (this.putPostRead) {
	        console.log("putPostRead");
	    }
	    this.putPostRead = function(postRead) {
	        var config = {};
	        config.headers = {};
	        config.headers["Content-Type"] = "application/json"
	        return $http.put(_url + "/api/postReads/" + postRead.id, postRead, config)
	    }

	    if (this.deletePostRead) {
	        console.log("deletePostRead");
	    }
	    this.deletePostRead = function(id) {
	        return $http.delete("/api/postReads/" + id);
	    }

	    if (this.findPostReadByPersonIdOrderByDate) {
	    	window.console && console.log("findPostReadByPersonIdOrderByDate");
	    }
	    this.findPostReadByPersonIdOrderByDate = function(personId, projection) {
	        var config = {};
	        config.params = {
	            personId: personId,

	        }
	        config.params["projection"] = projection;
	        return $http.get(_url + "/api/postReads/search/findPostReadByPersonIdOrderByDate",  config)
	    };

	/*---------------------------------------------------------------------------*/

	/*---------------------------------------------------------------------------*/
		if (this.getPromotions) {
			window.console && console.log("getPromotions");
		}
	    this.getPromotions = function(_page, _size, _sort, projection) {
	        var config = {};
	        config.params = {};
	        config.params["page"] = page;
	        config.params["size"] = size;
	        config.params["sort"] = sort;
	        config.params["projection"] = projection;
			return $http.get(_url + "/api/promotions",  config)
	    }

	    if (this.postPromotion) {
	        window.console && console.log("postPromotion");
	    }
	    this.postPromotion = function(promotion) {
	        var config = {};
	        config.headers = {};
	        config.headers["Content-Type"] = "application/json"
	        return $http.post(_url + "/api/promotions/", promotion, config)
	    }

	    if (this.getPromotion) {
	        window.console && console.log("getPromotion");
	    }
	    this.getPromotion = function(id, projection) {
	        var config = {};
	        config.params = {};
	        config.params["projection"] = projection;
	        return $http.get(_url + "/api/promotions/" + id,  config)
	    }

	    if (this.putPromotion) {
	        console.log("putPromotion");
	    }
	    this.putPromotion = function(promotion) {
	        var config = {};
	        config.headers = {};
	        config.headers["Content-Type"] = "application/json"
	        return $http.put(_url + "/api/promotions/" + promotion.id, promotion, config)
	    }

	    if (this.deletePromotion) {
	        console.log("deletePromotion");
	    }
	    this.deletePromotion = function(id) {
	        return $http.delete("/api/promotions/" + id);
	    }


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
			return $http.get(_url + "/api/rows",  config)
	    }

	    if (this.postRow) {
	        window.console && console.log("postRow");
	    }
	    this.postRow = function(row) {
	        var config = {};
	        config.headers = {};
	        config.headers["Content-Type"] = "application/json"
	        return $http.post(_url + "/api/rows/", row, config)
	    }

	    if (this.getRow) {
	        window.console && console.log("getRow");
	    }
	    this.getRow = function(id, projection) {
	        var config = {};
	        config.params = {};
	        config.params["projection"] = projection;
	        return $http.get(_url + "/api/rows/" + id,  config)
	    }

	    if (this.putRow) {
	        console.log("putRow");
	    }
	    this.putRow = function(row) {
	        var config = {};
	        config.headers = {};
	        config.headers["Content-Type"] = "application/json"
	        return $http.put(_url + "/api/rows/" + row.id, row, config)
	    }

	    if (this.deleteRow) {
	        console.log("deleteRow");
	    }
	    this.deleteRow = function(id) {
	        return $http.delete("/api/rows/" + id);
	    }


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
			return $http.get(_url + "/api/sponsors",  config)
	    }

	    if (this.postSponsor) {
	        window.console && console.log("postSponsor");
	    }
	    this.postSponsor = function(sponsor) {
	        var config = {};
	        config.headers = {};
	        config.headers["Content-Type"] = "application/json"
	        return $http.post(_url + "/api/sponsors/", sponsor, config)
	    }

	    if (this.getSponsor) {
	        window.console && console.log("getSponsor");
	    }
	    this.getSponsor = function(id, projection) {
	        var config = {};
	        config.params = {};
	        config.params["projection"] = projection;
	        return $http.get(_url + "/api/sponsors/" + id,  config)
	    }

	    if (this.putSponsor) {
	        console.log("putSponsor");
	    }
	    this.putSponsor = function(sponsor) {
	        var config = {};
	        config.headers = {};
	        config.headers["Content-Type"] = "application/json"
	        return $http.put(_url + "/api/sponsors/" + sponsor.id, sponsor, config)
	    }

	    if (this.deleteSponsor) {
	        console.log("deleteSponsor");
	    }
	    this.deleteSponsor = function(id) {
	        return $http.delete("/api/sponsors/" + id);
	    }


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
			return $http.get(_url + "/api/stations",  config)
	    }

	    if (this.postStation) {
	        window.console && console.log("postStation");
	    }
	    this.postStation = function(station) {
	        var config = {};
	        config.headers = {};
	        config.headers["Content-Type"] = "application/json"
	        return $http.post(_url + "/api/stations/", station, config)
	    }

	    if (this.getStation) {
	        window.console && console.log("getStation");
	    }
	    this.getStation = function(id, projection) {
	        var config = {};
	        config.params = {};
	        config.params["projection"] = projection;
	        return $http.get(_url + "/api/stations/" + id,  config)
	    }

	    if (this.putStation) {
	        console.log("putStation");
	    }
	    this.putStation = function(station) {
	        var config = {};
	        config.headers = {};
	        config.headers["Content-Type"] = "application/json"
	        return $http.put(_url + "/api/stations/" + station.id, station, config)
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
	        return $http.get(_url + "/api/stations/search/findByName",  config)
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
	        return $http.get(_url + "/api/stations/search/findByPersonIdAndNetworkId",  config)
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
			return $http.get(_url + "/api/stationPerspectives",  config)
	    }

	    if (this.postStationPerspective) {
	        window.console && console.log("postStationPerspective");
	    }
	    this.postStationPerspective = function(stationPerspective) {
	        var config = {};
	        config.headers = {};
	        config.headers["Content-Type"] = "application/json"
	        return $http.post(_url + "/api/stationPerspectives/", stationPerspective, config)
	    }

	    if (this.getStationPerspective) {
	        window.console && console.log("getStationPerspective");
	    }
	    this.getStationPerspective = function(id, projection) {
	        var config = {};
	        config.params = {};
	        config.params["projection"] = projection;
	        return $http.get(_url + "/api/stationPerspectives/" + id,  config)
	    }

	    if (this.putStationPerspective) {
	        console.log("putStationPerspective");
	    }
	    this.putStationPerspective = function(stationPerspective) {
	        var config = {};
	        config.headers = {};
	        config.headers["Content-Type"] = "application/json"
	        return $http.put(_url + "/api/stationPerspectives/" + stationPerspective.id, stationPerspective, config)
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
	        return $http.get(_url + "/api/stationPerspectives/search/findStationPerspectivesByStation",  config)
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
			return $http.get(_url + "/api/stationRoles",  config)
	    }

	    if (this.postStationRole) {
	        window.console && console.log("postStationRole");
	    }
	    this.postStationRole = function(stationRole) {
	        var config = {};
	        config.headers = {};
	        config.headers["Content-Type"] = "application/json"
	        return $http.post(_url + "/api/stationRoles/", stationRole, config)
	    }

	    if (this.getStationRole) {
	        window.console && console.log("getStationRole");
	    }
	    this.getStationRole = function(id, projection) {
	        var config = {};
	        config.params = {};
	        config.params["projection"] = projection;
	        return $http.get(_url + "/api/stationRoles/" + id,  config)
	    }

	    if (this.putStationRole) {
	        console.log("putStationRole");
	    }
	    this.putStationRole = function(stationRole) {
	        var config = {};
	        config.headers = {};
	        config.headers["Content-Type"] = "application/json"
	        return $http.put(_url + "/api/stationRoles/" + stationRole.id, stationRole, config)
	    }

	    if (this.deleteStationRole) {
	        console.log("deleteStationRole");
	    }
	    this.deleteStationRole = function(id) {
	        return $http.delete("/api/stationRoles/" + id);
	    }


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
			return $http.get(_url + "/api/taxonomies",  config)
	    }

	    if (this.postTaxonomy) {
	        window.console && console.log("postTaxonomy");
	    }
	    this.postTaxonomy = function(taxonomy) {
	        var config = {};
	        config.headers = {};
	        config.headers["Content-Type"] = "application/json"
	        return $http.post(_url + "/api/taxonomies/", taxonomy, config)
	    }

	    if (this.getTaxonomy) {
	        window.console && console.log("getTaxonomy");
	    }
	    this.getTaxonomy = function(id, projection) {
	        var config = {};
	        config.params = {};
	        config.params["projection"] = projection;
	        return $http.get(_url + "/api/taxonomies/" + id,  config)
	    }

	    if (this.putTaxonomy) {
	        console.log("putTaxonomy");
	    }
	    this.putTaxonomy = function(taxonomy) {
	        var config = {};
	        config.headers = {};
	        config.headers["Content-Type"] = "application/json"
	        return $http.put(_url + "/api/taxonomies/" + taxonomy.id, taxonomy, config)
	    }

	    if (this.deleteTaxonomy) {
	        console.log("deleteTaxonomy");
	    }
	    this.deleteTaxonomy = function(id) {
	        return $http.delete("/api/taxonomies/" + id);
	    }

	    if (this.findByStationId) {
	    	window.console && console.log("findByStationId");
	    }
	    this.findByStationId = function(stationId, projection) {
	        var config = {};
	        config.params = {
	            stationId: stationId,

	        }
	        config.params["projection"] = projection;
	        return $http.get(_url + "/api/taxonomies/search/findByStationId",  config)
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
	        return $http.get(_url + "/api/taxonomies/search/findByTypeAndName",  config)
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
			return $http.get(_url + "/api/terms",  config)
	    }

	    if (this.postTerm) {
	        window.console && console.log("postTerm");
	    }
	    this.postTerm = function(term) {
	        var config = {};
	        config.headers = {};
	        config.headers["Content-Type"] = "application/json"
	        return $http.post(_url + "/api/terms/", term, config)
	    }

	    if (this.getTerm) {
	        window.console && console.log("getTerm");
	    }
	    this.getTerm = function(id, projection) {
	        var config = {};
	        config.params = {};
	        config.params["projection"] = projection;
	        return $http.get(_url + "/api/terms/" + id,  config)
	    }

	    if (this.putTerm) {
	        console.log("putTerm");
	    }
	    this.putTerm = function(term) {
	        var config = {};
	        config.headers = {};
	        config.headers["Content-Type"] = "application/json"
	        return $http.put(_url + "/api/terms/" + term.id, term, config)
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
	        return $http.get(_url + "/api/terms/search/findTermsByParentId",  config)
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
	        return $http.get(_url + "/api/terms/search/findRootsPage",  config)
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
	        return $http.get(_url + "/api/terms/search/findRoots",  config)
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
	        return $http.get(_url + "/api/terms/search/countTerms",  config)
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
			return $http.get(_url + "/api/termPerspectives",  config)
	    }

	    if (this.postTermPerspective) {
	        window.console && console.log("postTermPerspective");
	    }
	    this.postTermPerspective = function(termPerspective) {
	        var config = {};
	        config.headers = {};
	        config.headers["Content-Type"] = "application/json"
	        return $http.post(_url + "/api/termPerspectives/", termPerspective, config)
	    }

	    if (this.getTermPerspective) {
	        window.console && console.log("getTermPerspective");
	    }
	    this.getTermPerspective = function(id, projection) {
	        var config = {};
	        config.params = {};
	        config.params["projection"] = projection;
	        return $http.get(_url + "/api/termPerspectives/" + id,  config)
	    }

	    if (this.putTermPerspective) {
	        console.log("putTermPerspective");
	    }
	    this.putTermPerspective = function(termPerspective) {
	        var config = {};
	        config.headers = {};
	        config.headers["Content-Type"] = "application/json"
	        return $http.put(_url + "/api/termPerspectives/" + termPerspective.id, termPerspective, config)
	    }

	    if (this.deleteTermPerspective) {
	        console.log("deleteTermPerspective");
	    }
	    this.deleteTermPerspective = function(id) {
	        return $http.delete("/api/termPerspectives/" + id);
	    }


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
			return $http.get(_url + "/api/users",  config)
	    }

	    if (this.postUser) {
	        window.console && console.log("postUser");
	    }
	    this.postUser = function(user) {
	        var config = {};
	        config.headers = {};
	        config.headers["Content-Type"] = "application/json"
	        return $http.post(_url + "/api/users/", user, config)
	    }

	    if (this.getUser) {
	        window.console && console.log("getUser");
	    }
	    this.getUser = function(username, projection) {
	        var config = {};
	        config.params = {};
	        config.params["projection"] = projection;
	        return $http.get(_url + "/api/users/" + username,  config)
	    }

	    if (this.putUser) {
	        console.log("putUser");
	    }
	    this.putUser = function(user) {
	        var config = {};
	        config.headers = {};
	        config.headers["Content-Type"] = "application/json"
	        return $http.put(_url + "/api/users/" + user.username, user, config)
	    }

	    if (this.deleteUser) {
	        console.log("deleteUser");
	    }
	    this.deleteUser = function(username) {
	        return $http.delete("/api/users/" + username);
	    }

	    if (this.findByUsernameAndPassword) {
	    	window.console && console.log("findByUsernameAndPassword");
	    }
	    this.findByUsernameAndPassword = function(username, password, projection) {
	        var config = {};
	        config.params = {
	            username: username,
	            password: password,

	        }
	        config.params["projection"] = projection;
	        return $http.get(_url + "/api/users/search/findByUsernameAndPassword",  config)
	    };

	    if (this.findByUsernameAndEnabled) {
	    	window.console && console.log("findByUsernameAndEnabled");
	    }
	    this.findByUsernameAndEnabled = function(username, enabled, projection) {
	        var config = {};
	        config.params = {
	            username: username,
	            enabled: enabled,

	        }
	        config.params["projection"] = projection;
	        return $http.get(_url + "/api/users/search/findByUsernameAndEnabled",  config)
	    };

	/*---------------------------------------------------------------------------*/

	/*---------------------------------------------------------------------------*/
		if (this.getWordpress) {
			window.console && console.log("getWordpress");
		}
	    this.getWordpress = function(_page, _size, _sort, projection) {
	        var config = {};
	        config.params = {};
	        config.params["page"] = page;
	        config.params["size"] = size;
	        config.params["sort"] = sort;
	        config.params["projection"] = projection;
			return $http.get(_url + "/api/wordpress",  config)
	    }

	    if (this.postWordpress) {
	        window.console && console.log("postWordpress");
	    }
	    this.postWordpress = function(wordpress) {
	        var config = {};
	        config.headers = {};
	        config.headers["Content-Type"] = "application/json"
	        return $http.post(_url + "/api/wordpress/", wordpress, config)
	    }

	    if (this.getWordpress) {
	        window.console && console.log("getWordpress");
	    }
	    this.getWordpress = function(id, projection) {
	        var config = {};
	        config.params = {};
	        config.params["projection"] = projection;
	        return $http.get(_url + "/api/wordpress/" + id,  config)
	    }

	    if (this.putWordpress) {
	        console.log("putWordpress");
	    }
	    this.putWordpress = function(wordpress) {
	        var config = {};
	        config.headers = {};
	        config.headers["Content-Type"] = "application/json"
	        return $http.put(_url + "/api/wordpress/" + wordpress.id, wordpress, config)
	    }

	    if (this.deleteWordpress) {
	        console.log("deleteWordpress");
	    }
	    this.deleteWordpress = function(id) {
	        return $http.delete("/api/wordpress/" + id);
	    }


	/*---------------------------------------------------------------------------*/
})