package com.wordrails.business;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

import com.wordrails.persistence.PromotionRepository;

@RepositoryEventHandler(Promotion.class)
@Component
public class PromotionEventHandler {
	
	private @Autowired PromotionRepository promotionRepository;
	
	@HandleBeforeCreate
	public void handleBeforeCreate(Promotion promotion) throws UnauthorizedException {
		promotion.date = new Date();
	}
}