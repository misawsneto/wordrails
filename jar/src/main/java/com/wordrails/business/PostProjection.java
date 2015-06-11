package com.wordrails.business;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.data.rest.core.config.Projection;

@Projection(types=Post.class)
public interface PostProjection {
	Integer getId();
	
	@JsonFormat(shape=JsonFormat.Shape.NUMBER)
	Date getDate();
	
	String getTitle();
	String getSubheading();
	String getTopper();
	String getBody();
	Sponsor getSponsor();
	Set<Promotion> getPromotions();
	Person getAuthor();
	Station getStation();
	Integer getWordpressId();
	String getSlug();
	String getOriginalSlug();
	List<Term> getTerms();
	ImageProjection getFeaturedImage();
	List<ImageProjection> getImages();
	String getState();
	boolean getImageLandscape();
	Integer getImageId();
	Integer getImageSmallId();
	Integer getImageMediumId();
	Integer getImageLargeId();
	Integer getFavoritesCount();
    Integer getReadsCount();
	Integer getRecommendsCount();
	Integer getCommentsCount();
	Date getUpdatedAt();
	String getExternalFeaturedImgUrl();
	String getExternalVideoUrl();
	Integer getReadTime();
	String getImageCaptionText();
	String getImageCreditsText();
	Boolean getNotify();
	Double getLat();
	Double getLng();
}