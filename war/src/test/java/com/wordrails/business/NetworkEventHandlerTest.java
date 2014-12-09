package com.wordrails.business;

import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.wordrails.persistence.NetworkRepository;
import com.wordrails.test.AbstractTest;

@Component
public class NetworkEventHandlerTest extends AbstractTest {
	
	@Autowired
	NetworkEventHandler handler;
	
	@Test
	public void handleBeforeCreateTest() throws Exception {
		Network network1 = new Network();
		network1.defaultTaxonomy = null;
		network1.name = "wordRAILS";

		handler.handleBeforeCreate(network1);
		context.getBean(NetworkRepository.class).save(network1);
		
		Assert.notNull(network1.defaultTaxonomy);

		Network network2 = new Network();
		network2.defaultTaxonomy = null;
		
		handler.handleBeforeCreate(network2);
		boolean thrownExcpetion = false;
		try{
			context.getBean(NetworkRepository.class).save(network2);
		}catch(ConstraintViolationException e){
			thrownExcpetion = true;
		}
		Assert.isTrue(thrownExcpetion);
	}
}