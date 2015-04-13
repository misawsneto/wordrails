package com.wordrails.business;

import java.util.Date;

import org.springframework.data.rest.core.config.Projection;

import com.fasterxml.jackson.annotation.JsonFormat;

@Projection(types=Favorite.class)
public interface FavoriteProjection {
	Integer getId();
	
	@JsonFormat(shape=JsonFormat.Shape.NUMBER)
	Date getCreatedAt();
	
	Post getPost();
	Person getPerson();
}