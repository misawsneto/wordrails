package co.xarx.trix.domain.query;

import co.xarx.trix.domain.BaseEntity;
import co.xarx.trix.domain.page.Block;
import co.xarx.trix.domain.query.statement.AbstractObjectSortedStatement;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.Set;


@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "queryfixed")
public class FixedQuery extends BaseEntity implements ObjectQuery {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer id;

	@Override
	public Integer getId() {
		return id;
	}
	@NotNull
	@ElementCollection
	@JoinTable(name = "queryfixed_indexes")
	public Set<Integer> indexes;

	@JoinColumn(name = "objectstatement_id")
	@OneToOne(cascade = CascadeType.ALL)
	public AbstractObjectSortedStatement objectStatement;

	@Override
	public Map<Integer, Block> fetch(QueryRunner executor) {
		return executor.execute(this);
	}
}
