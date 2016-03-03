package co.xarx.trix.persistence;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

@NoRepositoryBean
@RepositoryRestResource(exported = false)
public interface DatabaseRepository<T> extends JpaRepository<T, Integer>, QueryDslPredicateExecutor<T> {

	@Override
	@RestResource(exported = false)
	<S extends T> S save(S entity);

	@Override
	@RestResource(exported = false)
	<S extends T> List<S> save(Iterable<S> entities);

	@Override
	@RestResource(exported = false)
	T findOne(Integer id);

	@Override
	@RestResource(exported = false)
	boolean exists(Integer id);

	@Override
	@RestResource(exported = false)
	List<T> findAll();

	@Override
	@RestResource(exported = false)
	List<T> findAll(Iterable<Integer> ids);

	@Override
	@RestResource(exported = false)
	long count();

	@Override
	@RestResource(exported = false)
	void delete(Integer id);

	@Override
	@RestResource(exported = false)
	void delete(T entity);

	@Override
	@RestResource(exported = false)
	void delete(Iterable<? extends T> entities);

	@Override
	@RestResource(exported = false)
	void deleteAll();

	@Override
	@RestResource(exported = false)
	List<T> findAll(Sort sort);

	@Override
	@RestResource(exported = false)
	<S extends T> S saveAndFlush(S entity);

	@Override
	@RestResource(exported = false)
	void deleteInBatch(Iterable<T> entities);

	@Override
	@RestResource(exported = false)
	void deleteAllInBatch();

	@Override
	@RestResource(exported = false)
	T getOne(Integer integer);

	@Override
	@RestResource(exported = false)
	Page<T> findAll(Pageable pageable);
}
