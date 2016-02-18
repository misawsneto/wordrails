package co.xarx.trix.domain.query;

import co.xarx.trix.domain.BaseEntity;
import co.xarx.trix.domain.page.Block;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.Set;


@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "query_fixed")
public class FixedQuery extends BaseEntity implements Query {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer id;

	@Override
	public Integer getId() {
		return id;
	}
	@NotNull
	@ElementCollection
	@JoinTable(name = "query_fixed_indexes")
	public Set<Integer> indexes;

	@JoinColumn(name = "object_query_id")
	@OneToOne(cascade = CascadeType.ALL)
	public AbstractObjectQuery objectQuery;

	@Override
	public Map<Integer, Block> fetch(QueryExecutor executor) {
		return executor.execute(this);
	}
}
