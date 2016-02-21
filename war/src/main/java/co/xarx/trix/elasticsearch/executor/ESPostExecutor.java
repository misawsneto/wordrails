package co.xarx.trix.elasticsearch.executor;

import co.xarx.trix.api.PostView;
import co.xarx.trix.domain.query.Executor;
import co.xarx.trix.domain.query.elasticsearch.ElasticSearchQuery;
import co.xarx.trix.util.StringUtil;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rometools.utils.Lists;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.highlight.HighlightField;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component("post_executor")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ESPostExecutor implements Executor<ElasticSearchQuery, PostView> {

	@Value("${elasticsearch.index}")
	private String indexName;
	@Autowired
	protected Client client;

	private static final String ES_TYPE = "post";

	public List<PostView> execute(ElasticSearchQuery query, Integer size, Integer from) {
		SearchRequestBuilder searchRequestBuilder = client
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
}