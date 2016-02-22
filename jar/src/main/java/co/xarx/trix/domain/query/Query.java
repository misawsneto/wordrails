package co.xarx.trix.domain.query;

import co.xarx.trix.domain.page.Block;
import co.xarx.trix.domain.query.statement.Statement;

import java.util.Map;
import java.util.Set;

public interface Query {

	default Class getType() {
		return getObjectStatement().getType();
	}

	Set<Integer> getIndexes();

	Statement getObjectStatement();

	Map<Integer, Block> fetch(QueryRunner executor); //visitor design pattern
}
