package co.xarx.trix.test.config;

import co.xarx.trix.config.database.RepositoryFactoryBean;
import co.xarx.trix.config.multitenancy.MultiTenantHibernatePersistence;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
		basePackages = "co.xarx.trix.persistence"
		,repositoryFactoryBeanClass = RepositoryFactoryBean.class
)
public class DatabaseTestConfig {


	@Bean
	public DataSource dataSource() throws Exception {
//		String jdbcUrl = String.format(H2_JDBC_URL_TEMPLATE, System.getProperty("user.dir"));
//		JdbcDataSource ds = new JdbcDataSource();
//		ds.setURL(jdbcUrl);
//		ds.setUser("sa");
//		ds.setPassword("");
//
//		return ds;

		return new EmbeddedDatabaseBuilder()
				.setType(EmbeddedDatabaseType.H2)
//				.addScript("classpath:create-schema.sql")
//				.addScript("classpath:create-data-test.sql")
				.build();
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() throws Exception {
		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		vendorAdapter.setGenerateDdl(true);

		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
		factory.setDataSource(dataSource());
		factory.setPersistenceUnitName("wordrails");
		factory.setPersistenceProviderClass(MultiTenantHibernatePersistence.class);
		factory.setJpaVendorAdapter(vendorAdapter);
		factory.setPackagesToScan("co.xarx.trix.domain");
		factory.afterPropertiesSet();

		return factory;
	}

	public Properties jpaProperties() {
		Properties props = new Properties();
		props.put("hibernate.query.substitutions", "true 'Y', false 'N'");
		props.put("hibernate.hbm2ddl.auto", "create-drop");
		props.put("hibernate.show_sql", "false");
		props.put("hibernate.format_sql", "true");

		return props;
	}


	@Bean
	public JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) throws Exception {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(entityManagerFactory);
		return transactionManager;
	}

//	@Bean
//	@Autowired
//	public DataSourceInitializer dataSourceInitializer(final DataSource dataSource) {
//		final DataSourceInitializer initializer = new DataSourceInitializer();
//		initializer.setDataSource(dataSource);
//		initializer.setDatabasePopulator(databasePopulator());
//		initializer.setDatabaseCleaner(databaseCleaner());
//		return initializer;
//	}
//
//
//	private DatabasePopulator databasePopulator() {
//		final ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
//		populator.addScript(H2_SCHEMA_SCRIPT);
//		populator.addScript(H2_DATA_SCRIPT);
//		return populator;
//	}
//
//	private DatabasePopulator databaseCleaner() {
//		final ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
//		populator.addScript(H2_CLEANER_SCRIPT);
//		return populator;
//	}
}
