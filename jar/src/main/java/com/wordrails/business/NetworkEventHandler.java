package com.wordrails.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wordrails.persistence.NetworkRepository;
import com.wordrails.persistence.TaxonomyRepository;

@RepositoryEventHandler(Network.class)
@Component
public class NetworkEventHandler {
	
	@HandleBeforeCreate
	@Transactional
	public void handleBeforeCreate(Network network) {
	}
}