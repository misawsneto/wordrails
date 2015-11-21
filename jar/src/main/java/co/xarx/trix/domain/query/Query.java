package co.xarx.trix.domain.query;

import co.xarx.trix.domain.page.Block;

import javax.persistence.MappedSuperclass;
import java.util.Map;
import java.util.Set;

@MappedSuperclass
public interface Query {

	Set<Integer> getIndexes();

	void setElasticSearchQuery(ElasticSearchQuery query);

	ElasticSearchQuery getElasticSearchQuery();

	Map<Integer, Block> fetch(QueryExecutor executor); //visitor design pattern
}
