package co.xarx.trix.persistence.elasticsearch;

import co.xarx.trix.domain.Bookmark;
import co.xarx.trix.services.ElasticSearchService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class BookmarkEsRespository {

	@Value("${elasticsearch.index}")
	private String indexName;

	@Autowired
	private ObjectMapper objectMapper;

	private static final String ES_TYPE = "bookmark";

	private @Autowired
	ElasticSearchService elasticSearchService;

	public SearchResponse runQuery(String query, FieldSortBuilder sort, Integer size, Integer page){
		SearchRequestBuilder searchRequestBuilder = elasticSearchService.getElasticsearchClient()
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

		SearchResponse response = searchRequestBuilder.execute().actionGet();
		return response;
	}

	public void save(Bookmark bookmark) {
		elasticSearchService.index(formatObjectJson(bookmark), bookmark.id.toString(), indexName, ES_TYPE);
	}

	public void update(Bookmark bookmark){
		elasticSearchService.update(formatObjectJson(bookmark), bookmark.id.toString(), indexName, ES_TYPE);
	}

	public void delete(Bookmark bookmark){
		delete(bookmark.id);
	}

	public void delete(Integer bookmarkId){
		elasticSearchService.delete(String.valueOf(bookmarkId), indexName, ES_TYPE);
	}

	public String formatObjectJson(Bookmark bookmark){

		String doc = null;

		try {
			doc = objectMapper.writeValueAsString(
					makeBookmarkView(bookmark)
			);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		return doc;
	}

	public BookmarkIndexed makeBookmarkView(Bookmark bookmark){
		BookmarkIndexed bookmarkIndexed = new BookmarkIndexed();

		bookmarkIndexed.id = bookmark.id;
		bookmarkIndexed.post = bookmark.post;
		bookmarkIndexed.person = bookmark.person;
		bookmarkIndexed.createdAt = bookmark.createdAt;
		bookmarkIndexed.updatedAt = bookmark.updatedAt;

		return bookmarkIndexed;
	}

	public JSONObject convertToView(String json){

		return (JSONObject) JSONValue.parse(json);
	}

	public JSONObject convertToPostView(String json){
		return (JSONObject) convertToView(json).get("post");
	}
}
