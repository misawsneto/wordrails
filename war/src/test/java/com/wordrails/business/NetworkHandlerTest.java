package com.wordrails.business;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.wordrails.persistence.NetworkRepository;
import com.wordrails.test.AbstractTest;

@Component
public class NetworkHandlerTest extends AbstractTest {
	
	@Autowired
	NetworkEventHandler handler;
	
	@Test
	public void handleBeforeCreateTest() throws Exception {
//		Network network = new Network();
//		network.defaultTaxonomy = null;
//		network.name = "wordRAILS";
//
//		handler.handleBeforeCreate(network);
//		context.getBean(NetworkRepository.class).save(network);
//		
//		Assert.notNull(network.defaultTaxonomy);
	}
}