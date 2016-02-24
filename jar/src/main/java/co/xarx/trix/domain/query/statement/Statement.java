package co.xarx.trix.domain.query.statement;

import co.xarx.trix.domain.query.Command;
import co.xarx.trix.domain.query.CommandBuilder;

import java.io.Serializable;
import java.util.Map;

public interface Statement {

	void addIdExclusion(Serializable id);

	Command build(CommandBuilder builder);

	void addSort(String attribute, Boolean asc);

	Map<String, Boolean> getSorts();

	Class getType();
}
