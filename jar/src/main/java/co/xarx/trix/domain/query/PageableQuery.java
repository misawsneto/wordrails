package co.xarx.trix.domain.query;

import co.xarx.trix.domain.BaseEntity;
import co.xarx.trix.domain.page.Block;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

@Entity
public class PageableQuery extends BaseEntity implements Query {

	@Transient
	private Integer size;

	@Transient
	private Integer from;

	@Transient
	private Set<Integer> indexExceptions; //indexes that are already filled

	@OneToOne(cascade = CascadeType.ALL)
	public ElasticSearchQuery query;

	@Override
	public ElasticSearchQuery getElasticSearchQuery() {
		return query;
	}

	@Override
	public void setElasticSearchQuery(ElasticSearchQuery query) {
		this.query = query;
	}

	@Override
	public Set<Integer> getIndexes() {
		Set<Integer> indexes = new TreeSet<>();
		for (int i = from; i < from + size; i++) {
			if(!indexExceptions.contains(i)) indexes.add(i);
		}
		return indexes;
	}

	public void setIndexExceptions(Set<Integer> indexExceptions) {
		this.indexExceptions = indexExceptions;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public Integer getFrom() {
		return from;
	}

	public void setFrom(Integer from) {
		this.from = from;
	}

	@Override
	public Map<Integer, Block> fetch(QueryExecutor executor) {
		return executor.execute(this);
	}
}
