package co.xarx.trix.domain.query;

import co.xarx.trix.annotation.SdkExclude;
import co.xarx.trix.annotation.SdkInclude;
import co.xarx.trix.domain.BaseEntity;
import co.xarx.trix.domain.page.Block;
import co.xarx.trix.domain.query.statement.AbstractStatement;
import lombok.AccessLevel;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@SdkExclude
@lombok.Getter @lombok.Setter @lombok.NoArgsConstructor
@Entity
@Table(name = "querypageable")
public class PageableQuery extends BaseEntity implements Query {

	@Id
	@Setter(AccessLevel.NONE)
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer id;

	@Transient
	private Integer size;

	@Transient
	private Integer from;

	@Transient
	private Set<Integer> indexExceptions; //indexes that are already filled

	@SdkInclude
	@JoinColumn(name = "statement_id")
	@OneToOne(cascade = CascadeType.ALL)
	public AbstractStatement objectStatement;

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
		this.objectStatement.addIdExclusion(id);
	}

	public Integer getSize() {
		return size - indexExceptions.size();
	}

	@Override
	public Map<Integer, Block> fetch(QueryRunner executor) {
		return executor.execute(this);
	}
}
