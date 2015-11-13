package com.wordrails.domain;

import java.util.Set;

import org.springframework.data.rest.core.config.Projection;

@Projection(types=Sponsor.class)
public interface SponsorProjection {
	Integer getId();
	
	String getLogo();
	String getNetwork();
	Set<Image> getImages();
	String getName();
	String getKeywords();
}