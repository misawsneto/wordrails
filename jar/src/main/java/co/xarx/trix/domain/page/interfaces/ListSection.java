package co.xarx.trix.domain.page.interfaces;


import java.util.List;

public interface ListSection<T extends Block> extends Section {

	List<T> getBlocks();
}
