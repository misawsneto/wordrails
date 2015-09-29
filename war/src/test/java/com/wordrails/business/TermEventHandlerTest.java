package com.wordrails.business;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.wordrails.persistence.TaxonomyRepository;
import com.wordrails.test.AbstractTest;

public class TermEventHandlerTest extends AbstractTest {
	
	@Autowired NetworkEventHandler networkEventHandler;
	@Autowired TermEventHandler termEventHandler;
	
	@Test
	public void test() throws Exception {		
//		Network network = new Network();
//		network.name = "Network";
//		networkEventHandler.handleBeforeCreate(network);
//
//		Taxonomy taxonomy = new Taxonomy();
//		taxonomy.owningNetwork = network;
//		taxonomy.type = Taxonomy.NETWORK_TAXONOMY;
//		taxonomy = context.getBean(TaxonomyRepository.class).save(taxonomy);
	}
}