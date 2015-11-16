package co.xarx.trix.services;

import co.xarx.trix.api.PostView;
import co.xarx.trix.domain.page.interfaces.Block;
import co.xarx.trix.domain.query.ElasticSearchQuery;
import co.xarx.trix.domain.query.QueryExecutor;
import co.xarx.trix.persistence.elasticsearch.PostEsRepository;
import co.xarx.trix.util.TrixUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class QueryExecutorService implements QueryExecutor {

	@Autowired
	private PostEsRepository postEsRepository;

	@Override
	public List<Block> execute(ElasticSearchQuery query) {
		SearchResponse searchResponse = postEsRepository.runQuery(query.getQuery(), null, query.getSize(), query.getPage(), "body");

		ObjectMapper objectMapper = new ObjectMapper();
		List<PostView> postsViews = new ArrayList<>();
		List<Block<PostView>> blocks = new ArrayList<>();

		for (SearchHit hit : searchResponse.getHits().getHits()) {
			try {
				final PostView postView = objectMapper.readValue(
						objectMapper.writeValueAsString(
								objectMapper.readValue(
										hit.getSourceAsString(), PostView.class)), PostView.class);

				Map<String, HighlightField> highlights = hit.getHighlightFields();
				if (highlights != null && highlights.get("body") != null) {
					StringBuilder sb = new StringBuilder();
					for (Text fragment : highlights.get("body").getFragments()) {
						sb.append(fragment.toString()).append(" ");
					}

					postView.snippet = sb.toString();
				} else {
					postView.snippet = TrixUtil.simpleSnippet(postView.body, 100);
				}

				postView.snippet = TrixUtil.htmlStriped(postView.snippet);
				postView.snippet = postView.snippet.replaceAll("\\{snippet\\}", "<b>").replaceAll("\\{#snippet\\}", "</b>");
				postsViews.add(postView);
				blocks.add(new Block<PostView>() {
					@Override
					public PostView getObject() {
						return postView;
					}

					@Override
					public String getType() {
						return Block.POST_BLOCK;
					}
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return null;
	}
}
