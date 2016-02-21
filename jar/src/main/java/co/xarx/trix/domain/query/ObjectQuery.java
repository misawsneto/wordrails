package co.xarx.trix.domain.query;

import co.xarx.trix.domain.page.Block;
import co.xarx.trix.domain.query.statement.ObjectStatement;

import javax.persistence.MappedSuperclass;
import java.util.Map;
import java.util.Set;

@MappedSuperclass
public interface ObjectQuery {

	default String getObjectType() {
		return getObjectStatement().getObjectType();
	}

	Set<Integer> getIndexes();

	ObjectStatement getObjectStatement();

	Map<Integer, Block> fetch(QueryRunner executor); //visitor design pattern
}
