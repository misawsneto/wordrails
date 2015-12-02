package co.xarx.trix.config.spring;

import co.xarx.trix.config.multitenancy.MultiTenantHibernatePersistence;
import co.xarx.trix.factory.ElasticSearchExecutorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.FieldRetrievingFactoryBean;
import org.springframework.beans.factory.config.ServiceLocatorFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement()
@EnableJpaAuditing(dateTimeProviderRef = "dateTimeProvider")
@EnableJpaRepositories(basePackages = "co.xarx.trix.persistence")
public class DatabaseConfig {

	@Autowired
	Environment env;
	@Autowired
	DataSourceConfig dataSourceConfig;

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {

		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		vendorAdapter.setGenerateDdl(true);

		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
		factory.setDataSource(dataSourceConfig.dataSource());
		factory.setPersistenceUnitName("wordrails");
		factory.setPersistenceProviderClass(MultiTenantHibernatePersistence.class);
		factory.setJpaVendorAdapter(vendorAdapter);
		factory.setPackagesToScan("co.xarx.trix.domain");
		factory.afterPropertiesSet();

		return factory;
	}

	@Bean
	public ServiceLocatorFactoryBean elasticSearchExecutorFactory() {
		ServiceLocatorFactoryBean serviceLocatorFactoryBean = new ServiceLocatorFactoryBean();
		serviceLocatorFactoryBean.setServiceLocatorInterface(ElasticSearchExecutorFactory.class);

		return serviceLocatorFactoryBean;
	}

	@Bean
	public JpaTransactionManager transactionManager(){
		JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
		jpaTransactionManager.setDataSource(dataSourceConfig.dataSource());
		jpaTransactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
		return jpaTransactionManager;
	}

	@Bean
	public FieldRetrievingFactoryBean dateTimeProvider() {
		FieldRetrievingFactoryBean fieldRetrievingFactoryBean = new FieldRetrievingFactoryBean();
		fieldRetrievingFactoryBean.setStaticField("org.springframework.data.auditing.CurrentDateTimeProvider.INSTANCE");
		return fieldRetrievingFactoryBean;
	}
}
