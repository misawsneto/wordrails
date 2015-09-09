package com.wordrails.business;

import org.springframework.data.rest.core.config.Projection;

@Projection(types=TrixFile.class)
public interface FileProjection {
	Integer getId();
}