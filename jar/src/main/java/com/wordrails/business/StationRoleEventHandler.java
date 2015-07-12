package com.wordrails.business;

import com.wordrails.auth.TrixAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeDelete;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wordrails.persistence.TaxonomyRepository;
import com.wordrails.persistence.TermRepository;

@RepositoryEventHandler(StationRole.class)
@Component
public class StationRoleEventHandler {

	private @Autowired
	TrixAuthenticationProvider authProvider;
	
	@Transactional
	@HandleBeforeSave
	public void handleBeforeSave(StationRole stationRole) {
//		Person person = accessControllerUtil.getLoggedPerson();
		if(stationRole.admin){
			stationRole.writer = true;
			stationRole.editor = true;
		}else if(stationRole.editor){
			stationRole.admin = false;
			stationRole.writer = true;
		}else if(stationRole.writer){
			stationRole.admin = false;
			stationRole.editor = false;
		}else{
			stationRole.admin = false;
			stationRole.editor = false;
			stationRole.writer = false;
		}
	}
	
	@Transactional
	@HandleBeforeCreate
	public void handleBeforeCreate(StationRole stationRole) {
//		Person person = accessControllerUtil.getLoggedPerson();
		if(stationRole.admin){
			stationRole.writer = true;
			stationRole.editor = true;
		}else if(stationRole.editor){
			stationRole.admin = false;
			stationRole.writer = true;
		}else if(stationRole.writer){
			stationRole.admin = false;
			stationRole.editor = false;
		}else{
			stationRole.admin = false;
			stationRole.editor = false;
			stationRole.writer = false;
		}
	}

//	private @Autowired TaxonomyRepository taxonomyRepository;
//	private @Autowired TermRepository termRepository;
//	
//	@HandleBeforeSave
//	@Transactional
//	public void handleBeforeSave(StationRole stationRole) {
//		if(stationRole.writer){
//			Taxonomy taxonomy = taxonomyRepository.findAuthorTaxonomyByStationId(
//					stationRole.station, Taxonomy.STATION_AUTHOR_TAXONOMY);
//			Term term = termRepository.findTermAuthorTaxonomy(stationRole.person.name, taxonomy);
//			if(term == null){
//				term = new Term();
//				term.name = stationRole.person.name;
//				term.taxonomy = taxonomy;
//				taxonomyRepository.save(taxonomy);
//			}
//		}else{
//			Taxonomy taxonomy = taxonomyRepository.findAuthorTaxonomyByStationId(
//					stationRole.station, Taxonomy.STATION_AUTHOR_TAXONOMY);
//			Term term = termRepository.findTermAuthorTaxonomy(stationRole.person.name, taxonomy);
//			if(term != null){
//				termRepository.delete(term);
//			}
//		}
//	}
//	
//	@HandleBeforeCreate
//	@Transactional
//	public void handleBeforeCreate(StationRole stationRole) {
//		if(stationRole.writer){
//			Taxonomy taxonomy = taxonomyRepository.findAuthorTaxonomyByStationId(
//					stationRole.station, Taxonomy.STATION_AUTHOR_TAXONOMY);
//			if(taxonomy != null){
//				Term term = new Term();
//				term.name = stationRole.person.name;
//				term.taxonomy = taxonomy;
//				taxonomyRepository.save(taxonomy);
//			}
//		}
//	}
//	
//	@HandleBeforeDelete
//	@Transactional
//	public void handleBeforeDelete(StationRole stationRole) {
//		if(stationRole.writer){
//			Taxonomy taxonomy = taxonomyRepository.findAuthorTaxonomyByStationId(
//					stationRole.station, Taxonomy.STATION_AUTHOR_TAXONOMY);
//			Term term = termRepository.findTermAuthorTaxonomy(stationRole.person.name, taxonomy);
//			if(term != null){
//				termRepository.delete(term);
//			}
//		}
//	}
}