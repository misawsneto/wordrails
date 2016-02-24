package co.xarx.trix.config.spring;

import co.xarx.trix.config.database.RepositoryFactoryBean;
import co.xarx.trix.config.flyway.FlywayIntegrator;
import co.xarx.trix.config.multitenancy.MultiTenantHibernatePersistence;
import org.springframework.beans.factory.config.FieldRetrievingFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@EnableJpaAuditing(dateTimeProviderRef = "dateTimeProvider")
@EnableJpaRepositories(
		basePackages = "co.xarx.trix.persistence",
		repositoryFactoryBeanClass = RepositoryFactoryBean.class
)
public class DatabaseConfig {

	@Bean
	public FlywayIntegrator flywayIntegrator() {
		return new FlywayIntegrator();
	}

	@Bean
	@DependsOn("flywayIntegrator")
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		vendorAdapter.setGenerateDdl(true);

		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
		factory.setDataSource(dataSource);
		factory.setPersistenceUnitName("wordrails");
		factory.setPersistenceProviderClass(MultiTenantHibernatePersistence.class);
		factory.setJpaVendorAdapter(vendorAdapter);
		factory.setPackagesToScan("co.xarx.trix.domain");
		factory.setJpaProperties(hibernateProperties());
		factory.afterPropertiesSet();

		return factory;
	}

	Properties hibernateProperties() {
		return new Properties() {
			{
				setProperty("hibernate.hbm2ddl.auto", "update");
				setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
				setProperty("hibernate.globally_quoted_identifiers", "false");
				setProperty("hibernate.show_sql", "false");
				setProperty("hibernate.format", "true");
				setProperty("hibernate.current_session_context_class", "thread");
				setProperty("hibernate.default_batch_fetch_size", "200");
				setProperty("hibernate.classloading.use_current_tccl_as_parent", "false");
			}
		};
	}


	@Bean
	public JpaTransactionManager transactionManager(DataSource dataSource){
		JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
//		jpaTransactionManager.setDataSource(dataSource);
		jpaTransactionManager.setEntityManagerFactory(entityManagerFactory(dataSource).getObject());
		return jpaTransactionManager;
	}

	@Bean
	public FieldRetrievingFactoryBean dateTimeProvider() {
		FieldRetrievingFactoryBean fieldRetrievingFactoryBean = new FieldRetrievingFactoryBean();
		fieldRetrievingFactoryBean.setStaticField("org.springframework.data.auditing.CurrentDateTimeProvider.INSTANCE");
		return fieldRetrievingFactoryBean;
	}
}
