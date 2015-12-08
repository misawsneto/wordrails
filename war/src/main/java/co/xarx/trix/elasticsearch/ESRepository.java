package co.xarx.trix.elasticsearch;

import co.xarx.trix.domain.ElasticSearchEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface ESRepository<T extends ElasticSearchEntity> extends ElasticsearchRepository<T, Integer> {

	@Override
	<S extends T> S save(S entity);
}
