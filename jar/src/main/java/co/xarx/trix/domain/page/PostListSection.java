package co.xarx.trix.domain.page;

import co.xarx.trix.domain.page.interfaces.ListSection;
import co.xarx.trix.domain.page.interfaces.QueryableSection;
import co.xarx.trix.domain.query.ElasticSearchQuery;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@PrimaryKeyJoinColumn(name = "section_id", referencedColumnName = "id")
public class PostListSection extends BaseSection implements ListSection<PostBlock>, QueryableSection<ElasticSearchQuery<PostBlock>> {

	public static final String TYPE = "post_list_section";

	@NotNull
	@OneToOne
	public ElasticSearchQuery<PostBlock> query;

	@Override
	public List<PostBlock> getBlocks() {
		return query.getResults();
	}

	@Override
	public ElasticSearchQuery<PostBlock> getQuery() {
		return query;
	}

	@Override
	public void setQuery(ElasticSearchQuery<PostBlock> query) {
		this.query = query;
	}

	@Override
	public String getType() {
		return TYPE;
	}
}
