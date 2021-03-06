package co.xarx.trix.persistence;

import co.xarx.trix.annotation.SdkExclude;
import co.xarx.trix.domain.User;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

@RepositoryRestResource(exported = true)
public interface UserRepository extends JpaRepository<User, Integer>, QueryDslPredicateExecutor<User> {

	@RestResource(exported = false)
	@Query("SELECT CASE WHEN COUNT(u) > 0 THEN 'true' ELSE 'false' END FROM User u WHERE u.username = :username")
	boolean existsByUsername(@Param("username") String username);

	@RestResource(exported = false)
	@Cacheable(value = "user", key = "#p0")
	@Query("select user from User user where user.username = :username")
	User findUserByUsername(@Param("username") String username);

	@RestResource(exported = false)
	@Query("select user from Person person join person.user as user where person.email = :email")
	User findUserByEmail(@Param("email") String email);

	@Override
	@SdkExclude
	@CacheEvict(value = "user", key = "#p0.username")
	User save(User user);

	@Override
	@SdkExclude
	@CacheEvict(value = "user", key = "#p0.username")
	void delete(User user);

	@Query("select uc.user from UserConnection uc " +
			"where uc.providerId=:providerId and uc.providerUserId=:providerUserId")
	User findSocialUser(@Param("providerId") String providerId, @Param("providerUserId") String
			providerUserId);
}
