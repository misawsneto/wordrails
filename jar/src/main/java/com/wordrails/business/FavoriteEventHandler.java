package com.wordrails.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleAfterDelete;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeDelete;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

import com.wordrails.persistence.QueryPersistence;
import com.wordrails.security.FavoriteSecurityChecker;

@RepositoryEventHandler(Favorite.class)
@Component
public class FavoriteEventHandler {
	
	private @Autowired FavoriteSecurityChecker favoriteSecurityChecker;
	private @Autowired QueryPersistence queryPersistence;
	
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
	
	@HandleAfterCreate
	public void handleAfterCreate(Favorite favorite){
		queryPersistence.incrementFavoritesCount(favorite.post.id);
	}
	
	@HandleAfterDelete
	public void handleAfterDelete(Favorite favorite){
		queryPersistence.decrementFavoritesCount(favorite.post.id);
	}
	
}