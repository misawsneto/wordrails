package co.xarx.trix.services.post;

import co.xarx.trix.api.PostView;
import co.xarx.trix.config.multitenancy.TenantContextHolder;
import co.xarx.trix.domain.ESPerson;
import co.xarx.trix.domain.ESPost;
import co.xarx.trix.domain.Post;
import co.xarx.trix.domain.page.query.statement.PostStatement;
import co.xarx.trix.persistence.ESPersonRepository;
import co.xarx.trix.util.Constants;
import co.xarx.trix.util.StringUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.eclipse.persistence.jpa.jpql.Assert;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.BoolFilterBuilder;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.RangeFilterBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHitField;
import org.elasticsearch.search.highlight.HighlightBuilder;
import org.elasticsearch.search.highlight.HighlightField;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ResultsExtractor;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.FilterBuilders.*;
import static org.elasticsearch.index.query.QueryBuilders.*;

@Service
public class ESPostService {

	private ESPersonRepository esPersonRepository;
	private ObjectMapper objectMapper;
	private ModelMapper modelMapper;
	private ElasticsearchTemplate elasticsearchTemplate;

	@Autowired
	@SuppressWarnings("SpringJavaAutowiringInspection")
	public ESPostService(ESPersonRepository esPersonRepository, ObjectMapper objectMapper,
						 ModelMapper modelMapper, ElasticsearchTemplate elasticsearchTemplate) {
		this.esPersonRepository = esPersonRepository;
		this.objectMapper = objectMapper;
		this.modelMapper = modelMapper;
		this.elasticsearchTemplate = elasticsearchTemplate;
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
			mainQuery = mainQuery.must(matchQuery("authorId", personId));
		}

		if (publicationType != null) {
			mainQuery = mainQuery.must(matchQuery("state", publicationType));
		} else {
			mainQuery = mainQuery.must(matchQuery("state", Post.STATE_PUBLISHED));
		}

		if (postIds != null) {
			BoolQueryBuilder postQuery = boolQuery();
			for (Integer postId : postIds) {
				postQuery.should(matchQuery("id", postId));
			}
			mainQuery = mainQuery.must(postQuery);
		}

		if (stationIds != null) {
			BoolQueryBuilder stationQuery = boolQuery();
			for (Integer stationId : stationIds) {
				stationQuery.should(matchQuery("stationId", String.valueOf(stationId)));
			}
			mainQuery = mainQuery.must(stationQuery);
		}

		return mainQuery;
	}

	private BoolQueryBuilder getBooleanQuery(String q) {
		BoolQueryBuilder mainQuery = boolQuery();

		if (Strings.hasText(q)) {
			MultiMatchQueryBuilder queryText = multiMatchQuery(q)
					.field("body", 2)
					.field("title", 5)
					.field("topper")
					.field("subheading")
					.field("authorName")
					.field("terms.name")
					.fuzziness(Fuzziness.AUTO)
					.prefixLength(1);

			mainQuery = mainQuery.must(queryText);
		}
		return mainQuery;
	}

	List<PostSearchResult> findIds(PostStatement p) {
		List<FieldSortBuilder> sorts = getSorts(p.getOrders());

		BoolQueryBuilder q = getBooleanQuery(p.getQuery());
		BoolFilterBuilder f = boolFilter();

		applyTenantFilter(f);
		applyStateFilter(f, p.getState());
		applyTypeFilter(f, Constants.ObjectType.POST);
		applyDateFilter(f, p.getFrom(), p.getUntil());
		applyShouldFilter(f, p.getAuthors(), "authorId");
		applyShouldFilter(f, p.getStations(), "stationId");
		applyShouldFilter(f, p.getTags(), "tags");
		applyShouldFilter(f, p.getCategories(), "categories");


		SearchQuery query = getSearchQuery(p, sorts, q, f);

		ResultsExtractor<List<PostSearchResult>> extractor = getExtractor();

		return elasticsearchTemplate.query(query, extractor);
	}

	private List<FieldSortBuilder> getSorts(List<String> sort) {
		List<FieldSortBuilder> sorts = new ArrayList<>();
		if (sort != null) {
			for (String s : sort) {
				SortOrder d = SortOrder.ASC;

				if (s.charAt(0) == '-') {
					d = SortOrder.DESC;
					s = s.substring(1, s.length());
				}

				sorts.add(new FieldSortBuilder(s).order(d));
			}
		}
		return sorts;
	}

	private ResultsExtractor<List<PostSearchResult>> getExtractor() {
		return response -> {
				List<PostSearchResult> ids = new ArrayList<>();
				SearchHit[] hits = response.getHits().getHits();
				for (SearchHit hit : hits) {
					Integer id = Integer.valueOf(hit.getId());
					String snippet = null;
					Map<String, HighlightField> highlights = hit.getHighlightFields();
					if (highlights != null && highlights.get("body") != null) {
						for (Text fragment : highlights.get("body").getFragments()) {
							snippet = fragment.toString();
						}
					} else {

						Map<String, SearchHitField> fields = hit.getFields();
						if(fields != null  && fields.size() > 0) {
							String body = fields.get("body").getValue();

							snippet = StringUtil.simpleSnippet(body);
						}
					}
					ids.add(new PostSearchResult(id, snippet));
				}

				return ids;
			};
	}

	private SearchQuery getSearchQuery(PostStatement p, List<FieldSortBuilder> sorts, BoolQueryBuilder q, BoolFilterBuilder f) {
		NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();

		if (p.getQuery() != null && !p.getQuery().isEmpty()) {
			nativeSearchQueryBuilder.withQuery(q);
		}

		sorts.forEach(nativeSearchQueryBuilder::withSort);

		NativeSearchQuery searchQuery = nativeSearchQueryBuilder
				.withFilter(f)
				.withFields("id")
				.withFields("body")
				.withPageable(new PageRequest(0, 99999999))
				.build();

		return searchQuery;
	}

	private void applyStateFilter(BoolFilterBuilder f, String state) {
		if (state != null && !state.isEmpty()) {
			f.must(queryFilter(queryStringQuery(state).defaultField("state")));
		}
	}

	private void applyTypeFilter(BoolFilterBuilder f, String type) {
		if (type != null && !type.isEmpty()) {
			f.must(termFilter("_type", type.toLowerCase()));
		}
	}

	private void applyShouldFilter(BoolFilterBuilder f, List terms, String termName) {
		if(terms != null && !terms.isEmpty()) {
			BoolFilterBuilder boolFilter = boolFilter();
			for (Object term : terms) {
				boolFilter.should(termFilter(termName, term));
			}
			f.must(boolFilter);
		}
	}

	private void applyTenantFilter(BoolFilterBuilder q) {
		q.must(termFilter("tenantId", TenantContextHolder.getCurrentTenantId().toLowerCase()));
	}

	private void applyDateFilter(BoolFilterBuilder f, String from, String until) {
		if(from != null || until != null) {
			RangeFilterBuilder dateFilter = rangeFilter("date");

			if(from != null)
				dateFilter.from(from);
			if(until != null)
				dateFilter.to(until);

			f.must(dateFilter);
		}
	}
}
