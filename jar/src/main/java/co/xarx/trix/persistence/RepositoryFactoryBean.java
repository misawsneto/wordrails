package co.xarx.trix.persistence;

import co.xarx.trix.domain.Loggable;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

import javax.persistence.EntityManager;
import java.io.Serializable;

public class RepositoryFactoryBean<T extends Repository<S, ID>, S, ID extends Serializable> extends JpaRepositoryFactoryBean<T, S, ID> {

	protected RepositoryFactorySupport createRepositoryFactory(EntityManager entityManager) {
		return new RepositoryFactory(entityManager);
	}

	private static class RepositoryFactory extends JpaRepositoryFactory {

		private EntityManager entityManager;

		public RepositoryFactory(EntityManager entityManager) {
			super(entityManager);
			this.entityManager = entityManager;
		}

		@Override
		public Object getTargetRepository(RepositoryMetadata metadata) {
			if (Loggable.class.isAssignableFrom(metadata.getDomainType())) {
				return new LoggableRepositoryImpl(getEntityInformation(metadata.getDomainType()), entityManager);
			} else {
				return super.getTargetRepository(metadata, entityManager);
			}
		}

		@Override
		public SimpleJpaRepository<?, ?> getTargetRepository(RepositoryMetadata metadata, EntityManager entityManager) {
			if (Loggable.class.isAssignableFrom(metadata.getDomainType())) {
				return new LoggableRepositoryImpl(getEntityInformation(metadata.getDomainType()), entityManager);
			} else {
				return super.getTargetRepository(metadata, entityManager);
			}
		}

		@Override
		public Class<?> getRepositoryBaseClass(final RepositoryMetadata metadata) {
			if (Loggable.class.isAssignableFrom(metadata.getDomainType())) {
				return LoggableRepositoryImpl.class;
			} else {
				return super.getRepositoryBaseClass(metadata);
			}
		}

	}

}