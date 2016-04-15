package co.xarx.trix.persistence.custom;

import co.xarx.trix.config.multitenancy.TenantContextHolder;
import co.xarx.trix.domain.Post;
import co.xarx.trix.domain.QPost;
import co.xarx.trix.persistence.PostRepository;
import co.xarx.trix.services.security.PermissionFilterService;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Component
public class PostRepositoryImpl implements PostRepositoryCustom {

	//DON'T REFACTOR THIS CLASS TO USE AUTOWIRED CONSTRUCTOR
	//BECAUSE IT WILL CAUSE A CIRCULAR REFERENCE EXCEPTION
	@PersistenceContext
	private EntityManager em;
	@Autowired
	private PostRepository postRepository;
	@Autowired
	private PermissionFilterService permissionFilterService;

	@Override
	@Transactional
	public void delete(Integer id) {
		em.createNativeQuery("UPDATE post SET state=:state where id = :postId")
				.setParameter("postId", id).setParameter("state", Post.STATE_TRASH).executeUpdate();
	}

	@Override
	public Page<Post> findAll(Pageable pageable) {
		List<Integer> postIds = getPostIdsWithReadPermission();
		return postRepository.findAll(QPost.post.id.in(postIds), pageable);
//		Session session = em.unwrap(Session.class);
//		List<Integer> postIdsWithReadPermission = getPostIdsWithReadPermission();
//		enableFilter(session, postIdsWithReadPermission);
//		return postRepository.findAll(pageable);
	}

	public void enableFilter(Session session, List<Integer> ids) {
		Filter filter = session.enableFilter("idFilter");
		filter.setParameterList("ids", ids);
	}

	public List<Integer> getPostIdsWithReadPermission() {
		List<Integer> postIds = postRepository.findIds(TenantContextHolder.getCurrentTenantId());
		postIds = permissionFilterService.filterIds(postIds, Post.class, "read");
		return postIds;
	}
}
