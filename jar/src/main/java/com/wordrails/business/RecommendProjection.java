package com.wordrails.business;

import java.util.Date;

import org.springframework.data.rest.core.config.Projection;

import com.fasterxml.jackson.annotation.JsonFormat;

@Projection(types=Recommend.class)
public interface RecommendProjection {
	Integer getId();
	
	@JsonFormat(shape=JsonFormat.Shape.NUMBER)
	Date getCreatedAt();
	
	Post getPost();
	Person getPerson();
}