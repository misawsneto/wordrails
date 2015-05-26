package com.wordrails.api;

import java.io.Serializable;

public class PostView implements Serializable {
	private static final long serialVersionUID = -1474032487285763669L;
	
	public String title;
	public Integer postId;
	public Integer smallId;
	public Integer mediumId;
	public Integer largeId;
	public Boolean sponsor;
	public java.util.Date date;
	public String snippet;
	public String topper;
	public String state;
	public int readsCount;
	public int favoritesCount;
	public int recommendsCount;
	public int commentsCount;
	
	public Integer imageId;
	public Integer imageSmallId;
	public Integer imageMediumId;
	public Integer imageLargeId;
	public Integer readTime;

	public boolean imageLandscape;

	public String authorName;
	public Integer authorId;

	public String authorEmail;
	public String authorTwitter;
	public Integer authorSmallImageId;

	public String slug;
	public String externalFeaturedImgUrl;
	public String externalVideoUrl;
	
	public String imageCaptionText;
	public String imageCreditsText;

	public Integer authorCoverMediumId;

	public Integer authorImageSmallId;
	
}