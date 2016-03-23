package co.xarx.trix.services;

import co.xarx.trix.util.Constants;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.cluster.ClusterState;
import org.elasticsearch.cluster.metadata.IndexMetaData;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogram;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.*;

@Service
public class StatisticsService {

	private ElasticsearchTemplate elasticsearchTemplate;
	@Value("${spring.data.elasticsearch.nginx_log}")
	private String indexName;
	private Client client;

	@Autowired
	public StatisticsService(ElasticsearchTemplate elasticsearchTemplate, Client client){
		this.elasticsearchTemplate = elasticsearchTemplate;
		this.client = client;
	}

	public HashMap getPorpularNetworks(){
		return findMostPopular("network", null, null, 10);
	}

	public List<String> getNginxFields(){
		ClusterState cs = client.admin().cluster().prepareState().setIndices(indexName).execute().actionGet().getState();

		IndexMetaData indexMetaData = cs.getMetaData().index(indexName);
		MappingMetaData mappingMetaData = indexMetaData.mapping(Constants.ObjectType.NGINX);
		Map<String, Object> map = null;

		try {
			map = mappingMetaData.getSourceAsMap();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return getList("", map);
	}

	private static List<String> getList(String fieldName, Map<String, Object> mapProperties) {
		List<String> fieldList = new ArrayList<String>();
		Map<String, Object> map = (Map<String, Object>) mapProperties.get("properties");
		Set<String> keys = map.keySet();
		for (String key : keys) {
			if (((Map<String, Object>) map.get(key)).containsKey("type")) {
				fieldList.add(fieldName + "" + key);
			} else {
				List<String> tempList = getList(fieldName + "" + key + ".", (Map<String, Object>) map.get(key));
				fieldList.addAll(tempList);
			}
		}
		return fieldList;
	}

	private boolean isValidField(String field){
		if(getNginxFields().contains(field)) {
			return true;
		} else return false;
	}

	public boolean isValidTimeRange(Long begin, Long end){
		return begin != null && end != null && begin < end;
	}

	public HashMap findMostPopular(String field, Long startTimestamp, Long endTimestamp, Integer size){
		Assert.hasText(field, "Field is null");
		Assert.isTrue(isValidField(field), "Invalid field");

		String term = "by_" + field;

		SearchRequestBuilder search = client.prepareSearch();
		search.setTypes(Constants.ObjectType.NGINX)
				.addAggregation(AggregationBuilders
						.terms(term)
						.field(field)
						.size(size));

		if(isValidTimeRange(startTimestamp, endTimestamp)){
			search.addAggregation(AggregationBuilders
					.range("timestamp")
					.field("@timestamp")
					.addRange(startTimestamp, endTimestamp)
			);
		}

		SearchResponse response = search.execute().actionGet();

		Terms networks = response.getAggregations().get(term);
		HashMap buckets = new HashMap<String, String>();

		for (Terms.Bucket b : networks.getBuckets()) {
			buckets.put(b.getKey(), b.getDocCount());
		}

		return buckets;
	}
}
