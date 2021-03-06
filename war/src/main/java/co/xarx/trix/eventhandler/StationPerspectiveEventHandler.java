package co.xarx.trix.eventhandler;

import co.xarx.trix.domain.*;
import co.xarx.trix.persistence.RowRepository;
import co.xarx.trix.persistence.StationPerspectiveRepository;
import co.xarx.trix.persistence.TaxonomyRepository;
import co.xarx.trix.persistence.TermPerspectiveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@RepositoryEventHandler(StationPerspective.class)
@Component
public class StationPerspectiveEventHandler {

	private @Autowired
	TaxonomyRepository taxonomyRepository;

	private @Autowired
	StationPerspectiveRepository stationPerspectiveRepository;

	private @Autowired
	TermPerspectiveRepository termPerspectiveRepository;

	private @Autowired
	RowRepository rowRepository;

	@HandleBeforeCreate
	public void handleBeforeCreate(StationPerspective stationPerspective) {
		validate(stationPerspective);
	}

	@HandleAfterCreate
	public void handleAfterCreate(StationPerspective stationPerspective){
		Taxonomy taxonomy = taxonomyRepository.findOne(stationPerspective.taxonomy.id);
		TermPerspective tp = new TermPerspective();
		tp.perspective = stationPerspective;
		tp.stationId = stationPerspective.station.id;
		tp.rows = new ArrayList<Row>();

		int index = 0;
		for (Term term: taxonomy.terms){
			Row row = new Row();
			row.term = term;
			row.type = Row.ORDINARY_ROW;
			tp.rows.add(row);
			row.perspective = tp;
			row.index = index;
			index ++;
		}

		stationPerspective.perspectives = new HashSet<TermPerspective>();
		stationPerspective.perspectives.add(tp);

		termPerspectiveRepository.save(tp);

		for(Row row: tp.rows){
			row.perspective = tp;
			rowRepository.save(row);
		}

		stationPerspectiveRepository.save(stationPerspective);
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