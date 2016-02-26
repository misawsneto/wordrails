package co.xarx.trix.domain.query;

import co.xarx.trix.domain.query.statement.PostStatement;

public interface CommandBuilder<T extends Command> {

	T build(PostStatement statement);
}
