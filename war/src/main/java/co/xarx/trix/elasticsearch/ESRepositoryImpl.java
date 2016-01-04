package co.xarx.trix.elasticsearch;

import co.xarx.trix.domain.ElasticSearchEntity;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.repository.support.AbstractElasticsearchRepository;
import org.springframework.data.elasticsearch.repository.support.ElasticsearchEntityInformation;
import org.springframework.stereotype.Component;

@Component
public class ESRepositoryImpl<T extends ElasticSearchEntity>
		extends AbstractElasticsearchRepository<T, Integer> implements ESRepository<T> {

	public ESRepositoryImpl() {
	}

	public ESRepositoryImpl(ElasticsearchOperations elasticsearchOperations) {
		super(elasticsearchOperations);
	}

	public ESRepositoryImpl(ElasticsearchEntityInformation<T, Integer> metadata, ElasticsearchOperations elasticsearchOperations) {
		super(metadata, elasticsearchOperations);
	}

	@Override
	protected String stringIdRepresentation(Integer id) {
		return id.toString();
	}

//	@Override
//	public <S extends T> S save(S entity) {
//		Assert.notNull(entity, "Cannot save 'null' entity.");
//		elasticsearchOperations.index(createIndexQuery(entity));
//		elasticsearchOperations.refresh(entity.getTenantId(), true);
//		return entity;
//	}

//	public void delete(String tenant, Integer id) {
//		Assert.notNull(id, "Cannot delete entity with id 'null'.");
//		elasticsearchOperations.delete(entityInformation.getIndexName(), entityInformation.getType(), stringIdRepresentation(id));
//		elasticsearchOperations.refresh(entityInformation.getIndexName(), true);
//	}

//	private IndexQuery createIndexQuery(T entity) {
//		return new IndexQueryBuilder()
//				.withObject(entity)
//				.withId(stringIdRepresentation(entity.getId()))
//				.withType(entity.getType())
//				.withIndexName(entity.getTenantId())
//				.build();
//	}
}
