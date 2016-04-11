package co.xarx.trix.domain.page.query;

import co.xarx.trix.domain.page.query.statement.PostStatement;

public interface CommandBuilder<T extends Command> {

	T build(PostStatement statement);
}
