package co.xarx.trix.services;

import co.xarx.trix.api.RowView;
import co.xarx.trix.api.TermPerspectiveView;
import co.xarx.trix.comparator.RowComparator;
import co.xarx.trix.comparator.RowDifference;
import co.xarx.trix.comparator.RowsComparator;
import co.xarx.trix.comparator.RowsDifference;
import co.xarx.trix.converter.CellConverter;
import co.xarx.trix.converter.RowConverter;
import co.xarx.trix.domain.*;
import co.xarx.trix.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

@Service
public class PerspectiveService {

	@Autowired
	private RowComparator rowComparator;
	@Autowired
	private RowsComparator rowsComparator;
	@Autowired
	private RowConverter rowConverter;
	@Autowired
	private CellConverter cellConverter;
	@Autowired
	private StationPerspectiveRepository stationPerspectiveRepository;
	@Autowired
	private TermRepository termRepository;
	@Autowired
	private TermPerspectiveRepository termPerspectiveRepository;
	@Autowired
	private PostRepository postRepository;
	@Autowired
	private RowRepository rowRepository;
	@Autowired
	private CellRepository cellRepository;

	public TermPerspectiveView termPerspectiveView(Integer termPerspectiveId, Integer termId, Integer
			stationPerspectiveId, int page, int size)  {
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

	private TermPerspectiveView convertRowsToTermViewDefault(Term term, Integer stationId, List<Term> terms, int page, int size) {
		TermPerspectiveView termView = new TermPerspectiveView();
		termView.ordinaryRows = convertTermsToRows(term, terms, stationId, Row.ORDINARY_ROW, page, size);
		if (term != null)
			termView.homeRow = convertTermToRow(null, termRepository.findTermIdsByTaxonomyId(term.taxonomyId), stationId, 0, Row.HOME_ROW, page, size);
		termView.termId = (term != null ? term.id : null);

		return termView;
	}

	private TermPerspectiveView convertRowsToTermView(TermPerspective termPerspective, List<Row> rows, int page, int size, int lowerLimit, int upperLimit) {
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

	private RowView fillPostsNotPositionedInHomeRow(Row row, Integer stationId, int page, int size, int lowerLimit, int upperLimit) {
		RowView rowView;
		row.cells = fillPostsNotPositionedInHomeRows(row, stationId, page, size, lowerLimit, upperLimit);
		rowView = rowConverter.convertTo(row);
		return rowView;
	}

	public List<Cell> fillPostsNotPositionedInHomeRows(Row row, Integer stationId, int page, int size, int lowerLimit,
													  int	upperLimit) {
		List<Cell> positionedCells = cellRepository.findCellsPositioned(row.id, lowerLimit, upperLimit);

		List<Cell> cells = null;

		List<Integer> ids = termRepository.findTermIdsByTaxonomyId(row.homePerspective.taxonomyId);

		int numberPostsNotPositioned = size - positionedCells.size();
		if (numberPostsNotPositioned > 0) {
			List<Integer> postPositionedIds = convertCellsToPostsIds(positionedCells);
			Pageable pageable = new PageRequest(page, numberPostsNotPositioned);

			if (postPositionedIds.size() > 0) {
				List<Post> notPositionedPosts = (postRepository.findPostsNotPositioned(stationId, ids, postPositionedIds, pageable));
				cells = mergePostsPositionedsNotPositioneds(row, positionedCells, notPositionedPosts, size);
			} else {
				List<Post> posts = ids != null && ids.size() > 0 ? postRepository.findPostsPublished(stationId, ids, pageable) : new ArrayList<Post>(); //postRepository.findPostsPublished(stationId, ids, pageable);
				cells = convertPostsToCells(row, posts);
			}
		} else {
			cells = positionedCells;
		}
		return cells;
	}

	public void updateTerm(TermPerspective termPerspective, Integer termId) {
		if (termPerspective.term != null && termPerspective.term.id != termId) {
			termPerspective.term = termRepository.findOne(termId);
		}
		termPerspectiveRepository.save(termPerspective);
	}

	public void updateRow(RowView newRowView, TermPerspective termPerspective, String type) {
		Row newRow;
		if (newRowView != null) {
			RowDifference difference = null;
			newRow = rowConverter.convertFrom(newRowView);
			if (type.equals(Row.FEATURED_ROW)) {
				newRow.featuringPerspective = termPerspective;
				difference = rowComparator.getDifference(newRow.cells, termPerspective.featuredRow == null ? null : termPerspective.featuredRow.cells);
			} else if (type.equals(Row.SPLASHED_ROW)) {
				newRow.splashedPerspective = termPerspective;
				difference = rowComparator.getDifference(newRow.cells, termPerspective.splashedRow == null ? null : termPerspective.splashedRow.cells);
			} else if (type.equals(Row.HOME_ROW)) {
				newRow.homePerspective = termPerspective;
				difference = rowComparator.getDifference(newRow.cells, termPerspective.homeRow == null ? null : termPerspective.homeRow.cells);
			}

			if (difference != null && difference.cellsToDelete != null && difference.cellsToDelete.size() > 0) {
				cellRepository.deleteInBatch(difference.cellsToDelete);
			}
			newRow.cells = difference.cellsToSave;
			rowRepository.save(newRow);
		} else {
			if (type.equals(Row.FEATURED_ROW) && termPerspective.featuredRow != null) {
				rowRepository.delete(termPerspective.featuredRow);
			} else if (type.equals(Row.SPLASHED_ROW) && termPerspective.splashedRow != null) {
				rowRepository.delete(termPerspective.splashedRow);
			} else if (type.equals(Row.HOME_ROW) && termPerspective.homeRow != null) {
				rowRepository.delete(termPerspective.homeRow);
			}
		}
	}

	public void updateOrdinaryRows(List<RowView> newOrdinaryRowsView, TermPerspective termPerspective) {
		List<Row> newOrdinaryRow = null;
		if (newOrdinaryRowsView != null) {
			newOrdinaryRow = rowConverter.convertToEntities(newOrdinaryRowsView);
		}

		RowsDifference rowsDifference = rowsComparator.getDifference(newOrdinaryRow, termPerspective.rows);
		if (rowsDifference.rowsToDelete != null && rowsDifference.rowsToDelete.size() > 0) {
			rowRepository.deleteInBatch(rowsDifference.rowsToDelete);
		}

		if (rowsDifference.rowsToAdd != null && rowsDifference.rowsToAdd.size() > 0) {
			rowRepository.save(rowsDifference.rowsToAdd);
		}

		if (rowsDifference.rowsToUpdate != null && rowsDifference.rowsToUpdate.size() > 0) {
			rowRepository.save(rowsDifference.rowsToUpdate);
		}

		if (rowsDifference.cellsToDelete != null && rowsDifference.cellsToDelete.size() > 0) {
			cellRepository.deleteInBatch(rowsDifference.cellsToDelete);
		}
	}

	private List<RowView> fillPostsNotPositionedInRows(Term term, List<Row> rows, Integer stationId, int page, int size, int lowerLimit, int upperLimit) {
		List<RowView> rowsView = new ArrayList<RowView>(rows.size() + 1);
		if (term != null) {
			rowsView.add(convertTermToRow(term, loadTermsIds(term), stationId, 0, Row.ORDINARY_ROW, page, size));
		}
		for (Row row : rows) {
			row.cells = fillPostsNotPositionedInRows(row, stationId, page, size, lowerLimit, upperLimit);
			rowsView.add(rowConverter.convertTo(row));
		}
		return rowsView;
	}

	public List<Cell> fillPostsNotPositionedInRows(Row row, Integer stationId, int page, int size, int lowerLimit, int upperLimit) {
		List<Cell> positionedCells = cellRepository.findCellsPositioned(row.id, lowerLimit, upperLimit);

		List<Cell> cells = null;

		Term term = termRepository.findTreeByTermId(row.term.id);
		List<Integer> ids = new ArrayList<Integer>();
		ids.add(term.id);
		convertTermTreeToIds(term, ids);

		int numberPostsNotPositioned = size - positionedCells.size();
		if (numberPostsNotPositioned > 0) {
			List<Integer> postPositionedIds = convertCellsToPostsIds(positionedCells);
			Pageable pageable = new PageRequest(page, numberPostsNotPositioned);

			if (postPositionedIds.size() > 0) {
				List<Post> notPositionedPosts = (postRepository.findPostsNotPositioned(stationId, ids, postPositionedIds, pageable));
				cells = mergePostsPositionedsNotPositioneds(row, positionedCells, notPositionedPosts, size);
			} else {
				List<Post> posts = ids != null && ids.size() > 0 ? postRepository.findPostsPublished(stationId, ids, pageable) : new ArrayList<Post>();// postRepository.findPostsPublished(stationId, ids, pageable);
				cells = convertPostsToCells(row, posts);
			}
		} else {
			cells = positionedCells;
		}
		return cells;
	}

	private List<Cell> mergePostsPositionedsNotPositioneds(Row row, List<Cell> positionedCells, List<Post> notPositionedPosts, int size) {
		List<Cell> cells = new ArrayList<Cell>();

		Iterator<Cell> iteratorPositionedCell = positionedCells.iterator();
		Cell positionedCell = (iteratorPositionedCell.hasNext() ? iteratorPositionedCell.next() : null);

		Iterator<Post> iteratorNotPositionedPost = notPositionedPosts.iterator();
		Post notPositionedPost = (iteratorNotPositionedPost.hasNext() ? iteratorNotPositionedPost.next() : null);

		boolean endPostsPositioned = false;
		boolean endPostsNotPositioned = false;

		for (int i = 0; i < size; i++) {
			if (positionedCell != null && i == positionedCell.index) {
				cells.add(positionedCell);
				positionedCell = (iteratorPositionedCell.hasNext() ? iteratorPositionedCell.next() : null);
				if (positionedCell == null) {
					endPostsPositioned = true;
					if (endPostsNotPositioned) {
						break;
					}
				}
			} else {
				cells.add(convertPostToCell(row, notPositionedPost, i));
				notPositionedPost = (iteratorNotPositionedPost.hasNext() ? iteratorNotPositionedPost.next() : null);
				if (notPositionedPost == null) {
					endPostsNotPositioned = true;
					if (endPostsPositioned) {
						break;
					}
				}
			}
		}
		return cells;
	}

	public TermPerspectiveView convertTermToTermView(TermPerspective termPerspective) {
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

	private List<Integer> convertCellsToPostsIds(List<Cell> cells) {
		List<Integer> ids = new ArrayList<Integer>(cells.size());
		for (Cell cell : cells) {
			ids.add(cell.post.id);
		}
		return ids;
	}

	private List<RowView> convertTermsToRows(Term term, List<Term> terms, Integer stationId, String rowType, int page, int size) {
		List<RowView> rowsView = new ArrayList<RowView>(terms.size() + 1);

		if (term != null) {
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

	public List<Integer> loadTermsIds(Term term) {
		term = termRepository.findTreeByTermId(term.id);
		List<Integer> ids = new ArrayList<Integer>();
		ids.add(term.id);
		convertTermTreeToIds(term, ids);

		return ids;
	}

	private void convertTermTreeToIds(Term term, List<Integer> ids) {
		for (Term childTerm : term.children) {
			ids.add(childTerm.id);
			if (childTerm.children != null && childTerm.children.size() > 0) {
				convertTermTreeToIds(childTerm, ids);
			}
		}
	}

	public RowView convertTermToRow(Term term, List<Integer> termsIds, Integer stationId, int index, String rowType, int page, int size) {
		Pageable pageable = new PageRequest(page, size);

		List<Post> posts = termsIds != null && termsIds.size() > 0 ? postRepository.findPostsPublished(stationId, termsIds, pageable) : new ArrayList<Post>();

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

	private List<Cell> convertPostsToCells(Row row, List<Post> posts) {
		List<Cell> cells = new ArrayList<Cell>();
		int index = 0;

		for (Post post : posts) {
			cells.add(convertPostToCell(row, post, index));
			index++;
		}
		return cells;
	}

	private Cell convertPostToCell(Row row, Post post, int index) {
		Cell cell = new Cell();
		cell.index = index;
		cell.post = post;
		cell.row = row;
		cell.term = row.term;

		return cell;
	}
}
