package co.xarx.trix.config;

import co.xarx.trix.config.audit.EventAuthorProvider;
import org.javers.core.Javers;
import org.javers.hibernate.integration.HibernateUnproxyObjectAccessHook;
import org.javers.repository.sql.ConnectionProvider;
import org.javers.repository.sql.DialectName;
import org.javers.repository.sql.JaversSqlRepository;
import org.javers.repository.sql.SqlRepositoryBuilder;
import org.javers.spring.auditable.AuthorProvider;
import org.javers.spring.auditable.aspect.JaversAuditableAspect;
import org.javers.spring.auditable.aspect.springdata.JaversSpringDataAuditableRepositoryAspect;
import org.javers.spring.jpa.JpaHibernateConnectionProvider;
import org.javers.spring.jpa.TransactionalJaversBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class AuditConfig {

	@Bean
	public Javers javers(PlatformTransactionManager txManager) {
		JaversSqlRepository sqlRepository = SqlRepositoryBuilder
				.sqlRepository()
				.withConnectionProvider(jpaConnectionProvider())
				.withDialect(DialectName.MYSQL).build();

		return TransactionalJaversBuilder.javers()
				.withTxManager(txManager)
				.withObjectAccessHook(new HibernateUnproxyObjectAccessHook())
				.registerJaversRepository(sqlRepository).build();
	}

	@Bean
	public JaversSpringDataAuditableRepositoryAspect javersSpringDataAuditableRepositoryAspect(PlatformTransactionManager txManager) {
		return new JaversSpringDataAuditableRepositoryAspect(javers(txManager), authorProvider());
	}

	@Bean
	public JaversAuditableAspect javersAuditableAspect(PlatformTransactionManager txManager) {
		return new JaversAuditableAspect(javers(txManager), authorProvider());
	}

	@Bean
	public AuthorProvider authorProvider() {
		return new EventAuthorProvider();
	}


	@Bean
	public ConnectionProvider jpaConnectionProvider() {
		return new JpaHibernateConnectionProvider();
	}
}