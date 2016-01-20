package co.xarx.trix.converter;

import co.xarx.trix.api.CellView;
import co.xarx.trix.domain.Cell;
import co.xarx.trix.domain.Row;
import co.xarx.trix.persistence.CellRepository;
import co.xarx.trix.persistence.RowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CellConverter extends AbstractConverter<Cell, CellView> {

	@Autowired PostConverter postConverter;
	@Autowired CellRepository cellRepository;
	@Autowired RowRepository rowRepository;
	
	public List<Cell> convertToEntities(List<CellView> views, Row row){
		List<Cell> entities = new ArrayList<Cell>();
		for (CellView cellView : views) {
			Cell cell = convertFrom(cellView);
			cell.row = row;
			cell.term = row.term;
			entities.add(cell);
		}
		return entities;
	}
	
	@Override
	public Cell convertFrom(CellView cellView) {
		Cell cell = new Cell();
		if(cellView.id != null){
			cell = cellRepository.findOne(cellView.id);
		}else{
			cell.index = cellView.index;
			cell.post = postConverter.convertFrom(cellView.postView);
		}
		return cell;
	}

	public List<CellView> convertToViews(List<Cell> entities, boolean addBody){
		List<CellView> views = new ArrayList<CellView>(entities.size());
		for (Cell entity : entities) {
			CellView view = convertToView(entity, addBody);
			if(view!=null)
				views.add(view);
		}
		return views;
	}

	CellView convertToView(Cell cell, boolean addBody) {
		CellView cellView = new CellView();
		cellView.id = cell.id;
		cellView.index = cell.index;
		if(cell.post != null){
			cellView.postView = postConverter.convertToView(cell.post, addBody);
			if(cellView.postView == null || cellView.postView.id == null)
				return null;
		}else return null;
		return cellView;
	}

	@Override
	public CellView convertTo(Cell cell) {
		CellView cellView = new CellView();
		cellView.id = cell.id;
		cellView.index = cell.index;
		if(cell.post != null){
			cellView.postView = postConverter.convertTo(cell.post);
			if(cellView.postView == null || cellView.postView.id == null)
				return null;
		}else return null;
		return cellView;
	}
}