package co.xarx.trix.persistence;

import co.xarx.trix.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Integer>, QueryDslPredicateExecutor<User> {

	@Query("SELECT CASE WHEN COUNT(u) > 0 THEN 'true' ELSE 'false' END FROM User u WHERE u.username = :username")
	boolean existsByUsername(@Param("username") String username);
}
