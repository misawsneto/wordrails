package co.xarx.trix.config.spring;

import co.xarx.trix.elasticsearch.ESRepositoryFactoryBean;
import co.xarx.trix.factory.ElasticSearchExecutorFactory;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ServiceLocatorFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(
		basePackages = "co.xarx.trix.elasticsearch",
		repositoryFactoryBeanClass = ESRepositoryFactoryBean.class
)
public class ElasticSearchConfig {

	@Value("${elasticsearch.host:'localhost'}")
	private String host;
	@Value("${elasticsearch.port:9300}")
	private Integer port;
	@Value("${elasticsearch.cluster:'trix'}")
	private String cluster;
	@Value("${elasticsearch.username:'trix_admin'}")
	private String user;
	@Value("${elasticsearch.password}")
	private String password;

	@Bean(name = "elasticSearchExecutorFactory")
	public FactoryBean elasticSearchExecutorFactory() {
		ServiceLocatorFactoryBean serviceLocatorFactoryBean = new ServiceLocatorFactoryBean();
		serviceLocatorFactoryBean.setServiceLocatorInterface(ElasticSearchExecutorFactory.class);

		return serviceLocatorFactoryBean;
	}


	@Bean
	public Client elasticSearchClient() {
		Settings settings = ImmutableSettings.settingsBuilder()
				.put("cluster.name", cluster)
				.put("shield.user", user + ":" + password)
				.build();
		return new TransportClient(settings).addTransportAddress(new InetSocketTransportAddress(host, port));
	}

	@Bean
	public ElasticsearchOperations elasticsearchTemplate() {
		return new ElasticsearchTemplate(elasticSearchClient());
	}
}
