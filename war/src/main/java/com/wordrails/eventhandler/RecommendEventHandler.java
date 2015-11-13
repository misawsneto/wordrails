package com.wordrails.eventhandler;

import com.wordrails.business.Recommend;
import com.wordrails.business.UnauthorizedException;
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

@RepositoryEventHandler(Recommend.class)
@Component
public class RecommendEventHandler {
	
	private @Autowired FavoriteSecurityChecker recommendSecurityChecker;
	private @Autowired QueryPersistence queryPersistence;
	
	@HandleBeforeCreate
	public void handleBeforeCreate(Recommend recommend) throws UnauthorizedException {
		if(!recommendSecurityChecker.canWriteRecommend(recommend)){
			throw new UnauthorizedException();
		}
	}

	@HandleBeforeSave
	public void handleBeforeSave(Recommend recommend) throws UnauthorizedException {
		if(!recommendSecurityChecker.canWriteRecommend(recommend)){
			throw new UnauthorizedException();
		}
	}

	@HandleBeforeDelete
	public void handleBeforeDelete(Recommend recommend) throws UnauthorizedException {
		if(!recommendSecurityChecker.canWriteRecommend(recommend)){
			throw new UnauthorizedException();
		}
	}
	
	@HandleAfterCreate
	public void handleAfterCreate(Recommend recommend){
		queryPersistence.incrementRecommendsCount(recommend.post.id);
	}
	
	@HandleAfterDelete
	public void handleAfterDelete(Recommend recommend){
		queryPersistence.decrementRecommendsCount(recommend.post.id);
	}
	
}