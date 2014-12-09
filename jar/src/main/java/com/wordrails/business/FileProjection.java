package com.wordrails.business;

import org.springframework.data.rest.core.config.Projection;

@Projection(types=File.class)
public interface FileProjection {
	Integer getId();
}