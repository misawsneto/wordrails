package com.wordrails.domain;

import org.springframework.data.rest.core.config.Projection;

@Projection(types=NetworkRole.class)
public interface NetworkRoleProjection {
	Integer getId();
	Person getPerson();
	Boolean getAdmin();
}