package com.wordrails.comparator;

import java.util.List;

import com.wordrails.domain.Cell;
import com.wordrails.domain.Row;

public class RowsDifference {
	public List<Row> rowsToDelete;
	public List<Row> rowsToAdd;
	public List<Row> rowsToUpdate;
	
	public List<Cell> cellsToDelete;
}