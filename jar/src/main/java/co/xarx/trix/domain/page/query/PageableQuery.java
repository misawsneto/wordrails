package co.xarx.trix.domain.page.query;

import co.xarx.trix.annotation.SdkExclude;
import co.xarx.trix.annotation.SdkInclude;
import co.xarx.trix.domain.BaseEntity;
import co.xarx.trix.domain.page.Block;
import co.xarx.trix.domain.page.query.statement.AbstractStatement;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@SdkExclude
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "querypageable")
public class PageableQuery extends BaseEntity implements Query {

	public PageableQuery(AbstractStatement objectStatement) {
		this.objectStatement = objectStatement;
	}


	@Id
	@Setter(AccessLevel.NONE)
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer id;

	@Transient
	@lombok.Getter(AccessLevel.NONE)
	private Integer size;

	@Transient
	@lombok.Getter(AccessLevel.NONE)
	private Integer from;

	@Transient
	private Integer startShift;

	@Transient
	@lombok.Setter(AccessLevel.NONE)
	private Set<Integer> indexExceptions; //indexes that are already filled

	@SdkInclude
	@JoinColumn(name = "statement_id")
	@OneToOne(cascade = CascadeType.ALL)
	public AbstractStatement objectStatement;

	@Override
	public List<Integer> getIndexes() {
		List<Integer> tmpIndexes = IntStream.rangeClosed(from, from + size - 1).boxed().collect(Collectors.toList());
		List<Integer> indexes = new ArrayList<>(tmpIndexes);
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
		this.objectStatement.addIdExclusion(id);
	}

	public Integer getSize() {
		return size - indexExceptions.size();
	}

	@Override
	public Map<Integer, Block> fetch(QueryRunner executor) {
		int sizeExceptions = indexExceptions == null ? 0 : indexExceptions.size();
		return executor.execute(this, this.size - sizeExceptions, from - startShift);
	}
}
