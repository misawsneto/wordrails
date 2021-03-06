package co.xarx.trix.services.post;

import co.xarx.trix.api.PostView;
import co.xarx.trix.domain.ESPerson;
import co.xarx.trix.domain.ESPost;
import co.xarx.trix.domain.Post;
import co.xarx.trix.domain.page.query.statement.PostStatement;
import co.xarx.trix.persistence.ESPersonRepository;
import co.xarx.trix.services.AbstractElasticSearchService;
import co.xarx.trix.util.Constants;
import co.xarx.trix.util.StringUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.eclipse.persistence.jpa.jpql.Assert;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.BoolFilterBuilder;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.DecayFunctionBuilder;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.elasticsearch.index.query.functionscore.script.ScriptScoreFunctionBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHitField;
import org.elasticsearch.search.highlight.HighlightBuilder;
import org.elasticsearch.search.highlight.HighlightField;
import org.elasticsearch.search.rescore.QueryRescorer;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ResultsExtractor;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.FilterBuilders.boolFilter;
import static org.elasticsearch.index.query.FilterBuilders.queryFilter;
import static org.elasticsearch.index.query.FilterBuilders.termsFilter;
import static org.elasticsearch.index.query.QueryBuilders.*;
import static org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders.factorFunction;

@Service
public class ESPostService extends AbstractElasticSearchService {

	private ESPersonRepository esPersonRepository;
	private ObjectMapper objectMapper;
	private ModelMapper modelMapper;
	private ElasticsearchTemplate elasticsearchTemplate;
	private Client client;
	private String esIndex;

	@Autowired
	@SuppressWarnings("SpringJavaAutowiringInspection")
	public ESPostService(ESPersonRepository esPersonRepository,
						 ObjectMapper objectMapper,
						 ModelMapper modelMapper,
						 ElasticsearchTemplate elasticsearchTemplate,
						 Client client,
						 @Value("${elasticsearch.index}")
						 String esIndex) {
		this.esPersonRepository = esPersonRepository;
		this.objectMapper = objectMapper;
		this.modelMapper = modelMapper;
		this.elasticsearchTemplate = elasticsearchTemplate;
		this.client = client;
		this.esIndex = esIndex;
	}

