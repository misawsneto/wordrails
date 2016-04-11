package co.xarx.trix.web.rest.resource;

import co.xarx.trix.api.RowView;
import co.xarx.trix.api.TermPerspectiveView;
import co.xarx.trix.converter.CellConverter;
import co.xarx.trix.converter.RowConverter;
import co.xarx.trix.domain.*;
import co.xarx.trix.exception.ConflictException;
import co.xarx.trix.persistence.RowRepository;
import co.xarx.trix.persistence.StationPerspectiveRepository;
import co.xarx.trix.persistence.TermPerspectiveRepository;
import co.xarx.trix.persistence.TermRepository;
import co.xarx.trix.services.PerspectiveService;
import co.xarx.trix.web.rest.api.PerspectiveApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.ArrayList;
import java.util.List;

@Component
public class PerspectiveResource implements PerspectiveApi {

	private RowConverter rowConverter;
	private CellConverter cellConverter;
	private StationPerspectiveRepository stationPerspectiveRepository;
	private TermRepository termRepository;
	private TermPerspectiveRepository termPerspectiveRepository;
	private RowRepository rowRepository;
	private PerspectiveService perspectiveService;

	@Autowired
	public PerspectiveResource(RowConverter rowConverter, CellConverter cellConverter,
							   StationPerspectiveRepository stationPerspectiveRepository,
							   TermRepository termRepository, TermPerspectiveRepository termPerspectiveRepository,
							   RowRepository rowRepository, PerspectiveService perspectiveService) {
		this.rowConverter = rowConverter;
		this.cellConverter = cellConverter;
		this.stationPerspectiveRepository = stationPerspectiveRepository;
		this.termRepository = termRepository;
		this.termPerspectiveRepository = termPerspectiveRepository;
		this.rowRepository = rowRepository;
		this.perspectiveService = perspectiveService;
	}

	@Override
	@Transactional
	public Response putTermView(Integer id, TermPerspectiveView definition){
		Response response = Response.status(Status.NOT_ACCEPTABLE).build();

		TermPerspective termPerspective = termPerspectiveRepository.findOne(id);
		if(termPerspective != null){
			perspectiveService.updateTerm(termPerspective, definition.termId);
			perspectiveService.updateRow(definition.splashedRow, termPerspective, Row.SPLASHED_ROW);
			perspectiveService.updateRow(definition.featuredRow, termPerspective, Row.FEATURED_ROW);
			perspectiveService.updateRow(definition.homeRow, termPerspective, Row.HOME_ROW);
			perspectiveService.updateOrdinaryRows(definition.ordinaryRows, termPerspective);
			response = Response.status(Status.CREATED).build();
		}
		return response;
	}
	
	@Override
	@Transactional
	public Response postTermView(TermPerspectiveView definition) throws ConflictException {
		Response response = Response.status(Status.NOT_ACCEPTABLE).build();
		
		StationPerspective stationPerspective = stationPerspectiveRepository.findOne(definition.stationPerspectiveId);
		if(stationPerspective != null){
			Term term = null;
			if(definition.termId != null){
				term = termRepository.findOne(definition.termId);
			}
			
			if(term == null){
				if(termPerspectiveRepository.findPerspectiveAndTermNull(definition.stationPerspectiveId) != null){
					throw new ConflictException();
				}
			}
			
			TermPerspective termPerspective = new TermPerspective();
			termPerspective.perspective = stationPerspective;
			termPerspective.term = term;
			termPerspectiveRepository.save(termPerspective);

			List<Row> rows = new ArrayList<Row>(3);
			//Adding splashed posts
			if(definition.splashedRow != null){
				Row splashedRow = rowConverter.convertFrom(definition.splashedRow);
				splashedRow.splashedPerspective = termPerspective;
				rows.add(splashedRow);
			}
			
			//Adding featured row
			if(definition.featuredRow != null){
				Row featuredRow = rowConverter.convertFrom(definition.featuredRow);
				featuredRow.featuringPerspective = termPerspective;
				rows.add(featuredRow);
			}
			
			//Adding ordinary row
			if(definition.ordinaryRows != null){
				List<Row> ordinaryRows = rowConverter.convertToEntities(definition.ordinaryRows);
				for (Row row : ordinaryRows) {
					row.perspective = termPerspective;
				}
				rows.addAll(ordinaryRows);
			}

			for (Row row : rows) {
				rowRepository.save(row);
			}

			response = Response.status(Status.CREATED).entity("{\"id\": " + termPerspective.id + "}").build();
		}
		return response;
	}

