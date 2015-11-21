
package co.xarx.trix.persistence.elasticsearch;

import co.xarx.trix.domain.TermPerspective;
import co.xarx.trix.services.ElasticSearchService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Created by jonas on 26/09/15.
 */
@Component
public class PerspectiveEsRepository {

	@Value("${elasticsearch.index}")
	private String indexName;

	private @Autowired @Qualifier("simpleMapper")
	ObjectMapper simpleMapper;

	private @Autowired @Qualifier("objectMapper")
	ObjectMapper objectMapper;

	private static final String ES_TYPE = "perspective";

	@Autowired
	private ElasticSearchService elasticSearchService;

	public SearchResponse runQuery(String query, FieldSortBuilder sort, Integer size, Integer page){
		SearchRequestBuilder searchRequestBuilder = elasticSearchService.getElasticsearchClient()
														.prepareSearch(indexName)
														.setTypes("perspective")
														.setQuery(query);

		if (size != null && size > 0){
			searchRequestBuilder.setSize(size);

			if (page != null){
				searchRequestBuilder.setFrom(page * size);
			}
		}

		if (sort != null){
			searchRequestBuilder.addSort(sort);
		}

		SearchResponse response = searchRequestBuilder.execute().actionGet();
		return response;
	}

	public void save(TermPerspective perspective) {
		elasticSearchService.save(formatObjectJson(perspective), perspective.id.toString(), indexName, ES_TYPE);
	}

	public void update(TermPerspective perspective){
		elasticSearchService.update(formatObjectJson(perspective), perspective.id.toString(), indexName, ES_TYPE);
	}

	public void delete(TermPerspective perspective){
		delete(perspective.id);
	}

	public void deleteByStationPerspective(Integer stationPerspectiveId){
		Client client = elasticSearchService.getClient();
		SearchResponse response = client.prepareSearch(indexName)
				.setTypes(ES_TYPE)
				.setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
				.setPostFilter(FilterBuilders.termFilter("perspective.id", stationPerspectiveId))
				.execute()
				.actionGet();

		SearchHit[] resultList = response.getHits().getHits();

		for(SearchHit hit: resultList){
			try {
				delete(convertToViewObject(hit.getSourceAsString()).id);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public void delete(Integer perspectiveId){
		elasticSearchService.delete(String.valueOf(perspectiveId), indexName, ES_TYPE);
	}

	public String formatObjectJson(TermPerspective perspective){

		String doc = null;

		try {
			//doc = objectMapper.writeValueAsString(makeTermPerspectiveView(perspective));
			if(perspective.perspective != null) {
				perspective.perspective.perspectives = null;
				perspective.perspective.station = null;
				perspective.perspective.taxonomy = null;
			}

			doc = objectMapper.writeValueAsString(makeTermPerspectiveView(perspective));
			doc = doc == null ? null : doc;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return null;
		}

		//JSONObject toFormat = (JSONObject) JSONValue.parse(doc);

		//return toFormat.toJSONString();
		return doc;
	}

	public JSONObject convertToView(String json){

		return (JSONObject) JSONValue.parse(json);
	}

	public TermPerspective convertToViewObject(String json){
		TermPerspective tp = null;
		try {
			tp = simpleMapper.readValue(json, TermPerspective.class);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return tp;
	}

	public PerspectiveIndexed makeTermPerspectiveView(TermPerspective perspective){
		PerspectiveIndexed perspectiveView = new PerspectiveIndexed();

		perspectiveView.id = perspective.id;
		perspectiveView.featuredRow = perspective.featuredRow;
		perspectiveView.homeRow = perspective.homeRow;
		perspectiveView.splashedRow = perspective.splashedRow;
		perspectiveView.perspective = perspective.perspective;
		perspectiveView.rows = perspective.rows;
		perspectiveView.showPopular = perspective.showPopular;
		perspectiveView.showRecent = perspective.showRecent;
		perspectiveView.stationId = perspective.stationId;
		perspectiveView.taxonomyId = perspective.taxonomyId;
		perspectiveView.term = perspective.term;

		return perspectiveView;
	}
}