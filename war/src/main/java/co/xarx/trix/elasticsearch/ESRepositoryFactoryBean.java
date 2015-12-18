package co.xarx.trix.elasticsearch;

import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.repository.support.ElasticsearchEntityInformation;
import org.springframework.data.elasticsearch.repository.support.ElasticsearchEntityInformationCreator;
import org.springframework.data.elasticsearch.repository.support.ElasticsearchEntityInformationCreatorImpl;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactoryBeanSupport;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.util.Assert;

import java.io.Serializable;

public class ESRepositoryFactoryBean extends RepositoryFactoryBeanSupport {

	private ElasticsearchOperations operations;

	//DON'T DELETE THIS METHOD
	public void setElasticsearchOperations(ElasticsearchOperations operations) {
		Assert.notNull(operations);
		this.operations = operations;
	}

	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		Assert.notNull(operations, "ElasticsearchOperations must be configured!");
	}

	@Override
	protected RepositoryFactorySupport createRepositoryFactory() {
		return new RepositoryFactory(operations);
	}

	private static class RepositoryFactory extends RepositoryFactorySupport {

		private final ElasticsearchOperations elasticsearchOperations;
		private final ElasticsearchEntityInformationCreator entityInformationCreator;

		public RepositoryFactory(ElasticsearchOperations elasticsearchOperations) {
			Assert.notNull(elasticsearchOperations);
			this.elasticsearchOperations = elasticsearchOperations;
			this.entityInformationCreator = new ElasticsearchEntityInformationCreatorImpl(elasticsearchOperations.getElasticsearchConverter().getMappingContext());
		}

		@Override
		public <T, ID extends Serializable> ElasticsearchEntityInformation<T, ID> getEntityInformation(Class<T> domainClass) {
			return entityInformationCreator.getEntityInformation(domainClass);
		}

		@Override
		protected Object getTargetRepository(RepositoryMetadata metadata) {
			ElasticsearchEntityInformation<?, ?> entityInformation = getEntityInformation(metadata.getDomainType());
			return new ESRepositoryImpl(entityInformation, elasticsearchOperations);
		}

		@Override
		protected Class<?> getRepositoryBaseClass(final RepositoryMetadata metadata) {
			return ESRepositoryImpl.class;
		}

	}

}