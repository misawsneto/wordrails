package com.wordrails.business;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * @author arthur
 */
public class WordpressGetPostsParams {

	public Integer id;

	@JsonProperty("date_ini")
	@SerializedName("date_ini")
	public Date initialDate;

	@JsonProperty("date_end")
	@SerializedName("date_end")
	public Date finalDate;

	public Integer limit;
}
