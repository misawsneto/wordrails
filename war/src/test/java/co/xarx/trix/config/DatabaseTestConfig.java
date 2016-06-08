package co.xarx.trix.config;

import org.hibernate.jpa.HibernatePersistenceProvider;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DataSourceConnectionProvider;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultDSLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
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
@ComponentScan("co.xarx.trix.persistence")
@EnableTransactionManagement
@EnableJpaRepositories(
		basePackages = {"co.xarx.trix.persistence"}
)
public class DatabaseTestConfig {

	@Bean
	public DataSource dataSource() {
//		JDBCPool ds = new JDBCPool();
//		ds.setDatabase("TRIX");
//		ds.setUrl("jdbc:hsqldb:mem:ex");
//		ds.setUser("sa");
//		ds.setPassword("");
//
//		return ds;

		return new EmbeddedDatabaseBuilder()
				.setType(EmbeddedDatabaseType.HSQL)
				.addScript("classpath:acl_hsqldb.sql")
				.build();
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() throws Exception {
		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		vendorAdapter.setDatabasePlatform("org.hibernate.dialect.HSQLDialect");
		vendorAdapter.setGenerateDdl(true);
		vendorAdapter.setShowSql(true);

		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
		factory.setDataSource(dataSource());
		factory.setPackagesToScan("co.xarx.trix.domain");
		factory.setJpaVendorAdapter(vendorAdapter);
//		factory.setJpaProperties(jpaProperties());
		factory.setPersistenceProvider(new HibernatePersistenceProvider());
		factory.afterPropertiesSet();

		return factory;
	}

	public Properties jpaProperties() {
		Properties props = new Properties();
		props.put("hibernate.query.substitutions", "true 'Y', false 'N'");
		props.put("hibernate.hbm2ddl.auto", "update");
		props.put("hibernate.show_sql", "false");
		props.put("hibernate.format_sql", "true");
		props.put("hibernate.dialect", "org.hibernate.dialect.HSQLDialect");

		return props;
	}

	@Bean
	public JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) throws Exception {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(entityManagerFactory);
		transactionManager.setDataSource(dataSource());
		return transactionManager;
	}

	@Bean
	public DataSourceConnectionProvider dataSourceConnectionProvider() {
		return new DataSourceConnectionProvider(dataSource());
	}

	@Bean
	public DefaultConfiguration defaultConfiguration() {
		DefaultConfiguration defaultConfiguration = new DefaultConfiguration();
		defaultConfiguration.setConnectionProvider(dataSourceConnectionProvider());
		defaultConfiguration.setSQLDialect(SQLDialect.HSQLDB);
		return defaultConfiguration;
	}

	@Bean
	public DSLContext dslContext() {
		return new DefaultDSLContext(defaultConfiguration());
	}
}
