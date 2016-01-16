package co.xarx.trix.persistence;

import co.xarx.trix.domain.Post;
import co.xarx.trix.persistence.custom.CustomPostRepository;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.QueryDslJpaRepository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

public class CustomPostRepositoryImpl extends QueryDslJpaRepository<Post, Integer> implements CustomPostRepository {

	EntityManager em;

	public CustomPostRepositoryImpl(JpaEntityInformation<Post, Integer> entityInformation, EntityManager em) {
		super(entityInformation, em);
		this.em = em;
	}

	@Override
	@Transactional
	public void delete(Post post) {
		em.createNativeQuery("UPDATE post SET state=:state where id = :postId")
				.setParameter("postId", post.id).setParameter("state", Post.STATE_TRASH).executeUpdate();
	}

	@Override
	@Transactional
	public void deleteInBatch(Iterable<Post> entities) {
		for (Post entity : entities) {
			delete(entity);
		}
	}
}
