package co.xarx.trix.domain.query.statement;

import co.xarx.trix.domain.query.Command;
import co.xarx.trix.domain.query.CommandBuilder;

public interface Statement {

	Command build(CommandBuilder builder);

	Class getType();
}
