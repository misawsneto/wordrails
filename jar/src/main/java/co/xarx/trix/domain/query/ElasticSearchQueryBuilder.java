package co.xarx.trix.domain.query;

public interface ElasticSearchQueryBuilder extends QueryBuilder<ElasticSearchQuery> {

	ElasticSearchQuery build(PostQuery query);
}
