package co.xarx.trix.domain.query;

import co.xarx.trix.domain.BaseEntity;
import co.xarx.trix.domain.page.Block;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.Set;

@Entity
@Table(name = "query_fixed")
public class FixedQuery extends BaseEntity implements Query {

	@NotNull
	@ElementCollection
	@JoinTable(name = "query_fixed_indexes")
	public Set<Integer> indexes;

	@JoinColumn(name = "object_query_id")
	@OneToOne(cascade = CascadeType.ALL)
	public BaseObjectQuery objectQuery;

	@Override
	public BaseObjectQuery getObjectQuery() {
		return objectQuery;
	}

	public void setObjectQuery(ObjectQuery objectQuery) {
		this.objectQuery = (BaseObjectQuery) objectQuery;
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
