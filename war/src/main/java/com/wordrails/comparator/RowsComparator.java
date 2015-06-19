package com.wordrails.comparator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wordrails.business.Cell;
import com.wordrails.business.Row;

@Component
public class RowsComparator {

	private @Autowired RowComparator rowComparator; 

	public RowsDifference getDifference(List<Row> newRows, List<Row> oldRows){
		RowsDifference difference = new RowsDifference();
		difference.rowsToAdd = new ArrayList<Row>();
		difference.rowsToDelete = new ArrayList<Row>();
		difference.rowsToUpdate = new ArrayList<Row>();
		difference.cellsToDelete = new ArrayList<Cell>();

		if(newRows != null){
			if(oldRows != null){
				for (Row oldRow : oldRows) {
					boolean contains = false;
					Row newRow = null;

					for(int i = 0; i < newRows.size(); i++){
						newRow = newRows.get(i);
						if(newRow.id != null && newRow.id == oldRow.id){
							contains = true;
							RowDifference rowDifference = rowComparator.getDifference(newRow.cells, oldRow.cells);
							boolean cellsDifference = (rowDifference.cellsToSave != null && rowDifference.cellsToSave.size() > 0 ? true : false); 
							
							if(!newRow.equals(oldRow) ||  cellsDifference){
								difference.rowsToUpdate.add(newRow);
							}
							break;
						}
					}
					
					for(int i = 0; i < newRows.size(); i++){
						if(newRow.id == null){
							difference.rowsToAdd.add(newRow);
						}
					}
					
					if(!contains){
						difference.rowsToDelete.add(oldRow);
					}else{
						RowDifference rowDifference = rowComparator.getDifference(newRow.cells, oldRow.cells);
						difference.cellsToDelete.addAll(rowDifference.cellsToDelete);
					}
				}
			}else{
				difference.rowsToAdd.addAll(newRows);
			}
		}else{
			if(oldRows != null){
				difference.rowsToDelete.removeAll(oldRows);		
			}
		}
		return difference;
	}
}