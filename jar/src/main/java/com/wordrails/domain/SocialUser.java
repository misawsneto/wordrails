package com.wordrails.domain;


public interface SocialUser {

	String getProviderId();

	String getId();
	String getName();
	String getEmail();
	String getCoverUrl();
	String getProfileUrl();
	String getProfileImageUrl();
}
