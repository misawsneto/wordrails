package co.xarx.trix.comparator;

import java.util.List;

import co.xarx.trix.domain.Post;
import co.xarx.trix.domain.Row;

public class TermPerspectiveDifference {
	
	public Post splashedPostToDelete;
	public Post splashedPostToAdd;
	
	public List<Row> ordinaryRowsToDelete;
	public List<Row> ordinaryRowsToAdd;
	
	public Row featuredRowToDelete;
	public Row featuredRowToAdd;
}