package co.xarx.trix.domain.query;

import co.xarx.trix.domain.BaseEntity;
import co.xarx.trix.domain.page.Block;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.Set;

@Entity
public class FixedQuery extends BaseEntity implements Query {

	@NotNull
	@ElementCollection
	public Set<Integer> indexes;

	@OneToOne(cascade = CascadeType.ALL)
	public ElasticSearchQuery elasticSearchQuery;

	@Override
	public ElasticSearchQuery getElasticSearchQuery() {
		return elasticSearchQuery;
	}

	@Override
	public void setElasticSearchQuery(ElasticSearchQuery elasticSearchQuery) {
		this.elasticSearchQuery = elasticSearchQuery;
	}

	@Override
	public Set<Integer> getIndexes() {
		return indexes;
	}

	public void setIndexes(Set<Integer> indexes) {
		this.indexes = indexes;
	}

	@Override
	public Map<Integer, Block> fetch(QueryExecutor executor) {
		return executor.execute(this);
	}
}
