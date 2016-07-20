package co.xarx.trix.domain.page.query.statement;

import co.xarx.trix.domain.Comment;
import lombok.*;

import javax.persistence.*;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
//@AllArgsConstructor
@Entity
@Table(name = "statementcomment")
@PrimaryKeyJoinColumn(name = "statement_id", referencedColumnName = "id")
public class CommentStatement extends AbstractStatement implements Statement {

	private String query;

	@ElementCollection
	@JoinTable(name = "statementcomment_authors", joinColumns = @JoinColumn(name = "statement_id"))
	private List<Integer> authors;

	@ElementCollection
	@JoinTable(name = "statementcomment_posts", joinColumns = @JoinColumn(name = "statement_id"))
	private List<Integer> posts;

	@ElementCollection
	@JoinTable(name = "statementcomment_stations", joinColumns = @JoinColumn(name = "statement_id"))
	private List<Integer> stations;

	@Column(name = "from_date")
	private String from;
	@Column(name = "until_date")
	private String until;

	@Transient
	private List<String> tenants;

	@ElementCollection
	@JoinTable(name = "statementcomment_orders", joinColumns = @JoinColumn(name = "statement_id"))
	private List<String> orders;

	public CommentStatement(String query, List<Integer> authors, List<Integer> posts, List<Integer> stations, String from, String until, List<String> orders) {
		this.query = query;
		this.authors = authors;
		this.posts = posts;
		this.stations = stations;
		this.from = from;
		this.until = until;
		this.orders = orders;
	}

	@Override
	public Class getType() {
		return Comment.class;
	}
}
