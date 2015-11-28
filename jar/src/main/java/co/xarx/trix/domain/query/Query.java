package co.xarx.trix.domain.query;

import co.xarx.trix.domain.page.Block;

import javax.persistence.MappedSuperclass;
import java.util.Map;
import java.util.Set;

@MappedSuperclass
public interface Query {

	default String getObjectType() {
		return getObjectQuery().getObjectType();
	}

	Set<Integer> getIndexes();

	ObjectQuery getObjectQuery();

	Map<Integer, Block> fetch(QueryExecutor executor); //visitor design pattern
}
