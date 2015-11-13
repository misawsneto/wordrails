package com.wordrails.comparator;

import java.util.List;

import com.wordrails.domain.Post;
import com.wordrails.domain.Row;

public class TermPerspectiveDifference {
	
	public Post splashedPostToDelete;
	public Post splashedPostToAdd;
	
	public List<Row> ordinaryRowsToDelete;
	public List<Row> ordinaryRowsToAdd;
	
	public Row featuredRowToDelete;
	public Row featuredRowToAdd;
}