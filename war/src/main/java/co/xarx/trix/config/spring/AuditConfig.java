package co.xarx.trix.config.spring;

import co.xarx.trix.config.EventAuthorProvider;
import org.javers.core.Javers;
import org.javers.hibernate.integration.HibernateUnproxyObjectAccessHook;
import org.javers.repository.sql.ConnectionProvider;
import org.javers.repository.sql.DialectName;
import org.javers.repository.sql.JaversSqlRepository;
import org.javers.repository.sql.SqlRepositoryBuilder;
import org.javers.spring.auditable.AuthorProvider;
import org.javers.spring.auditable.aspect.JaversAuditableRepositoryAspect;
import org.javers.spring.jpa.JpaHibernateConnectionProvider;
import org.javers.spring.jpa.TransactionalJaversBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;

@Configuration
public class AuditConfig {

	//.. JaVers setup ..

	/**
	 * Creates JaVers instance with {@link JaversSqlRepository}
	 */
	@Bean
	public Javers javers() {
		JaversSqlRepository sqlRepository = SqlRepositoryBuilder
				.sqlRepository()
				.withConnectionProvider(jpaConnectionProvider())
				.withDialect(DialectName.MYSQL).build();

		return TransactionalJaversBuilder.javers()
				.withObjectAccessHook(new HibernateUnproxyObjectAccessHook())
				.registerJaversRepository(sqlRepository).build();
	}

	/**
	 * Enables Repository auto-audit aspect. <br/>
	 * <p>
	 * Use {@link org.javers.spring.annotation.JaversSpringDataAuditable}
	 * to annotate Spring Data Repositories
	 * or {@link org.javers.spring.annotation.JaversAuditable} for ordinary Repositories.
	 */
	@Bean
	public JaversAuditableRepositoryAspect javersAuditableRepositoryAspect() {
		return new JaversAuditableRepositoryAspect(javers(), authorProvider());
	}

	/**
	 * Required by Repository auto-audit aspect. <br/><br/>
	 * <p>
	 * Returns mock implementation for testing.
	 * <br/>
	 * Provide real implementation,
	 * when using Spring Security you can use
	 * {@link org.javers.spring.auditable.SpringSecurityAuthorProvider}.
	 */
	@Bean
	public AuthorProvider authorProvider() {
		return new EventAuthorProvider();
	}


	@Bean
	public ConnectionProvider jpaConnectionProvider() {
		return new JpaHibernateConnectionProvider();
	}

	@Bean
	public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
		return new PersistenceExceptionTranslationPostProcessor();
	}
}
