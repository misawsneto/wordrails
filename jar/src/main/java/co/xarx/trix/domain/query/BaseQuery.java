package co.xarx.trix.domain.query;

import co.xarx.trix.domain.BaseEntity;
import co.xarx.trix.domain.page.interfaces.Block;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Transient;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class BaseQuery extends BaseEntity implements Query {

	@Transient
	private Integer size;

	@Transient
	private Integer page;

	@Transient
	private List<Block> blocks;

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

	@Override
	public List<Block> getResults() {
		if(blocks == null) {
			throw new UnsupportedOperationException("You need to call fetch(QueryExecutor) before getting the results");
		}

		return blocks;
	}

	protected void setBlocks(List<Block> blocks) {
		this.blocks = blocks;
	}
}