	Pair<Integer, List<PostView>> searchIndex(BoolQueryBuilder boolQuery, Pageable pageable, SortBuilder sort) {
		Assert.isNotNull(boolQuery, "boolQuery must not be null");
		Assert.isNotNull(pageable, "pageable must not be null");

		BoolFilterBuilder f = boolFilter();

		applyTenantFilter(f);
		NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
		if (sort != null)
			nativeSearchQueryBuilder.withSort(sort);
		SearchQuery query = nativeSearchQueryBuilder
				.withPageable(pageable)
				.withHighlightFields(new HighlightBuilder.Field("body"))
				.withFilter(f)
				.withQuery(boolQuery)
				.build();

		Long[] totalHits = new Long[1];
		ResultsExtractor<List<PostView>> resultsExtractor = response -> {
			totalHits[0] = response.getHits().totalHits();
			List<PostView> postsViews = new ArrayList<>();
			SearchHit[] hits = response.getHits().getHits();
			List<ESPost> posts = new ArrayList<>();

			for (SearchHit hit : hits) {
				try {
					posts.add(objectMapper.readValue(hit.getSourceAsString(), ESPost.class));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			Set<Integer> authorIds = posts.stream().map(view -> view.authorId).collect(Collectors.toSet());
			Map<Integer, ESPerson> authors = Maps.uniqueIndex(esPersonRepository.findAll(authorIds), ESPerson::getId);

			for (ESPost post : posts) {
				post.setAuthor(authors.get(post.authorId));
			}

			for (int i = 0; i < hits.length; i++) {
				try {
					ESPost esPost = posts.get(i);
					PostView postView = modelMapper.map(esPost, PostView.class);
					SearchHit hit = hits[i];
					Map<String, HighlightField> highlights = hit.getHighlightFields();
					if (highlights != null && highlights.get("body") != null) {
						for (Text fragment : highlights.get("body").getFragments()) {
							if (postView.snippet == null) postView.snippet = "";
							postView.snippet = postView.snippet + " " + fragment.toString();
						}
					} else {
						postView.snippet = StringUtil.simpleSnippet(postView.body);
					}

					postView.snippet = StringUtil.htmlStriped(postView.snippet);
					postView.snippet = postView.snippet.replaceAll("\\{snippet\\}", "<b>").replaceAll("\\{#snippet\\}", "</b>");
					postsViews.add(postView);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return postsViews;
		};

		List<PostView> postView = elasticsearchTemplate.query(query, resultsExtractor);
		int total = totalHits[0].intValue();
		return new ImmutablePair(total, postView);
	}

	BoolQueryBuilder getBoolQueryBuilder(String q, Integer personId, String publicationType, Iterable<Integer> stationIds, Iterable<Integer> postIds) {
		BoolQueryBuilder mainQuery = getBooleanQuery(q);

		if (personId != null) {
			mainQuery.must(matchQuery("authorId", personId));
		}

		if (postIds != null) {
			BoolQueryBuilder postQuery = boolQuery();
			for (Integer postId : postIds) {
				postQuery.should(matchQuery("id", postId));
			}
			mainQuery.must(postQuery);
		}

		if (stationIds != null) {
			BoolQueryBuilder stationQuery = boolQuery();
			for (Integer stationId : stationIds) {
				stationQuery.should(matchQuery("stationId", String.valueOf(stationId)));
			}
			mainQuery.must(stationQuery);
		}

		if (publicationType != null) {
			mainQuery.must(matchQuery("state", publicationType));
		} else if (publicationType.equalsIgnoreCase("SCHEDULED")) {
			mainQuery.must(matchQuery("state", Post.STATE_PUBLISHED))
					.should(rangeQuery("scheduledDate").gt(new Date()));
		} else {
			mainQuery.must(matchQuery("state", Post.STATE_PUBLISHED))
					.should(matchQuery("scheduledDate", new Date(0)))
					.should(rangeQuery("scheduledDate").lt(new Date()))
					.minimumNumberShouldMatch(1);
		}

		return mainQuery;
	}

	private BoolQueryBuilder getBooleanQuery(String q) {
		BoolQueryBuilder mainQuery = boolQuery();

		if (Strings.hasText(q)) {
			MultiMatchQueryBuilder queryText = multiMatchQuery(q)
					.field("body", 4)
					.field("title", 10)
					.field("topper")
					.field("subheading")
					.field("authorName")
					.field("authorUsername")
					.field("terms.name")
					.boost(2) // boost this main query
					.fuzziness(Fuzziness.ONE)
					.prefixLength(1);

			mainQuery.must(queryText);
		}
		return mainQuery;
	}

	List<PostSearchResult> findIds(PostStatement p) {
		BoolQueryBuilder q = getBooleanQuery(p.getQuery());
		BoolFilterBuilder f = boolFilter();

		applyTenantFilter(f);
		applyStateFilter(f, p.getState());
		applyTypeFilter(f, Constants.ObjectType.POST);
		applyDateFilter(f, p.getFrom(), p.getUntil(), "date");
		applyShouldFilter(f, p.getAuthors(), "authorId");
		applyShouldFilter(f, p.getStations(), "stationId");
		applyShouldFilter(f, p.getTags(), "tags");
		applyShouldFilter(f, p.getCategories(), "categories");

		SearchRequestBuilder query = getSearchQuery(p, q, f);
		SearchResponse response = query.execute().actionGet();

		return extractResult(response);
	}

	private List<PostSearchResult> extractResult(SearchResponse response) {
		List<PostSearchResult> ids = new ArrayList<>();

		SearchHit[] hits = response.getHits().getHits();
		for (SearchHit hit : hits) {
			Integer id = Integer.valueOf(hit.getId());
			String snippet = null;
			Map<String, HighlightField> highlights = hit.getHighlightFields();
			if (highlights == null || highlights.get("body") == null) {
				Map<String, SearchHitField> fields = hit.getFields();
				if (fields != null && fields.size() > 0) {
					String body = fields.get("body").getValue();
					snippet = StringUtil.simpleSnippet(body);
				}
			} else {
				for (Text fragment : highlights.get("body").getFragments()) {
					snippet = fragment.toString();
				}
			}
			ids.add(new PostSearchResult(id, snippet));
		}

		return ids;
	}

	private SearchRequestBuilder getSearchQuery(PostStatement p, BoolQueryBuilder q, BoolFilterBuilder f) {
		SearchRequestBuilder requestBuilder = client.prepareSearch(esIndex).setTypes("post");

		if (p.getQuery() == null || p.getQuery().isEmpty()) {
			requestBuilder.addSort("date", SortOrder.DESC);
		} else {
//			long now = Instant.now().toEpochMilli();
//			DecayFunctionBuilder decay = ScoreFunctionBuilders.gaussDecayFunction("date", now, 80).setOffset(130).setDecay(0.3);
//			FunctionScoreQueryBuilder scoreQueryBuilder;
//			scoreQueryBuilder = functionScoreQuery(q, decay);
			requestBuilder.setQuery(q);
		}

		requestBuilder.addFields("id", "body");
		requestBuilder.setPostFilter(f);
		requestBuilder.setSize(9999999);
		return requestBuilder;
	}

	private void applyStateFilter(BoolFilterBuilder f, String state) {
		if (state != null && !state.isEmpty()) {
			long now = Instant.now().toEpochMilli();

			if (state.equalsIgnoreCase("SCHEDULED")) {
				f.must(queryFilter(queryStringQuery("PUBLISHED").defaultField("state")));
				applyDateFilter(f, now, null, "scheduledDate");
			} else if (state.equalsIgnoreCase("PUBLISHED")) {
				f.must(queryFilter(queryStringQuery("PUBLISHED").defaultField("state")));
				applyDateFilter(f, null, now, "scheduledDate");
				applyDateFilter(f, now, Long.MAX_VALUE, "unpublishDate");
			} else if (state.equalsIgnoreCase("UNPUBLISHED")) {
				f.must(queryFilter(queryStringQuery("UNPUBLISHED").defaultField("state")));
//				applyDateFilter(f, now, Long.MAX_VALUE, "unpublishDate");
			} else {
				f.must(queryFilter(queryStringQuery(state).defaultField("state")));
			}
		}
	}
}
