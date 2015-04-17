package com.wordrails.business;

import java.util.Date;
import java.util.List;

import org.springframework.data.rest.core.config.Projection;

import com.fasterxml.jackson.annotation.JsonFormat;

@Projection(types=Post.class)
public interface PostProjection {
	Integer getId();
	
	@JsonFormat(shape=JsonFormat.Shape.NUMBER)
	Date getDate();
	
	String getTitle();
	String getTopper();
	String getBody();
	Sponsor getSponsor();
	Person getAuthor();
	Station getStation();
	List<Term> getTerms();
	ImageProjection getFeaturedImage();
	List<ImageProjection> getImages();
	String getState();
	boolean getImageLandscape();
	Integer getImageId();
	Integer getImageSmallId();
	Integer getImageMediumId();
	Integer getImageLargeId();
}