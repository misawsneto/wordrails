package co.xarx.trix.domain.query;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
@PrimaryKeyJoinColumn(name = "query_id", referencedColumnName = "id")
public class ElasticSearchQuery extends BaseQuery implements Query {

	@Lob
	public String queryString;

	public String type;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getQueryString() {
		return queryString;
	}

	public void setQueryString(String query) {
		this.queryString = query;
	}

	@Override
	public void fetch(QueryExecutor executor) {
		setBlocks(executor.execute(this));
	}
}
