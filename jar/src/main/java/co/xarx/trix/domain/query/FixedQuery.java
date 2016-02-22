package co.xarx.trix.domain.query;

import co.xarx.trix.annotation.SdkExclude;
import co.xarx.trix.domain.BaseEntity;
import co.xarx.trix.domain.page.Block;
import co.xarx.trix.domain.query.statement.AbstractStatement;
import lombok.AccessLevel;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.Set;


@SdkExclude
@lombok.Getter @lombok.Setter @lombok.NoArgsConstructor
@Entity
@Table(name = "queryfixed")
public class FixedQuery extends BaseEntity implements Query {

	@Id
	@Setter(AccessLevel.NONE)
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer id;

	@NotNull
	@ElementCollection
	@JoinTable(name = "queryfixed_indexes")
	public Set<Integer> indexes;

	@JoinColumn(name = "statement_id")
	@OneToOne(cascade = CascadeType.ALL)
	public AbstractStatement objectStatement;

	@Override
	public Map<Integer, Block> fetch(QueryRunner executor) {
		return executor.execute(this);
	}
}
