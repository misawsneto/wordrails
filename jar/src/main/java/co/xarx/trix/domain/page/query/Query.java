package co.xarx.trix.domain.page.query;

import co.xarx.trix.domain.page.Block;
import co.xarx.trix.domain.page.query.statement.Statement;

import java.util.List;
import java.util.Map;

public interface Query {

	default Class getType() {
		return getObjectStatement().getType();
	}

	List<Integer> getIndexes();

	Statement getObjectStatement();

	Map<Integer, Block> fetch(QueryRunner executor); //visitor design pattern
}
