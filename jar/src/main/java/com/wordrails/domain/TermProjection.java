package com.wordrails.domain;

import java.util.Set;

import org.springframework.data.rest.core.config.Projection;

@Projection(types=Term.class)
public interface TermProjection {

	Integer getId();
	String getName();
	Taxonomy getTaxonomy();
	Term getParent();
	Set<Term> getChildren();
	String getColor();
}
