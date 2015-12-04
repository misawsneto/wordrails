package co.xarx.trix.elasticsearch;

import co.xarx.trix.domain.ElasticSearchEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

@NoRepositoryBean
public interface ESRepository<T extends ElasticSearchEntity, ID extends Serializable>
		extends ElasticsearchRepository<T, ID> {

	@Override
	<S extends T> S index(S entity);
}
