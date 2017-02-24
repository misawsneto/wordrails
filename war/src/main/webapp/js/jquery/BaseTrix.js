function AudioDto(id, duration, identifier, image, imageHash, imageUrl, provider, title) {
	return {
		id: id,
		duration: duration,
		identifier: identifier,
		image: image,
		imageHash: imageHash,
		imageUrl: imageUrl,
		provider: provider,
		title: title
	};
}

function AuthCredentialDto(id, facebookAppID, facebookAppSecret, googleAppID, googleAppSecret) {
	return {
		id: id,
		facebookAppID: facebookAppID,
		facebookAppSecret: facebookAppSecret,
		googleAppID: googleAppID,
		googleAppSecret: googleAppSecret
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

function ContainerSectionDto(id, children, bottomMargin, bottomPadding, leftMargin, leftPadding, orderPosition, pctSize, properties, rightMargin, rightPadding, title, topMargin, topPadding) {
	return {
		id: id,
		children: children,
		bottomMargin: bottomMargin,
		bottomPadding: bottomPadding,
		leftMargin: leftMargin,
		leftPadding: leftPadding,
		orderPosition: orderPosition,
		pctSize: pctSize,
		properties: properties,
		rightMargin: rightMargin,
		rightPadding: rightPadding,
		title: title,
		topMargin: topMargin,
		topPadding: topPadding
	};
}

function DocumentDto(id, identifier, provider, title, url) {
	return {
		id: id,
		identifier: identifier,
		provider: provider,
		title: title,
		url: url
	};
}

function ImageDto(id, caption, credits, externalImageUrl, hashes, imageUrl, largeHash, mediumHash, originalHash, originalUrl, smallHash, title, vertical) {
	return {
		id: id,
		caption: caption,
		credits: credits,
		externalImageUrl: externalImageUrl,
		hashes: hashes,
		imageUrl: imageUrl,
		largeHash: largeHash,
		mediumHash: mediumHash,
		originalHash: originalHash,
		originalUrl: originalUrl,
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

function NetworkDto(id, addStationRolesOnSignup, alertColors, allowSignup, allowSocialLogin, allowSponsors, appleStoreAddress, backgroundColor, backgroundColors, categoriesTaxonomyId, configured, defaultOrientationMode, defaultReadMode, domain, emailSignUpValidationEnabled, facebookAppID, facebookLink, facebookLoginAllowed, faviconHash, faviconId, faviconUrl, flurryAppleKey, flurryKey, googleAppID, googleLoginAllowed, googlePlusLink, homeTabName, info, instagramLink, invitationMessage, language, linkedInLink, loginFooterMessage, loginImageHash, loginImageId, loginImageSmallHash, loginImageUrl, logoImageHash, logoImageId, logoImageUrl, mainColor, name, navbarColor, navbarSecondaryColor, networkCreationToken, newsFontSize, pinterestLink, playStoreAddress, primaryColors, primaryFont, secondaryColors, secondaryFont, splashBgColor, splashImageHash, splashImageId, splashImageUrl, stationMenuName, subdomain, titleFontSize, trackingId, twitterLink, validationMessage, webFooter, youtubeLink) {
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
		emailSignUpValidationEnabled: emailSignUpValidationEnabled,
		facebookAppID: facebookAppID,
		facebookLink: facebookLink,
		facebookLoginAllowed: facebookLoginAllowed,
		faviconHash: faviconHash,
		faviconId: faviconId,
		faviconUrl: faviconUrl,
		flurryAppleKey: flurryAppleKey,
		flurryKey: flurryKey,
		googleAppID: googleAppID,
		googleLoginAllowed: googleLoginAllowed,
		googlePlusLink: googlePlusLink,
		homeTabName: homeTabName,
		info: info,
		instagramLink: instagramLink,
		invitationMessage: invitationMessage,
		language: language,
		linkedInLink: linkedInLink,
		loginFooterMessage: loginFooterMessage,
		loginImageHash: loginImageHash,
		loginImageId: loginImageId,
		loginImageSmallHash: loginImageSmallHash,
		loginImageUrl: loginImageUrl,
		logoImageHash: logoImageHash,
		logoImageId: logoImageId,
		logoImageUrl: logoImageUrl,
		mainColor: mainColor,
		name: name,
		navbarColor: navbarColor,
		navbarSecondaryColor: navbarSecondaryColor,
		networkCreationToken: networkCreationToken,
		newsFontSize: newsFontSize,
		pinterestLink: pinterestLink,
		playStoreAddress: playStoreAddress,
		primaryColors: primaryColors,
		primaryFont: primaryFont,
		secondaryColors: secondaryColors,
		secondaryFont: secondaryFont,
		splashBgColor: splashBgColor,
		splashImageHash: splashImageHash,
		splashImageId: splashImageId,
		splashImageUrl: splashImageUrl,
		stationMenuName: stationMenuName,
		subdomain: subdomain,
		titleFontSize: titleFontSize,
		trackingId: trackingId,
		twitterLink: twitterLink,
		validationMessage: validationMessage,
		webFooter: webFooter,
		youtubeLink: youtubeLink
	};
}

function NetworkCreateDto(id, contacted, email, name) {
	return {
		id: id,
		contacted: contacted,
		email: email,
		name: name
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

function PersonDto(id, bio, bookmarkPosts, coverHash, coverLargeHash, coverLargeUrl, coverMediumHash, coverMediumUrl, coverUrl, email, enabled, hasFacebookProfile, hasGoogleProfile, imageHash, imageLargeHash, imageLargeUrl, imageMediumHash, imageMediumUrl, imageSmallHash, imageSmallUrl, imageSocialUrl, imageUrl, lastLogin, name, networkAdmin, recommendPosts, seenWelcome, twitterHandle, username) {
	return {
		id: id,
		bio: bio,
		bookmarkPosts: bookmarkPosts,
		coverHash: coverHash,
		coverLargeHash: coverLargeHash,
		coverLargeUrl: coverLargeUrl,
		coverMediumHash: coverMediumHash,
		coverMediumUrl: coverMediumUrl,
		coverUrl: coverUrl,
		email: email,
		enabled: enabled,
		hasFacebookProfile: hasFacebookProfile,
		hasGoogleProfile: hasGoogleProfile,
		imageHash: imageHash,
		imageLargeHash: imageLargeHash,
		imageLargeUrl: imageLargeUrl,
		imageMediumHash: imageMediumHash,
		imageMediumUrl: imageMediumUrl,
		imageSmallHash: imageSmallHash,
		imageSmallUrl: imageSmallUrl,
		imageSocialUrl: imageSocialUrl,
		imageUrl: imageUrl,
		lastLogin: lastLogin,
		name: name,
		networkAdmin: networkAdmin,
		recommendPosts: recommendPosts,
		seenWelcome: seenWelcome,
		twitterHandle: twitterHandle,
		username: username
	};
}

function PostDto(id, author, body, bookmarksCount, commentsCount, date, externalVideoUrl, featuredAudio, featuredAudioHash, featuredImage, featuredVideo, featuredVideoHash, focusX, focusY, imageCredits, imageHash, imageId, imageLandscape, imageLargeHash, imageLargeUrl, imageMediumHash, imageMediumUrl, imageSmallHash, imageSmallUrl, imageUrl, lastModificationDate, lat, lng, mediaAudio, mediaGallery, mediaImage, mediaVideo, notify, originalSlug, readTime, recommendsCount, scheduledDate, slug, sponsored, state, station, stationId, subheading, tags, title, topper, unpublishDate) {
	return {
		id: id,
		author: author,
		body: body,
		bookmarksCount: bookmarksCount,
		commentsCount: commentsCount,
		date: date,
		externalVideoUrl: externalVideoUrl,
		featuredAudio: featuredAudio,
		featuredAudioHash: featuredAudioHash,
		featuredImage: featuredImage,
		featuredVideo: featuredVideo,
		featuredVideoHash: featuredVideoHash,
		focusX: focusX,
		focusY: focusY,
		imageCredits: imageCredits,
		imageHash: imageHash,
		imageId: imageId,
		imageLandscape: imageLandscape,
		imageLargeHash: imageLargeHash,
		imageLargeUrl: imageLargeUrl,
		imageMediumHash: imageMediumHash,
		imageMediumUrl: imageMediumUrl,
		imageSmallHash: imageSmallHash,
		imageSmallUrl: imageSmallUrl,
		imageUrl: imageUrl,
		lastModificationDate: lastModificationDate,
		lat: lat,
		lng: lng,
		mediaAudio: mediaAudio,
		mediaGallery: mediaGallery,
		mediaImage: mediaImage,
		mediaVideo: mediaVideo,
		notify: notify,
		originalSlug: originalSlug,
		readTime: readTime,
		recommendsCount: recommendsCount,
		scheduledDate: scheduledDate,
		slug: slug,
		sponsored: sponsored,
		state: state,
		station: station,
		stationId: stationId,
		subheading: subheading,
		tags: tags,
		title: title,
		topper: topper,
		unpublishDate: unpublishDate
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

function StationDto(id, alertColors, allowComments, allowSocialShare, allowWritersToAddSponsors, allowWritersToNotify, backgroundColor, backgroundColors, categoriesTaxonomyId, defaultPerspectiveId, logoHash, logoUrl, main, name, navbarColor, postsTitleSize, primaryColor, primaryColors, secondaryColors, showAuthorData, showAuthorSocialData, sponsored, stationPerspectives, stationSlug, subheading, topper, visibility, writable) {
	return {
		id: id,
		alertColors: alertColors,
		allowComments: allowComments,
		allowSocialShare: allowSocialShare,
		allowWritersToAddSponsors: allowWritersToAddSponsors,
		allowWritersToNotify: allowWritersToNotify,
		backgroundColor: backgroundColor,
		backgroundColors: backgroundColors,
		categoriesTaxonomyId: categoriesTaxonomyId,
		defaultPerspectiveId: defaultPerspectiveId,
		logoHash: logoHash,
		logoUrl: logoUrl,
		main: main,
		name: name,
		navbarColor: navbarColor,
		postsTitleSize: postsTitleSize,
		primaryColor: primaryColor,
		primaryColors: primaryColors,
		secondaryColors: secondaryColors,
		showAuthorData: showAuthorData,
		showAuthorSocialData: showAuthorSocialData,
		sponsored: sponsored,
		stationPerspectives: stationPerspectives,
		stationSlug: stationSlug,
		subheading: subheading,
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

function TaxonomyDto(id, name, type) {
	return {
		id: id,
		name: name,
		type: type
	};
}

function TermDto(id, color, description, imageHash, imageUrl, name, name_parent, taxonomyId, taxonomyName) {
	return {
		id: id,
		color: color,
		description: description,
		imageHash: imageHash,
		imageUrl: imageUrl,
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

function VideoDto(id, duration, externalVideoUrl, identifier, image, imageHash, imageUrl, provider, title, type) {
	return {
		id: id,
		duration: duration,
		externalVideoUrl: externalVideoUrl,
		identifier: identifier,
		image: image,
		imageHash: imageHash,
		imageUrl: imageUrl,
		provider: provider,
		title: title,
		type: type
	};
}

function BaseTrix(_url, _username, _password) {
	function logIn(complete) {
		return $.ajax({
			type: "POST",
			url: _url + "/j_spring_security_check",
			crossDomain: true,
			xhrFields: {
				withCredentials: true
			},
			data: {
				"j_username": _username,
				"j_password": _password
			},
			complete: complete
		});
	}

	function logOut(complete) {
		return $.ajax({
			type: "GET",
			url: _url + "/j_spring_security_logout",
			crossDomain: true,
			complete: complete
		});
	}

	var that = {};

	that.getUrl = function(){
		return _url;
	}

	that.logIn = function(complete){
		return logIn(complete);    
	}

	that._ajax = function(settings) {
		var error = settings.error;
		var complete = settings.complete;
		settings.error = function(jqXHR, textStatus, errorThrown) {
			if (jqXHR.status === 401) {
				logIn(function() {
					settings.error = error;
					settings.complete = complete;
					$.ajax(settings);
				});
			} else if (error) {
				error(jqXHR, textStatus, errorThrown);
			}
		};
		settings.complete = function(jqXHR, textStatus) {
			if (jqXHR.status !== 401 && complete) {
				complete(jqXHR, textStatus);
			}
		};

		settings.crossDomain = true
		settings.xhrFields = {withCredentials: true}

		return $.ajax(settings);
	};

	that.updatePostTerms = function(postId, terms) {
		return that._ajax({
			url: _url + "/api/posts/" + postId + "/updatePostTerms",
			data: terms,
			success: function(_data, _textStatus, _jqXHR) {
				if (_success) {
					_success(_data.content, _textStatus, _jqXHR);
				}
			},
			error: _error,
			complete: _complete
		});
	}    

	that.findPostsByStationIdAndAuthorIdAndState = function(stationId, authorId, state, _page, _size, _sort, _success, _error, _complete) {
		return that._ajax({
			url: _url + "/api/posts/" + stationId + "/findPostsByStationIdAndAuthorIdAndState",
			data: {
				page: _page,
				size: _size,
				sort: _sort,
				authorId: authorId,
				state: state
			},
			success: function(_data, _textStatus, _jqXHR) {
				if (_success) {
					_success(_data.content, _textStatus, _jqXHR);
				}
			},
			error: _error,
			complete: _complete
		});
	};

	that.findPostsAndPostsPromotedByAuthorId = function(stationId, authorId, _page, _size, _sort, _success, _error, _complete) {
		return that._ajax({
			url: _url + "/api/posts/" + stationId + "/findPostsAndPostsPromotedByAuthorId",
			data: {
				page: _page,
				size: _size,
				sort: _sort,
				authorId: authorId
			},
			success: function(_data, _textStatus, _jqXHR) {
				if (_success) {
					_success(_data.content, _textStatus, _jqXHR);
				}
			},
			error: _error,
			complete: _complete
		});
	};

	that.putPassword = function(oldPassword, newPassword, _success, _error, _complete) {
		return that._ajax({
			url: _url + "/api/persons/me/password",
			type: "PUT",
			data: {
				oldPassword: oldPassword,
				newPassword: newPassword
			},
			success: function(_data, _textStatus, _jqXHR) {
				if (_success) {
					_success(_data, _textStatus, _jqXHR);
				}
			},
			error: _error,
			complete: _complete
		});
	};

	that.searchPostsFromOrPromotedToStation = function(stationId, query, _page, _size, _success, _error, _complete) {
		return that._ajax({
			url: _url + "/api/posts/" + stationId + "/searchPostsFromOrPromotedToStation",
			data: {
				query: query,
				page: _page,
				size: _size
			},
			success: function(_data, _textStatus, _jqXHR) {
				if (_success) {
					_success(_data.content, _textStatus, _jqXHR);
				}
			},
			error: _error,
			complete: _complete
		});
	}

	that.getTaxonomiesToEdit = function(networkId, _success, _error, _complete) {
		return that._ajax({
			url: _url + "/api/taxonomies/networks/" + networkId + "/taxonomiesToEdit",
			success: function(_data, _textStatus, _jqXHR) {
				if (_success) {
					_success(_data.content, _textStatus, _jqXHR);
				}
			},
			error: _error,
			complete: _complete
		});
	}

	//{
	//	stationPerspectiveId: stationPerspectiveId,
	//	termPerspectiveId: termPerspectiveId,
	//	childTermId: childTermId,
	//    page: _page,
	//    size: _size
	//}
	that.getRowView = function(data, _success, _error, _complete) {
    	return that._ajax({
            url: _url + "/api/perspectives/rowViews",
            data: data,
            success: function(_data, _textStatus, _jqXHR) {
                if (_success) {
                    _success(_data, _textStatus, _jqXHR);
                }
            },
            error: _error,
            complete: _complete
        });
    }

    that.getCurrentPerson = function(_success, _error, _complete) {
    	return that._ajax({
            url: _url + "/api/persons/me",
            success: function(_data, _textStatus, _jqXHR) {
                if (_success) {
                    _success(_data, _textStatus, _jqXHR);
                }
            },
            error: _error,
            complete: _complete
        });
    }

    that.getNetworkPersonPermissions = function(networkId, _success, _error, _complete){
    	return that._ajax({
            url: _url + "/api/networks/" + networkId + "/permissions",
            success: function(_data, _textStatus, _jqXHR) {
                if (_success) {
                    _success(_data, _textStatus, _jqXHR);
                }
            },
            error: _error,
            complete: _complete
        });
    }


/*---------------------------------------------------------------------------*/
	if (that.getAudios) {
		console.log("getAudios");
	}
	that.getAudios = function(_page, _size, _sort, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/audios",
			data: {
				page: _page,
				size: _size,
				sort: _sort,
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
	};

	if (that.postAudio) {
		console.log("postAudio");
	}
	that.postAudio = function(audio, _success, _error, _complete) {
		return that._ajax({
			type: "POST",
			url: _url + "/api/audios",
			contentType: "application/json",
			data: JSON.stringify(audio),
			success: function(_data, _textStatus, _jqXHR) {
				if (_success) {
					var _value = _jqXHR.getResponseHeader("Location");
					if (_value) {
						var _index = _value.lastIndexOf("/");
						var _suffix = _value.substring(_index + 1);
						var id = _suffix;
						_success(id, _textStatus, _jqXHR);
					}
				}
			},
			error: _error,
			complete: _complete
		});
	};

	if (that.getAudio) {
		console.log("getAudio");
	}
	that.getAudio = function(id, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/audios/" + id,
			data: {
				projection: projection
			},
			success: _success,
			error: _error,
			complete: _complete
		});
	};

	if (that.putAudio) {
		console.log("putAudio");
	}
	that.putAudio = function(audio, _success, _error, _complete) {
		return that._ajax({
			type: "PUT",
			url: _url + "/api/audios/" + audio.id,
			contentType: "application/json",
			data: JSON.stringify(audio),
			success: _success,
			error: _error,
			complete: _complete
		});
	};

	if (that.deleteAudio) {
		console.log("deleteAudio");
	}
	that.deleteAudio = function(id, _success, _error, _complete) {
		return that._ajax({
			type: "DELETE",
			url: _url + "/api/audios/" + id,
			success: _success,
			error: _error,
			complete: _complete
		});
	};

	if (that.findAudiosOrderByDate) {
		console.log("findAudiosOrderByDate");
	}
	that.findAudiosOrderByDate = function(page, size, sort, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/audios/search/findAudiosOrderByDate",
			data: {
				page: page,
				size: size,
				sort: sort,

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
	};

	if (that.getAudioImage) {
		console.log("getAudioImage");
	}
	that.getAudioImage = function(id, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/audios/" + id + "/image",
			data: {
				projection: projection
			},
			success: _success,
			error: _error,
			complete: _complete
		});
	};

	if (that.putAudioImage) {
		console.log("putAudioImage");
	}
	that.putAudioImage = function(id, image, _success, _error, _complete) {
		if (image === null) {
			return that._ajax({
				type: "DELETE",
				url: _url + "/api/audios/" + id + "/image",
				success: _success,
				error: _error,
				complete: _complete
			});
		} else {
			return that._ajax({
				type: "PUT",
				url: _url + "/api/audios/" + id + "/image",
				contentType: "text/uri-list",
				data: image,
				success: _success,
				error: _error,
				complete: _complete
			});
		}
	};
/*---------------------------------------------------------------------------*/

/*---------------------------------------------------------------------------*/
	if (that.getAuthCredentials) {
		console.log("getAuthCredentials");
	}
	that.getAuthCredentials = function(_page, _size, _sort, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/authCredentials",
			data: {
				page: _page,
				size: _size,
				sort: _sort,
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
	};

	if (that.postAuthCredential) {
		console.log("postAuthCredential");
	}
	that.postAuthCredential = function(authCredential, _success, _error, _complete) {
		return that._ajax({
			type: "POST",
			url: _url + "/api/authCredentials",
			contentType: "application/json",
			data: JSON.stringify(authCredential),
			success: function(_data, _textStatus, _jqXHR) {
				if (_success) {
					var _value = _jqXHR.getResponseHeader("Location");
					if (_value) {
						var _index = _value.lastIndexOf("/");
						var _suffix = _value.substring(_index + 1);
						var id = _suffix;
						_success(id, _textStatus, _jqXHR);
					}
				}
			},
			error: _error,
			complete: _complete
		});
	};

	if (that.getAuthCredential) {
		console.log("getAuthCredential");
	}
	that.getAuthCredential = function(id, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/authCredentials/" + id,
			data: {
				projection: projection
			},
			success: _success,
			error: _error,
			complete: _complete
		});
	};

	if (that.putAuthCredential) {
		console.log("putAuthCredential");
	}
	that.putAuthCredential = function(authCredential, _success, _error, _complete) {
		return that._ajax({
			type: "PUT",
			url: _url + "/api/authCredentials/" + authCredential.id,
			contentType: "application/json",
			data: JSON.stringify(authCredential),
			success: _success,
			error: _error,
			complete: _complete
		});
	};

	if (that.deleteAuthCredential) {
		console.log("deleteAuthCredential");
	}
	that.deleteAuthCredential = function(id, _success, _error, _complete) {
		return that._ajax({
			type: "DELETE",
			url: _url + "/api/authCredentials/" + id,
			success: _success,
			error: _error,
			complete: _complete
		});
	};

	if (that.findAuthCredentialByTenantId) {
		console.log("findAuthCredentialByTenantId");
	}
	that.findAuthCredentialByTenantId = function(tenantId, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/authCredentials/search/findAuthCredentialByTenantId",
			data: {
				tenantId: tenantId,

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
	};

	if (that.getAuthCredentialNetwork) {
		console.log("getAuthCredentialNetwork");
	}
	that.getAuthCredentialNetwork = function(id, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/authCredentials/" + id + "/network",
			data: {
				projection: projection
			},
			success: _success,
			error: _error,
			complete: _complete
		});
	};

	if (that.putAuthCredentialNetwork) {
		console.log("putAuthCredentialNetwork");
	}
	that.putAuthCredentialNetwork = function(id, network, _success, _error, _complete) {
		if (network === null) {
			return that._ajax({
				type: "DELETE",
				url: _url + "/api/authCredentials/" + id + "/network",
				success: _success,
				error: _error,
				complete: _complete
			});
		} else {
			return that._ajax({
				type: "PUT",
				url: _url + "/api/authCredentials/" + id + "/network",
				contentType: "text/uri-list",
				data: network,
				success: _success,
				error: _error,
				complete: _complete
			});
		}
	};
/*---------------------------------------------------------------------------*/

/*---------------------------------------------------------------------------*/
	if (that.getCells) {
		console.log("getCells");
	}
	that.getCells = function(_page, _size, _sort, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/cells",
			data: {
				page: _page,
				size: _size,
				sort: _sort,
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
	};

	if (that.postCell) {
		console.log("postCell");
	}
	that.postCell = function(cell, _success, _error, _complete) {
		return that._ajax({
			type: "POST",
			url: _url + "/api/cells",
			contentType: "application/json",
			data: JSON.stringify(cell),
			success: function(_data, _textStatus, _jqXHR) {
				if (_success) {
					var _value = _jqXHR.getResponseHeader("Location");
					if (_value) {
						var _index = _value.lastIndexOf("/");
						var _suffix = _value.substring(_index + 1);
						var id = _suffix;
						_success(id, _textStatus, _jqXHR);
					}
				}
			},
			error: _error,
			complete: _complete
		});
	};

	if (that.getCell) {
		console.log("getCell");
	}
	that.getCell = function(id, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/cells/" + id,
			data: {
				projection: projection
			},
			success: _success,
			error: _error,
			complete: _complete
		});
	};

	if (that.putCell) {
		console.log("putCell");
	}
	that.putCell = function(cell, _success, _error, _complete) {
		return that._ajax({
			type: "PUT",
			url: _url + "/api/cells/" + cell.id,
			contentType: "application/json",
			data: JSON.stringify(cell),
			success: _success,
			error: _error,
			complete: _complete
		});
	};

	if (that.deleteCell) {
		console.log("deleteCell");
	}
	that.deleteCell = function(id, _success, _error, _complete) {
		return that._ajax({
			type: "DELETE",
			url: _url + "/api/cells/" + id,
			success: _success,
			error: _error,
			complete: _complete
		});
	};


	if (that.getCellPost) {
		console.log("getCellPost");
	}
	that.getCellPost = function(id, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/cells/" + id + "/post",
			data: {
				projection: projection
			},
			success: _success,
			error: _error,
			complete: _complete
		});
	};

	if (that.putCellPost) {
		console.log("putCellPost");
	}
	that.putCellPost = function(id, post, _success, _error, _complete) {
		if (post === null) {
			return that._ajax({
				type: "DELETE",
				url: _url + "/api/cells/" + id + "/post",
				success: _success,
				error: _error,
				complete: _complete
			});
		} else {
			return that._ajax({
				type: "PUT",
				url: _url + "/api/cells/" + id + "/post",
				contentType: "text/uri-list",
				data: post,
				success: _success,
				error: _error,
				complete: _complete
			});
		}
	};


	if (that.getCellRow) {
		console.log("getCellRow");
	}
	that.getCellRow = function(id, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/cells/" + id + "/row",
			data: {
				projection: projection
			},
			success: _success,
			error: _error,
			complete: _complete
		});
	};

	if (that.putCellRow) {
		console.log("putCellRow");
	}
	that.putCellRow = function(id, row, _success, _error, _complete) {
		if (row === null) {
			return that._ajax({
				type: "DELETE",
				url: _url + "/api/cells/" + id + "/row",
				success: _success,
				error: _error,
				complete: _complete
			});
		} else {
			return that._ajax({
				type: "PUT",
				url: _url + "/api/cells/" + id + "/row",
				contentType: "text/uri-list",
				data: row,
				success: _success,
				error: _error,
				complete: _complete
			});
		}
	};


	if (that.getCellTerm) {
		console.log("getCellTerm");
	}
	that.getCellTerm = function(id, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/cells/" + id + "/term",
			data: {
				projection: projection
			},
			success: _success,
			error: _error,
			complete: _complete
		});
	};

	if (that.putCellTerm) {
		console.log("putCellTerm");
	}
	that.putCellTerm = function(id, term, _success, _error, _complete) {
		if (term === null) {
			return that._ajax({
				type: "DELETE",
				url: _url + "/api/cells/" + id + "/term",
				success: _success,
				error: _error,
				complete: _complete
			});
		} else {
			return that._ajax({
				type: "PUT",
				url: _url + "/api/cells/" + id + "/term",
				contentType: "text/uri-list",
				data: term,
				success: _success,
				error: _error,
				complete: _complete
			});
		}
	};
/*---------------------------------------------------------------------------*/

/*---------------------------------------------------------------------------*/
	if (that.getComments) {
		console.log("getComments");
	}
	that.getComments = function(_page, _size, _sort, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/comments",
			data: {
				page: _page,
				size: _size,
				sort: _sort,
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
	};

	if (that.postComment) {
		console.log("postComment");
	}
	that.postComment = function(comment, _success, _error, _complete) {
		return that._ajax({
			type: "POST",
			url: _url + "/api/comments",
			contentType: "application/json",
			data: JSON.stringify(comment),
			success: function(_data, _textStatus, _jqXHR) {
				if (_success) {
					var _value = _jqXHR.getResponseHeader("Location");
					if (_value) {
						var _index = _value.lastIndexOf("/");
						var _suffix = _value.substring(_index + 1);
						var id = _suffix;
						_success(id, _textStatus, _jqXHR);
					}
				}
			},
			error: _error,
			complete: _complete
		});
	};

	if (that.getComment) {
		console.log("getComment");
	}
	that.getComment = function(id, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/comments/" + id,
			data: {
				projection: projection
			},
			success: _success,
			error: _error,
			complete: _complete
		});
	};

	if (that.putComment) {
		console.log("putComment");
	}
	that.putComment = function(comment, _success, _error, _complete) {
		return that._ajax({
			type: "PUT",
			url: _url + "/api/comments/" + comment.id,
			contentType: "application/json",
			data: JSON.stringify(comment),
			success: _success,
			error: _error,
			complete: _complete
		});
	};

	if (that.deleteComment) {
		console.log("deleteComment");
	}
	that.deleteComment = function(id, _success, _error, _complete) {
		return that._ajax({
			type: "DELETE",
			url: _url + "/api/comments/" + id,
			success: _success,
			error: _error,
			complete: _complete
		});
	};

	if (that.findPostCommentsOrderByDate) {
		console.log("findPostCommentsOrderByDate");
	}
	that.findPostCommentsOrderByDate = function(postId, page, size, sort, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/comments/search/findPostCommentsOrderByDate",
			data: {
				postId: postId,
				page: page,
				size: size,
				sort: sort,

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
	};

	if (that.getCommentAuthor) {
		console.log("getCommentAuthor");
	}
	that.getCommentAuthor = function(id, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/comments/" + id + "/author",
			data: {
				projection: projection
			},
			success: _success,
			error: _error,
			complete: _complete
		});
	};

	if (that.putCommentAuthor) {
		console.log("putCommentAuthor");
	}
	that.putCommentAuthor = function(id, author, _success, _error, _complete) {
		if (author === null) {
			return that._ajax({
				type: "DELETE",
				url: _url + "/api/comments/" + id + "/author",
				success: _success,
				error: _error,
				complete: _complete
			});
		} else {
			return that._ajax({
				type: "PUT",
				url: _url + "/api/comments/" + id + "/author",
				contentType: "text/uri-list",
				data: author,
				success: _success,
				error: _error,
				complete: _complete
			});
		}
	};


	if (that.getCommentPost) {
		console.log("getCommentPost");
	}
	that.getCommentPost = function(id, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/comments/" + id + "/post",
			data: {
				projection: projection
			},
			success: _success,
			error: _error,
			complete: _complete
		});
	};

	if (that.putCommentPost) {
		console.log("putCommentPost");
	}
	that.putCommentPost = function(id, post, _success, _error, _complete) {
		if (post === null) {
			return that._ajax({
				type: "DELETE",
				url: _url + "/api/comments/" + id + "/post",
				success: _success,
				error: _error,
				complete: _complete
			});
		} else {
			return that._ajax({
				type: "PUT",
				url: _url + "/api/comments/" + id + "/post",
				contentType: "text/uri-list",
				data: post,
				success: _success,
				error: _error,
				complete: _complete
			});
		}
	};
