package co.xarx.trix.domain.query;

import java.util.Map;

public interface SortedQuery {

	void addSort(String attribute, Boolean asc);

	Map<String, Boolean> getSorts();
}
