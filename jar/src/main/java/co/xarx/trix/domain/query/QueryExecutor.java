package co.xarx.trix.domain.query;

import java.util.List;

public interface QueryExecutor {

	<T> List<T> execute(Query<T> query);
}
