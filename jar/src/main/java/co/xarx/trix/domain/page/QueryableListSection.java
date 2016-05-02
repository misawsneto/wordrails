package co.xarx.trix.domain.page;

import co.xarx.trix.domain.page.query.FixedQuery;
import co.xarx.trix.domain.page.query.PageableQuery;
import co.xarx.trix.domain.page.query.SectionPopulator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Entity
@Getter
@Setter
@NoArgsConstructor
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

	public QueryableListSection(Integer size, PageableQuery query, List<FixedQuery> fixedQueries) {
		this.size = size;
		this.pageableQuery = query;
		this.fixedQueries = fixedQueries;
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

	@Override
	@JsonProperty("blocks")
	public List<Block> getBlocks() {
		ArrayList <Block> blocks = new ArrayList<Block>();
		for (FixedQuery item :fixedQueries){
			blocks.add(new BlockImpl<>(item, FixedQuery.class));
		}
		return blocks;
	}

	@NotNull
	@JsonIgnore
	public boolean isPageable = false;

	@NotNull
	public Integer size;

	public Integer mSize;

	@Override
	public Type getType() {
		return Type.QUERYABLE;
	}

	@JsonProperty("createdAt")
	public Date getCreatedAt(){
		return this.createdAt;
	}

	@PrePersist
	public void onCreate() {
		if (isPageable && size == null || size == 0)
			throw new PersistenceException("Can't create a pageable section with no default size");
	}

	@Override
	public void populate(SectionPopulator populator, Integer from) {
		this.blocks = populator.fetchQueries(this, from);
	}
}
