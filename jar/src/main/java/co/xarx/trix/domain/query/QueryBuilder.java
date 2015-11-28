package co.xarx.trix.domain.query;

public interface QueryBuilder {

	ElasticSearchQuery build(PostQuery query);
}
