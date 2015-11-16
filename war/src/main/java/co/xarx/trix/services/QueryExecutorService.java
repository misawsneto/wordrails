package co.xarx.trix.services;

import co.xarx.trix.api.PostView;
import co.xarx.trix.domain.page.interfaces.Block;
import co.xarx.trix.domain.query.ElasticSearchQuery;
import co.xarx.trix.domain.query.QueryExecutor;
import co.xarx.trix.persistence.elasticsearch.PostEsRepository;
import org.elasticsearch.action.search.SearchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QueryExecutorService implements QueryExecutor {

	@Autowired
	private PostEsRepository postEsRepository;

	@Override
	public List<Block> execute(ElasticSearchQuery query) {
		List<Block> blocks = new ArrayList<>();

		if(query.getType().equals("post")) {
			SearchResponse searchResponse = postEsRepository.runQuery(query.getQueryString(), null, query.getSize(), query.getPage(), "body");
			List<PostView> views = postEsRepository.getViews(searchResponse.getHits().hits());
			for(final PostView view : views) {
				blocks.add(new Block<PostView>() {
					@Override
					public PostView getObject() {
						return view;
					}

					@Override
					public String getType() {
						return Block.POST_BLOCK;
					}
				});
			}
		}

		return blocks;
	}
}
