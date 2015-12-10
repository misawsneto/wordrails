package co.xarx.trix.domain.query;

import co.xarx.trix.domain.BaseEntity;
import co.xarx.trix.domain.page.Block;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
		Set<Integer> tmpIndexes = IntStream.rangeClosed(from, from+size-1).boxed().collect(Collectors.toSet());
		Set<Integer> indexes = new TreeSet<>(tmpIndexes);
		tmpIndexes.stream().filter(index -> indexExceptions != null && indexExceptions.contains(index))
				.forEach(indexes::remove);
		return indexes;
	}

	public void addIndexException(Integer indexException) {
		if(this.indexExceptions == null)
			indexExceptions = new TreeSet<>();

		this.indexExceptions.add(indexException);
	}

	public void addIdException(Serializable id) {
		this.getElasticSearchQuery().addIdException(id);
	}

	public Integer getSize() {
		return size - indexExceptions.size();
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