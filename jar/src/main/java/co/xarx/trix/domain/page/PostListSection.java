package co.xarx.trix.domain.page;

import co.xarx.trix.domain.page.interfaces.ListSection;
import co.xarx.trix.domain.page.interfaces.QueryableSection;
import co.xarx.trix.domain.query.ElasticSearchQuery;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
public class PostListSection extends BaseSection implements ListSection<PostBlock>, QueryableSection<ElasticSearchQuery> {

	public static final String TYPE = "POST_LIST_SECTION";

	@Transient
	private List<PostBlock> blocks;

	@NotNull
	@OneToOne
	private ElasticSearchQuery query;

	@Override
	public List<PostBlock> getBlocks() {
		return blocks;
	}

	@Override
	public ElasticSearchQuery getQuery() {
		return query;
	}

	@Override
	public void setBlocks(List<PostBlock> blocks) {
		this.blocks = blocks;
	}

	@Override
	public void setQuery(ElasticSearchQuery query) {
		this.query = query;
	}

	@Override
	public String getType() {
		return TYPE;
	}
}
