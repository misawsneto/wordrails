package co.xarx.trix.web.rest;

import co.xarx.trix.api.RowView;
import co.xarx.trix.api.TermPerspectiveView;
import co.xarx.trix.comparator.RowComparator;
import co.xarx.trix.comparator.RowDifference;
import co.xarx.trix.comparator.RowsComparator;
import co.xarx.trix.comparator.RowsDifference;
import co.xarx.trix.converter.CellConverter;
import co.xarx.trix.converter.PostConverter;
import co.xarx.trix.converter.RowConverter;
import co.xarx.trix.converter.TermConverter;
import co.xarx.trix.domain.*;
import co.xarx.trix.exception.ConflictException;
import co.xarx.trix.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

@Path("/perspectives")
@Component
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PerspectiveResource {
	
	private @Autowired
	RowComparator rowComparator;
	private @Autowired
	RowsComparator rowsComparator;

	private @Autowired
	RowConverter rowConverter;
	private @Autowired
	CellConverter cellConverter;
	private @Autowired
	PostConverter postConverter;
	private @Autowired
	TermConverter termConverter;
	
	private @Autowired
	StationPerspectiveRepository stationPerspectiveRepository;
	private @Autowired
	TaxonomyRepository taxonomyRepository;
	private @Autowired TermRepository termRepository;
	private @Autowired
	TermPerspectiveRepository termPerspectiveRepository;
	private @Autowired
	PostRepository postRepository;
	private @Autowired RowRepository rowRepository;
	private @Autowired
	CellRepository cellRepository;

	@Path("/termPerspectiveDefinitions/{id}")
	@PUT
	@Transactional
	public Response putTermView(@PathParam("id") Integer id, TermPerspectiveView definition){
		Response response = Response.status(Status.NOT_ACCEPTABLE).build();

		TermPerspective termPerspective = termPerspectiveRepository.findOne(id);
		if(termPerspective != null){
			updateTerm(termPerspective, definition.termId);
			updateRow(definition.splashedRow, termPerspective, Row.SPLASHED_ROW);
			updateRow(definition.featuredRow, termPerspective, Row.FEATURED_ROW);
			updateRow(definition.homeRow, termPerspective, Row.HOME_ROW);
			updateOrdinaryRows(definition.ordinaryRows, termPerspective);
			response = Response.status(Status.CREATED).build();
		}
		return response;
	}
	
	@Path("/termPerspectiveDefinitions")
	@POST
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
			rowRepository.save(rows);

			response = Response.status(Status.CREATED).entity("{\"id\": " + termPerspective.id +"}").build();
		}
		return response;
	}

	@Path("/termPerspectiveViews")
	@GET
	public TermPerspectiveView getTermPerspectiveView(@QueryParam("termPerspectiveId") Integer termPerspectiveId, 
				@QueryParam("termId") Integer termId, @QueryParam("stationPerspectiveId") Integer stationPerspectiveId, 
				@QueryParam("page") int page, @QueryParam("size") int size){
		
		TermPerspectiveView termView = new TermPerspectiveView();
		StationPerspective stationPerspective = stationPerspectiveRepository.findOne(stationPerspectiveId);
		
		if(stationPerspective != null){
			TermPerspective termPerspective;
			if(termPerspectiveId != null){
				termPerspective = termPerspectiveRepository.findOne(termPerspectiveId);
			}else{
				if(termId != null){
					termPerspective = termPerspectiveRepository.findPerspectiveAndTerm(stationPerspectiveId, termId);
				}else{
					termPerspective = termPerspectiveRepository.findPerspectiveAndTermNull(stationPerspectiveId);
				}
			}
			
			if(termPerspective != null){
				int lowerLimit = (page == 0 ? 0 : (page - 1) * size);
				int upperLimit = (page == 0 ? size : page * size);
				
				List<Row> rows = rowRepository.findByPerspective(termPerspective);
				Collections.sort(rows);
				termView = convertRowsToTermView(termPerspective, rows, page, size, lowerLimit, upperLimit);
				termView.stationPerspectiveId = termPerspective.perspective.id;
				termView.stationId = termPerspective.stationId;
			}else{
				List<Term> terms = null;
				Term term = null;
				if(termId != null){
					term = termRepository.findOne(termId);
					if(term != null){
						terms = termRepository.findByParent(term);
					}
				}else{
					terms = termRepository.findRoots(stationPerspective.taxonomy.id);
				}
				termView = convertRowsToTermViewDefault(term, stationPerspective.station.id, terms, page, size);
				termView.stationPerspectiveId = stationPerspective.id;
				termView.stationId = stationPerspective.stationId;
			}
		}
		return termView;
	}

	@Path("/termPerspectiveDefinitions/{id}")
	@GET
	public TermPerspectiveView getTermPerspectiveDefinition(@PathParam("id") Integer id){
		TermPerspectiveView termView = null;
		
		TermPerspective termPerspective = termPerspectiveRepository.findOne(id);
		if(termPerspective != null){
			termView = convertTermToTermView(termPerspective);
		}
		return termView;
	}

	@Path("/rowViews")
	@GET
	public RowView getRowView(@QueryParam("stationPerspectiveId") Integer stationPerspectiveId,
	                          @QueryParam("termPerspectiveId") Integer termPerspectiveId, @QueryParam("childTermId") Integer childTermId,
	                          @QueryParam("withBody") Boolean withBody, @QueryParam("page") int page, @QueryParam("size") int size){

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

				List<Cell> cells = fillPostsNotPositionedInRows(row, termPerspective.perspective.station.id, page, size, lowerLimit, upperLimit);
				rowView = new RowView();
				rowView.id = row.id;
				rowView.cells = cellConverter.convertToViews(cells, withBody != null ? withBody : false);
				rowView.termId = term.id;
				rowView.termName = term.name;
				rowView.type = Row.ORDINARY_ROW;
			}else{
				StationPerspective stationPerspective = stationPerspectiveRepository.findOne(stationPerspectiveId);
				if(stationPerspective != null){
					rowView = convertTermToRow(term, loadTermsIds(term), stationPerspective.station.id, 0, Row.ORDINARY_ROW, page, size);
				}
			}
		}else if(stationPerspectiveId != null){
            StationPerspective stationPerspective = stationPerspectiveRepository.findOne(stationPerspectiveId);
            TermPerspective termPerspective = stationPerspective.perspectives != null && stationPerspective.perspectives.size() > 0 ? new ArrayList<TermPerspective>(stationPerspective.perspectives).get(0) : null;
            if(termPerspective != null && termPerspective.homeRow != null){
                int lowerLimit = (page == 1 ? 0 : (page - 1) * size);
                int upperLimit = page * size;

                Row row = termPerspective.homeRow;

                List<Cell> cells = fillPostsNotPositionedInRows(row, stationPerspectiveId, page, size, lowerLimit, upperLimit);
                rowView = new RowView();
                rowView.id = row.id;
                rowView.cells = cellConverter.convertToViews(cells, withBody != null ? withBody : false);
                rowView.type = Row.ORDINARY_ROW;
            }else{
                rowView = convertTermToRow(null, termRepository.findTermIdsByTaxonomyId(stationPerspective.taxonomyId), stationPerspective.station.id, 0, Row.ORDINARY_ROW, page, size);
            }
        }
		return rowView;
	}

	private void updateTerm(TermPerspective termPerspective, Integer termId){
		if(termPerspective.term != null && termPerspective.term.id != termId){
			termPerspective.term = termRepository.findOne(termId);
		}
		termPerspectiveRepository.save(termPerspective);
	}
	
	private void updateRow(RowView newRowView, TermPerspective termPerspective, String type){
		Row newRow;
		if(newRowView != null){
			RowDifference difference = null;
			newRow = rowConverter.convertFrom(newRowView);
			if(type.equals(Row.FEATURED_ROW)){
				newRow.featuringPerspective = termPerspective;
				difference = rowComparator.getDifference(newRow.cells, termPerspective.featuredRow == null ? null : termPerspective.featuredRow.cells);
			}else if(type.equals(Row.SPLASHED_ROW)){
				newRow.splashedPerspective = termPerspective;
				difference = rowComparator.getDifference(newRow.cells, termPerspective.splashedRow == null ? null : termPerspective.splashedRow.cells);
			}

			if(difference != null && difference.cellsToDelete != null && difference.cellsToDelete.size() > 0){
				cellRepository.deleteInBatch(difference.cellsToDelete);
			}
			newRow.cells = difference.cellsToSave;
			rowRepository.save(newRow);
		}else{
			if(type.equals(Row.FEATURED_ROW) && termPerspective.featuredRow != null){
				rowRepository.delete(termPerspective.featuredRow);
			}else if(type.equals(Row.SPLASHED_ROW) && termPerspective.splashedRow != null){
				rowRepository.delete(termPerspective.splashedRow);
			} 
		}
	}
	
	private void updateOrdinaryRows(List<RowView> newOrdinaryRowsView, TermPerspective termPerspective){
		List<Row> newOrdinaryRow = null;
		if(newOrdinaryRowsView != null){
			newOrdinaryRow = rowConverter.convertToEntities(newOrdinaryRowsView);
		}
		
		RowsDifference rowsDifference = rowsComparator.getDifference(newOrdinaryRow, termPerspective.rows);
		if(rowsDifference.rowsToDelete != null && rowsDifference.rowsToDelete.size() > 0){
			rowRepository.deleteInBatch(rowsDifference.rowsToDelete);
		}
		
		if(rowsDifference.rowsToAdd != null && rowsDifference.rowsToAdd.size() > 0){
			rowRepository.save(rowsDifference.rowsToAdd);
		}
		
		if(rowsDifference.rowsToUpdate != null && rowsDifference.rowsToUpdate.size() > 0){
			rowRepository.save(rowsDifference.rowsToUpdate);
		}
		
		if(rowsDifference.cellsToDelete != null && rowsDifference.cellsToDelete.size() > 0){
			cellRepository.deleteInBatch(rowsDifference.cellsToDelete);
		}
	}
	
	private TermPerspectiveView convertRowsToTermViewDefault(Term term, Integer stationId, List<Term> terms, int page, int size){
		TermPerspectiveView termView = new TermPerspectiveView();
		termView.ordinaryRows = convertTermsToRows(term, terms, stationId, Row.ORDINARY_ROW, page, size);
        termView.homeRow = convertTermToRow(null, termRepository.findTermIdsByTaxonomyId(term.taxonomyId), stationId, 0, Row.HOME_ROW, page, size);
		termView.termId = (term != null ? term.id : null);

		return termView;
	}

	private TermPerspectiveView convertRowsToTermView(TermPerspective termPerspective, List<Row> rows, int page, int size, int lowerLimit, int upperLimit){
		TermPerspectiveView termView = new TermPerspectiveView();
		termView.splashedRow = (termPerspective.splashedRow != null ? rowConverter.convertTo(termPerspective.splashedRow) : null);
		termView.featuredRow = (termPerspective.featuredRow != null ? rowConverter.convertTo(termPerspective.featuredRow) : null);
		termView.ordinaryRows = fillPostsNotPositionedInRows(termPerspective.term, rows, termPerspective.perspective.station.id, page, size, lowerLimit, upperLimit);
        termView.homeRow = termPerspective.homeRow == null ? convertTermToRow(null, termRepository.findTermIdsByTaxonomyId(termPerspective.taxonomyId), termPerspective.stationId, 0, Row.HOME_ROW, page, size) :
                fillPostsNotPositionedInHomeRow(termPerspective.homeRow, termPerspective.perspective.station.id, page, size, lowerLimit, upperLimit);
        termView.homeRow.termPerspectiveId = termPerspective.id;
		termView.termId = (termPerspective.term != null ? termPerspective.term.id : null);
		termView.stationId = termPerspective.stationId;
		termView.taxonomyId = termPerspective.taxonomyId;
		termView.id = termPerspective.id;

		return termView;
	}

    private RowView fillPostsNotPositionedInHomeRow(Row row, Integer stationId, int page, int size, int lowerLimit, int upperLimit){
        RowView rowView;
        row.cells = fillPostsNotPositionedInHomeRows(row, stationId, page, size, lowerLimit, upperLimit);
        rowView = rowConverter.convertTo(row);
        return rowView;
    }

    private List<Cell> fillPostsNotPositionedInHomeRows(Row row, Integer stationId, int page, int size, int lowerLimit, int upperLimit){
        List<Cell> positionedCells = cellRepository.findCellsPositioned(row.id, lowerLimit, upperLimit);

        List<Cell> cells = null;

        List<Integer> ids = termRepository.findTermIdsByTaxonomyId(row.term.taxonomyId);

        int numberPostsNotPositioned = size - positionedCells.size();
        if(numberPostsNotPositioned > 0){
            List<Integer> postPositionedIds = convertCellsToPostsIds(positionedCells);
            Pageable pageable = new PageRequest(page, numberPostsNotPositioned);

            if(postPositionedIds.size() > 0){
                List<Post> notPositionedPosts = (postRepository.findPostsNotPositioned(stationId, ids, postPositionedIds, pageable));
                cells = mergePostsPositionedsNotPositioneds(row, positionedCells, notPositionedPosts, size);
            }else{
                List<Post> posts = postRepository.findPostsPublished(stationId, ids, pageable);
                cells = convertPostsToCells(row, posts);
            }
        }else{
            cells = positionedCells;
        }
        return cells;
    }

	private List<RowView> fillPostsNotPositionedInRows(Term term, List<Row> rows, Integer stationId, int page, int size, int lowerLimit, int upperLimit){
		List<RowView> rowsView = new ArrayList<RowView>(rows.size() + 1);
		if(term != null){
			rowsView.add(convertTermToRow(term, loadTermsIds(term), stationId, 0, Row.ORDINARY_ROW, page, size));
		}
		for (Row row : rows) {
			row.cells = fillPostsNotPositionedInRows(row, stationId, page, size, lowerLimit, upperLimit);
			rowsView.add(rowConverter.convertTo(row));
		}
		return rowsView;
	}

	private List<Cell> fillPostsNotPositionedInRows(Row row, Integer stationId, int page, int size, int lowerLimit, int upperLimit){
		List<Cell> positionedCells = cellRepository.findCellsPositioned(row.id, lowerLimit, upperLimit);
		
		List<Cell> cells = null;

		List<Term> terms = termRepository.findAll();
		Term term = termRepository.findTreeByTermId(row.term.id);
		List<Integer> ids = new ArrayList<Integer>();
		ids.add(term.id);
		convertTermTreeToIds(term, ids);
		
		int numberPostsNotPositioned = size - positionedCells.size();
		if(numberPostsNotPositioned > 0){
			List<Integer> postPositionedIds = convertCellsToPostsIds(positionedCells);
			Pageable pageable = new PageRequest(page, numberPostsNotPositioned);

			if(postPositionedIds.size() > 0){
				List<Post> notPositionedPosts = (postRepository.findPostsNotPositioned(stationId, ids, postPositionedIds, pageable));
				cells = mergePostsPositionedsNotPositioneds(row, positionedCells, notPositionedPosts, size);
			}else{
				List<Post> posts = postRepository.findPostsPublished(stationId, ids, pageable);
				cells = convertPostsToCells(row, posts);
			}
		}else{
			cells = positionedCells;
		}
		return cells;
	}

	private List<Cell> mergePostsPositionedsNotPositioneds(Row row, List<Cell> positionedCells, List<Post> notPositionedPosts, int size){
		List<Cell> cells = new ArrayList<Cell>();

		Iterator<Cell> iteratorPositionedCell = positionedCells.iterator();
		Cell positionedCell = (iteratorPositionedCell.hasNext() ? iteratorPositionedCell.next() : null);

		Iterator<Post> iteratorNotPositionedPost = notPositionedPosts.iterator();
		Post notPositionedPost = (iteratorNotPositionedPost.hasNext() ? iteratorNotPositionedPost.next() :  null);

		boolean endPostsPositioned = false;
		boolean endPostsNotPositioned = false;
		
		for(int i = 0; i < size; i++){
			if(positionedCell != null && i == positionedCell.index){
				cells.add(positionedCell);
				positionedCell = (iteratorPositionedCell.hasNext() ? iteratorPositionedCell.next() : null);
				if(positionedCell == null){
					endPostsPositioned = true;
					if(endPostsNotPositioned){
						break;
					}
				}
			}else{
				cells.add(convertPostToCell(row, notPositionedPost, i));
				notPositionedPost = (iteratorNotPositionedPost.hasNext() ? iteratorNotPositionedPost.next() :  null);
				if(notPositionedPost == null){
					endPostsNotPositioned = true;
					if(endPostsPositioned){
						break;
					}
				}
			}
		}
		return cells;
	}

	private TermPerspectiveView convertTermToTermView(TermPerspective termPerspective){
		TermPerspectiveView termView = new TermPerspectiveView();
		termView.featuredRow = (termPerspective.featuredRow != null ? rowConverter.convertTo(termPerspective.featuredRow) : null);
        termView.homeRow = (termPerspective.homeRow != null ? rowConverter.convertTo(termPerspective.homeRow) : null);
		termView.ordinaryRows = (List<RowView>) rowConverter.convertToViews(rowRepository.findByPerspective(termPerspective));
		termView.splashedRow = (termPerspective.splashedRow != null ? rowConverter.convertTo(termPerspective.splashedRow) : null);
		termView.termId = termPerspective.term.id;
		termView.termName = termPerspective.term.name;
		termView.taxonomyId = termPerspective.taxonomyId;
		termView.stationId = termPerspective.stationId;

		return termView;
	}

	private List<Integer> convertCellsToPostsIds(List<Cell> cells){
		List<Integer> ids = new ArrayList<Integer>(cells.size());
		for (Cell cell : cells) {
			ids.add(cell.post.id);
		}
		return ids;
	}

	private List<RowView> convertTermsToRows(Term term, List<Term> terms, Integer stationId, String rowType, int page, int size){
		List<RowView> rowsView = new ArrayList<RowView>(terms.size() + 1);
		
		if(term != null){
			List<Integer> ids = new ArrayList<Integer>(1);
			ids.add(term.id);
			rowsView.add(convertTermToRow(term, ids, stationId, 0, Row.ORDINARY_ROW, page, size));
		}

		int index = 0;
		for (Term childTerm : terms) {
			rowsView.add(convertTermToRow(childTerm, loadTermsIds(childTerm), stationId, index, rowType, page, size));
			index++;
		}
		return rowsView;
	}

	private List<Integer> loadTermsIds(Term term){
		term = termRepository.findTreeByTermId(term.id);
		List<Integer> ids = new ArrayList<Integer>();
		ids.add(term.id);
		convertTermTreeToIds(term, ids);
		
		return ids;
	}
	
	private void convertTermTreeToIds(Term term, List<Integer> ids){
		for (Term childTerm : term.children) {
			ids.add(childTerm.id);
			if(childTerm.children != null && childTerm.children.size() > 0){
				convertTermTreeToIds(childTerm, ids);
			}
		}
	}
	
	private RowView convertTermToRow(Term term, List<Integer> termsIds, Integer stationId, int index, String rowType, int page, int size){
		Pageable pageable = new PageRequest(page, size);
		
		List<Post> posts = postRepository.findPostsPublished(stationId, termsIds, pageable);

		Row row = new Row();
		row.index = index;
		row.term = term;
		row.type = rowType;

		RowView rowView = new RowView();
		rowView.cells = cellConverter.convertToViews(convertPostsToCells(row, posts)); 
		rowView.termId = term != null ? term.id : null;
		rowView.termName = term != null ? term.name : null;
		rowView.type = rowType;
		rowView.id = row.id;

		return rowView;
	}

	private List<Cell> convertPostsToCells(Row row, List<Post> posts){
		List<Cell> cells = new ArrayList<Cell>();
		int index = 0;

		for (Post post : posts) {
			cells.add(convertPostToCell(row, post, index));
			index++;
		}
		return cells;
	}

	private Cell convertPostToCell(Row row, Post post, int index){
		Cell cell = new Cell();
		cell.index = index;
		cell.post = post;
		cell.row = row;
		cell.term = row.term;

		return cell;
	}
}