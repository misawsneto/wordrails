package co.xarx.trix.persistence;

import co.xarx.trix.annotation.SdkExclude;
import co.xarx.trix.domain.User;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

@RepositoryRestResource(exported = true)
public interface UserRepository extends DatabaseRepository<User, Integer> {

	@RestResource(exported = false)
	@Query("SELECT CASE WHEN COUNT(u) > 0 THEN 'true' ELSE 'false' END FROM User u WHERE u.username = :username")
	boolean existsByUsername(@Param("username") String username);

	@RestResource(exported = false)
	@Cacheable(value = "user", key = "#p0")
	@Query("select user from User user where user.username = :username")
	User findUserByUsername(@Param("username") String username);

	@Override
	@SdkExclude
	@CacheEvict(value = "user", key = "#p0.username")
	User save(User user);

	@Override
	@SdkExclude
	@CacheEvict(value = "user", key = "#p0.username")
	void delete(User user);
}
