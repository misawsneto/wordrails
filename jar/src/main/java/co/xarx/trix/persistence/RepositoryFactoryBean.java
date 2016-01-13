package co.xarx.trix.persistence;

import co.xarx.trix.domain.Loggable;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.data.repository.core.support.TransactionalRepositoryFactoryBeanSupport;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;

public class RepositoryFactoryBean<T extends Repository<S, ID>, S, ID extends Serializable> extends TransactionalRepositoryFactoryBeanSupport<T, S, ID> {

	private EntityManager entityManager;


	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public void setMappingContext(MappingContext<?, ?> mappingContext) {
		super.setMappingContext(mappingContext);
	}

	@Override
	protected RepositoryFactorySupport doCreateRepositoryFactory() {
		return new RepositoryFactory(entityManager);
	}

	@Override
	public void afterPropertiesSet() {

		Assert.notNull(entityManager, "EntityManager must not be null!");
		super.afterPropertiesSet();
	}

	private static class RepositoryFactory extends JpaRepositoryFactory {

		private EntityManager entityManager;

		public RepositoryFactory(EntityManager entityManager) {
			super(entityManager);
			this.entityManager = entityManager;
		}

		@Override
		protected Object getTargetRepository(RepositoryMetadata metadata) {
			if (metadata.getDomainType().equals(Loggable.class)) {
				return new LoggableRepositoryImpl(metadata.getDomainType(), entityManager);
			} else {
				return super.getTargetRepository(metadata);
			}
		}

		@Override
		protected Class<?> getRepositoryBaseClass(final RepositoryMetadata metadata) {
			return LoggableRepositoryImpl.class;
		}

	}

}