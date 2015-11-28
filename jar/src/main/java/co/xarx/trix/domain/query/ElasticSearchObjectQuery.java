package co.xarx.trix.domain.query;

public interface ElasticSearchObjectQuery {

	ElasticSearchQuery build(QueryBuilder builder);
}
