package co.xarx.trix.elasticsearch;

import co.xarx.trix.domain.ElasticSearchEntity;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.data.elasticsearch.repository.support.AbstractElasticsearchRepository;
import org.springframework.data.elasticsearch.repository.support.ElasticsearchEntityInformation;
import org.springframework.util.Assert;

import java.io.Serializable;

public class ESRepositoryImpl<T extends ElasticSearchEntity, ID extends Serializable>
		extends AbstractElasticsearchRepository<T, ID> implements ESRepository<T, ID> {

	public ESRepositoryImpl() {
	}

	public ESRepositoryImpl(ElasticsearchOperations elasticsearchOperations) {
		super(elasticsearchOperations);
	}

	public ESRepositoryImpl(ElasticsearchEntityInformation<T, ID> metadata, ElasticsearchOperations elasticsearchOperations) {
		super(metadata, elasticsearchOperations);
	}

	@Override
	protected String stringIdRepresentation(ID id) {
		return id.toString();
	}

	@Override
	public <S extends T> S save(S entity) {
		Assert.notNull(entity, "Cannot save 'null' entity.");
		elasticsearchOperations.index(createIndexQuery(entity));
		elasticsearchOperations.refresh(entity.getTenantId(), true);
		return entity;
	}

	private IndexQuery createIndexQuery(T entity) {
		return new IndexQueryBuilder()
				.withObject(entity)
				.withId(stringIdRepresentation((ID) entity.getId()))
				.withType(entity.getType())
				.withIndexName(entity.getTenantId())
				.build();
	}
}
