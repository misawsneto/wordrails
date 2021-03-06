package co.xarx.trix.persistence;

import co.xarx.trix.annotation.SdkExclude;
import co.xarx.trix.domain.Network;
import co.xarx.trix.domain.NetworkCreate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

@RepositoryRestResource(exported = true)
public interface NetworkCreateRepository extends JpaRepository<NetworkCreate, Integer>,
		QueryDslPredicateExecutor<NetworkCreate> {

	@Override
	@SdkExclude
	@RestResource(exported = true)
	NetworkCreate save(NetworkCreate networkCreate);
}