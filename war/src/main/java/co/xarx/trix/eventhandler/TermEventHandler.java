package co.xarx.trix.eventhandler;

import co.xarx.trix.domain.*;
import co.xarx.trix.exception.UnauthorizedException;
import co.xarx.trix.persistence.CellRepository;
import co.xarx.trix.persistence.RowRepository;
import co.xarx.trix.persistence.StationPerspectiveRepository;
import co.xarx.trix.persistence.TermPerspectiveRepository;
import co.xarx.trix.persistence.TermRepository;
import co.xarx.trix.security.TaxonomySecurityChecker;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeDelete;
import org.springframework.data.rest.core.annotation.HandleBeforeLinkSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RepositoryEventHandler(Term.class)
@Component
public class TermEventHandler {

	private @Autowired TermRepository termRepository;
	private @Autowired TermPerspectiveRepository termPerspectiveRepository;
	private @Autowired RowRepository rowRepository;
	private @Autowired CellRepository cellRepository;
	private @Autowired TaxonomySecurityChecker taxonomySecurityChecker;
	private @Autowired StationPerspectiveRepository stationPerspectiveRepository;

	@HandleAfterCreate
	public void handleAfterCreate(Term term) {
		if(!term.taxonomy.type.equals(Taxonomy.STATION_TAG_TAXONOMY) &&
				!term.taxonomy.type.equals(Taxonomy.STATION_AUTHOR_TAXONOMY) &&
				taxonomySecurityChecker.canCreate(term.taxonomy)){

			List<StationPerspective> perspectives = stationPerspectiveRepository.findByTaxonomy(term.taxonomy);
			if(perspectives != null && perspectives.size() > 0){
				List<Row> rows = new ArrayList<Row>(perspectives.size());
				for (StationPerspective stationPerspective : perspectives) {
					TermPerspective perspective = null;
					if(term.parent != null){
						perspective = termPerspectiveRepository.findPerspectiveAndTerm(stationPerspective.id, term.parent.id);
					}else{
						perspective = termPerspectiveRepository.findPerspectiveAndTermNull(stationPerspective.id);
					}

					if(perspective != null) {

						Row lastRow = rowRepository.findFirstByPerspectiveOrderByIndexDesc(perspective);
						int lastIndex = 0;
						if(lastRow != null){
							lastIndex = lastRow.index == null ? 0 : lastRow.index;
						}

						Row row = new Row();
						row.term = term;
						row.type = Row.ORDINARY_ROW;
						row.index = lastIndex + 1;
						row.perspective = perspective;
						rows.add(row);
					}
				}
				rowRepository.save(rows);
			}
		}else{
			throw new UnauthorizedException();
		}
	}

	@HandleBeforeLinkSave
	public void handleBeforeLinkSave(Term term, Object... parameters) {
		if (term.parent != null) {
			Integer termTaxonomyId = term.taxonomy.id;
			Integer parentTaxonomyId = term.parent.taxonomy.id;
			if (!termTaxonomyId.equals(parentTaxonomyId)) {
				throw new RuntimeException("A term and its parent must be associated to the same taxonomy");
			}
		}
	}

	@HandleBeforeDelete
	@Transactional
	public void handleBeforeDelete(Term term) {
		if(taxonomySecurityChecker.canEdit(term.taxonomy)){
			deleteCascade(term, term);
		}else{
			throw new UnauthorizedException();
		}
	}

	public void deleteCascade(Term termToDelete, Term term){
		if(term.termPerspectives != null && term.termPerspectives.size() > 0){
			termPerspectiveRepository.delete(term.termPerspectives);
		}

		List<Row> rows = rowRepository.findByTerm(term);
		if(rows != null && rows.size() > 0){
			rowRepository.delete(rows);
		}

		List<Term> terms = termRepository.findByParent(term);
		if(terms != null && terms.size() > 0){
			deleteCascade(termToDelete, terms);
		}
		termRepository.deletePostsTerms(term.id);
		if(!termToDelete.equals(term)){
			termRepository.delete(term);
		}
	}

	private void deleteCascade(Term termToDelete, List<Term> terms){
		for (Term term : terms) {
			deleteCascade(termToDelete, term);
		}
	}
}