/*---------------------------------------------------------------------------*/

/*---------------------------------------------------------------------------*/
	if (that.getContainerSections) {
		console.log("getContainerSections");
	}
	that.getContainerSections = function(_page, _size, _sort, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/containerSections",
			data: {
				page: _page,
				size: _size,
				sort: _sort,
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
	};

	if (that.postContainerSection) {
		console.log("postContainerSection");
	}
	that.postContainerSection = function(containerSection, _success, _error, _complete) {
		return that._ajax({
			type: "POST",
			url: _url + "/api/containerSections",
			contentType: "application/json",
			data: JSON.stringify(containerSection),
			success: function(_data, _textStatus, _jqXHR) {
				if (_success) {
					var _value = _jqXHR.getResponseHeader("Location");
					if (_value) {
						var _index = _value.lastIndexOf("/");
						var _suffix = _value.substring(_index + 1);
						var id = _suffix;
						_success(id, _textStatus, _jqXHR);
					}
				}
			},
			error: _error,
			complete: _complete
		});
	};

	if (that.getContainerSection) {
		console.log("getContainerSection");
	}
	that.getContainerSection = function(id, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/containerSections/" + id,
			data: {
				projection: projection
			},
			success: _success,
			error: _error,
			complete: _complete
		});
	};

	if (that.putContainerSection) {
		console.log("putContainerSection");
	}
	that.putContainerSection = function(containerSection, _success, _error, _complete) {
		return that._ajax({
			type: "PUT",
			url: _url + "/api/containerSections/" + containerSection.id,
			contentType: "application/json",
			data: JSON.stringify(containerSection),
			success: _success,
			error: _error,
			complete: _complete
		});
	};

	if (that.deleteContainerSection) {
		console.log("deleteContainerSection");
	}
	that.deleteContainerSection = function(id, _success, _error, _complete) {
		return that._ajax({
			type: "DELETE",
			url: _url + "/api/containerSections/" + id,
			success: _success,
			error: _error,
			complete: _complete
		});
	};


