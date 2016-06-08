package co.xarx.trix.config;

import co.xarx.trix.config.audit.EventAuthorProvider;
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

@Configuration
public class AuditConfig {

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

	@Bean
	public JaversAuditableRepositoryAspect javersAuditableRepositoryAspect() {
		return new JaversAuditableRepositoryAspect(javers(), authorProvider());
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