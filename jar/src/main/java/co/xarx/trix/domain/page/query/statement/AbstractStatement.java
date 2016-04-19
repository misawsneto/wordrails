package co.xarx.trix.domain.page.query.statement;

import co.xarx.trix.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "statement")
@Inheritance(strategy = InheritanceType.JOINED)
@JsonIgnoreProperties({"updatedAt", "createdAt"})
public abstract class AbstractStatement extends BaseEntity implements Statement, SortedStatement {

	@Id
	@Setter(AccessLevel.NONE)
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer id;

	@ElementCollection(fetch = FetchType.EAGER)
	@JoinTable(name = "statement_sorts", joinColumns = @JoinColumn(name = "statement_id"))
	@MapKeyColumn(name = "sort_attribute", nullable = false, length = 100)
	@Column(name = "is_asc", nullable = false)
	public Map<String, Boolean> sorts;

	@ElementCollection
	@JoinTable(name = "statement_exceptions", joinColumns = @JoinColumn(name = "statement_id"))
	public Set<Serializable> exceptionIds;

	public AbstractStatement() {
		sorts = new HashMap<>();
		exceptionIds = new HashSet<>();
	}

	@Override
	public void addSort(String attribute, Boolean asc) {
		sorts.put(attribute, asc);
	}

	public void addIdExclusion(Serializable id) {
		this.exceptionIds.add(id);
	}
}
