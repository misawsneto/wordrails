package co.xarx.trix.domain.page.query.statement;

import co.xarx.trix.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "statementpost")
@PrimaryKeyJoinColumn(name = "statement_id", referencedColumnName = "id")
public class PostStatement extends AbstractStatement implements Statement {

	private String query;

	@ElementCollection
	@JoinTable(name = "statementpost_authors", joinColumns = @JoinColumn(name = "statement_id"))
	private List<Integer> authors;

	@ElementCollection
	@JoinTable(name = "statementpost_stations", joinColumns = @JoinColumn(name = "statement_id"))
	private List<Integer> stations;

	private String state;
	@Column(name = "from_date")
	private String from;
	@Column(name = "until_date")
	private String until;

	@ElementCollection
	@JoinTable(name = "statementpost_categories", joinColumns = @JoinColumn(name = "statement_id"))
	private List<Integer> categories;

	@ElementCollection
	@JoinTable(name = "statementpost_tags", joinColumns = @JoinColumn(name = "statement_id"))
	private List<String> tags;

	@ElementCollection
	@JoinTable(name = "statementpost_orders", joinColumns = @JoinColumn(name = "statement_id"))
	private List<String> orders;

	public void addStationId(Integer stationId) {
		if (stations == null)
			stations = new ArrayList<>();

		stations.add(stationId);
	}

	@Override
	public Class getType() {
		return Post.class;
	}
}
