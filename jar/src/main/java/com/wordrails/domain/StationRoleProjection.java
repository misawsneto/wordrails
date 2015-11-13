package com.wordrails.domain;

import org.springframework.data.rest.core.config.Projection;

@Projection(types=StationRole.class)
public interface  StationRoleProjection {
	Integer getId();
	Station getStation();
	Person getPerson();
	Boolean getEditor();
	Boolean getWriter();
	Boolean getAdmin();
}