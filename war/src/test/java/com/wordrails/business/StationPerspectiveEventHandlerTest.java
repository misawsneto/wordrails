package com.wordrails.business;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.wordrails.persistence.NetworkRepository;
import com.wordrails.persistence.StationPerspectiveRepository;
import com.wordrails.persistence.StationRepository;
import com.wordrails.persistence.TaxonomyRepository;
import com.wordrails.persistence.TermRepository;
import com.wordrails.test.AbstractTest;

public class StationPerspectiveEventHandlerTest extends AbstractTest {
	
	@Autowired
	StationPerspectiveEventHandler handler;
	
	static StationPerspectiveRepository stationPerspectiveRepository; 
	static TaxonomyRepository taxonomyRepository; 
	static StationRepository stationRepository;
	static TermRepository termRepository;
	
	private static boolean settedUp = false;
	
	private final String STATION_NAME = "wordRAILS";
	private final String STATION_PERSPECTIVE_NAME = "wordRAILS Perspective Test";
	private final String TAXONOMY_NAME = "Localização";
	
	@Test
	public void handleBeforeCreateTestFailTerms() throws Exception {
		Taxonomy stationTaxonomy = taxonomyRepository.findByTypeAndName(Taxonomy.STATION_TAXONOMY, TAXONOMY_NAME);
		Taxonomy globalTaxonomy = taxonomyRepository.findByTypeAndName(Taxonomy.GLOBAL_TAXONOMY, TAXONOMY_NAME);
		
		StationPerspective stationPerspective = new StationPerspective();
		stationPerspective.station = stationRepository.findByName(STATION_NAME).get(0);
		stationPerspective.taxonomy = globalTaxonomy;
		stationPerspective.name = STATION_PERSPECTIVE_NAME;
		stationPerspective.perspectives = loadTermsPerspective(stationTaxonomy, globalTaxonomy, stationPerspective);
		
		try{
			handler.handleBeforeCreate(stationPerspective);
			stationPerspectiveRepository.save(stationPerspective);
		}catch(Exception e){}
		
		Assert.isNull(stationPerspective.id);
	}
	
	@Test
	public void handleBeforeCreateTestPass() throws Exception {
		StationPerspective stationPerspective = new StationPerspective();
		stationPerspective.station = stationRepository.findByName(STATION_NAME).get(0);
		stationPerspective.taxonomy = taxonomyRepository.findByTypeAndName(Taxonomy.GLOBAL_TAXONOMY, TAXONOMY_NAME);
		stationPerspective.name = STATION_PERSPECTIVE_NAME;
		
		handler.handleBeforeCreate(stationPerspective);
		stationPerspectiveRepository.save(stationPerspective);
		
		Assert.notNull(stationPerspective.id);
	}
	
	@Test
	public void handleBeforeCreateTestFail() throws Exception {
		StationPerspective stationPerspective = new StationPerspective();
		stationPerspective.station = stationRepository.findByName(STATION_NAME).get(0);;
		stationPerspective.taxonomy = taxonomyRepository.findByTypeAndName(Taxonomy.GLOBAL_TAXONOMY, TAXONOMY_NAME);
		
		try{
			handler.handleBeforeCreate(stationPerspective);
			stationPerspectiveRepository.save(stationPerspective);
		}catch(Exception e){
			
		}
		Assert.isNull(stationPerspective.id);
	}
	
	@Before
	public void loadDatabase(){
		if(!settedUp){
			stationPerspectiveRepository = context.getBean(StationPerspectiveRepository.class); 
			taxonomyRepository = context.getBean(TaxonomyRepository.class);
			stationRepository = context.getBean(StationRepository.class);
			termRepository = context.getBean(TermRepository.class);
			
			//-------------------- Station --------------------//
			Network network = new Network();
			network.name = "XARX";
			context.getBean(NetworkRepository.class).save(network);
			
			Station station = new Station();
			station.name = STATION_NAME;
			HashSet<Network> networks = new HashSet<Network>(1);
			networks.add(network);
			station.networks = networks;
			stationRepository.save(station);
			//-------------------- Station --------------------//
			
			//-------------------- Taxonomy --------------------// 
			Taxonomy taxonomy = new Taxonomy();
			taxonomy.name = TAXONOMY_NAME;
			taxonomy.type = Taxonomy.GLOBAL_TAXONOMY;
			taxonomyRepository.save(taxonomy);

			Taxonomy taxonomy2 = new Taxonomy();
			taxonomy2.name = TAXONOMY_NAME;
			taxonomy2.type = Taxonomy.STATION_TAXONOMY;
			taxonomy2.owningStation = station;
			taxonomyRepository.save(taxonomy2);
			
			Term rootTerm2 = new Term();
			rootTerm2.parent = null;
			rootTerm2.name = "Brasil";
			rootTerm2.taxonomy = taxonomy2;
			termRepository.save(rootTerm2);
			//-------------------- Term --------------------//
			Term rootTerm = new Term();
			rootTerm.parent = null;
			rootTerm.name = "Mundo";
			rootTerm.taxonomy = taxonomy;
			termRepository.save(rootTerm);
			
			List<Term> terms = new ArrayList<Term>(5);
			Term termRegion1 = new Term();
			termRegion1.name = "Nordeste";
			termRegion1.parent = rootTerm;
			terms.add(termRegion1);
			
			Term termRegion2 = new Term();
			termRegion2.name = "Sudeste";
			termRegion2.parent = rootTerm;
			terms.add(termRegion2);
			
			Term termRegion3 = new Term();
			termRegion3.name = "Centro-Oeste";
			termRegion3.parent = rootTerm;
			terms.add(termRegion3);
			
			Term termRegion4= new Term();
			termRegion4.name = "Sul";
			termRegion4.parent = rootTerm;
			terms.add(termRegion4);
			
			Term termRegion5 = new Term();
			termRegion5.name = "Norte";
			termRegion5.parent = rootTerm;
			terms.add(termRegion5);
			context.getBean(TermRepository.class).save(terms); 
			//-------------------- Term --------------------// 
			//-------------------- Taxonomy --------------------//
			
			settedUp = true;
		}
	}
	
	private Set<TermPerspective> loadTermsPerspective(Taxonomy stationTaxonomy, Taxonomy globalTaxonomy, StationPerspective stationPerspective){
		Set<TermPerspective> termsPerspectives = new HashSet<TermPerspective>();
		for (Term term : termRepository.findRoots(globalTaxonomy.id)) {
			termsPerspectives.add(convertTermToTermPerspective(term, stationPerspective));
		}
		termsPerspectives.add(convertTermToTermPerspective(termRepository.findRoots(stationTaxonomy.id).get(0), stationPerspective));
		return termsPerspectives;
	}
	
	private TermPerspective convertTermToTermPerspective(Term term, StationPerspective stationPerspective){
		TermPerspective termPerspective = new TermPerspective();
		termPerspective.term = term;
		termPerspective.perspective = stationPerspective;
		return termPerspective;
	}
}