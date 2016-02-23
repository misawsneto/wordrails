package co.xarx.trix.domain.page;

import co.xarx.trix.domain.query.FixedQuery;
import co.xarx.trix.domain.query.PageableQuery;
import co.xarx.trix.util.Constants;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@lombok.Getter
@lombok.Setter
@lombok.NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "sectionqueryablelist")
@PrimaryKeyJoinColumn(name = "section_id", referencedColumnName = "id")
public class QueryableListSection extends AbstractSection implements ListSection, QueryableSection {

	private static final long serialVersionUID = -1931423761123134760L;

	public QueryableListSection(Integer size, PageableQuery query) {
		this.size = size;
		this.pageableQuery = query;
	}

	public QueryableListSection(Integer size, List<FixedQuery> query) {
		this.size = size;
		this.fixedQueries = query;
	}

	@JsonIgnore
	@JoinTable(name = "section_fixedquery",
			joinColumns = @JoinColumn(name = "section_id"),
			inverseJoinColumns = @JoinColumn(name = "query_id"))
	@OneToMany(cascade = CascadeType.ALL)
	public List<FixedQuery> fixedQueries;

	@JsonIgnore
	@OneToOne
	public PageableQuery pageableQuery;

	@JsonIgnore
	@Transient
	public Map<Integer, Block> blocks;

	@JsonProperty("blocks")
	public Collection<Block> getBlockList() {
		if(blocks == null) blocks = new HashMap<>();

		return blocks.values();
	}

	@NotNull
	public boolean isPageable = false;

	@NotNull
	public Integer size;

	@Override
	public String getType() {
		return Constants.Section.QUERYABLE_LIST;
	}

	@PrePersist
	public void onCreate() {
		if (isPageable && size == null || size == 0)
			throw new PersistenceException("Can't create a pageable section with no default size");
	}
}