	@Override
	public TermPerspectiveView getTermPerspectiveView(Integer termPerspectiveId,
													  Integer termId,
													  Integer stationPerspectiveId,
													  int page,
													  int size){

		return perspectiveService.termPerspectiveView(termPerspectiveId, termId, stationPerspectiveId, page, size);
	}

	@Override
	public TermPerspectiveView getTermPerspectiveDefinition(Integer id){
		TermPerspectiveView termView = null;
		
		TermPerspective termPerspective = termPerspectiveRepository.findOne(id);
		if(termPerspective != null){
			termView = perspectiveService.convertTermToTermView(termPerspective);
		}
		return termView;
	}

	@Override
	public RowView getRowView(Integer stationPerspectiveId,
							  Integer termPerspectiveId,
							  Integer childTermId,
							  Boolean withBody,
							  int page,
							  int size){

		RowView rowView = null;

		Term term = childTermId != null ? termRepository.findOne(childTermId) : null;
		if(term != null){
			TermPerspective termPerspective = null;
			if(termPerspectiveId != null){
				termPerspective = termPerspectiveRepository.findOne(termPerspectiveId);
			}

			if(termPerspective != null){
				int lowerLimit = (page == 1 ? 0 : (page - 1) * size);
				int upperLimit = page * size;

				Row row = rowRepository.findByPerspectiveAndTerm(termPerspective, term);

				if(row == null)
					return rowView;

				List<Cell> cells = new ArrayList<>();
				rowView = new RowView();
				if(row.type != null && row.type.equals(Row.ORDINARY_ROW)) {
					rowView.type = Row.ORDINARY_ROW;
					cells = perspectiveService.fillPostsNotPositionedInRows(row, termPerspective.perspective.station.id, page, size, lowerLimit, upperLimit);
				}else if(row.type != null && row.type.equals(Row.HOME_ROW)) {
					rowView.type = Row.HOME_ROW;
					cells = perspectiveService.fillPostsNotPositionedInHomeRows(row, termPerspective.perspective.station.id, page, size, lowerLimit, upperLimit);
				}
				rowView.id = row.id;
				rowView.cells = cellConverter.convertToViews(cells, withBody != null ? withBody : false);
				rowView.termId = term.id;
				rowView.termName = term.name;

			}else{
				StationPerspective stationPerspective = stationPerspectiveRepository.findOne(stationPerspectiveId);
				if(stationPerspective != null){
					rowView = perspectiveService.convertTermToRow(term, perspectiveService.loadTermsIds(term), stationPerspective.station.id, 0, Row.ORDINARY_ROW, page, size);
				}
			}
		}else if(stationPerspectiveId != null){
			StationPerspective stationPerspective = stationPerspectiveRepository.findOne(stationPerspectiveId);
			TermPerspective termPerspective = stationPerspective.perspectives != null && stationPerspective.perspectives.size() > 0 ? new ArrayList<TermPerspective>(stationPerspective.perspectives).get(0) : null;
			if(termPerspective != null && termPerspective.homeRow != null){
				int lowerLimit = (page == 1 ? 0 : (page - 1) * size);
				int upperLimit = page * size;

				Row row = termPerspective.homeRow;

				List<Cell> cells = new ArrayList<Cell>();
				rowView = new RowView();
				if(row.type != null && row.type.equals(Row.ORDINARY_ROW.toString())) {
					rowView.type = Row.ORDINARY_ROW;
					cells = perspectiveService.fillPostsNotPositionedInRows(row, termPerspective.perspective.station.id, page, size, lowerLimit, upperLimit);
				}else if(row.type != null && row.type.equals(Row.HOME_ROW.toString())) {
					rowView.type = Row.HOME_ROW;
					cells = perspectiveService.fillPostsNotPositionedInHomeRows(row, termPerspective.perspective.station.id, page, size, lowerLimit, upperLimit);
				}
				rowView.id = row.id;
				rowView.cells = cellConverter.convertToViews(cells, withBody != null ? withBody : false);
				rowView.termId = null;
				rowView.termName = null;

			}else{
				rowView = perspectiveService.convertTermToRow(null, termRepository.findTermIdsByTaxonomyId
						(stationPerspective
						.taxonomyId), stationPerspective.station.id, 0, Row.ORDINARY_ROW, page, size);
			}
		}
		return rowView;
	}
}