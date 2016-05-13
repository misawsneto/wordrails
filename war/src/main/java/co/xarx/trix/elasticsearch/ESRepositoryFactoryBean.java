package co.xarx.trix.elasticsearch;

import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.repository.core.support.RepositoryFactoryBeanSupport;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.util.Assert;

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
}