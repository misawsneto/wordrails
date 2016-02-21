Facade(facade, entities) ::=

<<$entities:{entity | $Dto(entity)$}; separator="\n\n"$

function Base$facade$(_url, _username, _password) {
	function logIn(complete) {
		return \$.ajax({
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
		return \$.ajax({
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
					\$.ajax(settings);
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

		return \$.ajax(settings);
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

    
$entities:{entity | $FacadeMethods(entity)$}; separator="\n\n"$

	return that;
}>>