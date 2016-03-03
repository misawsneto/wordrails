package co.xarx.trix.domain.page.query.statement;

import co.xarx.trix.domain.Post;
import co.xarx.trix.domain.page.query.Command;
import co.xarx.trix.domain.page.query.CommandBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Set;


@lombok.Getter @lombok.Setter @lombok.NoArgsConstructor
@Entity
@Table(name = "statementobjectpost")
@PrimaryKeyJoinColumn(name = "statement_id", referencedColumnName = "id")
public class PostStatement extends AbstractStatement implements Statement {

	@Column(name = "before_date")
	public Date before;
	@Column(name = "after_date")
	public Date after;

	@ElementCollection
	@JoinTable(name = "statementobjectpost_tags")
	public Set<String> tags;
	@ElementCollection
	@JoinTable(name = "statementobjectpost_categories")
	public Set<Integer> categories;

	@Column(name = "author_username")
	public String authorUsername;

	@Column(name = "all_readable_stations")
	public boolean allReadableStations;

	@NotNull
	@ElementCollection
	@JoinTable(name = "statementobjectpost_stations")
	public Set<Integer> stationIds;

	@Column(name = "rich_text")
	public String richText;

	@Override
	public Command build(CommandBuilder builder) {
		return builder.build(this);
	}

	@Override
	public Class getType() {
		return Post.class;
	}
}
