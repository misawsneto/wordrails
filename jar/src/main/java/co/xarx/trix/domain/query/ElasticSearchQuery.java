package co.xarx.trix.domain.query;

import co.xarx.trix.domain.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Lob;
import java.util.List;

@Entity
public class ElasticSearchQuery<T> extends BaseEntity implements Query<T> {

	@Lob
	public String query;

	private QueryExecutor executor;

	@Override
	public String getQuery() {
		return query;
	}

	@Override
	public QueryExecutor getExecutor() {
		return executor;
	}


	public List<T> getResults() {
		return getExecutor().execute(this);
	}

	@Override
	public void setExecutor(QueryExecutor executor) {
		this.executor = executor;
	}
}
