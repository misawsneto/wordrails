package co.xarx.trix.config;

import co.xarx.trix.domain.Loggable;
import co.xarx.trix.domain.Post;
import co.xarx.trix.persistence.CustomPostRepositoryImpl;
import co.xarx.trix.persistence.LoggableRepositoryImpl;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.core.RepositoryMetadata;

import javax.persistence.EntityManager;

public class RepositoryFactory extends JpaRepositoryFactory {

//		private EntityManager entityManager;

	public RepositoryFactory(EntityManager entityManager) {
		super(entityManager);
//			this.entityManager = entityManager;
	}

	@Override
	public Object getTargetRepository(RepositoryMetadata metadata) {
		return super.getTargetRepository(metadata);
	}

	@Override
	public SimpleJpaRepository<?, ?> getTargetRepository(RepositoryMetadata metadata, EntityManager entityManager) {
		if (Post.class.isAssignableFrom(metadata.getDomainType())) {
			return new CustomPostRepositoryImpl(getEntityInformation((Class<Post>) metadata.getDomainType()), entityManager);
		} else if (Loggable.class.isAssignableFrom(metadata.getDomainType())) {
			return new LoggableRepositoryImpl(getEntityInformation(metadata.getDomainType()), entityManager);
		} else {
			return super.getTargetRepository(metadata, entityManager);
		}
	}

	@Override
	public Class<?> getRepositoryBaseClass(final RepositoryMetadata metadata) {
		if (Post.class.isAssignableFrom(metadata.getDomainType())) {
			return CustomPostRepositoryImpl.class;
		} else if (Loggable.class.isAssignableFrom(metadata.getDomainType())) {
			return LoggableRepositoryImpl.class;
		} else {
			return super.getRepositoryBaseClass(metadata);
		}
	}

}