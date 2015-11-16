package co.xarx.trix.domain.query;

import co.xarx.trix.domain.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Lob;
import java.util.List;

@Entity
public class ElasticSearchQuery<T> extends BaseEntity implements Query<T> {

	@Lob
	public String query;

	private QueryExecutor<T, ElasticSearchQuery<T>> executor;

	@Override
	public String getQuery() {
		return query;
	}

	public void setExecutor(QueryExecutor<T, ElasticSearchQuery<T>> executor) {
		this.executor = executor;
	}

	public QueryExecutor<T, ElasticSearchQuery<T>> getExecutor() {
		return executor;
	}

	public List<T> getResults() {
		return getExecutor().execute(this);
	}
}
