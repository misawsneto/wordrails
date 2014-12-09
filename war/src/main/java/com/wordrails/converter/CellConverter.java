package com.wordrails.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wordrails.api.CellView;
import com.wordrails.business.Cell;
import com.wordrails.business.Row;
import com.wordrails.persistence.CellRepository;
import com.wordrails.persistence.RowRepository;

@Component
public class CellConverter extends AbstractConverter<Cell, CellView> {

	@Autowired PostConverter postConverter;
	@Autowired CellRepository cellRepository;
	@Autowired RowRepository rowRepository;
	
	public List<Cell> convertToEntities(List<CellView> views, Row row){
		List<Cell> entities = new ArrayList<Cell>(views.size());
		for (CellView cellView : views) {
			Cell cell = convertToEntity(cellView);
			cell.row = row;
			cell.term = row.term;
			entities.add(cell);
		}
		return entities;
	}
	
	@Override
	Cell convertToEntity(CellView cellView) {
		Cell cell = new Cell();
		if(cellView.id != null){
			cell = cellRepository.findOne(cellView.id);
		}else{
			cell.index = cellView.index;
			cell.post = postConverter.convertToEntity(cellView.postView);
		}
		return cell;
	}

	@Override
	CellView convertToView(Cell cell) {
		CellView cellView = new CellView();
		cellView.id = cell.id;
		cellView.index = cell.index;
		if(cell.post != null){
			cellView.postView = postConverter.convertToView(cell.post); 
		}
		return cellView;
	}
}