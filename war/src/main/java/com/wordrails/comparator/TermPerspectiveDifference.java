package com.wordrails.comparator;

import java.util.List;

import com.wordrails.business.Post;
import com.wordrails.business.Row;

public class TermPerspectiveDifference {
	
	public Post splashedPostToDelete;
	public Post splashedPostToAdd;
	
	public List<Row> ordinaryRowsToDelete;
	public List<Row> ordinaryRowsToAdd;
	
	public Row featuredRowToDelete;
	public Row featuredRowToAdd;
}