package co.xarx.trix.domain.query;

import co.xarx.trix.domain.BaseEntity;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.multiMatchQuery;

@Entity
@Table(name = "esquery")
public class ElasticSearchQuery<T> extends BaseEntity {

	@Lob
	@NotNull
	public String queryString;

	@ElementCollection
	@JoinTable(name = "es_sorts", joinColumns = @JoinColumn(name = "query_id"))
	@Column(name = "sort_string", nullable = false)
	public List<String> sortStrings;

	public String highlightedField;

	@Transient
	private BoolQueryBuilder boolQueryBuilder;

	@Transient
	private List<FieldSortBuilder> fieldSortBuilders;

	@NotNull
	public String objectName;

	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}

	public void setSortStrings(List<String> sortStrings) {
		this.sortStrings = sortStrings;
	}

	public String getObjectName() {
		return objectName;
	}

	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	public List<FieldSortBuilder> getFieldSortBuilders() {
		if(fieldSortBuilders == null && sortStrings != null && !sortStrings.isEmpty()) {
			fieldSortBuilders = new ArrayList<>();
			sortStrings.stream().forEach(sort -> fieldSortBuilders.add(SortBuilders.fieldSort(sort)));
		}

		return fieldSortBuilders;
	}

	public BoolQueryBuilder getBoolQueryBuilder() {
		if(boolQueryBuilder == null && queryString != null && !queryString.isEmpty()) {
			boolQueryBuilder = boolQuery();
			boolQueryBuilder.must(multiMatchQuery(queryString));
		}

		return boolQueryBuilder;
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
