package co.xarx.trix.persistence;

import co.xarx.trix.domain.PasswordReset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

public interface PasswordResetRepository extends JpaRepository<PasswordReset, Integer>, QueryDslPredicateExecutor<PasswordReset> {
	@RestResource(exported = true)
	PasswordReset findByHash(@Param("hash") String hash);

	@Modifying
	@RestResource(exported = false)
	void deleteByUserId(@Param("id") Integer id);
}