/*---------------------------------------------------------------------------*/

/*---------------------------------------------------------------------------*/
	if (that.getDocuments) {
		console.log("getDocuments");
	}
	that.getDocuments = function(_page, _size, _sort, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/documents",
			data: {
				page: _page,
				size: _size,
				sort: _sort,
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
	};

	if (that.postDocument) {
		console.log("postDocument");
	}
	that.postDocument = function(document, _success, _error, _complete) {
		return that._ajax({
			type: "POST",
			url: _url + "/api/documents",
			contentType: "application/json",
			data: JSON.stringify(document),
			success: function(_data, _textStatus, _jqXHR) {
				if (_success) {
					var _value = _jqXHR.getResponseHeader("Location");
					if (_value) {
						var _index = _value.lastIndexOf("/");
						var _suffix = _value.substring(_index + 1);
						var id = _suffix;
						_success(id, _textStatus, _jqXHR);
					}
				}
			},
			error: _error,
			complete: _complete
		});
	};

	if (that.getDocument) {
		console.log("getDocument");
	}
	that.getDocument = function(id, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/documents/" + id,
			data: {
				projection: projection
			},
			success: _success,
			error: _error,
			complete: _complete
		});
	};

	if (that.putDocument) {
		console.log("putDocument");
	}
	that.putDocument = function(document, _success, _error, _complete) {
		return that._ajax({
			type: "PUT",
			url: _url + "/api/documents/" + document.id,
			contentType: "application/json",
			data: JSON.stringify(document),
			success: _success,
			error: _error,
			complete: _complete
		});
	};

	if (that.deleteDocument) {
		console.log("deleteDocument");
	}
	that.deleteDocument = function(id, _success, _error, _complete) {
		return that._ajax({
			type: "DELETE",
			url: _url + "/api/documents/" + id,
			success: _success,
			error: _error,
			complete: _complete
		});
	};

	if (that.findDocumentsOrderByDate) {
		console.log("findDocumentsOrderByDate");
	}
	that.findDocumentsOrderByDate = function(page, size, sort, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/documents/search/findDocumentsOrderByDate",
			data: {
				page: page,
				size: size,
				sort: sort,

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
	};

/*---------------------------------------------------------------------------*/

/*---------------------------------------------------------------------------*/
	if (that.getImages) {
		console.log("getImages");
	}
	that.getImages = function(_page, _size, _sort, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/images",
			data: {
				page: _page,
				size: _size,
				sort: _sort,
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
	};

	if (that.postImage) {
		console.log("postImage");
	}
	that.postImage = function(image, _success, _error, _complete) {
		return that._ajax({
			type: "POST",
			url: _url + "/api/images",
			contentType: "application/json",
			data: JSON.stringify(image),
			success: function(_data, _textStatus, _jqXHR) {
				if (_success) {
					var _value = _jqXHR.getResponseHeader("Location");
					if (_value) {
						var _index = _value.lastIndexOf("/");
						var _suffix = _value.substring(_index + 1);
						var id = _suffix;
						_success(id, _textStatus, _jqXHR);
					}
				}
			},
			error: _error,
			complete: _complete
		});
	};

	if (that.getImage) {
		console.log("getImage");
	}
	that.getImage = function(id, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/images/" + id,
			data: {
				projection: projection
			},
			success: _success,
			error: _error,
			complete: _complete
		});
	};

	if (that.putImage) {
		console.log("putImage");
	}
	that.putImage = function(image, _success, _error, _complete) {
		return that._ajax({
			type: "PUT",
			url: _url + "/api/images/" + image.id,
			contentType: "application/json",
			data: JSON.stringify(image),
			success: _success,
			error: _error,
			complete: _complete
		});
	};

	if (that.deleteImage) {
		console.log("deleteImage");
	}
	that.deleteImage = function(id, _success, _error, _complete) {
		return that._ajax({
			type: "DELETE",
			url: _url + "/api/images/" + id,
			success: _success,
			error: _error,
			complete: _complete
		});
	};

	if (that.findImagesOrderByDate) {
		console.log("findImagesOrderByDate");
	}
	that.findImagesOrderByDate = function(page, size, sort, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/images/search/findImagesOrderByDate",
			data: {
				page: page,
				size: size,
				sort: sort,

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
	};

/*---------------------------------------------------------------------------*/

/*---------------------------------------------------------------------------*/
	if (that.getMenuEntries) {
		console.log("getMenuEntries");
	}
	that.getMenuEntries = function(_page, _size, _sort, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/menuEntries",
			data: {
				page: _page,
				size: _size,
				sort: _sort,
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
	};

	if (that.postMenuEntry) {
		console.log("postMenuEntry");
	}
	that.postMenuEntry = function(menuEntry, _success, _error, _complete) {
		return that._ajax({
			type: "POST",
			url: _url + "/api/menuEntries",
			contentType: "application/json",
			data: JSON.stringify(menuEntry),
			success: function(_data, _textStatus, _jqXHR) {
				if (_success) {
					var _value = _jqXHR.getResponseHeader("Location");
					if (_value) {
						var _index = _value.lastIndexOf("/");
						var _suffix = _value.substring(_index + 1);
						var id = _suffix;
						_success(id, _textStatus, _jqXHR);
					}
				}
			},
			error: _error,
			complete: _complete
		});
	};

	if (that.getMenuEntry) {
		console.log("getMenuEntry");
	}
	that.getMenuEntry = function(id, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/menuEntries/" + id,
			data: {
				projection: projection
			},
			success: _success,
			error: _error,
			complete: _complete
		});
	};

	if (that.putMenuEntry) {
		console.log("putMenuEntry");
	}
	that.putMenuEntry = function(menuEntry, _success, _error, _complete) {
		return that._ajax({
			type: "PUT",
			url: _url + "/api/menuEntries/" + menuEntry.id,
			contentType: "application/json",
			data: JSON.stringify(menuEntry),
			success: _success,
			error: _error,
			complete: _complete
		});
	};

	if (that.deleteMenuEntry) {
		console.log("deleteMenuEntry");
	}
	that.deleteMenuEntry = function(id, _success, _error, _complete) {
		return that._ajax({
			type: "DELETE",
			url: _url + "/api/menuEntries/" + id,
			success: _success,
			error: _error,
			complete: _complete
		});
	};


/*---------------------------------------------------------------------------*/

