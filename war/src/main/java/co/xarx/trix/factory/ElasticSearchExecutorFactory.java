package co.xarx.trix.factory;

import co.xarx.trix.domain.query.ElasticSearchExecutor;

public interface ElasticSearchExecutorFactory {

	ElasticSearchExecutor getElasticSearchExecutor(String alias);
}
