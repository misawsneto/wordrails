package com.wordrails.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeDelete;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

import com.wordrails.security.FavoriteSecurityChecker;

@RepositoryEventHandler(Favorite.class)
@Component
public class FavoriteEventHandler {
	
	private @Autowired FavoriteSecurityChecker favoriteSecurityChecker;
	
	@HandleBeforeCreate
	public void handleBeforeCreate(Favorite favorite) throws UnauthorizedException {
		if(!favoriteSecurityChecker.canWrite(favorite)){
			throw new UnauthorizedException();
		}
	}

	@HandleBeforeSave
	public void handleBeforeSave(Favorite favorite) throws UnauthorizedException {
		if(!favoriteSecurityChecker.canWrite(favorite)){
			throw new UnauthorizedException();
		}
	}

	@HandleBeforeDelete
	public void handleBeforeDelete(Favorite favorite) throws UnauthorizedException {
		if(!favoriteSecurityChecker.canWrite(favorite)){
			throw new UnauthorizedException();
		}
	}
}