package co.xarx.trix.elasticsearch.mapper;

import co.xarx.trix.domain.ElasticSearchEntity;

public interface ESMapper<T extends ElasticSearchEntity, K> {

	T asDto(K entity);
}
