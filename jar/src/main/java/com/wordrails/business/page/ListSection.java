package com.wordrails.business.page;


import java.util.Collection;

public interface ListSection<T extends CellItem> {

	Collection<T> getCells();
}
