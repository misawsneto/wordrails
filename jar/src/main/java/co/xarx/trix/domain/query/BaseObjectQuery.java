package co.xarx.trix.domain.query;

import co.xarx.trix.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
@Table(name = "query_object_base")
@Inheritance(strategy = InheritanceType.JOINED)
@JsonIgnoreProperties({"updatedAt", "createdAt"})
public abstract class BaseObjectQuery extends BaseEntity implements ObjectQuery, SortedQuery {


	@ElementCollection(fetch = FetchType.EAGER)
	@JoinTable(name = "query_sorter", joinColumns = @JoinColumn(name = "query_id"))
	@MapKeyColumn(name = "sort_attribute", nullable = false)
	@Column(name = "is_asc", nullable = false)
	public Map<String, Boolean> sorts;

	@ElementCollection
	@JoinTable(name = "query_object_base_exceptions")
	public Set<Serializable> exceptionIds;

	public BaseObjectQuery() {
		sorts = new HashMap<>();
		exceptionIds = new HashSet<>();
	}

	@Override
	public void addSort(String attribute, Boolean asc) {
		sorts.put(attribute, asc);
	}

	@Override
	public Map<String, Boolean> getSorts() {
		return sorts;
	}

	public void addIdException(Serializable id) {
		this.exceptionIds.add(id);
	}
}
