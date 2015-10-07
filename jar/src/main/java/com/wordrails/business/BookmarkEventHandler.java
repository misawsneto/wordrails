package com.wordrails.business;

import com.wordrails.elasticsearch.BookmarkEsRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.*;
import org.springframework.stereotype.Component;

import com.wordrails.security.FavoriteSecurityChecker;

@RepositoryEventHandler(Bookmark.class)
@Component
public class BookmarkEventHandler {
	
	private @Autowired FavoriteSecurityChecker favoriteSecurityChecker;
	private @Autowired BookmarkEsRespository bookmarkEsRespository;
	
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

	@HandleAfterCreate
	public void handleAfterCreate(Bookmark bookmark){
		bookmarkEsRespository.save(bookmark);
	}

	@HandleAfterDelete
	public void handleAfterDelete(Bookmark bookmark){
		bookmarkEsRespository.delete(bookmark);
	}
}