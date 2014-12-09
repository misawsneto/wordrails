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

function FileDto(id, type, mime, name, url) {
	return {
		id: id,
		type: type,
		mime: mime,
		name: name,
		url: url
	};
}

function ImageDto(id, title, caption, original, small, medium, large) {
	return {
		id: id,
		title: title,
		caption: caption,
		original: original,
		small: small,
		medium: medium,
		large: large
	};
}

function NetworkDto(id, name, trackingId, defaultTaxonomy, allowSignup, allowComments, domain, subdomain, configured) {
	return {
		id: id,
		name: name,
		trackingId: trackingId,
		defaultTaxonomy: defaultTaxonomy,
		allowSignup: allowSignup,
		allowComments: allowComments,
		domain: domain,
		subdomain: subdomain,
		configured: configured
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

function PersonDto(id, name, username, bio, email, passwordReseted, twitterHandle) {
	return {
		id: id,
		name: name,
		username: username,
		bio: bio,
		email: email,
		passwordReseted: passwordReseted,
		twitterHandle: twitterHandle
	};
}

function PostDto(id, date, lastModificationDate, title, body, topper, state, originalSlug, slug, author, station) {
	return {
		id: id,
		date: date,
		lastModificationDate: lastModificationDate,
		title: title,
		body: body,
		topper: topper,
		state: state,
		originalSlug: originalSlug,
		slug: slug,
		author: author,
		station: station
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

function StationDto(id, name, writable, main, visibility, networks, stationPerspectives, postsTitleSize, topper, sponsored, social) {
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
		social: social
	};
}

function StationPerspectiveDto(id, name, station) {
	return {
		id: id,
		name: name,
		station: station
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

function TermDto(id, name, taxonomy) {
	return {
		id: id,
		name: name,
		taxonomy: taxonomy
	};
}

function TermPerspectiveDto(id, perspective) {
	return {
		id: id,
		perspective: perspective
	};
}

function BaseWordRails(_url, _username, _password) {
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
            type: "POST",
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

    if (that.getCommentImages) {
    	console.log("getCommentImages");
    }
    that.getCommentImages = function(id, _success, _error, _complete, projection) {
        return that._ajax({
            url: _url + "/api/comments/" + id + "/images",
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
	if (that.getFiles) {
		console.log("getFiles");
	}
    that.getFiles = function(_page, _size, _sort, _success, _error, _complete, projection) {
		return that._ajax({
            url: _url + "/api/files",
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

	if (that.postFile) {
		console.log("postFile");
	}
    that.postFile = function(file, _success, _error, _complete) {
        return that._ajax({
            type: "POST",
            url: _url + "/api/files",
            contentType: "application/json",
            data: JSON.stringify(file),
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

	if (that.getFile) {
		console.log("getFile");
	}
    that.getFile = function(id, _success, _error, _complete, projection) {
        return that._ajax({
            url: _url + "/api/files/" + id,
            data: {
                projection: projection
            },            
            success: _success,
            error: _error,
            complete: _complete
        });
    };

	if (that.putFile) {
		console.log("putFile");
	}
    that.putFile = function(file, _success, _error, _complete) {
        return that._ajax({
            type: "PUT",
            url: _url + "/api/files/" + file.id,
            contentType: "application/json",
            data: JSON.stringify(file),
            success: _success,
            error: _error,
            complete: _complete
        });
    };

	if (that.deleteFile) {
		console.log("deleteFile");
	}
    that.deleteFile = function(id, _success, _error, _complete) {
        return that._ajax({
            type: "DELETE",
            url: _url + "/api/files/" + id,
            success: _success,
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


    if (that.getImageComment) {
    	console.log("getImageComment");
    }
    that.getImageComment = function(id, _success, _error, _complete, projection) {
        return that._ajax({
            url: _url + "/api/images/" + id + "/comment",
            data: {
            	projection: projection
            },
            success: _success,
            error: _error,
            complete: _complete
        });
    };

    if (that.putImageComment) {
    	console.log("putImageComment");
    }
    that.putImageComment = function(id, comment, _success, _error, _complete) {
    	if (comment === null) {
    	    return that._ajax({
    	    	type: "DELETE",
    	    	url: _url + "/api/images/" + id + "/comment",
    	        success: _success,
    	        error: _error,
    	        complete: _complete
    	    });		
    	} else {
    	    return that._ajax({
    	    	type: "PUT",
    	        url: _url + "/api/images/" + id + "/comment",
    	        contentType: "text/uri-list",
    	        data: comment,
    	        success: _success,
    	        error: _error,
    	        complete: _complete
    	    });	
    	}
    };


    if (that.getImagePerson) {
    	console.log("getImagePerson");
    }
    that.getImagePerson = function(id, _success, _error, _complete, projection) {
        return that._ajax({
            url: _url + "/api/images/" + id + "/person",
            data: {
            	projection: projection
            },
            success: _success,
            error: _error,
            complete: _complete
        });
    };

    if (that.putImagePerson) {
    	console.log("putImagePerson");
    }
    that.putImagePerson = function(id, person, _success, _error, _complete) {
    	if (person === null) {
    	    return that._ajax({
    	    	type: "DELETE",
    	    	url: _url + "/api/images/" + id + "/person",
    	        success: _success,
    	        error: _error,
    	        complete: _complete
    	    });		
    	} else {
    	    return that._ajax({
    	    	type: "PUT",
    	        url: _url + "/api/images/" + id + "/person",
    	        contentType: "text/uri-list",
    	        data: person,
    	        success: _success,
    	        error: _error,
    	        complete: _complete
    	    });	
    	}
    };


    if (that.getImageNetwork) {
    	console.log("getImageNetwork");
    }
    that.getImageNetwork = function(id, _success, _error, _complete, projection) {
        return that._ajax({
            url: _url + "/api/images/" + id + "/network",
            data: {
            	projection: projection
            },
            success: _success,
            error: _error,
            complete: _complete
        });
    };

    if (that.putImageNetwork) {
    	console.log("putImageNetwork");
    }
    that.putImageNetwork = function(id, network, _success, _error, _complete) {
    	if (network === null) {
    	    return that._ajax({
    	    	type: "DELETE",
    	    	url: _url + "/api/images/" + id + "/network",
    	        success: _success,
    	        error: _error,
    	        complete: _complete
    	    });		
    	} else {
    	    return that._ajax({
    	    	type: "PUT",
    	        url: _url + "/api/images/" + id + "/network",
    	        contentType: "text/uri-list",
    	        data: network,
    	        success: _success,
    	        error: _error,
    	        complete: _complete
    	    });	
    	}
    };


    if (that.getImageLogoSponsor) {
    	console.log("getImageLogoSponsor");
    }
    that.getImageLogoSponsor = function(id, _success, _error, _complete, projection) {
        return that._ajax({
            url: _url + "/api/images/" + id + "/logoSponsor",
            data: {
            	projection: projection
            },
            success: _success,
            error: _error,
            complete: _complete
        });
    };

    if (that.putImageLogoSponsor) {
    	console.log("putImageLogoSponsor");
    }
    that.putImageLogoSponsor = function(id, logoSponsor, _success, _error, _complete) {
    	if (logoSponsor === null) {
    	    return that._ajax({
    	    	type: "DELETE",
    	    	url: _url + "/api/images/" + id + "/logoSponsor",
    	        success: _success,
    	        error: _error,
    	        complete: _complete
    	    });		
    	} else {
    	    return that._ajax({
    	    	type: "PUT",
    	        url: _url + "/api/images/" + id + "/logoSponsor",
    	        contentType: "text/uri-list",
    	        data: logoSponsor,
    	        success: _success,
    	        error: _error,
    	        complete: _complete
    	    });	
    	}
    };


    if (that.getImagePublicitySponsor) {
    	console.log("getImagePublicitySponsor");
    }
    that.getImagePublicitySponsor = function(id, _success, _error, _complete, projection) {
        return that._ajax({
            url: _url + "/api/images/" + id + "/publicitySponsor",
            data: {
            	projection: projection
            },
            success: _success,
            error: _error,
            complete: _complete
        });
    };

    if (that.putImagePublicitySponsor) {
    	console.log("putImagePublicitySponsor");
    }
    that.putImagePublicitySponsor = function(id, publicitySponsor, _success, _error, _complete) {
    	if (publicitySponsor === null) {
    	    return that._ajax({
    	    	type: "DELETE",
    	    	url: _url + "/api/images/" + id + "/publicitySponsor",
    	        success: _success,
    	        error: _error,
    	        complete: _complete
    	    });		
    	} else {
    	    return that._ajax({
    	    	type: "PUT",
    	        url: _url + "/api/images/" + id + "/publicitySponsor",
    	        contentType: "text/uri-list",
    	        data: publicitySponsor,
    	        success: _success,
    	        error: _error,
    	        complete: _complete
    	    });	
    	}
    };


    if (that.getImageOriginal) {
    	console.log("getImageOriginal");
    }
    that.getImageOriginal = function(id, _success, _error, _complete, projection) {
        return that._ajax({
            url: _url + "/api/images/" + id + "/original",
            data: {
            	projection: projection
            },
            success: _success,
            error: _error,
            complete: _complete
        });
    };

    if (that.putImageOriginal) {
    	console.log("putImageOriginal");
    }
    that.putImageOriginal = function(id, original, _success, _error, _complete) {
    	if (original === null) {
    	    return that._ajax({
    	    	type: "DELETE",
    	    	url: _url + "/api/images/" + id + "/original",
    	        success: _success,
    	        error: _error,
    	        complete: _complete
    	    });		
    	} else {
    	    return that._ajax({
    	    	type: "PUT",
    	        url: _url + "/api/images/" + id + "/original",
    	        contentType: "text/uri-list",
    	        data: original,
    	        success: _success,
    	        error: _error,
    	        complete: _complete
    	    });	
    	}
    };


    if (that.getImageSmall) {
    	console.log("getImageSmall");
    }
    that.getImageSmall = function(id, _success, _error, _complete, projection) {
        return that._ajax({
            url: _url + "/api/images/" + id + "/small",
            data: {
            	projection: projection
            },
            success: _success,
            error: _error,
            complete: _complete
        });
    };

    if (that.putImageSmall) {
    	console.log("putImageSmall");
    }
    that.putImageSmall = function(id, small, _success, _error, _complete) {
    	if (small === null) {
    	    return that._ajax({
    	    	type: "DELETE",
    	    	url: _url + "/api/images/" + id + "/small",
    	        success: _success,
    	        error: _error,
    	        complete: _complete
    	    });		
    	} else {
    	    return that._ajax({
    	    	type: "PUT",
    	        url: _url + "/api/images/" + id + "/small",
    	        contentType: "text/uri-list",
    	        data: small,
    	        success: _success,
    	        error: _error,
    	        complete: _complete
    	    });	
    	}
    };


    if (that.getImageMedium) {
    	console.log("getImageMedium");
    }
    that.getImageMedium = function(id, _success, _error, _complete, projection) {
        return that._ajax({
            url: _url + "/api/images/" + id + "/medium",
            data: {
            	projection: projection
            },
            success: _success,
            error: _error,
            complete: _complete
        });
    };

    if (that.putImageMedium) {
    	console.log("putImageMedium");
    }
    that.putImageMedium = function(id, medium, _success, _error, _complete) {
    	if (medium === null) {
    	    return that._ajax({
    	    	type: "DELETE",
    	    	url: _url + "/api/images/" + id + "/medium",
    	        success: _success,
    	        error: _error,
    	        complete: _complete
    	    });		
    	} else {
    	    return that._ajax({
    	    	type: "PUT",
    	        url: _url + "/api/images/" + id + "/medium",
    	        contentType: "text/uri-list",
    	        data: medium,
    	        success: _success,
    	        error: _error,
    	        complete: _complete
    	    });	
    	}
    };


    if (that.getImageLarge) {
    	console.log("getImageLarge");
    }
    that.getImageLarge = function(id, _success, _error, _complete, projection) {
        return that._ajax({
            url: _url + "/api/images/" + id + "/large",
            data: {
            	projection: projection
            },
            success: _success,
            error: _error,
            complete: _complete
        });
    };

    if (that.putImageLarge) {
    	console.log("putImageLarge");
    }
    that.putImageLarge = function(id, large, _success, _error, _complete) {
    	if (large === null) {
    	    return that._ajax({
    	    	type: "DELETE",
    	    	url: _url + "/api/images/" + id + "/large",
    	        success: _success,
    	        error: _error,
    	        complete: _complete
    	    });		
    	} else {
    	    return that._ajax({
    	    	type: "PUT",
    	        url: _url + "/api/images/" + id + "/large",
    	        contentType: "text/uri-list",
    	        data: large,
    	        success: _success,
    	        error: _error,
    	        complete: _complete
    	    });	
    	}
    };


    if (that.getImagePost) {
    	console.log("getImagePost");
    }
    that.getImagePost = function(id, _success, _error, _complete, projection) {
        return that._ajax({
            url: _url + "/api/images/" + id + "/post",
            data: {
            	projection: projection
            },
            success: _success,
            error: _error,
            complete: _complete
        });
    };

    if (that.putImagePost) {
    	console.log("putImagePost");
    }
    that.putImagePost = function(id, post, _success, _error, _complete) {
    	if (post === null) {
    	    return that._ajax({
    	    	type: "DELETE",
    	    	url: _url + "/api/images/" + id + "/post",
    	        success: _success,
    	        error: _error,
    	        complete: _complete
    	    });		
    	} else {
    	    return that._ajax({
    	    	type: "PUT",
    	        url: _url + "/api/images/" + id + "/post",
    	        contentType: "text/uri-list",
    	        data: post,
    	        success: _success,
    	        error: _error,
    	        complete: _complete
    	    });	
    	}
    };


    if (that.getImageFeaturingPosts) {
    	console.log("getImageFeaturingPosts");
    }
    that.getImageFeaturingPosts = function(id, _success, _error, _complete, projection) {
        return that._ajax({
            url: _url + "/api/images/" + id + "/featuringPosts",
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

    if (that.findBySubdomain) {
    	console.log("findBySubdomain");
    }
    that.findBySubdomain = function(subdomain, _success, _error, _complete, projection) {
        return that._ajax({
            url: _url + "/api/networks/search/findBySubdomain",
            data: {
            	subdomain: subdomain,

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

    if (that.getNetworkPersonsNetworkRoles) {
    	console.log("getNetworkPersonsNetworkRoles");
    }
    that.getNetworkPersonsNetworkRoles = function(id, _success, _error, _complete, projection) {
        return that._ajax({
            url: _url + "/api/networks/" + id + "/personsNetworkRoles",
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


    if (that.getNetworkStations) {
    	console.log("getNetworkStations");
    }
    that.getNetworkStations = function(id, _success, _error, _complete, projection) {
        return that._ajax({
            url: _url + "/api/networks/" + id + "/stations",
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


    if (that.getNetworkTaxonomies) {
    	console.log("getNetworkTaxonomies");
    }
    that.getNetworkTaxonomies = function(id, _success, _error, _complete, projection) {
        return that._ajax({
            url: _url + "/api/networks/" + id + "/taxonomies",
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
    if (that.patchNetworkTaxonomies) {
    	console.log("patchNetworkTaxonomies");
    }
    that.patchNetworkTaxonomies = function(id, taxonomies, _success, _error, _complete) {		
    	var _data = "";
    	for (var i = 0; i < taxonomies.length; ++i) {
    		_data += taxonomies[i] + "\n";
    	}
        return that._ajax({
        	type: "PATCH",
            url: _url + "/api/networks/" + id + "/taxonomies",
            contentType: "text/uri-list",
            data: _data,        
            success: _success,
            error: _error,
            complete: _complete
        });
    };

    if (that.putNetworkTaxonomies) {
    	console.log("putNetworkTaxonomies");
    }
    that.putNetworkTaxonomies = function(id, taxonomies, _success, _error, _complete) {		
    	var _data = "";
    	for (var i = 0; i < taxonomies.length; ++i) {
    		_data += taxonomies[i] + "\n";
    	}
        return that._ajax({
        	type: "PUT",
            url: _url + "/api/networks/" + id + "/taxonomies",
            contentType: "text/uri-list",
            data: _data,        
            success: _success,
            error: _error,
            complete: _complete
        });
    };


    if (that.getNetworkSponsors) {
    	console.log("getNetworkSponsors");
    }
    that.getNetworkSponsors = function(id, _success, _error, _complete, projection) {
        return that._ajax({
            url: _url + "/api/networks/" + id + "/sponsors",
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


    if (that.getNetworkDefaultTaxonomy) {
    	console.log("getNetworkDefaultTaxonomy");
    }
    that.getNetworkDefaultTaxonomy = function(id, _success, _error, _complete, projection) {
        return that._ajax({
            url: _url + "/api/networks/" + id + "/defaultTaxonomy",
            data: {
            	projection: projection
            },
            success: _success,
            error: _error,
            complete: _complete
        });
    };

    if (that.putNetworkDefaultTaxonomy) {
    	console.log("putNetworkDefaultTaxonomy");
    }
    that.putNetworkDefaultTaxonomy = function(id, defaultTaxonomy, _success, _error, _complete) {
    	if (defaultTaxonomy === null) {
    	    return that._ajax({
    	    	type: "DELETE",
    	    	url: _url + "/api/networks/" + id + "/defaultTaxonomy",
    	        success: _success,
    	        error: _error,
    	        complete: _complete
    	    });		
    	} else {
    	    return that._ajax({
    	    	type: "PUT",
    	        url: _url + "/api/networks/" + id + "/defaultTaxonomy",
    	        contentType: "text/uri-list",
    	        data: defaultTaxonomy,
    	        success: _success,
    	        error: _error,
    	        complete: _complete
    	    });	
    	}
    };


    if (that.getNetworkOwnedTaxonomies) {
    	console.log("getNetworkOwnedTaxonomies");
    }
    that.getNetworkOwnedTaxonomies = function(id, _success, _error, _complete, projection) {
        return that._ajax({
            url: _url + "/api/networks/" + id + "/ownedTaxonomies",
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


    if (that.getNetworkLogo) {
    	console.log("getNetworkLogo");
    }
    that.getNetworkLogo = function(id, _success, _error, _complete, projection) {
        return that._ajax({
            url: _url + "/api/networks/" + id + "/logo",
            data: {
            	projection: projection
            },
            success: _success,
            error: _error,
            complete: _complete
        });
    };

    if (that.putNetworkLogo) {
    	console.log("putNetworkLogo");
    }
    that.putNetworkLogo = function(id, logo, _success, _error, _complete) {
    	if (logo === null) {
    	    return that._ajax({
    	    	type: "DELETE",
    	    	url: _url + "/api/networks/" + id + "/logo",
    	        success: _success,
    	        error: _error,
    	        complete: _complete
    	    });		
    	} else {
    	    return that._ajax({
    	    	type: "PUT",
    	        url: _url + "/api/networks/" + id + "/logo",
    	        contentType: "text/uri-list",
    	        data: logo,
    	        success: _success,
    	        error: _error,
    	        complete: _complete
    	    });	
    	}
    };
/*---------------------------------------------------------------------------*/

/*---------------------------------------------------------------------------*/
	if (that.getNetworkRoles) {
		console.log("getNetworkRoles");
	}
    that.getNetworkRoles = function(_page, _size, _sort, _success, _error, _complete, projection) {
		return that._ajax({
            url: _url + "/api/networkRoles",
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

	if (that.postNetworkRole) {
		console.log("postNetworkRole");
	}
    that.postNetworkRole = function(networkRole, _success, _error, _complete) {
        return that._ajax({
            type: "POST",
            url: _url + "/api/networkRoles",
            contentType: "application/json",
            data: JSON.stringify(networkRole),
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

	if (that.getNetworkRole) {
		console.log("getNetworkRole");
	}
    that.getNetworkRole = function(id, _success, _error, _complete, projection) {
        return that._ajax({
            url: _url + "/api/networkRoles/" + id,
            data: {
                projection: projection
            },            
            success: _success,
            error: _error,
            complete: _complete
        });
    };

	if (that.putNetworkRole) {
		console.log("putNetworkRole");
	}
    that.putNetworkRole = function(networkRole, _success, _error, _complete) {
        return that._ajax({
            type: "PUT",
            url: _url + "/api/networkRoles/" + networkRole.id,
            contentType: "application/json",
            data: JSON.stringify(networkRole),
            success: _success,
            error: _error,
            complete: _complete
        });
    };

	if (that.deleteNetworkRole) {
		console.log("deleteNetworkRole");
	}
    that.deleteNetworkRole = function(id, _success, _error, _complete) {
        return that._ajax({
            type: "DELETE",
            url: _url + "/api/networkRoles/" + id,
            success: _success,
            error: _error,
            complete: _complete
        });
    };


    if (that.getNetworkRoleNetwork) {
    	console.log("getNetworkRoleNetwork");
    }
    that.getNetworkRoleNetwork = function(id, _success, _error, _complete, projection) {
        return that._ajax({
            url: _url + "/api/networkRoles/" + id + "/network",
            data: {
            	projection: projection
            },
            success: _success,
            error: _error,
            complete: _complete
        });
    };

    if (that.putNetworkRoleNetwork) {
    	console.log("putNetworkRoleNetwork");
    }
    that.putNetworkRoleNetwork = function(id, network, _success, _error, _complete) {
    	if (network === null) {
    	    return that._ajax({
    	    	type: "DELETE",
    	    	url: _url + "/api/networkRoles/" + id + "/network",
    	        success: _success,
    	        error: _error,
    	        complete: _complete
    	    });		
    	} else {
    	    return that._ajax({
    	    	type: "PUT",
    	        url: _url + "/api/networkRoles/" + id + "/network",
    	        contentType: "text/uri-list",
    	        data: network,
    	        success: _success,
    	        error: _error,
    	        complete: _complete
    	    });	
    	}
    };


    if (that.getNetworkRolePerson) {
    	console.log("getNetworkRolePerson");
    }
    that.getNetworkRolePerson = function(id, _success, _error, _complete, projection) {
        return that._ajax({
            url: _url + "/api/networkRoles/" + id + "/person",
            data: {
            	projection: projection
            },
            success: _success,
            error: _error,
            complete: _complete
        });
    };

    if (that.putNetworkRolePerson) {
    	console.log("putNetworkRolePerson");
    }
    that.putNetworkRolePerson = function(id, person, _success, _error, _complete) {
    	if (person === null) {
    	    return that._ajax({
    	    	type: "DELETE",
    	    	url: _url + "/api/networkRoles/" + id + "/person",
    	        success: _success,
    	        error: _error,
    	        complete: _complete
    	    });		
    	} else {
    	    return that._ajax({
    	    	type: "PUT",
    	        url: _url + "/api/networkRoles/" + id + "/person",
    	        contentType: "text/uri-list",
    	        data: person,
    	        success: _success,
    	        error: _error,
    	        complete: _complete
    	    });	
    	}
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

    if (that.getPersonComments) {
    	console.log("getPersonComments");
    }
    that.getPersonComments = function(id, _success, _error, _complete, projection) {
        return that._ajax({
            url: _url + "/api/persons/" + id + "/comments",
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


    if (that.getPersonPersonsStationPermissions) {
    	console.log("getPersonPersonsStationPermissions");
    }
    that.getPersonPersonsStationPermissions = function(id, _success, _error, _complete, projection) {
        return that._ajax({
            url: _url + "/api/persons/" + id + "/personsStationPermissions",
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


    if (that.getPersonPersonsNetworkRoles) {
    	console.log("getPersonPersonsNetworkRoles");
    }
    that.getPersonPersonsNetworkRoles = function(id, _success, _error, _complete, projection) {
        return that._ajax({
            url: _url + "/api/persons/" + id + "/personsNetworkRoles",
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


    if (that.getPersonPosts) {
    	console.log("getPersonPosts");
    }
    that.getPersonPosts = function(id, _success, _error, _complete, projection) {
        return that._ajax({
            url: _url + "/api/persons/" + id + "/posts",
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


    if (that.getPersonPromotions) {
    	console.log("getPersonPromotions");
    }
    that.getPersonPromotions = function(id, _success, _error, _complete, projection) {
        return that._ajax({
            url: _url + "/api/persons/" + id + "/promotions",
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


    if (that.getPersonFollowing) {
    	console.log("getPersonFollowing");
    }
    that.getPersonFollowing = function(id, _success, _error, _complete, projection) {
        return that._ajax({
            url: _url + "/api/persons/" + id + "/following",
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
    if (that.patchPersonFollowing) {
    	console.log("patchPersonFollowing");
    }
    that.patchPersonFollowing = function(id, following, _success, _error, _complete) {		
    	var _data = "";
    	for (var i = 0; i < following.length; ++i) {
    		_data += following[i] + "\n";
    	}
        return that._ajax({
        	type: "PATCH",
            url: _url + "/api/persons/" + id + "/following",
            contentType: "text/uri-list",
            data: _data,        
            success: _success,
            error: _error,
            complete: _complete
        });
    };

    if (that.putPersonFollowing) {
    	console.log("putPersonFollowing");
    }
    that.putPersonFollowing = function(id, following, _success, _error, _complete) {		
    	var _data = "";
    	for (var i = 0; i < following.length; ++i) {
    		_data += following[i] + "\n";
    	}
        return that._ajax({
        	type: "PUT",
            url: _url + "/api/persons/" + id + "/following",
            contentType: "text/uri-list",
            data: _data,        
            success: _success,
            error: _error,
            complete: _complete
        });
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

    if (that.findPostsAndPostsPromoted) {
    	console.log("findPostsAndPostsPromoted");
    }
    that.findPostsAndPostsPromoted = function(stationId, termsIds, page, size, sort, _success, _error, _complete, projection) {
        return that._ajax({
            url: _url + "/api/posts/search/findPostsAndPostsPromoted",
            data: {
            	stationId: stationId,
            	termsIds: termsIds,
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

    if (that.findPostsNotPositioned) {
    	console.log("findPostsNotPositioned");
    }
    that.findPostsNotPositioned = function(stationId, termsIds, idsToExclude, page, size, sort, _success, _error, _complete, projection) {
        return that._ajax({
            url: _url + "/api/posts/search/findPostsNotPositioned",
            data: {
            	stationId: stationId,
            	termsIds: termsIds,
            	idsToExclude: idsToExclude,
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

    if (that.findPostsFromOrPromotedToStation) {
    	console.log("findPostsFromOrPromotedToStation");
    }
    that.findPostsFromOrPromotedToStation = function(stationId, page, size, sort, _success, _error, _complete, projection) {
        return that._ajax({
            url: _url + "/api/posts/search/findPostsFromOrPromotedToStation",
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

    if (that.findPosts) {
    	console.log("findPosts");
    }
    that.findPosts = function(stationId, termId, page, size, sort, _success, _error, _complete, projection) {
        return that._ajax({
            url: _url + "/api/posts/search/findPosts",
            data: {
            	stationId: stationId,
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

    if (that.getPostSponsor) {
    	console.log("getPostSponsor");
    }
    that.getPostSponsor = function(id, _success, _error, _complete, projection) {
        return that._ajax({
            url: _url + "/api/posts/" + id + "/sponsor",
            data: {
            	projection: projection
            },
            success: _success,
            error: _error,
            complete: _complete
        });
    };

    if (that.putPostSponsor) {
    	console.log("putPostSponsor");
    }
    that.putPostSponsor = function(id, sponsor, _success, _error, _complete) {
    	if (sponsor === null) {
    	    return that._ajax({
    	    	type: "DELETE",
    	    	url: _url + "/api/posts/" + id + "/sponsor",
    	        success: _success,
    	        error: _error,
    	        complete: _complete
    	    });		
    	} else {
    	    return that._ajax({
    	    	type: "PUT",
    	        url: _url + "/api/posts/" + id + "/sponsor",
    	        contentType: "text/uri-list",
    	        data: sponsor,
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


    if (that.getPostImages) {
    	console.log("getPostImages");
    }
    that.getPostImages = function(id, _success, _error, _complete, projection) {
        return that._ajax({
            url: _url + "/api/posts/" + id + "/images",
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


    if (that.getPostPromotions) {
    	console.log("getPostPromotions");
    }
    that.getPostPromotions = function(id, _success, _error, _complete, projection) {
        return that._ajax({
            url: _url + "/api/posts/" + id + "/promotions",
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
	if (that.getPromotions) {
		console.log("getPromotions");
	}
    that.getPromotions = function(_page, _size, _sort, _success, _error, _complete, projection) {
		return that._ajax({
            url: _url + "/api/promotions",
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

	if (that.postPromotion) {
		console.log("postPromotion");
	}
    that.postPromotion = function(promotion, _success, _error, _complete) {
        return that._ajax({
            type: "POST",
            url: _url + "/api/promotions",
            contentType: "application/json",
            data: JSON.stringify(promotion),
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

	if (that.getPromotion) {
		console.log("getPromotion");
	}
    that.getPromotion = function(id, _success, _error, _complete, projection) {
        return that._ajax({
            url: _url + "/api/promotions/" + id,
            data: {
                projection: projection
            },            
            success: _success,
            error: _error,
            complete: _complete
        });
    };

	if (that.putPromotion) {
		console.log("putPromotion");
	}
    that.putPromotion = function(promotion, _success, _error, _complete) {
        return that._ajax({
            type: "PUT",
            url: _url + "/api/promotions/" + promotion.id,
            contentType: "application/json",
            data: JSON.stringify(promotion),
            success: _success,
            error: _error,
            complete: _complete
        });
    };

	if (that.deletePromotion) {
		console.log("deletePromotion");
	}
    that.deletePromotion = function(id, _success, _error, _complete) {
        return that._ajax({
            type: "DELETE",
            url: _url + "/api/promotions/" + id,
            success: _success,
            error: _error,
            complete: _complete
        });
    };


    if (that.getPromotionPost) {
    	console.log("getPromotionPost");
    }
    that.getPromotionPost = function(id, _success, _error, _complete, projection) {
        return that._ajax({
            url: _url + "/api/promotions/" + id + "/post",
            data: {
            	projection: projection
            },
            success: _success,
            error: _error,
            complete: _complete
        });
    };

    if (that.putPromotionPost) {
    	console.log("putPromotionPost");
    }
    that.putPromotionPost = function(id, post, _success, _error, _complete) {
    	if (post === null) {
    	    return that._ajax({
    	    	type: "DELETE",
    	    	url: _url + "/api/promotions/" + id + "/post",
    	        success: _success,
    	        error: _error,
    	        complete: _complete
    	    });		
    	} else {
    	    return that._ajax({
    	    	type: "PUT",
    	        url: _url + "/api/promotions/" + id + "/post",
    	        contentType: "text/uri-list",
    	        data: post,
    	        success: _success,
    	        error: _error,
    	        complete: _complete
    	    });	
    	}
    };


    if (that.getPromotionPromoter) {
    	console.log("getPromotionPromoter");
    }
    that.getPromotionPromoter = function(id, _success, _error, _complete, projection) {
        return that._ajax({
            url: _url + "/api/promotions/" + id + "/promoter",
            data: {
            	projection: projection
            },
            success: _success,
            error: _error,
            complete: _complete
        });
    };

    if (that.putPromotionPromoter) {
    	console.log("putPromotionPromoter");
    }
    that.putPromotionPromoter = function(id, promoter, _success, _error, _complete) {
    	if (promoter === null) {
    	    return that._ajax({
    	    	type: "DELETE",
    	    	url: _url + "/api/promotions/" + id + "/promoter",
    	        success: _success,
    	        error: _error,
    	        complete: _complete
    	    });		
    	} else {
    	    return that._ajax({
    	    	type: "PUT",
    	        url: _url + "/api/promotions/" + id + "/promoter",
    	        contentType: "text/uri-list",
    	        data: promoter,
    	        success: _success,
    	        error: _error,
    	        complete: _complete
    	    });	
    	}
    };


    if (that.getPromotionStation) {
    	console.log("getPromotionStation");
    }
    that.getPromotionStation = function(id, _success, _error, _complete, projection) {
        return that._ajax({
            url: _url + "/api/promotions/" + id + "/station",
            data: {
            	projection: projection
            },
            success: _success,
            error: _error,
            complete: _complete
        });
    };

    if (that.putPromotionStation) {
    	console.log("putPromotionStation");
    }
    that.putPromotionStation = function(id, station, _success, _error, _complete) {
    	if (station === null) {
    	    return that._ajax({
    	    	type: "DELETE",
    	    	url: _url + "/api/promotions/" + id + "/station",
    	        success: _success,
    	        error: _error,
    	        complete: _complete
    	    });		
    	} else {
    	    return that._ajax({
    	    	type: "PUT",
    	        url: _url + "/api/promotions/" + id + "/station",
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
/*---------------------------------------------------------------------------*/

/*---------------------------------------------------------------------------*/
	if (that.getSponsors) {
		console.log("getSponsors");
	}
    that.getSponsors = function(_page, _size, _sort, _success, _error, _complete, projection) {
		return that._ajax({
            url: _url + "/api/sponsors",
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

	if (that.postSponsor) {
		console.log("postSponsor");
	}
    that.postSponsor = function(sponsor, _success, _error, _complete) {
        return that._ajax({
            type: "POST",
            url: _url + "/api/sponsors",
            contentType: "application/json",
            data: JSON.stringify(sponsor),
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

	if (that.getSponsor) {
		console.log("getSponsor");
	}
    that.getSponsor = function(id, _success, _error, _complete, projection) {
        return that._ajax({
            url: _url + "/api/sponsors/" + id,
            data: {
                projection: projection
            },            
            success: _success,
            error: _error,
            complete: _complete
        });
    };

	if (that.putSponsor) {
		console.log("putSponsor");
	}
    that.putSponsor = function(sponsor, _success, _error, _complete) {
        return that._ajax({
            type: "PUT",
            url: _url + "/api/sponsors/" + sponsor.id,
            contentType: "application/json",
            data: JSON.stringify(sponsor),
            success: _success,
            error: _error,
            complete: _complete
        });
    };

	if (that.deleteSponsor) {
		console.log("deleteSponsor");
	}
    that.deleteSponsor = function(id, _success, _error, _complete) {
        return that._ajax({
            type: "DELETE",
            url: _url + "/api/sponsors/" + id,
            success: _success,
            error: _error,
            complete: _complete
        });
    };


    if (that.getSponsorLogo) {
    	console.log("getSponsorLogo");
    }
    that.getSponsorLogo = function(id, _success, _error, _complete, projection) {
        return that._ajax({
            url: _url + "/api/sponsors/" + id + "/logo",
            data: {
            	projection: projection
            },
            success: _success,
            error: _error,
            complete: _complete
        });
    };

    if (that.putSponsorLogo) {
    	console.log("putSponsorLogo");
    }
    that.putSponsorLogo = function(id, logo, _success, _error, _complete) {
    	if (logo === null) {
    	    return that._ajax({
    	    	type: "DELETE",
    	    	url: _url + "/api/sponsors/" + id + "/logo",
    	        success: _success,
    	        error: _error,
    	        complete: _complete
    	    });		
    	} else {
    	    return that._ajax({
    	    	type: "PUT",
    	        url: _url + "/api/sponsors/" + id + "/logo",
    	        contentType: "text/uri-list",
    	        data: logo,
    	        success: _success,
    	        error: _error,
    	        complete: _complete
    	    });	
    	}
    };


    if (that.getSponsorNetwork) {
    	console.log("getSponsorNetwork");
    }
    that.getSponsorNetwork = function(id, _success, _error, _complete, projection) {
        return that._ajax({
            url: _url + "/api/sponsors/" + id + "/network",
            data: {
            	projection: projection
            },
            success: _success,
            error: _error,
            complete: _complete
        });
    };

    if (that.putSponsorNetwork) {
    	console.log("putSponsorNetwork");
    }
    that.putSponsorNetwork = function(id, network, _success, _error, _complete) {
    	if (network === null) {
    	    return that._ajax({
    	    	type: "DELETE",
    	    	url: _url + "/api/sponsors/" + id + "/network",
    	        success: _success,
    	        error: _error,
    	        complete: _complete
    	    });		
    	} else {
    	    return that._ajax({
    	    	type: "PUT",
    	        url: _url + "/api/sponsors/" + id + "/network",
    	        contentType: "text/uri-list",
    	        data: network,
    	        success: _success,
    	        error: _error,
    	        complete: _complete
    	    });	
    	}
    };


    if (that.getSponsorImages) {
    	console.log("getSponsorImages");
    }
    that.getSponsorImages = function(id, _success, _error, _complete, projection) {
        return that._ajax({
            url: _url + "/api/sponsors/" + id + "/images",
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

    if (that.findByName) {
    	console.log("findByName");
    }
    that.findByName = function(name, _success, _error, _complete, projection) {
        return that._ajax({
            url: _url + "/api/stations/search/findByName",
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

    if (that.findByPersonIdAndNetworkId) {
    	console.log("findByPersonIdAndNetworkId");
    }
    that.findByPersonIdAndNetworkId = function(personId, networkId, _success, _error, _complete, projection) {
        return that._ajax({
            url: _url + "/api/stations/search/findByPersonIdAndNetworkId",
            data: {
            	personId: personId,
            	networkId: networkId,

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

    if (that.getStationNetworks) {
    	console.log("getStationNetworks");
    }
    that.getStationNetworks = function(id, _success, _error, _complete, projection) {
        return that._ajax({
            url: _url + "/api/stations/" + id + "/networks",
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
    if (that.patchStationNetworks) {
    	console.log("patchStationNetworks");
    }
    that.patchStationNetworks = function(id, networks, _success, _error, _complete) {		
    	var _data = "";
    	for (var i = 0; i < networks.length; ++i) {
    		_data += networks[i] + "\n";
    	}
        return that._ajax({
        	type: "PATCH",
            url: _url + "/api/stations/" + id + "/networks",
            contentType: "text/uri-list",
            data: _data,        
            success: _success,
            error: _error,
            complete: _complete
        });
    };

    if (that.putStationNetworks) {
    	console.log("putStationNetworks");
    }
    that.putStationNetworks = function(id, networks, _success, _error, _complete) {		
    	var _data = "";
    	for (var i = 0; i < networks.length; ++i) {
    		_data += networks[i] + "\n";
    	}
        return that._ajax({
        	type: "PUT",
            url: _url + "/api/stations/" + id + "/networks",
            contentType: "text/uri-list",
            data: _data,        
            success: _success,
            error: _error,
            complete: _complete
        });
    };


    if (that.getStationPersonsStationRoles) {
    	console.log("getStationPersonsStationRoles");
    }
    that.getStationPersonsStationRoles = function(id, _success, _error, _complete, projection) {
        return that._ajax({
            url: _url + "/api/stations/" + id + "/personsStationRoles",
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


    if (that.getStationPromotions) {
    	console.log("getStationPromotions");
    }
    that.getStationPromotions = function(id, _success, _error, _complete, projection) {
        return that._ajax({
            url: _url + "/api/stations/" + id + "/promotions",
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
/*---------------------------------------------------------------------------*/

/*---------------------------------------------------------------------------*/
	if (that.getStationRoles) {
		console.log("getStationRoles");
	}
    that.getStationRoles = function(_page, _size, _sort, _success, _error, _complete, projection) {
		return that._ajax({
            url: _url + "/api/stationRoles",
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

	if (that.postStationRole) {
		console.log("postStationRole");
	}
    that.postStationRole = function(stationRole, _success, _error, _complete) {
        return that._ajax({
            type: "POST",
            url: _url + "/api/stationRoles",
            contentType: "application/json",
            data: JSON.stringify(stationRole),
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

	if (that.getStationRole) {
		console.log("getStationRole");
	}
    that.getStationRole = function(id, _success, _error, _complete, projection) {
        return that._ajax({
            url: _url + "/api/stationRoles/" + id,
            data: {
                projection: projection
            },            
            success: _success,
            error: _error,
            complete: _complete
        });
    };

	if (that.putStationRole) {
		console.log("putStationRole");
	}
    that.putStationRole = function(stationRole, _success, _error, _complete) {
        return that._ajax({
            type: "PUT",
            url: _url + "/api/stationRoles/" + stationRole.id,
            contentType: "application/json",
            data: JSON.stringify(stationRole),
            success: _success,
            error: _error,
            complete: _complete
        });
    };

	if (that.deleteStationRole) {
		console.log("deleteStationRole");
	}
    that.deleteStationRole = function(id, _success, _error, _complete) {
        return that._ajax({
            type: "DELETE",
            url: _url + "/api/stationRoles/" + id,
            success: _success,
            error: _error,
            complete: _complete
        });
    };


    if (that.getStationRoleStation) {
    	console.log("getStationRoleStation");
    }
    that.getStationRoleStation = function(id, _success, _error, _complete, projection) {
        return that._ajax({
            url: _url + "/api/stationRoles/" + id + "/station",
            data: {
            	projection: projection
            },
            success: _success,
            error: _error,
            complete: _complete
        });
    };

    if (that.putStationRoleStation) {
    	console.log("putStationRoleStation");
    }
    that.putStationRoleStation = function(id, station, _success, _error, _complete) {
    	if (station === null) {
    	    return that._ajax({
    	    	type: "DELETE",
    	    	url: _url + "/api/stationRoles/" + id + "/station",
    	        success: _success,
    	        error: _error,
    	        complete: _complete
    	    });		
    	} else {
    	    return that._ajax({
    	    	type: "PUT",
    	        url: _url + "/api/stationRoles/" + id + "/station",
    	        contentType: "text/uri-list",
    	        data: station,
    	        success: _success,
    	        error: _error,
    	        complete: _complete
    	    });	
    	}
    };


    if (that.getStationRolePerson) {
    	console.log("getStationRolePerson");
    }
    that.getStationRolePerson = function(id, _success, _error, _complete, projection) {
        return that._ajax({
            url: _url + "/api/stationRoles/" + id + "/person",
            data: {
            	projection: projection
            },
            success: _success,
            error: _error,
            complete: _complete
        });
    };

    if (that.putStationRolePerson) {
    	console.log("putStationRolePerson");
    }
    that.putStationRolePerson = function(id, person, _success, _error, _complete) {
    	if (person === null) {
    	    return that._ajax({
    	    	type: "DELETE",
    	    	url: _url + "/api/stationRoles/" + id + "/person",
    	        success: _success,
    	        error: _error,
    	        complete: _complete
    	    });		
    	} else {
    	    return that._ajax({
    	    	type: "PUT",
    	        url: _url + "/api/stationRoles/" + id + "/person",
    	        contentType: "text/uri-list",
    	        data: person,
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

    if (that.findByStationId) {
    	console.log("findByStationId");
    }
    that.findByStationId = function(stationId, _success, _error, _complete, projection) {
        return that._ajax({
            url: _url + "/api/taxonomies/search/findByStationId",
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

    if (that.findByTypeAndName) {
    	console.log("findByTypeAndName");
    }
    that.findByTypeAndName = function(type, name, _success, _error, _complete, projection) {
        return that._ajax({
            url: _url + "/api/taxonomies/search/findByTypeAndName",
            data: {
            	type: type,
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

    if (that.getTaxonomyNetworks) {
    	console.log("getTaxonomyNetworks");
    }
    that.getTaxonomyNetworks = function(id, _success, _error, _complete, projection) {
        return that._ajax({
            url: _url + "/api/taxonomies/" + id + "/networks",
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


    if (that.getTaxonomyDefaultNetworks) {
    	console.log("getTaxonomyDefaultNetworks");
    }
    that.getTaxonomyDefaultNetworks = function(id, _success, _error, _complete, projection) {
        return that._ajax({
            url: _url + "/api/taxonomies/" + id + "/defaultNetworks",
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

    if (that.getTermCells) {
    	console.log("getTermCells");
    }
    that.getTermCells = function(id, _success, _error, _complete, projection) {
        return that._ajax({
            url: _url + "/api/terms/" + id + "/cells",
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


    if (that.getTermPosts) {
    	console.log("getTermPosts");
    }
    that.getTermPosts = function(id, _success, _error, _complete, projection) {
        return that._ajax({
            url: _url + "/api/terms/" + id + "/posts",
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


    if (that.getTermRows) {
    	console.log("getTermRows");
    }
    that.getTermRows = function(id, _success, _error, _complete, projection) {
        return that._ajax({
            url: _url + "/api/terms/" + id + "/rows",
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

    return that;
}