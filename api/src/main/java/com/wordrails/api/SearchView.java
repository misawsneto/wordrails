package com.wordrails.api;

import org.json.simple.JSONObject;

import java.util.List;
import java.util.Map;

public class SearchView {
	public List<JSONObject> posts;
	public int hits;
	//TODO implement facets in search response
	public Map<TermDto, Integer> facets;
}

