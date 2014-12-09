package com.wordrails.business;

import org.springframework.data.rest.core.config.Projection;

@Projection(types=Network.class)
public interface NetworkProjection {
	Integer getId();
	String getName();
}