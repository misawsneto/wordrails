package com.wordrails.api;

import java.util.List;
import java.util.Map;

public class SearchView {
	public List<PostView> posts;
	public int hits;
	public Map<TermDto, Integer> facets;
}

