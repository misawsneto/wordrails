package co.xarx.trix.domain.query;

import co.xarx.trix.domain.BaseEntity;
import co.xarx.trix.domain.page.interfaces.Block;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Transient;
import java.util.List;

@Entity
public class ElasticSearchQuery extends BaseEntity implements Query {

	@Transient
	private Integer size;

	@Transient
	private Integer page;

	@Lob
	public String query;

	@Override
	public String getQuery() {
		return query;
	}

	@Override
	public Integer getSize() {
		return size;
	}

	@Override
	public Integer getPage() {
		return page;
	}

	@Override
	public void setSize(Integer size) {
		this.size = size;
	}

	@Override
	public void setPage(Integer page) {
		this.page = page;
	}

	public List<Block> getResults(QueryExecutor executor) {
		return executor.execute(this);
	}
}
