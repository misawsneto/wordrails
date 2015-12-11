package co.xarx.trix.services;

import co.xarx.trix.domain.Person;
import co.xarx.trix.domain.Post;
import co.xarx.trix.domain.PostRead;
import co.xarx.trix.domain.QPostRead;
import co.xarx.trix.elasticsearch.ESPostRepository;
import co.xarx.trix.elasticsearch.domain.ESPost;
import co.xarx.trix.persistence.PostReadRepository;
import co.xarx.trix.persistence.PostRepository;
import co.xarx.trix.persistence.QueryPersistence;
import org.hibernate.exception.ConstraintViolationException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	private ModelMapper modelMapper;

//	public void searchIndex(SearchQuery query) {
//		elasticSearchTemplate.index(indexQuery);
//		elasticSearchTemplate.refresh(SampleEntity.class, true);
//
//	}

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
