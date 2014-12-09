package com.wordrails.business;

import java.util.Set;

import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

@RepositoryEventHandler(StationPerspective.class)
@Component
public class StationPerspectiveEventHandler {
	
	@HandleBeforeCreate
	public void handleBeforeCreate(StationPerspective stationPerspective) {
		validate(stationPerspective);
	}
	
	@HandleBeforeSave
	public void handleBeforeSave(StationPerspective stationPerspective) {
		validate(stationPerspective);
	}	
	
	private void validate(StationPerspective stationPerspective){
		if(stationPerspective.perspectives != null && stationPerspective.perspectives.size() > 0){
			validateTermPerspective(stationPerspective.perspectives, stationPerspective.taxonomy);
		}
	}

	private void validateTermPerspective(Set<TermPerspective> termsPerspectives, Taxonomy taxonomy){
		for (TermPerspective termPerspective : termsPerspectives) {
			if(!termPerspective.term.taxonomy.equals(taxonomy)){
				throw new RuntimeException("All terms must be associated with same taxonomy");
			}
		}
	}
}