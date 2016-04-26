package co.xarx.trix.eventhandler;

import co.xarx.trix.domain.*;
import co.xarx.trix.domain.page.AbstractSection;
import co.xarx.trix.domain.page.Page;
import co.xarx.trix.domain.page.QueryableListSection;
import co.xarx.trix.domain.page.Section;
import co.xarx.trix.domain.page.query.PageableQuery;
import co.xarx.trix.domain.page.query.statement.PostStatement;
import co.xarx.trix.exception.UnauthorizedException;
import co.xarx.trix.persistence.*;
import co.xarx.trix.services.ESStartupIndexerService;
import co.xarx.trix.services.security.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@RepositoryEventHandler(Station.class)
@Component
public class StationEventHandler {

	@Autowired
	StationRolesRepository personStationRolesRepository;
	@Autowired
	PostEventHandler postEventHandler;
	@Autowired
	PostRepository postRepository;
	@Autowired
	StationPerspectiveRepository stationPerspectiveRepository;
	@Autowired
	StationRepository stationRepository;
	@Autowired
	TaxonomyEventHandler taxonomyEventHandler;
	@Autowired
	TaxonomyRepository taxonomyRepository;
	@Autowired
	NotificationRepository notificationRepository;
	@Autowired
	PostReadRepository postReadRepository;
	@Autowired
	QueryPersistence queryPersistence;
	@Autowired
	TermRepository termRepository;
	@Autowired
	TermPerspectiveRepository termPerspectiveRepository;
	@Autowired
	RowRepository rowRepository;
	@Autowired
	StationPerspectiveEventHandler stationPerspectiveEventHandler;
	@Autowired
	private AuthService authProvider;
	@Autowired
	private ESStartupIndexerService elasticSearchService;
	@Autowired
	private ESStationRepository esStationRepository;
	@Autowired
	private PageRepository pageRepository;
	@Autowired
	private ObjectStatementRepository statementRepository;
	@Autowired
	private PageableQueryRepository pageableQueryRepository;
	@Autowired
	private SectionRepository sectionRepository;

	@HandleBeforeCreate
	public void handleBeforeCreate(Station station) throws UnauthorizedException {
		Person personLogged = authProvider.getLoggedPerson();
		if(personLogged.networkAdmin){
			if(station.stationPerspectives == null || station.stationPerspectives.size() == 0){
				Set<StationPerspective> perspectives = new HashSet<StationPerspective>(1);
				
				//Perspective Default
				StationPerspective stationPerspective = new StationPerspective();
				stationPerspective.station = station;
				stationPerspective.name = station.name + " (Default)";
				perspectives.add(stationPerspective);
				station.stationPerspectives = perspectives;
				
				Set<Taxonomy> taxonomies = new HashSet<Taxonomy>();

				//Station Default Taxonomy
				Taxonomy sTaxonomy = new Taxonomy();
				sTaxonomy.name = "Station: " + station.name;
				sTaxonomy.owningStation = station;
				sTaxonomy.type = Taxonomy.STATION_TAXONOMY;
				taxonomies.add(sTaxonomy);
				station.ownedTaxonomies = taxonomies;
				stationPerspective.taxonomy = sTaxonomy;
			}
		}else{
			throw new UnauthorizedException();
		}
	}