/*---------------------------------------------------------------------------*/
	if (that.getNetworks) {
		console.log("getNetworks");
	}
	that.getNetworks = function(_page, _size, _sort, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/networks",
			data: {
				page: _page,
				size: _size,
				sort: _sort,
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
	};

	if (that.postNetwork) {
		console.log("postNetwork");
	}
	that.postNetwork = function(network, _success, _error, _complete) {
		return that._ajax({
			type: "POST",
			url: _url + "/api/networks",
			contentType: "application/json",
			data: JSON.stringify(network),
			success: function(_data, _textStatus, _jqXHR) {
				if (_success) {
					var _value = _jqXHR.getResponseHeader("Location");
					if (_value) {
						var _index = _value.lastIndexOf("/");
						var _suffix = _value.substring(_index + 1);
						var id = _suffix;
						_success(id, _textStatus, _jqXHR);
					}
				}
			},
			error: _error,
			complete: _complete
		});
	};

	if (that.getNetwork) {
		console.log("getNetwork");
	}
	that.getNetwork = function(id, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/networks/" + id,
			data: {
				projection: projection
			},
			success: _success,
			error: _error,
			complete: _complete
		});
	};

	if (that.putNetwork) {
		console.log("putNetwork");
	}
	that.putNetwork = function(network, _success, _error, _complete) {
		return that._ajax({
			type: "PUT",
			url: _url + "/api/networks/" + network.id,
			contentType: "application/json",
			data: JSON.stringify(network),
			success: _success,
			error: _error,
			complete: _complete
		});
	};

	if (that.deleteNetwork) {
		console.log("deleteNetwork");
	}
	that.deleteNetwork = function(id, _success, _error, _complete) {
		return that._ajax({
			type: "DELETE",
			url: _url + "/api/networks/" + id,
			success: _success,
			error: _error,
			complete: _complete
		});
	};

	if (that.findByTenantId) {
		console.log("findByTenantId");
	}
	that.findByTenantId = function(tenantId, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/networks/search/findByTenantId",
			data: {
				tenantId: tenantId,

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
	};

	if (that.getNetworkFavicon) {
		console.log("getNetworkFavicon");
	}
	that.getNetworkFavicon = function(id, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/networks/" + id + "/favicon",
			data: {
				projection: projection
			},
			success: _success,
			error: _error,
			complete: _complete
		});
	};

	if (that.putNetworkFavicon) {
		console.log("putNetworkFavicon");
	}
	that.putNetworkFavicon = function(id, favicon, _success, _error, _complete) {
		if (favicon === null) {
			return that._ajax({
				type: "DELETE",
				url: _url + "/api/networks/" + id + "/favicon",
				success: _success,
				error: _error,
				complete: _complete
			});
		} else {
			return that._ajax({
				type: "PUT",
				url: _url + "/api/networks/" + id + "/favicon",
				contentType: "text/uri-list",
				data: favicon,
				success: _success,
				error: _error,
				complete: _complete
			});
		}
	};


	if (that.getNetworkFooter) {
		console.log("getNetworkFooter");
	}
	that.getNetworkFooter = function(id, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/networks/" + id + "/footer",
			data: {
				projection: projection
			},
			success: _success,
			error: _error,
			complete: _complete
		});
	};

	if (that.putNetworkFooter) {
		console.log("putNetworkFooter");
	}
	that.putNetworkFooter = function(id, footer, _success, _error, _complete) {
		if (footer === null) {
			return that._ajax({
				type: "DELETE",
				url: _url + "/api/networks/" + id + "/footer",
				success: _success,
				error: _error,
				complete: _complete
			});
		} else {
			return that._ajax({
				type: "PUT",
				url: _url + "/api/networks/" + id + "/footer",
				contentType: "text/uri-list",
				data: footer,
				success: _success,
				error: _error,
				complete: _complete
			});
		}
	};


	if (that.getNetworkHeader) {
		console.log("getNetworkHeader");
	}
	that.getNetworkHeader = function(id, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/networks/" + id + "/header",
			data: {
				projection: projection
			},
			success: _success,
			error: _error,
			complete: _complete
		});
	};

	if (that.putNetworkHeader) {
		console.log("putNetworkHeader");
	}
	that.putNetworkHeader = function(id, header, _success, _error, _complete) {
		if (header === null) {
			return that._ajax({
				type: "DELETE",
				url: _url + "/api/networks/" + id + "/header",
				success: _success,
				error: _error,
				complete: _complete
			});
		} else {
			return that._ajax({
				type: "PUT",
				url: _url + "/api/networks/" + id + "/header",
				contentType: "text/uri-list",
				data: header,
				success: _success,
				error: _error,
				complete: _complete
			});
		}
	};


	if (that.getNetworkLoginImage) {
		console.log("getNetworkLoginImage");
	}
	that.getNetworkLoginImage = function(id, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/networks/" + id + "/loginImage",
			data: {
				projection: projection
			},
			success: _success,
			error: _error,
			complete: _complete
		});
	};

	if (that.putNetworkLoginImage) {
		console.log("putNetworkLoginImage");
	}
	that.putNetworkLoginImage = function(id, loginImage, _success, _error, _complete) {
		if (loginImage === null) {
			return that._ajax({
				type: "DELETE",
				url: _url + "/api/networks/" + id + "/loginImage",
				success: _success,
				error: _error,
				complete: _complete
			});
		} else {
			return that._ajax({
				type: "PUT",
				url: _url + "/api/networks/" + id + "/loginImage",
				contentType: "text/uri-list",
				data: loginImage,
				success: _success,
				error: _error,
				complete: _complete
			});
		}
	};


	if (that.getNetworkLogoImage) {
		console.log("getNetworkLogoImage");
	}
	that.getNetworkLogoImage = function(id, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/networks/" + id + "/logoImage",
			data: {
				projection: projection
			},
			success: _success,
			error: _error,
			complete: _complete
		});
	};

	if (that.putNetworkLogoImage) {
		console.log("putNetworkLogoImage");
	}
	that.putNetworkLogoImage = function(id, logoImage, _success, _error, _complete) {
		if (logoImage === null) {
			return that._ajax({
				type: "DELETE",
				url: _url + "/api/networks/" + id + "/logoImage",
				success: _success,
				error: _error,
				complete: _complete
			});
		} else {
			return that._ajax({
				type: "PUT",
				url: _url + "/api/networks/" + id + "/logoImage",
				contentType: "text/uri-list",
				data: logoImage,
				success: _success,
				error: _error,
				complete: _complete
			});
		}
	};


	if (that.getNetworkSidemenu) {
		console.log("getNetworkSidemenu");
	}
	that.getNetworkSidemenu = function(id, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/networks/" + id + "/sidemenu",
			data: {
				projection: projection
			},
			success: _success,
			error: _error,
			complete: _complete
		});
	};

	if (that.putNetworkSidemenu) {
		console.log("putNetworkSidemenu");
	}
	that.putNetworkSidemenu = function(id, sidemenu, _success, _error, _complete) {
		if (sidemenu === null) {
			return that._ajax({
				type: "DELETE",
				url: _url + "/api/networks/" + id + "/sidemenu",
				success: _success,
				error: _error,
				complete: _complete
			});
		} else {
			return that._ajax({
				type: "PUT",
				url: _url + "/api/networks/" + id + "/sidemenu",
				contentType: "text/uri-list",
				data: sidemenu,
				success: _success,
				error: _error,
				complete: _complete
			});
		}
	};


	if (that.getNetworkSplashImage) {
		console.log("getNetworkSplashImage");
	}
	that.getNetworkSplashImage = function(id, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/networks/" + id + "/splashImage",
			data: {
				projection: projection
			},
			success: _success,
			error: _error,
			complete: _complete
		});
	};

	if (that.putNetworkSplashImage) {
		console.log("putNetworkSplashImage");
	}
	that.putNetworkSplashImage = function(id, splashImage, _success, _error, _complete) {
		if (splashImage === null) {
			return that._ajax({
				type: "DELETE",
				url: _url + "/api/networks/" + id + "/splashImage",
				success: _success,
				error: _error,
				complete: _complete
			});
		} else {
			return that._ajax({
				type: "PUT",
				url: _url + "/api/networks/" + id + "/splashImage",
				contentType: "text/uri-list",
				data: splashImage,
				success: _success,
				error: _error,
				complete: _complete
			});
		}
	};
/*---------------------------------------------------------------------------*/

/*---------------------------------------------------------------------------*/
	if (that.getNetworkCreates) {
		console.log("getNetworkCreates");
	}
	that.getNetworkCreates = function(_page, _size, _sort, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/networkCreates",
			data: {
				page: _page,
				size: _size,
				sort: _sort,
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
	};

	if (that.postNetworkCreate) {
		console.log("postNetworkCreate");
	}
	that.postNetworkCreate = function(networkCreate, _success, _error, _complete) {
		return that._ajax({
			type: "POST",
			url: _url + "/api/networkCreates",
			contentType: "application/json",
			data: JSON.stringify(networkCreate),
			success: function(_data, _textStatus, _jqXHR) {
				if (_success) {
					var _value = _jqXHR.getResponseHeader("Location");
					if (_value) {
						var _index = _value.lastIndexOf("/");
						var _suffix = _value.substring(_index + 1);
						var id = _suffix;
						_success(id, _textStatus, _jqXHR);
					}
				}
			},
			error: _error,
			complete: _complete
		});
	};

	if (that.getNetworkCreate) {
		console.log("getNetworkCreate");
	}
	that.getNetworkCreate = function(id, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/networkCreates/" + id,
			data: {
				projection: projection
			},
			success: _success,
			error: _error,
			complete: _complete
		});
	};

	if (that.putNetworkCreate) {
		console.log("putNetworkCreate");
	}
	that.putNetworkCreate = function(networkCreate, _success, _error, _complete) {
		return that._ajax({
			type: "PUT",
			url: _url + "/api/networkCreates/" + networkCreate.id,
			contentType: "application/json",
			data: JSON.stringify(networkCreate),
			success: _success,
			error: _error,
			complete: _complete
		});
	};

	if (that.deleteNetworkCreate) {
		console.log("deleteNetworkCreate");
	}
	that.deleteNetworkCreate = function(id, _success, _error, _complete) {
		return that._ajax({
			type: "DELETE",
			url: _url + "/api/networkCreates/" + id,
			success: _success,
			error: _error,
			complete: _complete
		});
	};


/*---------------------------------------------------------------------------*/

/*---------------------------------------------------------------------------*/
	if (that.getPages) {
		console.log("getPages");
	}
	that.getPages = function(_page, _size, _sort, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/pages",
			data: {
				page: _page,
				size: _size,
				sort: _sort,
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
	};

	if (that.postPage) {
		console.log("postPage");
	}
	that.postPage = function(page, _success, _error, _complete) {
		return that._ajax({
			type: "POST",
			url: _url + "/api/pages",
			contentType: "application/json",
			data: JSON.stringify(page),
			success: function(_data, _textStatus, _jqXHR) {
				if (_success) {
					var _value = _jqXHR.getResponseHeader("Location");
					if (_value) {
						var _index = _value.lastIndexOf("/");
						var _suffix = _value.substring(_index + 1);
						var id = _suffix;
						_success(id, _textStatus, _jqXHR);
					}
				}
			},
			error: _error,
			complete: _complete
		});
	};

	if (that.getPage) {
		console.log("getPage");
	}
	that.getPage = function(id, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/pages/" + id,
			data: {
				projection: projection
			},
			success: _success,
			error: _error,
			complete: _complete
		});
	};

	if (that.putPage) {
		console.log("putPage");
	}
	that.putPage = function(page, _success, _error, _complete) {
		return that._ajax({
			type: "PUT",
			url: _url + "/api/pages/" + page.id,
			contentType: "application/json",
			data: JSON.stringify(page),
			success: _success,
			error: _error,
			complete: _complete
		});
	};

	if (that.deletePage) {
		console.log("deletePage");
	}
	that.deletePage = function(id, _success, _error, _complete) {
		return that._ajax({
			type: "DELETE",
			url: _url + "/api/pages/" + id,
			success: _success,
			error: _error,
			complete: _complete
		});
	};

	if (that.findOne) {
		console.log("findOne");
	}
	that.findOne = function(pageId, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/pages/search/findOne",
			data: {
				pageId: pageId,

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
	};

	if (that.getPageStation) {
		console.log("getPageStation");
	}
	that.getPageStation = function(id, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/pages/" + id + "/station",
			data: {
				projection: projection
			},
			success: _success,
			error: _error,
			complete: _complete
		});
	};

	if (that.putPageStation) {
		console.log("putPageStation");
	}
	that.putPageStation = function(id, station, _success, _error, _complete) {
		if (station === null) {
			return that._ajax({
				type: "DELETE",
				url: _url + "/api/pages/" + id + "/station",
				success: _success,
				error: _error,
				complete: _complete
			});
		} else {
			return that._ajax({
				type: "PUT",
				url: _url + "/api/pages/" + id + "/station",
				contentType: "text/uri-list",
				data: station,
				success: _success,
				error: _error,
				complete: _complete
			});
		}
	};
/*---------------------------------------------------------------------------*/

