package co.xarx.trix.domain.query.statement;

import java.util.Map;

public interface SortedStatement extends Statement {

	void addSort(String attribute, Boolean asc);

	Map<String, Boolean> getSorts();
}
