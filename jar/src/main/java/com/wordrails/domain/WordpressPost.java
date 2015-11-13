package com.wordrails.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class WordpressPost {

	@JsonProperty("ID")
	public Integer id;
	public String title;

	@JsonProperty("content_raw")
	public String body;
	public String status;
	public String slug;
	public Date date;
	public Date modified;

	@JsonProperty("terms")
	public WordpressTerms terms;

	public WordpressPost() {
	}

	public WordpressPost(String title, String body, Date date) {
		this.title = title;
		this.body = body;
		this.date = date;
	}

	public WordpressPost(String title, String body, String status, Date date) {
		this.title = title;
		this.body = body;
		this.status = status;
		this.date = date;
	}

}
