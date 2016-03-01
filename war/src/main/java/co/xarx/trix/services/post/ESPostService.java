package co.xarx.trix.services.post;

import co.xarx.trix.api.PostView;
import co.xarx.trix.config.multitenancy.TenantContextHolder;
import co.xarx.trix.domain.Post;
import co.xarx.trix.elasticsearch.domain.ESPerson;
import co.xarx.trix.elasticsearch.domain.ESPost;
import co.xarx.trix.elasticsearch.repository.ESPersonRepository;
import co.xarx.trix.util.StringUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.eclipse.persistence.jpa.jpql.Assert;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.highlight.HighlightBuilder;
import org.elasticsearch.search.highlight.HighlightField;
import org.elasticsearch.search.sort.SortBuilder;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ResultsExtractor;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchQuery;
import static org.elasticsearch.index.query.QueryBuilders.multiMatchQuery;

@Service
public class ESPostService {

	private ESPersonRepository esPersonRepository;
	private ObjectMapper objectMapper;
	private ModelMapper modelMapper;
	private ElasticsearchTemplate elasticsearchTemplate;

	@Autowired
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

		boolQuery.must(matchQuery("tenantId", TenantContextHolder.getCurrentTenantId()));
		NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
		if (sort != null)
			nativeSearchQueryBuilder.withSort(sort);
		SearchQuery query = nativeSearchQueryBuilder
				.withPageable(pageable)
				.withHighlightFields(new HighlightBuilder.Field("body"))
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
					SearchHit hit = hits[i];
					ESPost esPost = posts.get(i);
					PostView postView = modelMapper.map(esPost, PostView.class);
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
		BoolQueryBuilder mainQuery = boolQuery();

		if (Strings.hasText(q)) {
			MultiMatchQueryBuilder queryText = multiMatchQuery(q).field("body", 2).field("title", 5).field("topper").field("subheading").field("authorName").field("terms.name").prefixLength(1);

			mainQuery = mainQuery.must(queryText);
		}

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
}
