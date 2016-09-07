package co.xarx.trix.persistence;

import co.xarx.trix.domain.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

public interface InvitationRepository extends JpaRepository<Invitation, Integer>, QueryDslPredicateExecutor<Invitation> {

	@RestResource(path = "findByInvitationHash")
	Invitation findByHash(@Param("hash") String hash);
}