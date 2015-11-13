package co.xarx.trix.domain.page;


import java.util.Collection;

public interface ListSection<T extends CellItem> {

	Collection<T> getCells();
}