/*---------------------------------------------------------------------------*/
	if (that.getPasswordResets) {
		console.log("getPasswordResets");
	}
	that.getPasswordResets = function(_page, _size, _sort, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/passwordResets",
			data: {
				page: _page,
				size: _size,
				sort: _sort,
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
	};

	if (that.postPasswordReset) {
		console.log("postPasswordReset");
	}
	that.postPasswordReset = function(passwordReset, _success, _error, _complete) {
		return that._ajax({
			type: "POST",
			url: _url + "/api/passwordResets",
			contentType: "application/json",
			data: JSON.stringify(passwordReset),
			success: function(_data, _textStatus, _jqXHR) {
				if (_success) {
					var _value = _jqXHR.getResponseHeader("Location");
					if (_value) {
						var _index = _value.lastIndexOf("/");
						var _suffix = _value.substring(_index + 1);
						var id = _suffix;
						_success(id, _textStatus, _jqXHR);
					}
				}
			},
			error: _error,
			complete: _complete
		});
	};

	if (that.getPasswordReset) {
		console.log("getPasswordReset");
	}
	that.getPasswordReset = function(id, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/passwordResets/" + id,
			data: {
				projection: projection
			},
			success: _success,
			error: _error,
			complete: _complete
		});
	};

	if (that.putPasswordReset) {
		console.log("putPasswordReset");
	}
	that.putPasswordReset = function(passwordReset, _success, _error, _complete) {
		return that._ajax({
			type: "PUT",
			url: _url + "/api/passwordResets/" + passwordReset.id,
			contentType: "application/json",
			data: JSON.stringify(passwordReset),
			success: _success,
			error: _error,
			complete: _complete
		});
	};

	if (that.deletePasswordReset) {
		console.log("deletePasswordReset");
	}
	that.deletePasswordReset = function(id, _success, _error, _complete) {
		return that._ajax({
			type: "DELETE",
			url: _url + "/api/passwordResets/" + id,
			success: _success,
			error: _error,
			complete: _complete
		});
	};

	if (that.findByHash) {
		console.log("findByHash");
	}
	that.findByHash = function(hash, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/passwordResets/search/findByHash",
			data: {
				hash: hash,

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
	};

/*---------------------------------------------------------------------------*/

/*---------------------------------------------------------------------------*/
	if (that.getPersons) {
		console.log("getPersons");
	}
	that.getPersons = function(_page, _size, _sort, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/persons",
			data: {
				page: _page,
				size: _size,
				sort: _sort,
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
	};

	if (that.postPerson) {
		console.log("postPerson");
	}
	that.postPerson = function(person, _success, _error, _complete) {
		return that._ajax({
			type: "POST",
			url: _url + "/api/persons",
			contentType: "application/json",
			data: JSON.stringify(person),
			success: function(_data, _textStatus, _jqXHR) {
				if (_success) {
					var _value = _jqXHR.getResponseHeader("Location");
					if (_value) {
						var _index = _value.lastIndexOf("/");
						var _suffix = _value.substring(_index + 1);
						var id = _suffix;
						_success(id, _textStatus, _jqXHR);
					}
				}
			},
			error: _error,
			complete: _complete
		});
	};

	if (that.getPerson) {
		console.log("getPerson");
	}
	that.getPerson = function(id, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/persons/" + id,
			data: {
				projection: projection
			},
			success: _success,
			error: _error,
			complete: _complete
		});
	};

	if (that.putPerson) {
		console.log("putPerson");
	}
	that.putPerson = function(person, _success, _error, _complete) {
		return that._ajax({
			type: "PUT",
			url: _url + "/api/persons/" + person.id,
			contentType: "application/json",
			data: JSON.stringify(person),
			success: _success,
			error: _error,
			complete: _complete
		});
	};

	if (that.deletePerson) {
		console.log("deletePerson");
	}
	that.deletePerson = function(id, _success, _error, _complete) {
		return that._ajax({
			type: "DELETE",
			url: _url + "/api/persons/" + id,
			success: _success,
			error: _error,
			complete: _complete
		});
	};

	if (that.findByUsernameAndTenantId) {
		console.log("findByUsernameAndTenantId");
	}
	that.findByUsernameAndTenantId = function(tenantId, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/persons/search/findByUsernameAndTenantId",
			data: {
				tenantId: tenantId,

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
	};

	if (that.findByUsernamesAndRoles) {
		console.log("findByUsernamesAndRoles");
	}
	that.findByUsernamesAndRoles = function(roles, tenantId, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/persons/search/findByUsernamesAndRoles",
			data: {
				roles: roles,
				tenantId: tenantId,

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
	};

	if (that.findPersons) {
		console.log("findPersons");
	}
	that.findPersons = function(usernameOrEmailOrName, page, size, sort, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/persons/search/findPersons",
			data: {
				usernameOrEmailOrName: usernameOrEmailOrName,
				page: page,
				size: size,
				sort: sort,

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
	};

	if (that.findByUsername) {
		console.log("findByUsername");
	}
	that.findByUsername = function(username, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/persons/search/findByUsername",
			data: {
				username: username,

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
	};

	if (that.getPersonBookmarkPosts) {
		console.log("getPersonBookmarkPosts");
	}
	that.getPersonBookmarkPosts = function(id, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/persons/" + id + "/bookmarkPosts",
			data: {
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
	};
	if (that.patchPersonBookmarkPosts) {
		console.log("patchPersonBookmarkPosts");
	}
	that.patchPersonBookmarkPosts = function(id, bookmarkPosts, _success, _error, _complete) {		
		var _data = "";
		for (var i = 0; i < bookmarkPosts.length; ++i) {
			_data += bookmarkPosts[i] + "\n";
		}
		return that._ajax({
			type: "PATCH",
			url: _url + "/api/persons/" + id + "/bookmarkPosts",
			contentType: "text/uri-list",
			data: _data,
			success: _success,
			error: _error,
			complete: _complete
		});
	};

	if (that.putPersonBookmarkPosts) {
		console.log("putPersonBookmarkPosts");
	}
	that.putPersonBookmarkPosts = function(id, bookmarkPosts, _success, _error, _complete) {		
		var _data = "";
		for (var i = 0; i < bookmarkPosts.length; ++i) {
			_data += bookmarkPosts[i] + "\n";
		}
		return that._ajax({
			type: "PUT",
			url: _url + "/api/persons/" + id + "/bookmarkPosts",
			contentType: "text/uri-list",
			data: _data,
			success: _success,
			error: _error,
			complete: _complete
		});
	};


	if (that.getPersonCover) {
		console.log("getPersonCover");
	}
	that.getPersonCover = function(id, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/persons/" + id + "/cover",
			data: {
				projection: projection
			},
			success: _success,
			error: _error,
			complete: _complete
		});
	};

	if (that.putPersonCover) {
		console.log("putPersonCover");
	}
	that.putPersonCover = function(id, cover, _success, _error, _complete) {
		if (cover === null) {
			return that._ajax({
				type: "DELETE",
				url: _url + "/api/persons/" + id + "/cover",
				success: _success,
				error: _error,
				complete: _complete
			});
		} else {
			return that._ajax({
				type: "PUT",
				url: _url + "/api/persons/" + id + "/cover",
				contentType: "text/uri-list",
				data: cover,
				success: _success,
				error: _error,
				complete: _complete
			});
		}
	};


	if (that.getPersonImage) {
		console.log("getPersonImage");
	}
	that.getPersonImage = function(id, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/persons/" + id + "/image",
			data: {
				projection: projection
			},
			success: _success,
			error: _error,
			complete: _complete
		});
	};

	if (that.putPersonImage) {
		console.log("putPersonImage");
	}
	that.putPersonImage = function(id, image, _success, _error, _complete) {
		if (image === null) {
			return that._ajax({
				type: "DELETE",
				url: _url + "/api/persons/" + id + "/image",
				success: _success,
				error: _error,
				complete: _complete
			});
		} else {
			return that._ajax({
				type: "PUT",
				url: _url + "/api/persons/" + id + "/image",
				contentType: "text/uri-list",
				data: image,
				success: _success,
				error: _error,
				complete: _complete
			});
		}
	};


	if (that.getPersonRecommendPosts) {
		console.log("getPersonRecommendPosts");
	}
	that.getPersonRecommendPosts = function(id, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/persons/" + id + "/recommendPosts",
			data: {
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
	};
	if (that.patchPersonRecommendPosts) {
		console.log("patchPersonRecommendPosts");
	}
	that.patchPersonRecommendPosts = function(id, recommendPosts, _success, _error, _complete) {		
		var _data = "";
		for (var i = 0; i < recommendPosts.length; ++i) {
			_data += recommendPosts[i] + "\n";
		}
		return that._ajax({
			type: "PATCH",
			url: _url + "/api/persons/" + id + "/recommendPosts",
			contentType: "text/uri-list",
			data: _data,
			success: _success,
			error: _error,
			complete: _complete
		});
	};

	if (that.putPersonRecommendPosts) {
		console.log("putPersonRecommendPosts");
	}
	that.putPersonRecommendPosts = function(id, recommendPosts, _success, _error, _complete) {		
		var _data = "";
		for (var i = 0; i < recommendPosts.length; ++i) {
			_data += recommendPosts[i] + "\n";
		}
		return that._ajax({
			type: "PUT",
			url: _url + "/api/persons/" + id + "/recommendPosts",
			contentType: "text/uri-list",
			data: _data,
			success: _success,
			error: _error,
			complete: _complete
		});
	};


	if (that.getPersonUser) {
		console.log("getPersonUser");
	}
	that.getPersonUser = function(id, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/persons/" + id + "/user",
			data: {
				projection: projection
			},
			success: _success,
			error: _error,
			complete: _complete
		});
	};

	if (that.putPersonUser) {
		console.log("putPersonUser");
	}
	that.putPersonUser = function(id, user, _success, _error, _complete) {
		if (user === null) {
			return that._ajax({
				type: "DELETE",
				url: _url + "/api/persons/" + id + "/user",
				success: _success,
				error: _error,
				complete: _complete
			});
		} else {
			return that._ajax({
				type: "PUT",
				url: _url + "/api/persons/" + id + "/user",
				contentType: "text/uri-list",
				data: user,
				success: _success,
				error: _error,
				complete: _complete
			});
		}
	};
/*---------------------------------------------------------------------------*/

