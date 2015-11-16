package co.xarx.trix.domain.query;

import co.xarx.trix.domain.page.interfaces.Block;

import java.util.List;

public interface Query {

	Integer getSize();

	Integer getPage();

	void setSize(Integer size);

	void setPage(Integer page);

	List<Block> getResults();

	void fetch(QueryExecutor executor); //visitor design pattern
}
