package co.xarx.trix.domain.query;

import co.xarx.trix.domain.BaseEntity;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;

import java.util.List;

public class ElasticSearchQuery<T> extends BaseEntity {

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

	public List<T> execute(ElasticSearchExecutor<T> executor, Integer size, Integer from) {
		return executor.execute(this, size, from);
	}
}
