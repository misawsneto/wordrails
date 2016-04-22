package co.xarx.trix.domain.page;

import co.xarx.trix.domain.page.query.FixedQuery;
import co.xarx.trix.domain.page.query.PageableQuery;
import co.xarx.trix.domain.page.query.QueryableSectionPopulator;
import co.xarx.trix.util.Constants;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "sectionqueryablelist")
@PrimaryKeyJoinColumn(name = "section_id", referencedColumnName = "id")
public class QueryableListSection extends AbstractSection implements QueryableSection {

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
	@JoinTable(name = "sectionqueryablelist_queryfixed",
			joinColumns = @JoinColumn(name = "section_id"),
			inverseJoinColumns = @JoinColumn(name = "query_id"))
	@OneToMany(cascade = CascadeType.ALL)
	public List<FixedQuery> fixedQueries;

	@JsonIgnore
	@OneToOne
	public PageableQuery pageableQuery;

	@JsonIgnore
	@Transient
	@Setter(AccessLevel.NONE)
	@Getter(AccessLevel.NONE)
	public Map<Integer, Block> blocks;

	@JsonProperty("blocks")
	public List<Block> getBlocks() {
		if(blocks == null)
			blocks = new HashMap<>();

		return new ArrayList(blocks.values());
	}

	@NotNull
	@JsonIgnore
	public boolean isPageable = false;

	@NotNull
	public Integer size;

	public Integer mSize;

	@Override
	public String getType() {
		return Constants.Section.QUERYABLE_LIST;
	}

	@Override
	@JsonProperty("createdAt")
	public Date getCreatedAt(){
		return super.getCreatedAt();
	}

	@PrePersist
	public void onCreate() {
		if (isPageable && size == null || size == 0)
			throw new PersistenceException("Can't create a pageable section with no default size");
	}

	@Override
	public void populate(QueryableSectionPopulator populator, Integer from) {
		this.blocks = populator.fetchQueries(this, from);
	}
}
