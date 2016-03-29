package co.xarx.trix.domain.page.query.statement;

import co.xarx.trix.domain.page.query.Command;
import co.xarx.trix.domain.page.query.CommandBuilder;

public interface Statement {

	Command build(CommandBuilder builder);

	Class getType();
}
