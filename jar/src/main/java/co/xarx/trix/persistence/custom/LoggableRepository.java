package co.xarx.trix.persistence.custom;

import co.xarx.trix.domain.Loggable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

@NoRepositoryBean
public interface LoggableRepository<T extends Loggable, ID extends Serializable> extends JpaRepository<T, ID>, QueryDslPredicateExecutor<T> {

//	@Override
//	void delete(ID integer);
//
//	void deleteWithLog(T entity);
}
