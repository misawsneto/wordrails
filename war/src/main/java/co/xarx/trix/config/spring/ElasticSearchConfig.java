package co.xarx.trix.config.spring;

import co.xarx.trix.elasticsearch.CustomGenericRepositoryFactoryBean;
import co.xarx.trix.factory.ElasticSearchExecutorFactory;
import co.xarx.trix.services.ElasticSearchService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ServiceLocatorFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import javax.annotation.PostConstruct;

@Configuration
@EnableElasticsearchRepositories(
		basePackages = "co.xarx.trix.elasticsearch",
		repositoryFactoryBeanClass = CustomGenericRepositoryFactoryBean.class
)
public class ElasticSearchConfig {

	@Value("${elasticsearch.host:'localhost'}")
	private String host;
	@Value("${elasticsearch.port:9300}")
	private Integer port;


	@Bean
	public ServiceLocatorFactoryBean elasticSearchExecutorFactory() {
		ServiceLocatorFactoryBean serviceLocatorFactoryBean = new ServiceLocatorFactoryBean();
		serviceLocatorFactoryBean.setServiceLocatorInterface(ElasticSearchExecutorFactory.class);

		return serviceLocatorFactoryBean;
	}

	@Bean
	@PostConstruct
	public ElasticSearchService elasticsearchService() {
		return new ElasticSearchService(host, port);
	}

	@Bean
	public ElasticsearchOperations elasticsearchTemplate() {
		return new ElasticsearchTemplate(elasticsearchService().getClient());
	}
}
