package co.xarx.trix.domain.page.query;

import co.xarx.trix.domain.page.query.statement.Statement;

public interface Executor<T, K extends Statement> {

	Iterable<T> execute(K command, Integer size, Integer from);
}
