package co.xarx.trix.domain.page;


import java.util.Map;

public interface ListSection extends Section {

	Map<Integer, Block> getBlocks();

	void setBlocks(Map<Integer, Block> blocks);
}
