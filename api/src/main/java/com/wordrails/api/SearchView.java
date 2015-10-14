package com.wordrails.api;

import org.json.simple.JSONObject;

import java.util.List;
import java.util.Map;

public class SearchView {
	public List<PostView> posts;
	public int hits;
	//TODO implement facets in search response
	public Map<TermDto, Integer> facets;
}

