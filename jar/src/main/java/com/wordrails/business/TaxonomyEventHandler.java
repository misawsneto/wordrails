package com.wordrails.business;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeDelete;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wordrails.persistence.NetworkRepository;
import com.wordrails.persistence.StationPerspectiveRepository;
import com.wordrails.persistence.TaxonomyRepository;
import com.wordrails.persistence.TermRepository;
import com.wordrails.security.TaxonomySecurityChecker;

@RepositoryEventHandler(Taxonomy.class)
@Component
public class TaxonomyEventHandler {
	
	private @Autowired TaxonomySecurityChecker taxonomySecurityChecker;
	private @Autowired TermEventHandler termEventHandler;
	private @Autowired TaxonomyRepository taxonomyRepository;
	private @Autowired NetworkRepository networkRepository;
	private @Autowired StationPerspectiveRepository stationPerspectiveRepository;
	private @Autowired TermRepository termRepository;
	
	@HandleBeforeCreate
	public void handleBeforeCreate(Taxonomy taxonomy) throws UnauthorizedException, OperationNotSupportedException {
		if(!taxonomySecurityChecker.canCreate(taxonomy)){
			throw new UnauthorizedException();
		}else if(taxonomy.type.equals(Taxonomy.STATION_AUTHOR_TAXONOMY)){
			throw new OperationNotSupportedException();
		}
	}
	
	@HandleBeforeSave
	public void handleBeforeSave(Taxonomy taxonomy) throws UnauthorizedException, OperationNotSupportedException {
		if(!taxonomySecurityChecker.canEdit(taxonomy)){
			throw new UnauthorizedException();
		}else if(taxonomy.type.equals(Taxonomy.STATION_AUTHOR_TAXONOMY)){
			throw new OperationNotSupportedException();
		}
	}
	
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