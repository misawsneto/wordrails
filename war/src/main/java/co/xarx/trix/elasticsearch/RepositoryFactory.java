package co.xarx.trix.elasticsearch;

import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.repository.query.ElasticsearchPartQuery;
import org.springframework.data.elasticsearch.repository.query.ElasticsearchQueryMethod;
import org.springframework.data.elasticsearch.repository.query.ElasticsearchStringQuery;
import org.springframework.data.elasticsearch.repository.support.ElasticsearchEntityInformation;
import org.springframework.data.elasticsearch.repository.support.ElasticsearchEntityInformationCreator;
import org.springframework.data.elasticsearch.repository.support.ElasticsearchEntityInformationCreatorImpl;
import org.springframework.data.repository.core.NamedQueries;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.data.repository.query.QueryLookupStrategy;
import org.springframework.data.repository.query.RepositoryQuery;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.lang.reflect.Method;

public class RepositoryFactory extends RepositoryFactorySupport {

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

	@Override
	protected QueryLookupStrategy getQueryLookupStrategy(QueryLookupStrategy.Key key) {
		return new ElasticsearchQueryLookupStrategy();
	}

	private class ElasticsearchQueryLookupStrategy implements QueryLookupStrategy {

		@Override
		public RepositoryQuery resolveQuery(Method method, RepositoryMetadata metadata, NamedQueries namedQueries) {

			ElasticsearchQueryMethod queryMethod = new ElasticsearchQueryMethod(method, metadata);
			String namedQueryName = queryMethod.getNamedQueryName();

			if (namedQueries.hasQuery(namedQueryName)) {
				String namedQuery = namedQueries.getQuery(namedQueryName);
				return new ElasticsearchStringQuery(queryMethod, elasticsearchOperations, namedQuery);
			} else if (queryMethod.hasAnnotatedQuery()) {
				return new ElasticsearchStringQuery(queryMethod, elasticsearchOperations, queryMethod.getAnnotatedQuery());
			}
			return new ElasticsearchPartQuery(queryMethod, elasticsearchOperations);
		}
	}

}