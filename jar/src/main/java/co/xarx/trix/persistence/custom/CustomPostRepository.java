package co.xarx.trix.persistence.custom;

import co.xarx.trix.annotation.GeneratorIgnore;
import co.xarx.trix.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;

@GeneratorIgnore
@NoRepositoryBean
public interface CustomPostRepository extends JpaRepository<Post, Integer>, QueryDslPredicateExecutor<Post> {

	@Override
	void delete(Post post);

	@Override
	void deleteInBatch(Iterable<Post> entities);
}
