package co.xarx.trix.eventhandler;

import co.xarx.trix.domain.StationPerspective;
import co.xarx.trix.domain.Taxonomy;
import co.xarx.trix.domain.Term;
import co.xarx.trix.exception.OperationNotSupportedException;
import co.xarx.trix.exception.UnauthorizedException;
import co.xarx.trix.persistence.StationPerspectiveRepository;
import co.xarx.trix.persistence.TaxonomyRepository;
import co.xarx.trix.persistence.TermRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.HandleBeforeDelete;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

import java.util.List;

@RepositoryEventHandler(Taxonomy.class)
@Component
public class TaxonomyEventHandler {

	@Autowired
	private TermEventHandler termEventHandler;
	@Autowired
	private TaxonomyRepository taxonomyRepository;
	@Autowired
	private StationPerspectiveRepository stationPerspectiveRepository;
	@Autowired
	private TermRepository termRepository;
	
	@HandleAfterSave
	public void handleAfterSave(Taxonomy taxonomy) {
		List<Term> terms = termRepository.findByTaxonomy(taxonomy);
		
		if(terms!=null && terms.size() > 0){
			for (Term term : terms) {
				term.taxonomyName = taxonomy.name;
			}
			termRepository.save(terms);
		}
	}

	@HandleBeforeDelete
	public void handleBeforeDelete(Taxonomy taxonomy) throws UnauthorizedException, OperationNotSupportedException {
		taxonomyRepository.deleteTaxonomyNetworks(taxonomy.id);
		for (Term term : termRepository.findRoots(taxonomy.id)) {
			termEventHandler.handleBeforeDelete(term);
			termRepository.delete(term);
		}
		List<StationPerspective> stationsPerspectives = stationPerspectiveRepository.findByTaxonomy(taxonomy);
		if (stationsPerspectives != null && stationsPerspectives.size() > 0) {
			stationPerspectiveRepository.delete(stationsPerspectives);
		}
	}
}