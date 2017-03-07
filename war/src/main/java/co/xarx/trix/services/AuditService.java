package co.xarx.trix.services;

import co.xarx.trix.api.v2.PostData;
import co.xarx.trix.config.audit.EventAuthorProvider;
import co.xarx.trix.domain.Post;
import co.xarx.trix.persistence.PostRepository;
import org.javers.core.Javers;
import org.javers.core.metamodel.object.CdoSnapshot;
import org.javers.repository.jql.QueryBuilder;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

@Service
public class AuditService {
	private Javers javers;
	private ModelMapper mapper;
	private PostRepository postRepository;
	private EventAuthorProvider eventAuthorProvider;
	private List<String> attributesToAvoid;

	@Autowired
	public AuditService(Javers javers, PostRepository postRepository, ModelMapper mapper, EventAuthorProvider eventAuthorProvider){
		this.javers = javers;
		this.mapper = mapper;
		this.postRepository = postRepository;
		this.eventAuthorProvider = eventAuthorProvider;

		attributesToAvoid = new ArrayList<>();
		attributesToAvoid.add("categories");
		attributesToAvoid.add("author");
		attributesToAvoid.add("image");
		attributesToAvoid.add("audio");
		attributesToAvoid.add("image");
		attributesToAvoid.add("video");
	}

	public boolean avoidIt(String attrName){
		return attributesToAvoid.contains(attrName);
	}

	public Map getPostVersions(Integer postId) throws NoSuchFieldException, IllegalAccessException {
		Post domainPost = postRepository.findOne(postId);
		List<CdoSnapshot> snapshots = javers.findSnapshots(QueryBuilder.byInstanceId(postId, PostData.class).build());
		Map versions = new LinkedHashMap<Long, PostData>();

		for (CdoSnapshot snapshot : snapshots) {
			PostData post = mapper.map(domainPost, PostData.class);
			Class<?> c = post.getClass();
			for (String attrName : snapshot.getChanged()) {
				if(avoidIt(attrName)) continue;

				Object value = snapshot.getPropertyValue(attrName);

				Field f = c.getDeclaredField(attrName);
				f.setAccessible(true);
				f.set(post, f.getType().cast(value));
			}
			LocalDateTime l = snapshot.getCommitMetadata().getCommitDate();
			Long date = Date.from(l.toInstant(ZoneOffset.UTC)).getTime();
			versions.put(date, post);
		}
		return versions;
	}

	public void saveChange(Post post){
		PostData p = mapper.map(post, PostData.class);
		javers.commit(eventAuthorProvider.getAuthor().toString(), p);
	}
}
