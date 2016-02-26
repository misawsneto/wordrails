package co.xarx.trix.persistence;

import co.xarx.trix.domain.Loggable;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.QueryDslJpaRepository;

import javax.persistence.EntityManager;
import java.io.Serializable;


public class LoggableRepositoryImpl<T extends Loggable, ID extends Serializable> extends QueryDslJpaRepository<T, ID> implements org.springframework.data.jpa.repository.JpaRepository<T, ID>, org.springframework.data.querydsl.QueryDslPredicateExecutor<T> {



	public LoggableRepositoryImpl(JpaEntityInformation<T, ID> entityInformation, EntityManager em) {
		super(entityInformation, em);
	}

//	@Override
//	public void delete(ID id) {
//		T t = findOne(id);
//		deleteWithLog(t);
//	}
//
//
//	@Override
//	public void deleteWithLog(T entity) {
//		super.delete(entity);
//	}
}
