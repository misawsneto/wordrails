package co.xarx.trix.services.comment;

import co.xarx.trix.domain.page.query.statement.CommentStatement;
import co.xarx.trix.services.AbstractElasticSearchService;
import co.xarx.trix.util.Constants;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.BoolFilterBuilder;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static org.elasticsearch.index.query.FilterBuilders.boolFilter;
import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.multiMatchQuery;

@Service
public class ESCommentService extends AbstractElasticSearchService {

	private ElasticsearchTemplate esTemplate;

	@Autowired
	public ESCommentService(ElasticsearchTemplate esTemplate) {
		this.esTemplate = esTemplate;
	}


	public List<Integer> findIds(CommentStatement params) {
		List<FieldSortBuilder> sorts = getSorts(params.getOrders());

		BoolQueryBuilder q = getBooleanQuery(params.getQuery());
		BoolFilterBuilder f = boolFilter();

		applyTenantFilter(f);
		applyTypeFilter(f, Constants.ObjectType.COMMENT);
		applyDateFilter(f, params.getFrom(), params.getUntil(), "date");
		applyShouldFilter(f, params.getAuthors(), "authorId");
		applyShouldFilter(f, params.getStations(), "stationId");
		applyShouldFilter(f, params.getPosts(), "postId");
		applyShouldFilter(f, params.getTenants(), "tenantId");


		SearchQuery query = getSearchQuery(params.getQuery(), sorts, q, f);

//		List<ESComment> ts = esTemplate.queryForList(query, ESComment.class);
		return esTemplate.query(query, response -> {
			List<Integer> ids = new ArrayList<>();
			SearchHit[] hits = response.getHits().getHits();
			for (SearchHit hit : hits) {
				ids.add(Integer.valueOf(hit.getId()));
			}
			return ids;
		});
	}

	private BoolQueryBuilder getBooleanQuery(String q) {
		BoolQueryBuilder mainQuery = boolQuery();

		if (Strings.hasText(q)) {
			MultiMatchQueryBuilder queryText = multiMatchQuery(q)
					.field("body", 5)
					.fuzziness(Fuzziness.AUTO)
					.prefixLength(1);

			mainQuery = mainQuery.must(queryText);
		}
		return mainQuery;
	}

	private SearchQuery getSearchQuery(String query, List<FieldSortBuilder> sorts, BoolQueryBuilder q,
									   BoolFilterBuilder f) {
		NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();

		if (query != null && !query.isEmpty()) {
			nativeSearchQueryBuilder.withQuery(q);
		}

		sorts.forEach(nativeSearchQueryBuilder::withSort);

		return nativeSearchQueryBuilder
				.withFilter(f)
				.withFields("id")
				.withFields("body")
				.withPageable(new PageRequest(0, 99999999))
				.build();
	}

}
