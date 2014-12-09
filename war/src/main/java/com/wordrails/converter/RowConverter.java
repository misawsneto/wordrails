package com.wordrails.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wordrails.api.RowView;
import com.wordrails.business.Row;
import com.wordrails.persistence.CellRepository;
import com.wordrails.persistence.RowRepository;
import com.wordrails.persistence.TermPerspectiveRepository;
import com.wordrails.persistence.TermRepository;

@Component
public class RowConverter extends AbstractConverter<Row, RowView>{

	@Autowired CellConverter cellConverter;
	@Autowired CellRepository cellRepository;
	
	@Autowired TermRepository termRepository;
	@Autowired TermPerspectiveRepository termPerspectiveRepository;
	@Autowired RowRepository rowRepository;
	
	@Override
	public Row convertToEntity(RowView rowView) {
		Row row = new Row();
		if(rowView.termPerspectiveId != null){
			if(rowView.type.equals(Row.FEATURED_ROW)){
				row.featuringPerspective = termPerspectiveRepository.findOne(rowView.termPerspectiveId);
			}else if(rowView.type.equals(Row.ORDINARY_ROW)){
				row.perspective = termPerspectiveRepository.findOne(rowView.termPerspectiveId);
			}else if(rowView.type.equals(Row.SPLASHED_ROW)){
				row.splashedPerspective = termPerspectiveRepository.findOne(rowView.termPerspectiveId);
			}
		}
		row.index = rowView.index;
		if(rowView.termId != null){
			row.term = termRepository.findOne(rowView.termId);
		}
		row.type = rowView.type;
		if(rowView.cells != null){
			row.cells = cellConverter.convertToEntities(rowView.cells, row);
		}
		row.id = rowView.id;
		return row;
	}

	@Override
	public RowView convertToView(Row row) {
		RowView rowView = new RowView();
		rowView.id = row.id;
		rowView.termId = (row.term != null ? row.term.id : null);
		rowView.type = row.type;
		rowView.index = row.index;
		if(row.type.equals(Row.FEATURED_ROW)){
			rowView.termPerspectiveId = row.featuringPerspective.id;
		}else if(row.type.equals(Row.ORDINARY_ROW)){
			rowView.termPerspectiveId = row.perspective.id;
		}else if(row.type.equals(Row.SPLASHED_ROW)){
			rowView.termPerspectiveId = row.splashedPerspective.id;
		}
		if(row.cells != null){
			rowView.cells = cellConverter.convertToViews(row.cells);
		}
		return rowView;
	}
}