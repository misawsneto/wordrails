package com.wordrails.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeDelete;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

import com.wordrails.security.FavoriteSecurityChecker;

@RepositoryEventHandler(Bookmark.class)
@Component
public class BookmarkEventHandler {
	
	private @Autowired FavoriteSecurityChecker favoriteSecurityChecker;
	
	@HandleBeforeCreate
	public void handleBeforeCreate(Bookmark bookmark) throws UnauthorizedException {
		if(!favoriteSecurityChecker.canWriteBookmark(bookmark)){
			throw new UnauthorizedException();
		}
	}

	@HandleBeforeSave
	public void handleBeforeSave(Bookmark bookmark) throws UnauthorizedException {
		if(!favoriteSecurityChecker.canWriteBookmark(bookmark)){
			throw new UnauthorizedException();
		}
	}

	@HandleBeforeDelete
	public void handleBeforeDelete(Bookmark bookmark) throws UnauthorizedException {
		if(!favoriteSecurityChecker.canWriteBookmark(bookmark)){
			throw new UnauthorizedException();
		}
	}
}