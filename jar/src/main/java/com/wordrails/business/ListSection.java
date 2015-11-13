package com.wordrails.business;


import java.util.Collection;

public interface ListSection<T extends CellItem> {

	Collection<T> getCells();
}
