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
	
	public Integer imageId;
	public Integer imageSmallId;
	public Integer imageMediumId;
	public Integer imageLargeId;

	public boolean imageLandscape;

}