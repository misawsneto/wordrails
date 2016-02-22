package co.xarx.trix.services.elasticsearch;

import co.xarx.trix.domain.query.Command;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;

import java.util.List;

public class ElasticSearchCommand implements Command {

	public String highlightedField;
	private BoolQueryBuilder boolQueryBuilder;
	private List<FieldSortBuilder> fieldSortBuilders;

	public BoolQueryBuilder getBoolQueryBuilder() {
		return boolQueryBuilder;
	}

	public void setBoolQueryBuilder(BoolQueryBuilder boolQueryBuilder) {
		this.boolQueryBuilder = boolQueryBuilder;
	}

	public List<FieldSortBuilder> getFieldSortBuilders() {
		return fieldSortBuilders;
	}

	public void setFieldSortBuilders(List<FieldSortBuilder> fieldSortBuilders) {
		this.fieldSortBuilders = fieldSortBuilders;
	}

	public String getHighlightedField() {
		return highlightedField;
	}

	public void setHighlightedField(String highlightedField) {
		this.highlightedField = highlightedField;
	}
}
