package co.xarx.trix.domain.page.query.statement;

import co.xarx.trix.domain.Person;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "statementperson")
@PrimaryKeyJoinColumn(name = "statement_id", referencedColumnName = "id")
public class PersonStatement extends AbstractStatement implements Statement {

	private String query;

	@ElementCollection
	@JoinTable(name = "statementperson_authors", joinColumns = @JoinColumn(name = "statement_id"))
	private List<String> usernames;

	@ElementCollection
	@JoinTable(name = "statementperson_authors", joinColumns = @JoinColumn(name = "statement_id"))
	private List<String> emails;

	@ElementCollection
	@JoinTable(name = "statementpost_orders", joinColumns = @JoinColumn(name = "statement_id"))
	private List<String> orders;

	@Override
	public Class getType() {
		return Person.class;
	}
}
