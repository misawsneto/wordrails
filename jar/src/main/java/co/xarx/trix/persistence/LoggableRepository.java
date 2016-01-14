package co.xarx.trix.persistence;

import co.xarx.trix.annotation.GeneratorIgnore;
import co.xarx.trix.domain.Loggable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

@GeneratorIgnore
@NoRepositoryBean
public interface LoggableRepository<T extends Loggable, ID extends Serializable> extends JpaRepository<T, ID> {

//	@Override
//	void delete(ID integer);
//
//	void deleteWithLog(T entity);
}
