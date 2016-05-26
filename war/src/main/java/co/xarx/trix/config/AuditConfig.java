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

//	@Autowired
//	private JpaTransactionManager jpaTransactionManager;
//	@Autowired
//	private EntityManagerFactory entityManagerFactory;
//	@Autowired
//	private PlatformTransactionManager platformTransactionManager;
//	@Autowired
//	private DataSource dataSource;

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

	@Bean
	public AuthorProvider authorProvider() {
		return new EventAuthorProvider();
	}


	@Bean
	public ConnectionProvider jpaConnectionProvider() {
		JpaHibernateConnectionProvider jpaHibernateConnectionProvider = new JpaHibernateConnectionProvider();
		return jpaHibernateConnectionProvider;
	}
}