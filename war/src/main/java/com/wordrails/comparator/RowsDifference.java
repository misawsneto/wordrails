package com.wordrails.comparator;

import java.util.List;

import com.wordrails.business.Cell;
import com.wordrails.business.Row;

public class RowsDifference {
	public List<Row> rowsToDelete;
	public List<Row> rowsToAdd;
	public List<Row> rowsToUpdate;
	
	public List<Cell> cellsToDelete;
}