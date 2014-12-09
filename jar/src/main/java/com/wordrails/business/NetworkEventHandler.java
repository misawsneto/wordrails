package com.wordrails.business;

import java.util.HashSet;

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
	private @Autowired NetworkRepository networkRepository;
	private @Autowired TaxonomyRepository taxonomyRepository;
	
	@HandleBeforeCreate
	@Transactional
	public void handleBeforeCreate(Network network) {
		if (network.defaultTaxonomy == null) {
			Taxonomy taxonomy = new Taxonomy();
			taxonomy.name = "Default";
			taxonomy.type = Taxonomy.NETWORK_TAXONOMY;
			taxonomy.owningNetwork = network;
			taxonomyRepository.save(taxonomy);

			network.taxonomies = new HashSet<Taxonomy>();
			network.taxonomies.add(taxonomy);
			network.defaultTaxonomy = taxonomy;
			networkRepository.save(network);
		}
	}
}