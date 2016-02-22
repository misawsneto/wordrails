package co.xarx.trix.domain.query.statement;

import co.xarx.trix.domain.BaseEntity;
import co.xarx.trix.domain.query.Command;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@lombok.Getter @Setter
@Entity
@Table(name = "statement")
@Inheritance(strategy = InheritanceType.JOINED)
@JsonIgnoreProperties({"updatedAt", "createdAt"})
public abstract class AbstractStatement extends BaseEntity implements Statement {

	@Id
	@Setter(AccessLevel.NONE)
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer id;

	@ElementCollection(fetch = FetchType.EAGER)
	@JoinTable(name = "query_sorts", joinColumns = @JoinColumn(name = "query_id"))
	@MapKeyColumn(name = "sort_attribute", nullable = false)
	@Column(name = "is_asc", nullable = false)
	public Map<String, Boolean> sorts;

	@ElementCollection
	@JoinTable(name = "statement_exceptions")
	public Set<Serializable> exceptionIds;

	public AbstractStatement() {
		sorts = new HashMap<>();
		exceptionIds = new HashSet<>();
	}

	@Override
	public void addSort(String attribute, Boolean asc) {
		sorts.put(attribute, asc);
	}

	@Override
	public void addIdExclusion(Serializable id) {
		this.exceptionIds.add(id);
	}
}
