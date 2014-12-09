package com.wordrails.api;

import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wordrails.business.ConflictException;
import com.wordrails.business.StationEventHandler;
import com.wordrails.business.StationPerspective;
import com.wordrails.persistence.StationPerspectiveRepository;
import com.wordrails.persistence.StationRepository;
import com.wordrails.persistence.TaxonomyRepository;
import com.wordrails.persistence.TermPerspectiveRepository;
import com.wordrails.persistence.TermRepository;
import com.wordrails.test.AbstractTest;

@Component
public class PerspectiveResourceTest extends AbstractTest {

	@Autowired
	PerspectiveResource perspectiveResource;
	
	@Autowired
	StationEventHandler stationHandler;
	
	@Autowired
	StationPerspectiveRepository stationPerspectiveRepository; 
	@Autowired
	TaxonomyRepository taxonomyRepository; 
	@Autowired
	StationRepository stationRepository;
	@Autowired
	TermRepository termRepository;
	@Autowired
	TermPerspectiveRepository termPerspectiveRepository;
	
	private final String STATION_NAME = "wordRAILS";
	private final String TAXONOMY_NAME = "Localização";
	
	private static boolean settedUp = false;
	private static StationPerspective stationPerspective;
	
//	@Test
//	public void perspectiveViewTest() throws Exception{
//		perspectiveResource.getTermPerspectiveView(null, null, stationPerspective.id, 0, 10);
//	}
//	
//	@Test
//	public void perspectiveViewTest2() throws Exception{
//		List<Term> terms = termRepository.findByParent(null);
//		Term term = (terms.size() > 0 ? terms.get(0) : null);
//		perspectiveResource.getRowView(stationPerspective.station.id, null, term.id, 0, 10);
//	}
//	
//	@Test
//	public void perspectiveViewTest3() throws Exception{
//		List<TermPerspective> termsPerspective = termPerspectiveRepository.findByPerspective(stationPerspective);
//		
//		if(termsPerspective.size() > 0){
//			TermPerspective termPerspective = termsPerspective.get(0);
//			
//			List<Term> terms = termRepository.findByParent(termPerspective.term);
//			Term term = (terms.size() > 0 ? terms.get(0) : null);
//			perspectiveResource.getRowView(stationPerspective.station.id, termPerspective.id, term.id, 0, 10);
//		}
//	}
//	
//	@Test
//	public void perspectiveDefinitionTest() throws Exception{
//		perspectiveResource.getTermPerspectiveDefinition(stationPerspective.id);
//
//		List<Term> terms = termRepository.findByParent(null);
//		Term term = (terms.size() > 0 ? terms.get(0) : null);
//		perspectiveResource.getTermPerspectiveView(null, term.id, stationPerspective.id, 0, 10);
//	}
	
	@Before
	public void loadDatabase() throws ConflictException{
		if(!settedUp){
//			//-------------------- Taxonomy --------------------// 
//			Taxonomy taxonomy = new Taxonomy();
//			taxonomy.name = TAXONOMY_NAME;
//			taxonomy.type = Taxonomy.GLOBAL_TAXONOMY;
//			taxonomyRepository.save(taxonomy);
//
//			//-------------------- Term --------------------//
//			Term mundo = new Term();
//			mundo.parent = null;
//			mundo.name = "Mundo";
//			mundo.taxonomy = taxonomy;
//			termRepository.save(mundo);
//			
//			List<Term> terms = new ArrayList<Term>(5);
//			Term nordeste = new Term();
//			nordeste.name = "Nordeste";
//			nordeste.parent = mundo;
//			nordeste.taxonomy = taxonomy;
//			terms.add(nordeste);
//			
//			Term sudeste = new Term();
//			sudeste.name = "Sudeste";
//			sudeste.parent = mundo;
//			sudeste.taxonomy = taxonomy;
//			terms.add(sudeste);
//			
//			Term centroOeste = new Term();
//			centroOeste.name = "Centro-Oeste";
//			centroOeste.parent = mundo;
//			centroOeste.taxonomy = taxonomy;
//			terms.add(centroOeste);
//			
//			Term sul = new Term();
//			sul.name = "Sul";
//			sul.parent = mundo;
//			sul.taxonomy = taxonomy;
//			terms.add(sul);
//			
//			Term norte = new Term();
//			norte.name = "Norte";
//			norte.parent = mundo;
//			norte.taxonomy = taxonomy;
//			terms.add(norte);
//			context.getBean(TermRepository.class).save(terms); 
//			//-------------------- Term --------------------// 
//			//-------------------- Taxonomy --------------------//
//
//			//-------------------- Station --------------------//
//			Network network = new Network();
//			network.name = "XARX";
//			network.defaultTaxonomy = taxonomy;
//			context.getBean(NetworkRepository.class).save(network);
//			
//			Station station = new Station();
//			station.name = STATION_NAME;
//			HashSet<Network> networks = new HashSet<Network>(1);
//			networks.add(network);
//			station.networks = networks;
//			stationHandler.handleBeforeCreate(station);
//			stationRepository.save(station);
//			stationPerspective = station.perspectives.iterator().next();
//			
//			Taxonomy taxonomy2 = new Taxonomy();
//			taxonomy2.name = TAXONOMY_NAME;
//			taxonomy2.type = Taxonomy.STATION_TAXONOMY;
//			taxonomy2.owningStation = station;
//			taxonomyRepository.save(taxonomy2);
//			
//			Term brasil = new Term();
//			brasil.parent = null;
//			brasil.name = "Brasil";
//			brasil.taxonomy = taxonomy2;
//			termRepository.save(brasil);
//			//-------------------- Station --------------------//
//			
//			
//			stationPerspective.taxonomy = taxonomy;
			stationPerspective = stationPerspectiveRepository.findOne(1);
			
			//-------------------- TermPespective --------------------//
			TermPerspectiveView termPerspectiveView = new TermPerspectiveView();
			termPerspectiveView.stationPerspectiveId = stationPerspective.id;
			termPerspectiveView.termId = null;
//			
//			List<RowView> rowsView = new ArrayList<RowView>(2);
//			
//			RowView nordesteRowView = new RowView();
//			nordesteRowView.termId = nordeste.id;
//			nordesteRowView.index = 0;
//			nordesteRowView.type = Row.ORDINARY_ROW;
//			rowsView.add(nordesteRowView);
//			
//			RowView sudesteRowView = new RowView();
//			sudesteRowView.termId = sudeste.id;
//			sudesteRowView.index = 1;
//			sudesteRowView.type = Row.ORDINARY_ROW;
//			rowsView.add(sudesteRowView);

//			termPerspectiveView.ordinaryRows = rowsView;
			perspectiveResource.postTermView(termPerspectiveView);
			//-------------------- TermPespective --------------------//

			settedUp = true;
		}
	}
}