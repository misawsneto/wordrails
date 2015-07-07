package com.wordrails.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wordrails.services.CacheService;

@RepositoryEventHandler(Network.class)
@Component
public class NetworkEventHandler {
	
	@Autowired
	private CacheService cacheService;  
	
	@HandleBeforeCreate
	@Transactional
	public void handleBeforeCreate(Network network) {
	}
	
	@HandleAfterSave
	@Transactional
	public void handleAfterSave(Network network){
		cacheService.updateNetwork(network.id);
		cacheService.updateNetwork(network.subdomain);
	}
}