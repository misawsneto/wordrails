package com.wordrails.persistence;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.wordrails.test.AbstractTest;

public class TaxonomyRepositoryTest extends AbstractTest {
	private @Autowired TaxonomyRepository repository;
	
	@Test
	public void test() {
		repository.findByStationId(1);
	}	
}