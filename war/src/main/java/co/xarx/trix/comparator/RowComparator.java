package co.xarx.trix.comparator;

import java.util.ArrayList;
import java.util.List;

import co.xarx.trix.domain.Cell;
import org.springframework.stereotype.Component;

@Component
public class RowComparator {

	public RowDifference getDifference(List<Cell> newCells, List<Cell> oldCells){
		RowDifference difference = new RowDifference();
		difference.cellsToDelete = new ArrayList<Cell>();

		if(newCells != null){
			difference.cellsToSave = new ArrayList<Cell>(newCells);
			if(oldCells != null){
				for (Cell oldCell : oldCells) {
					boolean contains = false;
					
					for (Cell newCell : difference.cellsToSave) {
						if(newCell.id != null && oldCell.id.equals(newCell.id)){
							contains = true;
							break;
						}
					}

					if(!contains){
						difference.cellsToDelete.add(oldCell);
					}
				}
			}
		}else{
			difference.cellsToSave = new ArrayList<Cell>();
			if(oldCells != null){
				difference.cellsToDelete.addAll(oldCells);		
			}
		}
		return difference;
	}
}