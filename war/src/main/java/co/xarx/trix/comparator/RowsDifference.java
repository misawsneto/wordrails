package co.xarx.trix.comparator;

import java.util.List;

import co.xarx.trix.domain.Cell;
import co.xarx.trix.domain.Row;

public class RowsDifference {
	public List<Row> rowsToDelete;
	public List<Row> rowsToAdd;
	public List<Row> rowsToUpdate;
	
	public List<Cell> cellsToDelete;
}