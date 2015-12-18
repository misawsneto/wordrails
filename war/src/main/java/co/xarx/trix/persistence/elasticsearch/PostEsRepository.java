package co.xarx.trix.persistence.elasticsearch;

import co.xarx.trix.api.PostView;
import co.xarx.trix.converter.PostConverter;
import co.xarx.trix.domain.Post;
import co.xarx.trix.domain.query.ElasticSearchExecutor;
import co.xarx.trix.domain.query.ElasticSearchQuery;
import co.xarx.trix.services.ElasticSearchService;
import co.xarx.trix.util.StringUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.rometools.utils.Lists;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.highlight.HighlightField;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component("post_executor")
public class PostEsRepository implements ElasticSearchExecutor<PostView> {

	@Value("${elasticsearch.index}")
	private String indexName;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private PostConverter postConverter;

	private static final String ES_TYPE = "post";

	@Autowired
	private ElasticSearchService elasticSearchService;

	public List<PostView> execute(ElasticSearchQuery query, Integer size, Integer from) {
		SearchRequestBuilder searchRequestBuilder = elasticSearchService
				.getElasticsearchClient()
				.prepareSearch(indexName)
				.setTypes(ES_TYPE)
				.setQuery(query.getBoolQueryBuilder())
				.setSize(size)
				.setFrom(from);

		searchRequestBuilder.addHighlightedField(query.getHighlightedField(), 100, 4);
		searchRequestBuilder.setHighlighterPreTags("{snippet}");
		searchRequestBuilder.setHighlighterPostTags("{#snippet}");

		if (Lists.isNotEmpty(query.getFieldSortBuilders())){
			searchRequestBuilder.addSort(new FieldSortBuilder("_score").order(SortOrder.DESC));
			List<FieldSortBuilder> sortBuilders = query.getFieldSortBuilders();
			sortBuilders.stream().forEach(searchRequestBuilder::addSort);
		}

		SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();

		return getViews(searchResponse.getHits().hits());
	}

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

		if (highlightedField != null){
			searchRequestBuilder.addHighlightedField(highlightedField, 100, 4);
			searchRequestBuilder.setHighlighterPreTags("{snippet}");
			searchRequestBuilder.setHighlighterPostTags("{#snippet}");
		}

		if (sort != null){
			searchRequestBuilder.addSort(new FieldSortBuilder("_score").order(SortOrder.DESC));
			searchRequestBuilder.addSort(sort);
		}

		return searchRequestBuilder.execute().actionGet();
	}

	public List<PostView> getViews(SearchHit... hits) {
		List<PostView> views = new ArrayList<>();

		for (SearchHit hit : hits) {
			try {
				ObjectMapper objectMapper = new ObjectMapper();
				objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				final PostView postView = objectMapper.readValue(hit.getSourceAsString(), PostView.class);

				Map<String, HighlightField> highlights = hit.getHighlightFields();
				if (highlights != null && highlights.get("body") != null) {
					StringBuilder sb = new StringBuilder();
					for (Text fragment : highlights.get("body").getFragments()) {
						sb.append(fragment.toString()).append(" ");
					}

					postView.snippet = sb.toString();
				} else {
					postView.snippet = StringUtil.simpleSnippet(postView.body);
				}

				postView.snippet = StringUtil.htmlStriped(postView.snippet);
				postView.snippet = postView.snippet.replaceAll("\\{snippet\\}", "<b>").replaceAll("\\{#snippet\\}", "</b>");
				views.add(postView);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return views;
	}

	public void save(Post post) {
		elasticSearchService.index(formatObjectJson(post), post.id.toString(), indexName, ES_TYPE);
	}

	public void update(Post post){
		elasticSearchService.update(formatObjectJson(post), post.id.toString(), indexName, ES_TYPE);
	}

	public void delete(Post post){
		delete(post.id);
	}

	public void delete(Integer postId){
		elasticSearchService.delete(String.valueOf(postId), indexName, ES_TYPE);
	}

	public String formatObjectJson(Post post){
		String doc;
		try {
			objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
			doc = objectMapper.writeValueAsString(postConverter.convertToView(post, true));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return null;
		}

		return doc;
	}
}