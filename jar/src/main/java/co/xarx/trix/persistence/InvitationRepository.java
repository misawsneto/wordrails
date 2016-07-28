package co.xarx.trix.persistence;

import co.xarx.trix.domain.Invitation;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

public interface InvitationRepository extends DatabaseRepository<Invitation, Integer> {

	@RestResource(path = "findByInvitationHash")
	Invitation findByHash(@Param("hash") String hash);
}