package co.xarx.trix.services;

import co.xarx.trix.api.PostView;
import co.xarx.trix.config.multitenancy.TenantContextHolder;
import co.xarx.trix.domain.Post;
import co.xarx.trix.elasticsearch.domain.ESPerson;
import co.xarx.trix.elasticsearch.domain.ESPost;
import co.xarx.trix.elasticsearch.repository.ESPersonRepository;
import co.xarx.trix.elasticsearch.repository.ESPostRepository;
import co.xarx.trix.persistence.PostRepository;
import co.xarx.trix.persistence.QueryPersistence;
import co.xarx.trix.util.StringUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.highlight.HighlightBuilder;
import org.elasticsearch.search.highlight.HighlightField;
import org.elasticsearch.search.sort.SortBuilder;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ResultsExtractor;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.QueryBuilders.*;

@Service
public class PostService {

	private static final Logger log = LoggerFactory.getLogger(PostService.class);

	@Autowired
	private QueryPersistence queryPersistence;
	@Autowired
	private SchedulerService schedulerService;
	@Autowired
	private PostRepository postRepository;
	@Autowired
	private ESPostRepository esPostRepository;
	@Autowired
	private ElasticSearchService elasticSearchService;
	@Autowired
	private ESPersonRepository esPersonRepository;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private ElasticsearchTemplate elasticsearchTemplate;
	@Autowired
	private MobileService mobileService;

	public Pair<Integer, List<PostView>> searchIndex(BoolQueryBuilder boolQuery, Pageable pageable, SortBuilder sort) {
		boolQuery.must(matchQuery("tenantId", TenantContextHolder.getCurrentTenantId()));
		NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
		if(sort != null) nativeSearchQueryBuilder.withSort(sort);
		SearchQuery query = nativeSearchQueryBuilder
				.withPageable(pageable)
				.withHighlightFields(new HighlightBuilder.Field("body"))
				.withQuery(boolQuery).build();

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

	public void publishScheduledPost(Integer postId) {
		Post scheduledPost = postRepository.findOne(postId);
		if (scheduledPost != null && scheduledPost.state.equals(Post.STATE_SCHEDULED)) {
			if (scheduledPost.notify) {
				mobileService.buildNotification(scheduledPost);
			}

			scheduledPost.date = new Date();
			scheduledPost.state = Post.STATE_PUBLISHED;
			postRepository.save(scheduledPost);
		}
	}

	public Post convertPost(int postId, String state) {
		return convertPost(postRepository.findOne(postId), state);
	}

	public Post convertPost(Post dbPost, String state) {

		if (dbPost != null) {
			log.debug("Before convert: " + dbPost.getClass().getSimpleName());
			if (state.equals(dbPost.state)) {
				return dbPost; //they are the same type. no need for convertion
			}

			if (dbPost.state.equals(Post.STATE_SCHEDULED)) { //if converting FROM scheduled, unschedule
				schedulerService.unschedule(dbPost.getId());
			} else if (state.equals(Post.STATE_SCHEDULED)) { //if converting TO scheduled, schedule
				schedulerService.schedule(dbPost.getId(), dbPost.scheduledDate);
			}

			dbPost.state = state;

			queryPersistence.changePostState(dbPost.getId(), state);

			if (state.equals(Post.STATE_PUBLISHED)) {
				dbPost = postRepository.findOne(dbPost.getId()); //do it again so modelmapper don't cry... stupid framework
				elasticSearchService.saveIndex(dbPost, ESPost.class, esPostRepository);
			} else {
				elasticSearchService.deleteIndex(dbPost.getId(), esPostRepository);
			}
		}

		return dbPost;
	}

//	@Autowired
//	private Javers javers;
//	@Autowired
//	private EventAuthorProvider eventAuthorProvider;

	@Transactional(noRollbackFor = Exception.class)
	public void countPostRead(Integer postId, Integer personId, String sessionId) {

//		ReadPostEvent readPostEvent = new ReadPostEvent();
//		readPostEvent.setPersonId(personId);
//		readPostEvent.getDates().add(new Date());
//		javers.commit(eventAuthorProvider.provide(), )
//		QPostRead pr = QPostRead.postRead;
//		if (person == null || person.username.equals("wordrails")) {
//			if(postReadRepository.findAll(pr.sessionid.eq(sessionId).and(pr.post.id.eq(post.id)))
//					.iterator().hasNext()) {
//				return;
//			}
//		} else {
//			if(postReadRepository.findAll(pr.sessionid.eq("0").and(pr.post.id.eq(post.id)).and(pr.person.id.eq(person.id)))
//					.iterator().hasNext()) {
//				return;
//			}
//		}
//
//		PostRead postRead = new PostRead();
//		postRead.person = person;
//		postRead.post = post;
//		postRead.sessionid = "0"; // constraint fails if null
//		if (postRead.person != null && postRead.person.username.equals("wordrails")) { // if user wordrails, include session to uniquely identify the user.
//			postRead.person = null;
//			postRead.sessionid = sessionId;
//		}
//
//		try {
//			postReadRepository.save(postRead);
//			queryPersistence.incrementReadsCount(post.id);
//		} catch (ConstraintViolationException | DataIntegrityViolationException e) {
//			log.info("user already read this post");
//		}
	}

	public BoolQueryBuilder getBoolQueryBuilder(String q, Integer personId, String publicationType, Iterable<Integer> stationIds, Iterable<Integer> postIds) {
		BoolQueryBuilder mainQuery = boolQuery();

		if (Strings.hasText(q)) {
			MultiMatchQueryBuilder queryText = multiMatchQuery(q)
					.field("body", 2)
					.field("title", 5)
					.field("topper")
					.field("subheading")
					.field("authorName")
					.field("terms.name")
					.prefixLength(1);

			mainQuery = mainQuery.must(queryText);
		}

		if(personId != null){
			mainQuery = mainQuery.must(
					matchQuery("authorId", personId));
		}

		if(publicationType != null){
			mainQuery = mainQuery.must(
					matchQuery("state", publicationType));
		} else {
			mainQuery = mainQuery.must(
					matchQuery("state", Post.STATE_PUBLISHED));
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
			for(Integer stationId: stationIds){
				stationQuery.should(
						matchQuery("stationId", String.valueOf(stationId)));
			}
			mainQuery = mainQuery.must(stationQuery);
		}

		return mainQuery;
	}
}
