package co.xarx.trix.services;

import co.xarx.trix.api.PostView;
import co.xarx.trix.config.multitenancy.TenantContextHolder;
import co.xarx.trix.domain.Person;
import co.xarx.trix.domain.Post;
import co.xarx.trix.domain.PostRead;
import co.xarx.trix.domain.QPostRead;
import co.xarx.trix.elasticsearch.ESPersonRepository;
import co.xarx.trix.elasticsearch.ESPostRepository;
import co.xarx.trix.elasticsearch.domain.ESPerson;
import co.xarx.trix.elasticsearch.domain.ESPost;
import co.xarx.trix.persistence.PostReadRepository;
import co.xarx.trix.persistence.PostRepository;
import co.xarx.trix.persistence.QueryPersistence;
import co.xarx.trix.util.StringUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.highlight.HighlightBuilder;
import org.elasticsearch.search.highlight.HighlightField;
import org.elasticsearch.search.sort.SortBuilder;
import org.hibernate.exception.ConstraintViolationException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ResultsExtractor;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.QueryBuilders.matchQuery;

@Service
public class PostService {

	private static final Logger log = LoggerFactory.getLogger(PostService.class);

	@Autowired
	private QueryPersistence queryPersistence;
	@Autowired
	private SchedulerService schedulerService;
	@Autowired
	private PostReadRepository postReadRepository;
	@Autowired
	private PostRepository postRepository;
	@Autowired
	private ESPostRepository esPostRepository;
	@Autowired
	private ESPersonRepository esPersonRepository;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private ElasticsearchTemplate elasticsearchTemplate;

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

	public void saveIndex(Post post) {
		ESPost esPost = modelMapper.map(post, ESPost.class);
		esPostRepository.save(esPost);
	}

	public void deleteIndex(Integer postId) {
		esPostRepository.delete(postId);
	}

	public Post convertPost(int postId, String state) {
		Post dbPost = postRepository.findOne(postId);

		if (dbPost != null) {
			log.debug("Before convert: " + dbPost.getClass().getSimpleName());
			if (state.equals(dbPost.state)) {
				return dbPost; //they are the same type. no need for convertion
			}

			if (dbPost.state.equals(Post.STATE_SCHEDULED)) { //if converting FROM scheduled, unschedule
				schedulerService.unschedule(dbPost.id);
			} else if (state.equals(Post.STATE_SCHEDULED)) { //if converting TO scheduled, schedule
				schedulerService.schedule(dbPost.id, dbPost.scheduledDate);
			}

			dbPost.state = state;

			queryPersistence.changePostState(postId, state);
			saveIndex(dbPost);
		}

		return dbPost;
	}

	@Transactional(noRollbackFor = Exception.class)
	public void countPostRead(Post post, Person person, String sessionId) {
		QPostRead pr = QPostRead.postRead;
		if (person == null || person.username.equals("wordrails")) {
			if(postReadRepository.findAll(pr.sessionid.eq(sessionId).and(pr.post.id.eq(post.id)))
					.iterator().hasNext()) {
				return;
			}
		} else {
			if(postReadRepository.findAll(pr.sessionid.eq("0").and(pr.post.id.eq(post.id)).and(pr.person.id.eq(person.id)))
					.iterator().hasNext()) {
				return;
			}
		}

		PostRead postRead = new PostRead();
		postRead.person = person;
		postRead.post = post;
		postRead.sessionid = "0"; // constraint fails if null
		if (postRead.person != null && postRead.person.username.equals("wordrails")) { // if user wordrails, include session to uniquely identify the user.
			postRead.person = null;
			postRead.sessionid = sessionId;
		}

		try {
			postReadRepository.save(postRead);
			queryPersistence.incrementReadsCount(post.id);
		} catch (ConstraintViolationException | DataIntegrityViolationException e) {
			log.info("user already read this post");
		}
	}
}