	@HandleAfterCreate
	public void handleAfterCreate(Station station){
		Term term1 = new Term();
		term1.name = "Categoria 1";

		Term term2 = new Term();
		term2.name = "Categoria 2";

		Set<Taxonomy> taxonomies = station.ownedTaxonomies;
		for (Taxonomy tax: taxonomies){
			if(tax.type.equals(Taxonomy.STATION_TAXONOMY)){
				if(station.categoriesTaxonomyId == null) {
					station.categoriesTaxonomyId = tax.id;
					// ---- create sample terms...
					term1.taxonomy = tax;
					term2.taxonomy = tax;

					tax.terms = new HashSet<Term>();
					tax.terms.add(term1);
					tax.terms.add(term2);
					termRepository.save(term1);
					termRepository.save(term2);
					taxonomyRepository.save(tax);
				}
			}
		}

		Person person = authProvider.getLoggedPerson();
		StationRole role = new StationRole();
		role.person = person;
		role.station = station;
		role.writer = true;
		role.admin = true;
		role.editor = true;
		personStationRolesRepository.save(role);

		StationPerspective stationPerspective = new ArrayList<>(station.stationPerspectives).get(0);
		try {
			TermPerspective tp = new TermPerspective();
			tp.term = null;
			tp.perspective = stationPerspective;
			tp.stationId = station.id;

			tp.rows = new ArrayList<Row>();

			Row row1 = new Row();
			row1.term = term1;
			row1.type = Row.ORDINARY_ROW;
			row1.index = 0;
			tp.rows.add(row1);

			Row row2 = new Row();
			row2.term = term2;
			row2.type = Row.ORDINARY_ROW;
			row2.index = 0;
			tp.rows.add(row2);

			stationPerspective = stationPerspectiveRepository.findOne(stationPerspective.id);

			termPerspectiveRepository.save(tp);
			row2.perspective = tp;
			row1.perspective = tp;
			rowRepository.save(row1);
			rowRepository.save(row2);
			stationPerspective.perspectives = new HashSet(Arrays.asList(tp));
			termPerspectiveRepository.save(tp);

			stationPerspectiveRepository.save(stationPerspective);
		}catch (Exception e){
			e.printStackTrace();
		}

		createDefaultPage(station);

		station.defaultPerspectiveId = stationPerspective.id;
		stationRepository.save(station);
		elasticSearchService.saveIndex(station, ESStation.class, esStationRepository);
	}

	private void createDefaultPage(Station station) {
		PostStatement postStatement = new PostStatement();
		postStatement.addStationId(station.getId());
		statementRepository.save(postStatement);

		PageableQuery query = new PageableQuery(postStatement);
		pageableQueryRepository.save(query);

		Page page = new Page();
		page.setTitle("Home");
		page.setStation(station);

		AbstractSection section = new QueryableListSection(10, query);
		section.setStyle(Section.Style.VERTICAL_LIST);
		section.setOrderPosition(0);
		section.setTitle("All Posts");
		section.setPage(page);

		page.addSection(section);
		pageRepository.save(page);
	}

	@HandleBeforeSave
	public void handleBeforeSave(Station station){
		station.stationPerspectives = new HashSet<>(stationPerspectiveRepository.findByStationId(station.id));
	}

	@HandleBeforeDelete
	@Transactional
	public void handleBeforeDelete(Station station) throws UnauthorizedException{
		List<StationPerspective> stationsPerspectives = stationPerspectiveRepository.findByStationId(station.id);
		stationPerspectiveRepository.delete(stationsPerspectives);

		Taxonomy taxonomy = taxonomyRepository.findOne(station.categoriesTaxonomyId);

		if (taxonomy != null) {
			taxonomyEventHandler.handleBeforeDelete(taxonomy);
			taxonomyRepository.delete(taxonomy);
		}

		List<StationRole> stationsRoles = personStationRolesRepository.findByStation(station);
		if (stationsRoles != null && stationsRoles.size() > 0) {
			personStationRolesRepository.delete(stationsRoles);
		}

		List<Post> posts = postRepository.findByStation(station);

		if (posts != null && posts.size() > 0) {
			List<Integer> ids = posts.stream().map(post -> post.id).collect(Collectors.toList());
			queryPersistence.deleteAuthoritiesByStation(station.id);
			queryPersistence.deleteCellsInPosts(ids);
			queryPersistence.deleteCommentsInPosts(ids);
			queryPersistence.deleteNotificationsInPosts(ids);
			queryPersistence.deletePostReadsInPosts(ids);

			postRepository.forceDeleteAll(posts.stream().map(post -> post.id).collect(Collectors.toList()));
		}

		esStationRepository.delete(station.getId());
	}

	@HandleAfterSave
	@Transactional
	public void handleAfterSave(Station station){
		elasticSearchService.saveIndex(station, ESStation.class, esStationRepository);
	}
}