/*---------------------------------------------------------------------------*/
	if (that.getPosts) {
		console.log("getPosts");
	}
	that.getPosts = function(_page, _size, _sort, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/posts",
			data: {
				page: _page,
				size: _size,
				sort: _sort,
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
	};

	if (that.postPost) {
		console.log("postPost");
	}
	that.postPost = function(post, _success, _error, _complete) {
		return that._ajax({
			type: "POST",
			url: _url + "/api/posts",
			contentType: "application/json",
			data: JSON.stringify(post),
			success: function(_data, _textStatus, _jqXHR) {
				if (_success) {
					var _value = _jqXHR.getResponseHeader("Location");
					if (_value) {
						var _index = _value.lastIndexOf("/");
						var _suffix = _value.substring(_index + 1);
						var id = _suffix;
						_success(id, _textStatus, _jqXHR);
					}
				}
			},
			error: _error,
			complete: _complete
		});
	};

	if (that.getPost) {
		console.log("getPost");
	}
	that.getPost = function(id, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/posts/" + id,
			data: {
				projection: projection
			},
			success: _success,
			error: _error,
			complete: _complete
		});
	};

	if (that.putPost) {
		console.log("putPost");
	}
	that.putPost = function(post, _success, _error, _complete) {
		return that._ajax({
			type: "PUT",
			url: _url + "/api/posts/" + post.id,
			contentType: "application/json",
			data: JSON.stringify(post),
			success: _success,
			error: _error,
			complete: _complete
		});
	};

	if (that.deletePost) {
		console.log("deletePost");
	}
	that.deletePost = function(id, _success, _error, _complete) {
		return that._ajax({
			type: "DELETE",
			url: _url + "/api/posts/" + id,
			success: _success,
			error: _error,
			complete: _complete
		});
	};

	if (that.updateState) {
		console.log("updateState");
	}
	that.updateState = function(postId, state, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/posts/search/updateState",
			data: {
				postId: postId,
				state: state,

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
	};

	if (that.findBySlug) {
		console.log("findBySlug");
	}
	that.findBySlug = function(slug, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/posts/search/findBySlug",
			data: {
				slug: slug,

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
	};

	if (that.findPostBySlug) {
		console.log("findPostBySlug");
	}
	that.findPostBySlug = function(slug, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/posts/search/findPostBySlug",
			data: {
				slug: slug,

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
	};

	if (that.updatePostAuthor) {
		console.log("updatePostAuthor");
	}
	that.updatePostAuthor = function(newAuthorId, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/posts/search/updatePostAuthor",
			data: {
				newAuthorId: newAuthorId,

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
	};

	if (that.findStateById) {
		console.log("findStateById");
	}
	that.findStateById = function(id, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/posts/search/findStateById",
			data: {
				id: id,

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
	};

	if (that.findPostsPublished) {
		console.log("findPostsPublished");
	}
	that.findPostsPublished = function(termsIds, stationId, page, size, sort, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/posts/search/findPostsPublished",
			data: {
				termsIds: termsIds,
				stationId: stationId,
				page: page,
				size: size,
				sort: sort,

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
	};

	if (that.findPostsOrderByDateDesc) {
		console.log("findPostsOrderByDateDesc");
	}
	that.findPostsOrderByDateDesc = function(stationId, page, size, sort, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/posts/search/findPostsOrderByDateDesc",
			data: {
				stationId: stationId,
				page: page,
				size: size,
				sort: sort,

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
	};

	if (that.findByAuthorUsernameAndStateOrderByDateDesc) {
		console.log("findByAuthorUsernameAndStateOrderByDateDesc");
	}
	that.findByAuthorUsernameAndStateOrderByDateDesc = function(state, page, size, sort, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/posts/search/findByAuthorUsernameAndStateOrderByDateDesc",
			data: {
				state: state,
				page: page,
				size: size,
				sort: sort,

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
	};

	if (that.getPostAuthor) {
		console.log("getPostAuthor");
	}
	that.getPostAuthor = function(id, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/posts/" + id + "/author",
			data: {
				projection: projection
			},
			success: _success,
			error: _error,
			complete: _complete
		});
	};

	if (that.putPostAuthor) {
		console.log("putPostAuthor");
	}
	that.putPostAuthor = function(id, author, _success, _error, _complete) {
		if (author === null) {
			return that._ajax({
				type: "DELETE",
				url: _url + "/api/posts/" + id + "/author",
				success: _success,
				error: _error,
				complete: _complete
			});
		} else {
			return that._ajax({
				type: "PUT",
				url: _url + "/api/posts/" + id + "/author",
				contentType: "text/uri-list",
				data: author,
				success: _success,
				error: _error,
				complete: _complete
			});
		}
	};


	if (that.getPostComments) {
		console.log("getPostComments");
	}
	that.getPostComments = function(id, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/posts/" + id + "/comments",
			data: {
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
	};


	if (that.getPostFeaturedAudio) {
		console.log("getPostFeaturedAudio");
	}
	that.getPostFeaturedAudio = function(id, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/posts/" + id + "/featuredAudio",
			data: {
				projection: projection
			},
			success: _success,
			error: _error,
			complete: _complete
		});
	};

	if (that.putPostFeaturedAudio) {
		console.log("putPostFeaturedAudio");
	}
	that.putPostFeaturedAudio = function(id, featuredAudio, _success, _error, _complete) {
		if (featuredAudio === null) {
			return that._ajax({
				type: "DELETE",
				url: _url + "/api/posts/" + id + "/featuredAudio",
				success: _success,
				error: _error,
				complete: _complete
			});
		} else {
			return that._ajax({
				type: "PUT",
				url: _url + "/api/posts/" + id + "/featuredAudio",
				contentType: "text/uri-list",
				data: featuredAudio,
				success: _success,
				error: _error,
				complete: _complete
			});
		}
	};


	if (that.getPostFeaturedGallery) {
		console.log("getPostFeaturedGallery");
	}
	that.getPostFeaturedGallery = function(id, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/posts/" + id + "/featuredGallery",
			data: {
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
	};
	if (that.patchPostFeaturedGallery) {
		console.log("patchPostFeaturedGallery");
	}
	that.patchPostFeaturedGallery = function(id, featuredGallery, _success, _error, _complete) {		
		var _data = "";
		for (var i = 0; i < featuredGallery.length; ++i) {
			_data += featuredGallery[i] + "\n";
		}
		return that._ajax({
			type: "PATCH",
			url: _url + "/api/posts/" + id + "/featuredGallery",
			contentType: "text/uri-list",
			data: _data,
			success: _success,
			error: _error,
			complete: _complete
		});
	};

	if (that.putPostFeaturedGallery) {
		console.log("putPostFeaturedGallery");
	}
	that.putPostFeaturedGallery = function(id, featuredGallery, _success, _error, _complete) {		
		var _data = "";
		for (var i = 0; i < featuredGallery.length; ++i) {
			_data += featuredGallery[i] + "\n";
		}
		return that._ajax({
			type: "PUT",
			url: _url + "/api/posts/" + id + "/featuredGallery",
			contentType: "text/uri-list",
			data: _data,
			success: _success,
			error: _error,
			complete: _complete
		});
	};


	if (that.getPostFeaturedImage) {
		console.log("getPostFeaturedImage");
	}
	that.getPostFeaturedImage = function(id, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/posts/" + id + "/featuredImage",
			data: {
				projection: projection
			},
			success: _success,
			error: _error,
			complete: _complete
		});
	};

	if (that.putPostFeaturedImage) {
		console.log("putPostFeaturedImage");
	}
	that.putPostFeaturedImage = function(id, featuredImage, _success, _error, _complete) {
		if (featuredImage === null) {
			return that._ajax({
				type: "DELETE",
				url: _url + "/api/posts/" + id + "/featuredImage",
				success: _success,
				error: _error,
				complete: _complete
			});
		} else {
			return that._ajax({
				type: "PUT",
				url: _url + "/api/posts/" + id + "/featuredImage",
				contentType: "text/uri-list",
				data: featuredImage,
				success: _success,
				error: _error,
				complete: _complete
			});
		}
	};


	if (that.getPostFeaturedVideo) {
		console.log("getPostFeaturedVideo");
	}
	that.getPostFeaturedVideo = function(id, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/posts/" + id + "/featuredVideo",
			data: {
				projection: projection
			},
			success: _success,
			error: _error,
			complete: _complete
		});
	};

	if (that.putPostFeaturedVideo) {
		console.log("putPostFeaturedVideo");
	}
	that.putPostFeaturedVideo = function(id, featuredVideo, _success, _error, _complete) {
		if (featuredVideo === null) {
			return that._ajax({
				type: "DELETE",
				url: _url + "/api/posts/" + id + "/featuredVideo",
				success: _success,
				error: _error,
				complete: _complete
			});
		} else {
			return that._ajax({
				type: "PUT",
				url: _url + "/api/posts/" + id + "/featuredVideo",
				contentType: "text/uri-list",
				data: featuredVideo,
				success: _success,
				error: _error,
				complete: _complete
			});
		}
	};


	if (that.getPostGaleryHashes) {
		console.log("getPostGaleryHashes");
	}
	that.getPostGaleryHashes = function(id, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/posts/" + id + "/galeryHashes",
			data: {
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
	};
	if (that.patchPostGaleryHashes) {
		console.log("patchPostGaleryHashes");
	}
	that.patchPostGaleryHashes = function(id, galeryHashes, _success, _error, _complete) {		
		var _data = "";
		for (var i = 0; i < galeryHashes.length; ++i) {
			_data += galeryHashes[i] + "\n";
		}
		return that._ajax({
			type: "PATCH",
			url: _url + "/api/posts/" + id + "/galeryHashes",
			contentType: "text/uri-list",
			data: _data,
			success: _success,
			error: _error,
			complete: _complete
		});
	};

	if (that.putPostGaleryHashes) {
		console.log("putPostGaleryHashes");
	}
	that.putPostGaleryHashes = function(id, galeryHashes, _success, _error, _complete) {		
		var _data = "";
		for (var i = 0; i < galeryHashes.length; ++i) {
			_data += galeryHashes[i] + "\n";
		}
		return that._ajax({
			type: "PUT",
			url: _url + "/api/posts/" + id + "/galeryHashes",
			contentType: "text/uri-list",
			data: _data,
			success: _success,
			error: _error,
			complete: _complete
		});
	};


	if (that.getPostStation) {
		console.log("getPostStation");
	}
	that.getPostStation = function(id, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/posts/" + id + "/station",
			data: {
				projection: projection
			},
			success: _success,
			error: _error,
			complete: _complete
		});
	};

	if (that.putPostStation) {
		console.log("putPostStation");
	}
	that.putPostStation = function(id, station, _success, _error, _complete) {
		if (station === null) {
			return that._ajax({
				type: "DELETE",
				url: _url + "/api/posts/" + id + "/station",
				success: _success,
				error: _error,
				complete: _complete
			});
		} else {
			return that._ajax({
				type: "PUT",
				url: _url + "/api/posts/" + id + "/station",
				contentType: "text/uri-list",
				data: station,
				success: _success,
				error: _error,
				complete: _complete
			});
		}
	};


	if (that.getPostTags) {
		console.log("getPostTags");
	}
	that.getPostTags = function(id, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/posts/" + id + "/tags",
			data: {
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
	};
	if (that.patchPostTags) {
		console.log("patchPostTags");
	}
	that.patchPostTags = function(id, tags, _success, _error, _complete) {		
		var _data = "";
		for (var i = 0; i < tags.length; ++i) {
			_data += tags[i] + "\n";
		}
		return that._ajax({
			type: "PATCH",
			url: _url + "/api/posts/" + id + "/tags",
			contentType: "text/uri-list",
			data: _data,
			success: _success,
			error: _error,
			complete: _complete
		});
	};

	if (that.putPostTags) {
		console.log("putPostTags");
	}
	that.putPostTags = function(id, tags, _success, _error, _complete) {		
		var _data = "";
		for (var i = 0; i < tags.length; ++i) {
			_data += tags[i] + "\n";
		}
		return that._ajax({
			type: "PUT",
			url: _url + "/api/posts/" + id + "/tags",
			contentType: "text/uri-list",
			data: _data,
			success: _success,
			error: _error,
			complete: _complete
		});
	};


	if (that.getPostTerms) {
		console.log("getPostTerms");
	}
	that.getPostTerms = function(id, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/posts/" + id + "/terms",
			data: {
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
	};
	if (that.patchPostTerms) {
		console.log("patchPostTerms");
	}
	that.patchPostTerms = function(id, terms, _success, _error, _complete) {		
		var _data = "";
		for (var i = 0; i < terms.length; ++i) {
			_data += terms[i] + "\n";
		}
		return that._ajax({
			type: "PATCH",
			url: _url + "/api/posts/" + id + "/terms",
			contentType: "text/uri-list",
			data: _data,
			success: _success,
			error: _error,
			complete: _complete
		});
	};

	if (that.putPostTerms) {
		console.log("putPostTerms");
	}
	that.putPostTerms = function(id, terms, _success, _error, _complete) {		
		var _data = "";
		for (var i = 0; i < terms.length; ++i) {
			_data += terms[i] + "\n";
		}
		return that._ajax({
			type: "PUT",
			url: _url + "/api/posts/" + id + "/terms",
			contentType: "text/uri-list",
			data: _data,
			success: _success,
			error: _error,
			complete: _complete
		});
	};
/*---------------------------------------------------------------------------*/

/*---------------------------------------------------------------------------*/
	if (that.getRows) {
		console.log("getRows");
	}
	that.getRows = function(_page, _size, _sort, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/rows",
			data: {
				page: _page,
				size: _size,
				sort: _sort,
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
	};

	if (that.postRow) {
		console.log("postRow");
	}
	that.postRow = function(row, _success, _error, _complete) {
		return that._ajax({
			type: "POST",
			url: _url + "/api/rows",
			contentType: "application/json",
			data: JSON.stringify(row),
			success: function(_data, _textStatus, _jqXHR) {
				if (_success) {
					var _value = _jqXHR.getResponseHeader("Location");
					if (_value) {
						var _index = _value.lastIndexOf("/");
						var _suffix = _value.substring(_index + 1);
						var id = _suffix;
						_success(id, _textStatus, _jqXHR);
					}
				}
			},
			error: _error,
			complete: _complete
		});
	};

	if (that.getRow) {
		console.log("getRow");
	}
	that.getRow = function(id, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/rows/" + id,
			data: {
				projection: projection
			},
			success: _success,
			error: _error,
			complete: _complete
		});
	};

	if (that.putRow) {
		console.log("putRow");
	}
	that.putRow = function(row, _success, _error, _complete) {
		return that._ajax({
			type: "PUT",
			url: _url + "/api/rows/" + row.id,
			contentType: "application/json",
			data: JSON.stringify(row),
			success: _success,
			error: _error,
			complete: _complete
		});
	};

	if (that.deleteRow) {
		console.log("deleteRow");
	}
	that.deleteRow = function(id, _success, _error, _complete) {
		return that._ajax({
			type: "DELETE",
			url: _url + "/api/rows/" + id,
			success: _success,
			error: _error,
			complete: _complete
		});
	};


	if (that.getRowCells) {
		console.log("getRowCells");
	}
	that.getRowCells = function(id, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/rows/" + id + "/cells",
			data: {
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
	};


	if (that.getRowFeaturingPerspective) {
		console.log("getRowFeaturingPerspective");
	}
	that.getRowFeaturingPerspective = function(id, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/rows/" + id + "/featuringPerspective",
			data: {
				projection: projection
			},
			success: _success,
			error: _error,
			complete: _complete
		});
	};

	if (that.putRowFeaturingPerspective) {
		console.log("putRowFeaturingPerspective");
	}
	that.putRowFeaturingPerspective = function(id, featuringPerspective, _success, _error, _complete) {
		if (featuringPerspective === null) {
			return that._ajax({
				type: "DELETE",
				url: _url + "/api/rows/" + id + "/featuringPerspective",
				success: _success,
				error: _error,
				complete: _complete
			});
		} else {
			return that._ajax({
				type: "PUT",
				url: _url + "/api/rows/" + id + "/featuringPerspective",
				contentType: "text/uri-list",
				data: featuringPerspective,
				success: _success,
				error: _error,
				complete: _complete
			});
		}
	};


	if (that.getRowHomePerspective) {
		console.log("getRowHomePerspective");
	}
	that.getRowHomePerspective = function(id, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/rows/" + id + "/homePerspective",
			data: {
				projection: projection
			},
			success: _success,
			error: _error,
			complete: _complete
		});
	};

	if (that.putRowHomePerspective) {
		console.log("putRowHomePerspective");
	}
	that.putRowHomePerspective = function(id, homePerspective, _success, _error, _complete) {
		if (homePerspective === null) {
			return that._ajax({
				type: "DELETE",
				url: _url + "/api/rows/" + id + "/homePerspective",
				success: _success,
				error: _error,
				complete: _complete
			});
		} else {
			return that._ajax({
				type: "PUT",
				url: _url + "/api/rows/" + id + "/homePerspective",
				contentType: "text/uri-list",
				data: homePerspective,
				success: _success,
				error: _error,
				complete: _complete
			});
		}
	};


	if (that.getRowPerspective) {
		console.log("getRowPerspective");
	}
	that.getRowPerspective = function(id, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/rows/" + id + "/perspective",
			data: {
				projection: projection
			},
			success: _success,
			error: _error,
			complete: _complete
		});
	};

	if (that.putRowPerspective) {
		console.log("putRowPerspective");
	}
	that.putRowPerspective = function(id, perspective, _success, _error, _complete) {
		if (perspective === null) {
			return that._ajax({
				type: "DELETE",
				url: _url + "/api/rows/" + id + "/perspective",
				success: _success,
				error: _error,
				complete: _complete
			});
		} else {
			return that._ajax({
				type: "PUT",
				url: _url + "/api/rows/" + id + "/perspective",
				contentType: "text/uri-list",
				data: perspective,
				success: _success,
				error: _error,
				complete: _complete
			});
		}
	};


	if (that.getRowSplashedPerspective) {
		console.log("getRowSplashedPerspective");
	}
	that.getRowSplashedPerspective = function(id, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/rows/" + id + "/splashedPerspective",
			data: {
				projection: projection
			},
			success: _success,
			error: _error,
			complete: _complete
		});
	};

	if (that.putRowSplashedPerspective) {
		console.log("putRowSplashedPerspective");
	}
	that.putRowSplashedPerspective = function(id, splashedPerspective, _success, _error, _complete) {
		if (splashedPerspective === null) {
			return that._ajax({
				type: "DELETE",
				url: _url + "/api/rows/" + id + "/splashedPerspective",
				success: _success,
				error: _error,
				complete: _complete
			});
		} else {
			return that._ajax({
				type: "PUT",
				url: _url + "/api/rows/" + id + "/splashedPerspective",
				contentType: "text/uri-list",
				data: splashedPerspective,
				success: _success,
				error: _error,
				complete: _complete
			});
		}
	};


	if (that.getRowTerm) {
		console.log("getRowTerm");
	}
	that.getRowTerm = function(id, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/rows/" + id + "/term",
			data: {
				projection: projection
			},
			success: _success,
			error: _error,
			complete: _complete
		});
	};

	if (that.putRowTerm) {
		console.log("putRowTerm");
	}
	that.putRowTerm = function(id, term, _success, _error, _complete) {
		if (term === null) {
			return that._ajax({
				type: "DELETE",
				url: _url + "/api/rows/" + id + "/term",
				success: _success,
				error: _error,
				complete: _complete
			});
		} else {
			return that._ajax({
				type: "PUT",
				url: _url + "/api/rows/" + id + "/term",
				contentType: "text/uri-list",
				data: term,
				success: _success,
				error: _error,
				complete: _complete
			});
		}
	};
/*---------------------------------------------------------------------------*/

