package co.xarx.trix.persistence;

import co.xarx.trix.domain.Loggable;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.io.Serializable;

@Component

public class LoggableRepositoryImpl<T extends Loggable, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements LoggableRepository<T, ID>{


	public LoggableRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
		super(entityInformation, entityManager);
	}

	public LoggableRepositoryImpl(Class<T> domainClass, EntityManager em) {
		super(domainClass, em);
	}

	@Override
	public void delete(ID id) {
		T t = findOne(id);
		System.out.println("DELETE----------------------------");
		delete(t);
	}
}
