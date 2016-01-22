package co.xarx.trix.factory;

import co.xarx.trix.domain.query.ElasticSearchExecutor;
import co.xarx.trix.domain.query.ExecutorFactory;

public interface ElasticSearchExecutorFactory extends ExecutorFactory<ElasticSearchExecutor> {

	ElasticSearchExecutor getExecutor(String alias);
}