/*---------------------------------------------------------------------------*/
	if (that.getStations) {
		console.log("getStations");
	}
	that.getStations = function(_page, _size, _sort, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/stations",
			data: {
				page: _page,
				size: _size,
				sort: _sort,
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
	};

	if (that.postStation) {
		console.log("postStation");
	}
	that.postStation = function(station, _success, _error, _complete) {
		return that._ajax({
			type: "POST",
			url: _url + "/api/stations",
			contentType: "application/json",
			data: JSON.stringify(station),
			success: function(_data, _textStatus, _jqXHR) {
				if (_success) {
					var _value = _jqXHR.getResponseHeader("Location");
					if (_value) {
						var _index = _value.lastIndexOf("/");
						var _suffix = _value.substring(_index + 1);
						var id = _suffix;
						_success(id, _textStatus, _jqXHR);
					}
				}
			},
			error: _error,
			complete: _complete
		});
	};

	if (that.getStation) {
		console.log("getStation");
	}
	that.getStation = function(id, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/stations/" + id,
			data: {
				projection: projection
			},
			success: _success,
			error: _error,
			complete: _complete
		});
	};

	if (that.putStation) {
		console.log("putStation");
	}
	that.putStation = function(station, _success, _error, _complete) {
		return that._ajax({
			type: "PUT",
			url: _url + "/api/stations/" + station.id,
			contentType: "application/json",
			data: JSON.stringify(station),
			success: _success,
			error: _error,
			complete: _complete
		});
	};

	if (that.deleteStation) {
		console.log("deleteStation");
	}
	that.deleteStation = function(id, _success, _error, _complete) {
		return that._ajax({
			type: "DELETE",
			url: _url + "/api/stations/" + id,
			success: _success,
			error: _error,
			complete: _complete
		});
	};


	if (that.getStationLogo) {
		console.log("getStationLogo");
	}
	that.getStationLogo = function(id, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/stations/" + id + "/logo",
			data: {
				projection: projection
			},
			success: _success,
			error: _error,
			complete: _complete
		});
	};

	if (that.putStationLogo) {
		console.log("putStationLogo");
	}
	that.putStationLogo = function(id, logo, _success, _error, _complete) {
		if (logo === null) {
			return that._ajax({
				type: "DELETE",
				url: _url + "/api/stations/" + id + "/logo",
				success: _success,
				error: _error,
				complete: _complete
			});
		} else {
			return that._ajax({
				type: "PUT",
				url: _url + "/api/stations/" + id + "/logo",
				contentType: "text/uri-list",
				data: logo,
				success: _success,
				error: _error,
				complete: _complete
			});
		}
	};


	if (that.getStationOwnedTaxonomies) {
		console.log("getStationOwnedTaxonomies");
	}
	that.getStationOwnedTaxonomies = function(id, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/stations/" + id + "/ownedTaxonomies",
			data: {
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
	};


	if (that.getStationPages) {
		console.log("getStationPages");
	}
	that.getStationPages = function(id, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/stations/" + id + "/pages",
			data: {
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
	};


	if (that.getStationPosts) {
		console.log("getStationPosts");
	}
	that.getStationPosts = function(id, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/stations/" + id + "/posts",
			data: {
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
	};


	if (that.getStationStationPerspectives) {
		console.log("getStationStationPerspectives");
	}
	that.getStationStationPerspectives = function(id, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/stations/" + id + "/stationPerspectives",
			data: {
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
	};
/*---------------------------------------------------------------------------*/

/*---------------------------------------------------------------------------*/
	if (that.getStationPerspectives) {
		console.log("getStationPerspectives");
	}
	that.getStationPerspectives = function(_page, _size, _sort, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/stationPerspectives",
			data: {
				page: _page,
				size: _size,
				sort: _sort,
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
	};

	if (that.postStationPerspective) {
		console.log("postStationPerspective");
	}
	that.postStationPerspective = function(stationPerspective, _success, _error, _complete) {
		return that._ajax({
			type: "POST",
			url: _url + "/api/stationPerspectives",
			contentType: "application/json",
			data: JSON.stringify(stationPerspective),
			success: function(_data, _textStatus, _jqXHR) {
				if (_success) {
					var _value = _jqXHR.getResponseHeader("Location");
					if (_value) {
						var _index = _value.lastIndexOf("/");
						var _suffix = _value.substring(_index + 1);
						var id = _suffix;
						_success(id, _textStatus, _jqXHR);
					}
				}
			},
			error: _error,
			complete: _complete
		});
	};

	if (that.getStationPerspective) {
		console.log("getStationPerspective");
	}
	that.getStationPerspective = function(id, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/stationPerspectives/" + id,
			data: {
				projection: projection
			},
			success: _success,
			error: _error,
			complete: _complete
		});
	};

	if (that.putStationPerspective) {
		console.log("putStationPerspective");
	}
	that.putStationPerspective = function(stationPerspective, _success, _error, _complete) {
		return that._ajax({
			type: "PUT",
			url: _url + "/api/stationPerspectives/" + stationPerspective.id,
			contentType: "application/json",
			data: JSON.stringify(stationPerspective),
			success: _success,
			error: _error,
			complete: _complete
		});
	};

	if (that.deleteStationPerspective) {
		console.log("deleteStationPerspective");
	}
	that.deleteStationPerspective = function(id, _success, _error, _complete) {
		return that._ajax({
			type: "DELETE",
			url: _url + "/api/stationPerspectives/" + id,
			success: _success,
			error: _error,
			complete: _complete
		});
	};

	if (that.findStationPerspectivesByStation) {
		console.log("findStationPerspectivesByStation");
	}
	that.findStationPerspectivesByStation = function(stationId, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/stationPerspectives/search/findStationPerspectivesByStation",
			data: {
				stationId: stationId,

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
	};

	if (that.getStationPerspectivePerspectives) {
		console.log("getStationPerspectivePerspectives");
	}
	that.getStationPerspectivePerspectives = function(id, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/stationPerspectives/" + id + "/perspectives",
			data: {
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
	};


	if (that.getStationPerspectiveStation) {
		console.log("getStationPerspectiveStation");
	}
	that.getStationPerspectiveStation = function(id, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/stationPerspectives/" + id + "/station",
			data: {
				projection: projection
			},
			success: _success,
			error: _error,
			complete: _complete
		});
	};

	if (that.putStationPerspectiveStation) {
		console.log("putStationPerspectiveStation");
	}
	that.putStationPerspectiveStation = function(id, station, _success, _error, _complete) {
		if (station === null) {
			return that._ajax({
				type: "DELETE",
				url: _url + "/api/stationPerspectives/" + id + "/station",
				success: _success,
				error: _error,
				complete: _complete
			});
		} else {
			return that._ajax({
				type: "PUT",
				url: _url + "/api/stationPerspectives/" + id + "/station",
				contentType: "text/uri-list",
				data: station,
				success: _success,
				error: _error,
				complete: _complete
			});
		}
	};


	if (that.getStationPerspectiveTaxonomy) {
		console.log("getStationPerspectiveTaxonomy");
	}
	that.getStationPerspectiveTaxonomy = function(id, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/stationPerspectives/" + id + "/taxonomy",
			data: {
				projection: projection
			},
			success: _success,
			error: _error,
			complete: _complete
		});
	};

	if (that.putStationPerspectiveTaxonomy) {
		console.log("putStationPerspectiveTaxonomy");
	}
	that.putStationPerspectiveTaxonomy = function(id, taxonomy, _success, _error, _complete) {
		if (taxonomy === null) {
			return that._ajax({
				type: "DELETE",
				url: _url + "/api/stationPerspectives/" + id + "/taxonomy",
				success: _success,
				error: _error,
				complete: _complete
			});
		} else {
			return that._ajax({
				type: "PUT",
				url: _url + "/api/stationPerspectives/" + id + "/taxonomy",
				contentType: "text/uri-list",
				data: taxonomy,
				success: _success,
				error: _error,
				complete: _complete
			});
		}
	};
/*---------------------------------------------------------------------------*/

/*---------------------------------------------------------------------------*/
	if (that.getTaxonomies) {
		console.log("getTaxonomies");
	}
	that.getTaxonomies = function(_page, _size, _sort, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/taxonomies",
			data: {
				page: _page,
				size: _size,
				sort: _sort,
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
	};

	if (that.postTaxonomy) {
		console.log("postTaxonomy");
	}
	that.postTaxonomy = function(taxonomy, _success, _error, _complete) {
		return that._ajax({
			type: "POST",
			url: _url + "/api/taxonomies",
			contentType: "application/json",
			data: JSON.stringify(taxonomy),
			success: function(_data, _textStatus, _jqXHR) {
				if (_success) {
					var _value = _jqXHR.getResponseHeader("Location");
					if (_value) {
						var _index = _value.lastIndexOf("/");
						var _suffix = _value.substring(_index + 1);
						var id = _suffix;
						_success(id, _textStatus, _jqXHR);
					}
				}
			},
			error: _error,
			complete: _complete
		});
	};

	if (that.getTaxonomy) {
		console.log("getTaxonomy");
	}
	that.getTaxonomy = function(id, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/taxonomies/" + id,
			data: {
				projection: projection
			},
			success: _success,
			error: _error,
			complete: _complete
		});
	};

	if (that.putTaxonomy) {
		console.log("putTaxonomy");
	}
	that.putTaxonomy = function(taxonomy, _success, _error, _complete) {
		return that._ajax({
			type: "PUT",
			url: _url + "/api/taxonomies/" + taxonomy.id,
			contentType: "application/json",
			data: JSON.stringify(taxonomy),
			success: _success,
			error: _error,
			complete: _complete
		});
	};

	if (that.deleteTaxonomy) {
		console.log("deleteTaxonomy");
	}
	that.deleteTaxonomy = function(id, _success, _error, _complete) {
		return that._ajax({
			type: "DELETE",
			url: _url + "/api/taxonomies/" + id,
			success: _success,
			error: _error,
			complete: _complete
		});
	};

	if (that.findByTypeAndName) {
		console.log("findByTypeAndName");
	}
	that.findByTypeAndName = function(name, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/taxonomies/search/findByTypeAndName",
			data: {
				name: name,

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
	};

	if (that.findStationTaxonomy) {
		console.log("findStationTaxonomy");
	}
	that.findStationTaxonomy = function(stationId, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/taxonomies/search/findStationTaxonomy",
			data: {
				stationId: stationId,

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
	};

	if (that.getTaxonomyOwningNetwork) {
		console.log("getTaxonomyOwningNetwork");
	}
	that.getTaxonomyOwningNetwork = function(id, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/taxonomies/" + id + "/owningNetwork",
			data: {
				projection: projection
			},
			success: _success,
			error: _error,
			complete: _complete
		});
	};

	if (that.putTaxonomyOwningNetwork) {
		console.log("putTaxonomyOwningNetwork");
	}
	that.putTaxonomyOwningNetwork = function(id, owningNetwork, _success, _error, _complete) {
		if (owningNetwork === null) {
			return that._ajax({
				type: "DELETE",
				url: _url + "/api/taxonomies/" + id + "/owningNetwork",
				success: _success,
				error: _error,
				complete: _complete
			});
		} else {
			return that._ajax({
				type: "PUT",
				url: _url + "/api/taxonomies/" + id + "/owningNetwork",
				contentType: "text/uri-list",
				data: owningNetwork,
				success: _success,
				error: _error,
				complete: _complete
			});
		}
	};


	if (that.getTaxonomyOwningStation) {
		console.log("getTaxonomyOwningStation");
	}
	that.getTaxonomyOwningStation = function(id, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/taxonomies/" + id + "/owningStation",
			data: {
				projection: projection
			},
			success: _success,
			error: _error,
			complete: _complete
		});
	};

	if (that.putTaxonomyOwningStation) {
		console.log("putTaxonomyOwningStation");
	}
	that.putTaxonomyOwningStation = function(id, owningStation, _success, _error, _complete) {
		if (owningStation === null) {
			return that._ajax({
				type: "DELETE",
				url: _url + "/api/taxonomies/" + id + "/owningStation",
				success: _success,
				error: _error,
				complete: _complete
			});
		} else {
			return that._ajax({
				type: "PUT",
				url: _url + "/api/taxonomies/" + id + "/owningStation",
				contentType: "text/uri-list",
				data: owningStation,
				success: _success,
				error: _error,
				complete: _complete
			});
		}
	};


	if (that.getTaxonomyTerms) {
		console.log("getTaxonomyTerms");
	}
	that.getTaxonomyTerms = function(id, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/taxonomies/" + id + "/terms",
			data: {
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
	};
/*---------------------------------------------------------------------------*/

/*---------------------------------------------------------------------------*/
	if (that.getTerms) {
		console.log("getTerms");
	}
	that.getTerms = function(_page, _size, _sort, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/terms",
			data: {
				page: _page,
				size: _size,
				sort: _sort,
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
	};

	if (that.postTerm) {
		console.log("postTerm");
	}
	that.postTerm = function(term, _success, _error, _complete) {
		return that._ajax({
			type: "POST",
			url: _url + "/api/terms",
			contentType: "application/json",
			data: JSON.stringify(term),
			success: function(_data, _textStatus, _jqXHR) {
				if (_success) {
					var _value = _jqXHR.getResponseHeader("Location");
					if (_value) {
						var _index = _value.lastIndexOf("/");
						var _suffix = _value.substring(_index + 1);
						var id = _suffix;
						_success(id, _textStatus, _jqXHR);
					}
				}
			},
			error: _error,
			complete: _complete
		});
	};

	if (that.getTerm) {
		console.log("getTerm");
	}
	that.getTerm = function(id, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/terms/" + id,
			data: {
				projection: projection
			},
			success: _success,
			error: _error,
			complete: _complete
		});
	};

	if (that.putTerm) {
		console.log("putTerm");
	}
	that.putTerm = function(term, _success, _error, _complete) {
		return that._ajax({
			type: "PUT",
			url: _url + "/api/terms/" + term.id,
			contentType: "application/json",
			data: JSON.stringify(term),
			success: _success,
			error: _error,
			complete: _complete
		});
	};

	if (that.deleteTerm) {
		console.log("deleteTerm");
	}
	that.deleteTerm = function(id, _success, _error, _complete) {
		return that._ajax({
			type: "DELETE",
			url: _url + "/api/terms/" + id,
			success: _success,
			error: _error,
			complete: _complete
		});
	};

	if (that.findTermsByParentId) {
		console.log("findTermsByParentId");
	}
	that.findTermsByParentId = function(termId, page, size, sort, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/terms/search/findTermsByParentId",
			data: {
				termId: termId,
				page: page,
				size: size,
				sort: sort,

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
	};

	if (that.findRoots) {
		console.log("findRoots");
	}
	that.findRoots = function(taxonomyId, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/terms/search/findRoots",
			data: {
				taxonomyId: taxonomyId,

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
	};

	if (that.findRootsPage) {
		console.log("findRootsPage");
	}
	that.findRootsPage = function(taxonomyId, page, size, sort, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/terms/search/findRootsPage",
			data: {
				taxonomyId: taxonomyId,
				page: page,
				size: size,
				sort: sort,

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
	};

	if (that.findByTaxonomyId) {
		console.log("findByTaxonomyId");
	}
	that.findByTaxonomyId = function(taxonomyId, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/terms/search/findByTaxonomyId",
			data: {
				taxonomyId: taxonomyId,

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
	};

	if (that.findPostsByTerm) {
		console.log("findPostsByTerm");
	}
	that.findPostsByTerm = function(termId, page, size, sort, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/terms/search/findPostsByTerm",
			data: {
				termId: termId,
				page: page,
				size: size,
				sort: sort,

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
	};

	if (that.countTerms) {
		console.log("countTerms");
	}
	that.countTerms = function(termsIds, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/terms/search/countTerms",
			data: {
				termsIds: termsIds,

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
	};

	if (that.findTermsByPostId) {
		console.log("findTermsByPostId");
	}
	that.findTermsByPostId = function(postId, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/terms/search/findTermsByPostId",
			data: {
				postId: postId,

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
	};

	if (that.findTermsByPostSlug) {
		console.log("findTermsByPostSlug");
	}
	that.findTermsByPostSlug = function(slug, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/terms/search/findTermsByPostSlug",
			data: {
				slug: slug,

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
	};

	if (that.findByPerspectiveId) {
		console.log("findByPerspectiveId");
	}
	that.findByPerspectiveId = function(perspectiveId, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/terms/search/findByPerspectiveId",
			data: {
				perspectiveId: perspectiveId,

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
	};

	if (that.getTermChildren) {
		console.log("getTermChildren");
	}
	that.getTermChildren = function(id, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/terms/" + id + "/children",
			data: {
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
	};


	if (that.getTermImage) {
		console.log("getTermImage");
	}
	that.getTermImage = function(id, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/terms/" + id + "/image",
			data: {
				projection: projection
			},
			success: _success,
			error: _error,
			complete: _complete
		});
	};

	if (that.putTermImage) {
		console.log("putTermImage");
	}
	that.putTermImage = function(id, image, _success, _error, _complete) {
		if (image === null) {
			return that._ajax({
				type: "DELETE",
				url: _url + "/api/terms/" + id + "/image",
				success: _success,
				error: _error,
				complete: _complete
			});
		} else {
			return that._ajax({
				type: "PUT",
				url: _url + "/api/terms/" + id + "/image",
				contentType: "text/uri-list",
				data: image,
				success: _success,
				error: _error,
				complete: _complete
			});
		}
	};


	if (that.getTermParent) {
		console.log("getTermParent");
	}
	that.getTermParent = function(id, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/terms/" + id + "/parent",
			data: {
				projection: projection
			},
			success: _success,
			error: _error,
			complete: _complete
		});
	};

	if (that.putTermParent) {
		console.log("putTermParent");
	}
	that.putTermParent = function(id, parent, _success, _error, _complete) {
		if (parent === null) {
			return that._ajax({
				type: "DELETE",
				url: _url + "/api/terms/" + id + "/parent",
				success: _success,
				error: _error,
				complete: _complete
			});
		} else {
			return that._ajax({
				type: "PUT",
				url: _url + "/api/terms/" + id + "/parent",
				contentType: "text/uri-list",
				data: parent,
				success: _success,
				error: _error,
				complete: _complete
			});
		}
	};


	if (that.getTermTaxonomy) {
		console.log("getTermTaxonomy");
	}
	that.getTermTaxonomy = function(id, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/terms/" + id + "/taxonomy",
			data: {
				projection: projection
			},
			success: _success,
			error: _error,
			complete: _complete
		});
	};

	if (that.putTermTaxonomy) {
		console.log("putTermTaxonomy");
	}
	that.putTermTaxonomy = function(id, taxonomy, _success, _error, _complete) {
		if (taxonomy === null) {
			return that._ajax({
				type: "DELETE",
				url: _url + "/api/terms/" + id + "/taxonomy",
				success: _success,
				error: _error,
				complete: _complete
			});
		} else {
			return that._ajax({
				type: "PUT",
				url: _url + "/api/terms/" + id + "/taxonomy",
				contentType: "text/uri-list",
				data: taxonomy,
				success: _success,
				error: _error,
				complete: _complete
			});
		}
	};


	if (that.getTermTermPerspectives) {
		console.log("getTermTermPerspectives");
	}
	that.getTermTermPerspectives = function(id, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/terms/" + id + "/termPerspectives",
			data: {
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
	};
/*---------------------------------------------------------------------------*/

/*---------------------------------------------------------------------------*/
	if (that.getTermPerspectives) {
		console.log("getTermPerspectives");
	}
	that.getTermPerspectives = function(_page, _size, _sort, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/termPerspectives",
			data: {
				page: _page,
				size: _size,
				sort: _sort,
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
	};

	if (that.postTermPerspective) {
		console.log("postTermPerspective");
	}
	that.postTermPerspective = function(termPerspective, _success, _error, _complete) {
		return that._ajax({
			type: "POST",
			url: _url + "/api/termPerspectives",
			contentType: "application/json",
			data: JSON.stringify(termPerspective),
			success: function(_data, _textStatus, _jqXHR) {
				if (_success) {
					var _value = _jqXHR.getResponseHeader("Location");
					if (_value) {
						var _index = _value.lastIndexOf("/");
						var _suffix = _value.substring(_index + 1);
						var id = _suffix;
						_success(id, _textStatus, _jqXHR);
					}
				}
			},
			error: _error,
			complete: _complete
		});
	};

	if (that.getTermPerspective) {
		console.log("getTermPerspective");
	}
	that.getTermPerspective = function(id, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/termPerspectives/" + id,
			data: {
				projection: projection
			},
			success: _success,
			error: _error,
			complete: _complete
		});
	};

	if (that.putTermPerspective) {
		console.log("putTermPerspective");
	}
	that.putTermPerspective = function(termPerspective, _success, _error, _complete) {
		return that._ajax({
			type: "PUT",
			url: _url + "/api/termPerspectives/" + termPerspective.id,
			contentType: "application/json",
			data: JSON.stringify(termPerspective),
			success: _success,
			error: _error,
			complete: _complete
		});
	};

	if (that.deleteTermPerspective) {
		console.log("deleteTermPerspective");
	}
	that.deleteTermPerspective = function(id, _success, _error, _complete) {
		return that._ajax({
			type: "DELETE",
			url: _url + "/api/termPerspectives/" + id,
			success: _success,
			error: _error,
			complete: _complete
		});
	};


	if (that.getTermPerspectiveFeaturedRow) {
		console.log("getTermPerspectiveFeaturedRow");
	}
	that.getTermPerspectiveFeaturedRow = function(id, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/termPerspectives/" + id + "/featuredRow",
			data: {
				projection: projection
			},
			success: _success,
			error: _error,
			complete: _complete
		});
	};

	if (that.putTermPerspectiveFeaturedRow) {
		console.log("putTermPerspectiveFeaturedRow");
	}
	that.putTermPerspectiveFeaturedRow = function(id, featuredRow, _success, _error, _complete) {
		if (featuredRow === null) {
			return that._ajax({
				type: "DELETE",
				url: _url + "/api/termPerspectives/" + id + "/featuredRow",
				success: _success,
				error: _error,
				complete: _complete
			});
		} else {
			return that._ajax({
				type: "PUT",
				url: _url + "/api/termPerspectives/" + id + "/featuredRow",
				contentType: "text/uri-list",
				data: featuredRow,
				success: _success,
				error: _error,
				complete: _complete
			});
		}
	};


	if (that.getTermPerspectiveHomeRow) {
		console.log("getTermPerspectiveHomeRow");
	}
	that.getTermPerspectiveHomeRow = function(id, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/termPerspectives/" + id + "/homeRow",
			data: {
				projection: projection
			},
			success: _success,
			error: _error,
			complete: _complete
		});
	};

	if (that.putTermPerspectiveHomeRow) {
		console.log("putTermPerspectiveHomeRow");
	}
	that.putTermPerspectiveHomeRow = function(id, homeRow, _success, _error, _complete) {
		if (homeRow === null) {
			return that._ajax({
				type: "DELETE",
				url: _url + "/api/termPerspectives/" + id + "/homeRow",
				success: _success,
				error: _error,
				complete: _complete
			});
		} else {
			return that._ajax({
				type: "PUT",
				url: _url + "/api/termPerspectives/" + id + "/homeRow",
				contentType: "text/uri-list",
				data: homeRow,
				success: _success,
				error: _error,
				complete: _complete
			});
		}
	};


	if (that.getTermPerspectivePerspective) {
		console.log("getTermPerspectivePerspective");
	}
	that.getTermPerspectivePerspective = function(id, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/termPerspectives/" + id + "/perspective",
			data: {
				projection: projection
			},
			success: _success,
			error: _error,
			complete: _complete
		});
	};

	if (that.putTermPerspectivePerspective) {
		console.log("putTermPerspectivePerspective");
	}
	that.putTermPerspectivePerspective = function(id, perspective, _success, _error, _complete) {
		if (perspective === null) {
			return that._ajax({
				type: "DELETE",
				url: _url + "/api/termPerspectives/" + id + "/perspective",
				success: _success,
				error: _error,
				complete: _complete
			});
		} else {
			return that._ajax({
				type: "PUT",
				url: _url + "/api/termPerspectives/" + id + "/perspective",
				contentType: "text/uri-list",
				data: perspective,
				success: _success,
				error: _error,
				complete: _complete
			});
		}
	};


	if (that.getTermPerspectiveRows) {
		console.log("getTermPerspectiveRows");
	}
	that.getTermPerspectiveRows = function(id, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/termPerspectives/" + id + "/rows",
			data: {
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
	};


	if (that.getTermPerspectiveSplashedRow) {
		console.log("getTermPerspectiveSplashedRow");
	}
	that.getTermPerspectiveSplashedRow = function(id, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/termPerspectives/" + id + "/splashedRow",
			data: {
				projection: projection
			},
			success: _success,
			error: _error,
			complete: _complete
		});
	};

	if (that.putTermPerspectiveSplashedRow) {
		console.log("putTermPerspectiveSplashedRow");
	}
	that.putTermPerspectiveSplashedRow = function(id, splashedRow, _success, _error, _complete) {
		if (splashedRow === null) {
			return that._ajax({
				type: "DELETE",
				url: _url + "/api/termPerspectives/" + id + "/splashedRow",
				success: _success,
				error: _error,
				complete: _complete
			});
		} else {
			return that._ajax({
				type: "PUT",
				url: _url + "/api/termPerspectives/" + id + "/splashedRow",
				contentType: "text/uri-list",
				data: splashedRow,
				success: _success,
				error: _error,
				complete: _complete
			});
		}
	};


	if (that.getTermPerspectiveTerm) {
		console.log("getTermPerspectiveTerm");
	}
	that.getTermPerspectiveTerm = function(id, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/termPerspectives/" + id + "/term",
			data: {
				projection: projection
			},
			success: _success,
			error: _error,
			complete: _complete
		});
	};

	if (that.putTermPerspectiveTerm) {
		console.log("putTermPerspectiveTerm");
	}
	that.putTermPerspectiveTerm = function(id, term, _success, _error, _complete) {
		if (term === null) {
			return that._ajax({
				type: "DELETE",
				url: _url + "/api/termPerspectives/" + id + "/term",
				success: _success,
				error: _error,
				complete: _complete
			});
		} else {
			return that._ajax({
				type: "PUT",
				url: _url + "/api/termPerspectives/" + id + "/term",
				contentType: "text/uri-list",
				data: term,
				success: _success,
				error: _error,
				complete: _complete
			});
		}
	};
