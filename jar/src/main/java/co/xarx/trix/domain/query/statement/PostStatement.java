package co.xarx.trix.domain.query.statement;

import co.xarx.trix.domain.query.QueryBuilder;
import co.xarx.trix.domain.query.elasticsearch.ElasticSearchQuery;
import co.xarx.trix.util.Constants;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Set;


@lombok.Getter @lombok.Setter @lombok.NoArgsConstructor
@Entity
@Table(name = "objectstatementpost")
@PrimaryKeyJoinColumn(name = "objectstatement_id", referencedColumnName = "id")
public class PostStatement extends AbstractObjectSortedStatement<ElasticSearchQuery> {

	@Column(name = "before_date")
	public Date before;
	@Column(name = "after_date")
	public Date after;

	@ElementCollection
	@JoinTable(name = "objectstatementpost_tags")
	public Set<String> tags;
	@ElementCollection
	@JoinTable(name = "objectstatementpost_categories")
	public Set<Integer> categories;

	@Column(name = "author_username")
	public String authorUsername;

	@Column(name = "all_readable_stations")
	public boolean allReadableStations;

	@NotNull
	@ElementCollection
	@JoinTable(name = "objectstatementpost_stations")
	public Set<Integer> stationIds;

	@Column(name = "rich_text")
	public String richText;

	@Override
	public ElasticSearchQuery build(QueryBuilder<ElasticSearchQuery> builder) {
		return builder.build(this);
	}

	@Override
	public String getObjectType() {
		return Constants.ObjectType.POST;
	}
}
