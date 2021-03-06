package co.xarx.trix.converter;

import co.xarx.trix.api.CellView;
import co.xarx.trix.api.RowView;
import co.xarx.trix.domain.Cell;
import co.xarx.trix.domain.Row;
import co.xarx.trix.persistence.CellRepository;
import co.xarx.trix.persistence.RowRepository;
import co.xarx.trix.persistence.TermPerspectiveRepository;
import co.xarx.trix.persistence.TermRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RowConverter extends AbstractConverter<Row, RowView>{

	@Autowired CellConverter cellConverter;
	@Autowired CellRepository cellRepository;
	
	@Autowired TermRepository termRepository;
	@Autowired TermPerspectiveRepository termPerspectiveRepository;
	@Autowired RowRepository rowRepository;
	
	@Override
	public Row convertFrom(RowView rowView) {
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
	public RowView convertTo(Row row) {
		RowView rowView = new RowView();
		rowView.id = row.id;
		rowView.termId = (row.term != null ? row.term.id : null);
		rowView.termName = (row.term != null ? row.term.name : null);
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

	public List<RowView> convertToViews(List<Row> entities, boolean addBody){
		List<RowView> views = new ArrayList<RowView>(entities.size());
		for (Row entity : entities) {
			RowView view = convertToView(entity, addBody);
			if(view!=null)
				views.add(view);
		}
		return views;
	}

	public RowView convertToView(Row row, boolean addBody) {
		RowView rowView = new RowView();
		rowView.id = row.id;
		rowView.termId = (row.term != null ? row.term.id : null);
		rowView.termName = (row.term != null ? row.term.name : null);
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
			rowView.cells = cellConverter.convertToViews(row.cells, addBody);
		}
		return rowView;
	}
}