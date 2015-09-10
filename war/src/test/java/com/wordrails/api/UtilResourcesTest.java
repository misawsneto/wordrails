package com.wordrails.api;

import com.wordrails.business.Station;
import com.wordrails.business.Taxonomy;
import com.wordrails.persistence.TaxonomyRepository;
import com.wordrails.test.AbstractTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;


@Component
@TransactionConfiguration
@Transactional
public class UtilResourcesTest extends AbstractTest {

	private
	@PersistenceContext
	EntityManager manager;
	@Autowired UtilResource utilResource;

	@Autowired TaxonomyRepository taxonomyRepository;

	@Test
	public void testResources() throws Exception {
//		utilResource.updateStationTaxonomies(null);
//		List<Taxonomy> taxonomy = taxonomyRepository.findStationTaxonomy(9);
//		taxonomy.size();
	}
}