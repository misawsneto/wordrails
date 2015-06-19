package com.wordrails.business;

import java.util.Set;

import org.springframework.data.rest.core.config.Projection;

@Projection(types=Taxonomy.class)
public interface TaxonomyProjection {
	Integer getId();
	
	String getType();
	String getName();
	Set<Network> getNetworks();
	Set<Term> terms();
	Network owningNetwork();
	Station owningStation();
}