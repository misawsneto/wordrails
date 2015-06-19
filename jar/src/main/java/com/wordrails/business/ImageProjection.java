package com.wordrails.business;

import org.springframework.data.rest.core.config.Projection;

@Projection(types=Image.class)
public interface ImageProjection {
	Integer getId();
	FileProjection getSmall();
	FileProjection getMedium();
	FileProjection getLarge();
	Integer getPostId();
}