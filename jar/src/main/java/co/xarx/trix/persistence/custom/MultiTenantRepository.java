package co.xarx.trix.persistence.custom;

import co.xarx.trix.domain.MultiTenantEntity;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

@NoRepositoryBean
public interface MultiTenantRepository<T extends MultiTenantEntity, ID extends Serializable> {

	T findOne(ID id);

	boolean exists(ID id);

	Iterable<T> findAll();

	Iterable<T> findAll(Iterable<ID> ids);

	long count();
}