/*---------------------------------------------------------------------------*/

/*---------------------------------------------------------------------------*/
	if (that.getUsers) {
		console.log("getUsers");
	}
	that.getUsers = function(_page, _size, _sort, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/users",
			data: {
				page: _page,
				size: _size,
				sort: _sort,
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
	};

	if (that.postUser) {
		console.log("postUser");
	}
	that.postUser = function(user, _success, _error, _complete) {
		return that._ajax({
			type: "POST",
			url: _url + "/api/users",
			contentType: "application/json",
			data: JSON.stringify(user),
			success: function(_data, _textStatus, _jqXHR) {
				if (_success) {
					var _value = _jqXHR.getResponseHeader("Location");
					if (_value) {
						var _index = _value.lastIndexOf("/");
						var _suffix = _value.substring(_index + 1);
						var id = _suffix;
						_success(id, _textStatus, _jqXHR);
					}
				}
			},
			error: _error,
			complete: _complete
		});
	};

	if (that.getUser) {
		console.log("getUser");
	}
	that.getUser = function(id, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/users/" + id,
			data: {
				projection: projection
			},
			success: _success,
			error: _error,
			complete: _complete
		});
	};

	if (that.putUser) {
		console.log("putUser");
	}
	that.putUser = function(user, _success, _error, _complete) {
		return that._ajax({
			type: "PUT",
			url: _url + "/api/users/" + user.id,
			contentType: "application/json",
			data: JSON.stringify(user),
			success: _success,
			error: _error,
			complete: _complete
		});
	};

	if (that.deleteUser) {
		console.log("deleteUser");
	}
	that.deleteUser = function(id, _success, _error, _complete) {
		return that._ajax({
			type: "DELETE",
			url: _url + "/api/users/" + id,
			success: _success,
			error: _error,
			complete: _complete
		});
	};

	if (that.findSocialUser) {
		console.log("findSocialUser");
	}
	that.findSocialUser = function(providerUserId, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/users/search/findSocialUser",
			data: {
				providerUserId: providerUserId,

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
	};

/*---------------------------------------------------------------------------*/

/*---------------------------------------------------------------------------*/
	if (that.getVideos) {
		console.log("getVideos");
	}
	that.getVideos = function(_page, _size, _sort, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/videos",
			data: {
				page: _page,
				size: _size,
				sort: _sort,
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
	};

	if (that.postVideo) {
		console.log("postVideo");
	}
	that.postVideo = function(video, _success, _error, _complete) {
		return that._ajax({
			type: "POST",
			url: _url + "/api/videos",
			contentType: "application/json",
			data: JSON.stringify(video),
			success: function(_data, _textStatus, _jqXHR) {
				if (_success) {
					var _value = _jqXHR.getResponseHeader("Location");
					if (_value) {
						var _index = _value.lastIndexOf("/");
						var _suffix = _value.substring(_index + 1);
						var id = _suffix;
						_success(id, _textStatus, _jqXHR);
					}
				}
			},
			error: _error,
			complete: _complete
		});
	};

	if (that.getVideo) {
		console.log("getVideo");
	}
	that.getVideo = function(id, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/videos/" + id,
			data: {
				projection: projection
			},
			success: _success,
			error: _error,
			complete: _complete
		});
	};

	if (that.putVideo) {
		console.log("putVideo");
	}
	that.putVideo = function(video, _success, _error, _complete) {
		return that._ajax({
			type: "PUT",
			url: _url + "/api/videos/" + video.id,
			contentType: "application/json",
			data: JSON.stringify(video),
			success: _success,
			error: _error,
			complete: _complete
		});
	};

	if (that.deleteVideo) {
		console.log("deleteVideo");
	}
	that.deleteVideo = function(id, _success, _error, _complete) {
		return that._ajax({
			type: "DELETE",
			url: _url + "/api/videos/" + id,
			success: _success,
			error: _error,
			complete: _complete
		});
	};

	if (that.findVideosOrderByDate) {
		console.log("findVideosOrderByDate");
	}
	that.findVideosOrderByDate = function(page, size, sort, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/videos/search/findVideosOrderByDate",
			data: {
				page: page,
				size: size,
				sort: sort,

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
	};

	if (that.getVideoImage) {
		console.log("getVideoImage");
	}
	that.getVideoImage = function(id, _success, _error, _complete, projection) {
		return that._ajax({
			url: _url + "/api/videos/" + id + "/image",
			data: {
				projection: projection
			},
			success: _success,
			error: _error,
			complete: _complete
		});
	};

	if (that.putVideoImage) {
		console.log("putVideoImage");
	}
	that.putVideoImage = function(id, image, _success, _error, _complete) {
		if (image === null) {
			return that._ajax({
				type: "DELETE",
				url: _url + "/api/videos/" + id + "/image",
				success: _success,
				error: _error,
				complete: _complete
			});
		} else {
			return that._ajax({
				type: "PUT",
				url: _url + "/api/videos/" + id + "/image",
				contentType: "text/uri-list",
				data: image,
				success: _success,
				error: _error,
				complete: _complete
			});
		}
	};
/*---------------------------------------------------------------------------*/

	return that;
}