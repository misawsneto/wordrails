package co.xarx.trix.domain.query;

import co.xarx.trix.util.Constants;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "query_object_post")
@PrimaryKeyJoinColumn(name = "object_query_id", referencedColumnName = "id")
public class PostQuery extends BaseObjectQuery implements ElasticSearchObjectQuery, SortedQuery {

	@Column(name = "before_date")
	public Date before;
	@Column(name = "after_date")
	public Date after;

	@ElementCollection
	@JoinTable(name = "query_object_post_tags")
	public List<String> tags;
	@ElementCollection
	@JoinTable(name = "query_object_post_categories")
	public List<String> categories;

	public String authorUsername;
	public Integer stationId;
	public String richText;

	public Date getBefore() {
		return before;
	}

	public void setBefore(Date before) {
		this.before = before;
	}

	public Date getAfter() {
		return after;
	}

	public void setAfter(Date after) {
		this.after = after;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public List<String> getCategories() {
		return categories;
	}

	public void setCategories(List<String> categories) {
		this.categories = categories;
	}

	public String getAuthorUsername() {
		return authorUsername;
	}

	public void setAuthorUsername(String authorUsername) {
		this.authorUsername = authorUsername;
	}

	public Integer getStationId() {
		return stationId;
	}

	public void setStationId(Integer stationId) {
		this.stationId = stationId;
	}

	public String getRichText() {
		return richText;
	}

	public void setRichText(String richText) {
		this.richText = richText;
	}

	@Override
	public ElasticSearchQuery build(QueryBuilder builder) {
		return builder.build(this);
	}

	@Override
	public String getObjectType() {
		return Constants.ObjectType.POST;
	}
}
