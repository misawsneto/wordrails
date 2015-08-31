package com.wordrails.api;

import java.io.Serializable;
import java.util.Date;

public class PostView implements Serializable {
	private static final long serialVersionUID = -1474032487285763669L;

	public String title;
	public Integer postId;
	public String smallId;
	public String mediumId;
	public String largeId;
	public Boolean sponsor;
	public java.util.Date date;
	public String snippet;
	public String body;
	public String topper;
	public String state;
	public int readsCount;
	public int favoritesCount;
	public int bookmarksCount;
	public int recommendsCount;
	public int commentsCount;

	public String imageId;
	public String imageSmallId;
	public String imageMediumId;
	public String imageLargeId;
	public Integer readTime;

	public boolean imageLandscape;

	public String authorName;
	public String authorUsername;
	public Integer authorId;

	public String authorEmail;
	public String authorTwitter;
	public String authorSmallImageId;
	
	public String stationName;
	public Integer stationId;

	public String slug;
	public String externalFeaturedImgUrl;
	public String externalVideoUrl;

	public String imageCaptionText;
	public String imageCreditsText;
	public String imageTitleText;

	public String authorCoverMediumId;

	public String authorImageSmallId;

	public Double lat;

	public Double lng;

	public String subheading;

	public Date scheduledDate;

	public boolean notify;
}