package co.xarx.trix.persistence;

import co.xarx.trix.annotation.GeneratorIgnore;
import co.xarx.trix.domain.User;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import javax.persistence.QueryHint;

public interface UserRepository extends JpaRepository<User, Integer>, QueryDslPredicateExecutor<User> {

	@Query("SELECT CASE WHEN COUNT(u) > 0 THEN 'true' ELSE 'false' END FROM User u WHERE u.username = :username")
	boolean existsByUsername(@Param("username") String username);

	@Cacheable(value = "user", key = "#p0")
	@QueryHints({@QueryHint(name = "enabled", value = "true")})
	User findByUsername(@Param("username") String username);

	@Override
	@GeneratorIgnore
	@CacheEvict(value = "user", key = "#p0.user.username")
	User save(User user);

	@Override
	@GeneratorIgnore
	@CacheEvict(value = "user", key = "#p0.user.username")
	void delete(User user);
}
