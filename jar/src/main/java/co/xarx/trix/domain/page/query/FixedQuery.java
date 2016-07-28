package co.xarx.trix.domain.page.query;

import co.xarx.trix.annotation.SdkExclude;
import co.xarx.trix.domain.BaseEntity;
import co.xarx.trix.domain.page.Block;
import co.xarx.trix.domain.page.query.statement.AbstractStatement;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;


@SdkExclude
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "queryfixed")
public class FixedQuery extends BaseEntity implements Query {

	public FixedQuery(AbstractStatement objectStatement, List<Integer> indexes) {
		this.objectStatement = objectStatement;
		this.indexes = indexes;
	}

	@Id
	@Setter(AccessLevel.NONE)
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer id;

	@NotNull
	@ElementCollection
	@JoinTable(name = "queryfixed_indexes", joinColumns = @JoinColumn(name = "query_id"))
	public List<Integer> indexes;

	@JoinColumn(name = "statement_id")
	@OneToOne(cascade = CascadeType.ALL)
	public AbstractStatement objectStatement;

	@Override
	public Map<Integer, Block> fetch(QueryRunner executor) {
		return executor.execute(this, indexes.size(), 0);
	}
}
