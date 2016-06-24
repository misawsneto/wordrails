package co.xarx.trix.persistence;

import co.xarx.trix.domain.ElasticSearchEntity;
import co.xarx.trix.domain.AccessLog;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ESAccessLogRepository<T extends ElasticSearchEntity> extends ElasticsearchRepository<AccessLog, String> {
}
