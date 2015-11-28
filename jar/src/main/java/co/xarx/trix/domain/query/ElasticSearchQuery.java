package co.xarx.trix.domain.query;

import co.xarx.trix.domain.BaseEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.common.xcontent.XContentHelper;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.FieldSortBuilder;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import static org.elasticsearch.index.query.QueryBuilders.matchQuery;
import static org.elasticsearch.index.query.QueryBuilders.wrapperQuery;

public class ElasticSearchQuery<T> extends BaseEntity {

	public String boolQueryString;
	public String highlightedField;
	public String objectName;
	private BoolQueryBuilder boolQueryBuilder;
	private List<FieldSortBuilder> fieldSortBuilders;

	private static Map<String, Iterable<Object>> fromSource(String source, String value) {
		byte[] data = source.getBytes();
		Map<String, Object> values = XContentHelper.convertToMap(data, 0, data.length, true).v2();
		for (String s : values.keySet()) {
			if(s.equals(value) && values.get(s) instanceof Map){
				return (Map<String, Iterable<Object>>) values.get(s);
			}
		}
		return null;
	}

	private static BoolQueryBuilder getBoolQuery(Map<String, Iterable<Object>> source) {
		BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

		ObjectMapper m = new ObjectMapper();
		for (Map.Entry<String, Iterable<Object>> entry : source.entrySet()) {
			if(entry.getKey().equals("must")) {
				entry.getValue().forEach(
								value -> {
									try {
										boolQuery.must(wrapperQuery(m.writeValueAsString(value)));
									} catch (IOException e) {
										e.printStackTrace();
									}
								}
						);
			} else if(entry.getKey().equals("must_not")) {
					entry.getValue().forEach(
							value -> {
								try {
									boolQuery.mustNot(wrapperQuery(m.writeValueAsString(value)));
									} catch (IOException e) {
										e.printStackTrace();
									}
								}
						);
			} else if(entry.getKey().equals("should")) {
						entry.getValue().forEach(
								value -> {
									try {
										boolQuery.should(wrapperQuery(m.writeValueAsString(value)));
									} catch (IOException e) {
										e.printStackTrace();
									}
								}
						);
			}
		}

		return boolQuery;
	}

	public String getBoolQueryString() {
		return boolQueryString;
	}

	public void setBoolQueryString(String boolQueryString) {
		this.boolQueryString = boolQueryString;
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

	public void setQueryString(String boolQueryString) {
		this.boolQueryString = boolQueryString;
	}

	public String getObjectName() {
		return objectName;
	}

	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

//	public List<FieldSortBuilder> getFieldSortBuilders() {
//		if (fieldSortBuilders == null && sortStrings != null && !sortStrings.isEmpty()) {
//			fieldSortBuilders = new ArrayList<>();
//			sortStrings.keySet().stream().forEach(sort -> {
//				FieldSortBuilder fsb = SortBuilders.fieldSort(sort);
//				if(sortStrings.get(sort).equals("desc")) fsb.order(SortOrder.DESC);
//				else fsb.order(SortOrder.ASC);
//				fieldSortBuilders.add(SortBuilders.fieldSort(sort));
//			});
//		}
//
//		return fieldSortBuilders;
//	}

	public BoolQueryBuilder getBoolQueryBuilder() {
		if (boolQueryBuilder == null && boolQueryString != null && !boolQueryString.isEmpty()) {
			boolQueryBuilder = getBoolQuery(fromSource(boolQueryString, "bool"));
		}

		return boolQueryBuilder;
	}

	public void addIdException(Serializable id) {
		this.getBoolQueryBuilder().mustNot(matchQuery(this.getObjectName() + "._id", id));
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
