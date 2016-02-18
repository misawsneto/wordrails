package co.xarx.trix.domain.query;

import co.xarx.trix.domain.BaseEntity;
import co.xarx.trix.domain.page.Block;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "query_pageable")
public class PageableQuery extends BaseEntity implements Query {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer id;

	@Override
	public Integer getId() {
		return id;
	}

	@Transient
	private Integer size;

	@Transient
	private Integer from;

	@Transient
	private Set<Integer> indexExceptions; //indexes that are already filled

	@JoinColumn(name = "object_query_id")
	@OneToOne(cascade = CascadeType.ALL)
	public AbstractObjectQuery objectQuery;

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

	public void addIdExclusion(Serializable id) {
		this.objectQuery.addIdExclusion(id);
	}

	public Integer getSize() {
		return size - indexExceptions.size();
	}

	@Override
	public Map<Integer, Block> fetch(QueryExecutor executor) {
		return executor.execute(this);
	}
}
