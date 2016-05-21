package co.xarx.trix.services;

import co.xarx.trix.api.v2.PostData;
import co.xarx.trix.domain.Post;
import co.xarx.trix.persistence.PostRepository;
import org.javers.core.Javers;
import org.javers.core.metamodel.object.CdoSnapshot;
import org.javers.repository.jql.QueryBuilder;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Service
public class AuditService {
	@Autowired
	private Javers javers;
	@Autowired
	private PostRepository postRepository;
	@Autowired
	private ModelMapper mapper;

	public List<PostData> getPostVersions(Integer postId) throws NoSuchFieldException, IllegalAccessException {
		Post post = postRepository.findOne(postId);
		List<CdoSnapshot> snapshots = javers.findSnapshots(QueryBuilder.byInstanceId(postId, Post.class).build());
		List<PostData> versions = new ArrayList<>();

		for (CdoSnapshot snapshot : snapshots) {
			Class<?> c = post.getClass();
			for (String attrName : snapshot.getChanged()) {
				Object value = snapshot.getPropertyValue(attrName);

				Field f = null;
				f = c.getDeclaredField(attrName);
				f.setAccessible(true);
				f.set(post, value);
			}
			versions.add(mapper.map(post, PostData.class));
		}
		return versions;
	}
}
