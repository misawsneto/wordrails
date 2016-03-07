package co.xarx.trix.eventhandler;

import co.xarx.trix.domain.StationPerspective;
import co.xarx.trix.domain.Taxonomy;
import co.xarx.trix.domain.Term;
import co.xarx.trix.exception.OperationNotSupportedException;
import co.xarx.trix.exception.UnauthorizedException;
import co.xarx.trix.persistence.NetworkRepository;
import co.xarx.trix.persistence.StationPerspectiveRepository;
import co.xarx.trix.persistence.TaxonomyRepository;
import co.xarx.trix.persistence.TermRepository;
import co.xarx.trix.security.TaxonomySecurityChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.*;
import org.springframework.stereotype.Component;

import java.util.List;

@RepositoryEventHandler(Taxonomy.class)
@Component
public class TaxonomyEventHandler {
	
	private @Autowired TaxonomySecurityChecker taxonomySecurityChecker;
	private @Autowired TermEventHandler termEventHandler;
	private @Autowired
	TaxonomyRepository taxonomyRepository;
	private @Autowired NetworkRepository networkRepository;
	private @Autowired StationPerspectiveRepository stationPerspectiveRepository;
	private @Autowired TermRepository termRepository;
	
	@HandleBeforeCreate
	public void handleBeforeCreate(Taxonomy taxonomy) throws UnauthorizedException, OperationNotSupportedException {
		if(!taxonomySecurityChecker.canCreate(taxonomy)){
			throw new UnauthorizedException();
		}
	}
	
	@HandleBeforeSave
	public void handleBeforeSave(Taxonomy taxonomy) throws UnauthorizedException, OperationNotSupportedException {
		if(!taxonomySecurityChecker.canEdit(taxonomy)){
			throw new UnauthorizedException();
		}
	}
	
	@HandleAfterSave
	public void handleAfterSave(Taxonomy taxonomy) {
		List<Term> terms = termRepository.findByTaxonomy(taxonomy);
		
		if(terms!=null && terms.size() > 0){
			for (Term term : terms) {
				term.taxonomyName = taxonomy.name;
			}
			for (Term term: terms) {
				termRepository.save(term);
			}
		}
	}

	@HandleBeforeDelete
	public void handleBeforeDelete(Taxonomy taxonomy) throws UnauthorizedException, OperationNotSupportedException {
		if(!taxonomySecurityChecker.canEdit(taxonomy)){
			throw new UnauthorizedException();
//		}else if(taxonomy.type.equals(Taxonomy.STATION_AUTHOR_TAXONOMY)){
//			throw new OperationNotSupportedException();
		} else{
//			List<Network> networks = networkRepository.findByDefaultTaxonomy(taxonomy);
//			if(networks != null && networks.size() > 0){
//				throw new UnauthorizedException("Existe(m) rede(s) com essa taxonomia default.");
//			}else{
				taxonomyRepository.deleteTaxonomyNetworks(taxonomy.id);
				for (Term term : termRepository.findRoots(taxonomy.id)) {
					termEventHandler.handleBeforeDelete(term);
					termRepository.delete(term);
				}
				List<StationPerspective> stationsPerspectives = stationPerspectiveRepository.findByTaxonomy(taxonomy);
				if(stationsPerspectives != null && stationsPerspectives.size() > 0){
					stationPerspectiveRepository.delete(stationsPerspectives);
				}
//			}
		}
	}
}