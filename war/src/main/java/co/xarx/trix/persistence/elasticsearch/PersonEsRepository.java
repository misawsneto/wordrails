package co.xarx.trix.persistence.elasticsearch;

import co.xarx.trix.domain.Person;
import co.xarx.trix.services.ElasticSearchService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.search.highlight.HighlightField;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;


@Component
public class PersonEsRepository {

	@Value("${elasticsearch.index}")
	private String indexName;

	@Autowired
	private ObjectMapper objectMapper;

	private static final String ES_TYPE = "person";

	@Autowired
	private ElasticSearchService elasticSearchService;

	public SearchResponse runQuery(String query, FieldSortBuilder sort, Integer size, Integer page, String highlightedField){
		SearchRequestBuilder searchRequestBuilder = elasticSearchService
														.getElasticsearchClient()
														.prepareSearch(indexName)
														.setTypes(ES_TYPE)
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

		return searchRequestBuilder.execute().actionGet();
	}

	public void save(Person person) {
		elasticSearchService.index(formatObjectJson(person), person.id.toString(), indexName, ES_TYPE);
	}

	public void update(Person person){
		elasticSearchService.update(formatObjectJson(person), person.id.toString(), indexName, ES_TYPE);
	}

	public void delete(Person person){
		delete(person.id);
	}

	public void delete(Integer postId){
		elasticSearchService.delete(String.valueOf(postId), indexName, ES_TYPE);
	}

	public String formatObjectJson(Person person){

		String doc = null;
		Person p = new Person();
		p.id = person.id;
		p.name = person.name;
		p.username = person.username;
		p.bio = person.bio;
		p.imageHash = person.imageHash;
		p.coverHash = person.coverHash;
		p.networkId = person.networkId;
		p.email = person.email;
		p.personsNetworkRoles = person.personsNetworkRoles;

		try {
			doc = objectMapper.writeValueAsString(person);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		return doc;
	}

	public JSONObject makeObjectJson(Person person){
		String doc = null;

		Person p = new Person();
		p.id = person.id;
		p.name = person.name;
		p.username = person.username;
		p.bio = person.bio;
		p.imageHash = person.imageHash;
		p.coverHash = person.coverHash;
		p.networkId = person.networkId;
		p.email = person.email;
		p.personsNetworkRoles = person.personsNetworkRoles;
		p.personsStationPermissions = person.personsStationPermissions;

		try {
			doc = objectMapper.writeValueAsString(p);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		return (JSONObject) JSONValue.parse(doc);
	}

	public JSONObject convertToView(String json){
		return (JSONObject) JSONValue.parse(json);
	}

	public JSONObject convertToView(String json, Map<String, HighlightField> highlightFieldMap){
		JSONObject view = (JSONObject) JSONValue.parse(json);
		for(String key: highlightFieldMap.keySet()){
			String fragments = "";
			for(Text fragment: highlightFieldMap.get(key).getFragments()){
				fragments += fragment.string();
			}
			view.put(key, fragments);
		}
		return view;
	}
}