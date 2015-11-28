package co.xarx.trix.domain.page;

import co.xarx.trix.domain.query.FixedQuery;
import co.xarx.trix.domain.query.PageableQuery;
import co.xarx.trix.util.Constants;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Entity
@PrimaryKeyJoinColumn(name = "section_id", referencedColumnName = "id")
public class QueryableListSection extends BaseSection implements ListSection, QueryableSection {

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
		return blocks.values();
	}

	@NotNull
	public boolean isPageable = false;

	@NotNull
	public Integer size;

	@Override
	public PageableQuery getPageableQuery() {
		if(pageableQuery == null) return null;

		pageableQuery.setFrom(0);
		pageableQuery.setSize(this.getSize());
		return pageableQuery;
	}

	@Override
	public void setPageableQuery(PageableQuery pageableQuery) {
		this.pageableQuery = pageableQuery;
	}

	@Override
	public List<FixedQuery> getFixedQueries() {
		return fixedQueries;
	}

	@Override
	public void setFixedQueries(List<FixedQuery> fixedQueries) {
		this.fixedQueries = fixedQueries;
	}

	@Override
	public boolean isPageable() {
		return isPageable;
	}

	public void setPageable(boolean pageable) {
		isPageable = pageable;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer pageSize) {
		this.size = pageSize;
	}

	@Override
	public Map<Integer, Block> getBlocks() {
		return blocks;
	}

	public void setBlocks(Map<Integer, Block> blocks) {
		this.blocks = blocks;
	}

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
