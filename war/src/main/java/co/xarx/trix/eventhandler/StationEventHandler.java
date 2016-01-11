package co.xarx.trix.eventhandler;

import java.util.*;

import co.xarx.trix.auth.TrixAuthenticationProvider;
import co.xarx.trix.domain.*;
import co.xarx.trix.exception.UnauthorizedException;
import co.xarx.trix.persistence.elasticsearch.PerspectiveEsRepository;
import co.xarx.trix.persistence.*;
import co.xarx.trix.security.StationSecurityChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeDelete;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import co.xarx.trix.services.CacheService;

@RepositoryEventHandler(Station.class)
@Component
public class StationEventHandler {
	@Autowired StationRolesRepository personStationRolesRepository;
	@Autowired PostEventHandler postEventHandler;
	@Autowired
	PostRepository postRepository;
	@Autowired StationPerspectiveRepository stationPerspectiveRepository;
	@Autowired
	StationRepository stationRepository;
	@Autowired
	StationSecurityChecker stationSecurityChecker;
	@Autowired TaxonomyEventHandler taxonomyEventHandler;
	@Autowired
	TaxonomyRepository taxonomyRepository;
	@Autowired NotificationRepository notificationRepository;
	@Autowired PostReadRepository postReadRepository;
	@Autowired private TrixAuthenticationProvider authProvider;
	@Autowired CacheService cacheService;
	@Autowired QueryPersistence queryPersistence;
	@Autowired TermRepository termRepository;
	@Autowired
	TermPerspectiveRepository termPerspectiveRepository;
	@Autowired
	RowRepository rowRepository;
	@Autowired
	PerspectiveEsRepository perspectiveEsRepository;
	@Autowired StationPerspectiveEventHandler stationPerspectiveEventHandler;

	@HandleBeforeCreate
	public void handleBeforeCreate(Station station) throws UnauthorizedException {
		if(stationSecurityChecker.canCreate(station)){
			if(station.stationPerspectives == null || station.stationPerspectives.size() == 0 
					&& station.network != null){
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

				//Tag Default Taxonomy
				Taxonomy tTaxonomy = new Taxonomy();
				tTaxonomy.name = "Tags " + station.name;
				tTaxonomy.owningStation = station;
				tTaxonomy.type = Taxonomy.STATION_TAG_TAXONOMY;
				taxonomies.add(tTaxonomy);
				station.ownedTaxonomies = taxonomies;
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
			if(tax.type.equals(Taxonomy.STATION_TAG_TAXONOMY)){
				if(station.tagsTaxonomyId == null)
					station.tagsTaxonomyId = tax.id;
			}
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

		StationPerspective stationPerspective = new ArrayList<StationPerspective>(station.stationPerspectives).get(0);
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

		station.defaultPerspectiveId = stationPerspective.id;
		stationRepository.save(station);

		for (TermPerspective tp: stationPerspective.perspectives)
			perspectiveEsRepository.save(tp);

	}

	@HandleBeforeSave
	public void handleBeforeSave(Station station){
		station.stationPerspectives = new HashSet<StationPerspective>(stationPerspectiveRepository.findByStationId(station.id));
	}

	@HandleBeforeDelete
	@Transactional
	public void handleBeforeDelete(Station station) throws UnauthorizedException{
		if(stationSecurityChecker.canEdit(station)){
			stationRepository.deleteStationNetwork(station.id);

			List<StationPerspective> stationsPerspectives = stationPerspectiveRepository.findByStationId(station.id);
			stationPerspectiveRepository.delete(stationsPerspectives);

			Taxonomy taxonomy = taxonomyRepository.findOne(station.categoriesTaxonomyId);
			taxonomy.owningStation = null;

			taxonomyRepository.save(taxonomy);

//			if(taxonomies != null && !taxonomies.isEmpty()){
//				for (Taxonomy taxonomy : taxonomies) {
//					taxonomyEventHandler.handleBeforeDelete(taxonomy);
//					taxonomyRepository.delete(taxonomy);
//				}
//			}

			List<StationRole> stationsRoles = personStationRolesRepository.findByStation(station);
			if(stationsRoles != null && stationsRoles.size() > 0){
				personStationRolesRepository.delete(stationsRoles);
			}



			List<Post> posts = postRepository.findByStation(station);

			if(posts != null && posts.size() > 0){
//				for (Post post : posts) {
//					postEventHandler.handleBeforeDelete(post);
//					postRepository.delete(posts);
//				}

				List<Integer> ids = new ArrayList<Integer>();
				for (Post post : posts) {
					ids.add(post.id);
				}
				queryPersistence.deleteBookmarksInPosts(ids);
				queryPersistence.deleteCellsInPosts(ids);
				queryPersistence.deleteCommentsInPosts(ids);
				queryPersistence.deleteImagesInPosts(ids);
				queryPersistence.deleteNotificationsInPosts(ids);
				queryPersistence.deletePostReadsInPosts(ids);
				queryPersistence.deleteRecommendsInPosts(ids);

				postRepository.delete(posts);
			}

			List<Notification> notifications = notificationRepository.findByStation(station);
			if(notifications != null && notifications.size() > 0)
				notificationRepository.delete(notifications);

			//for(StationPerspective p: stationsPerspectives)

		}else{
			throw new UnauthorizedException();
		}
	}

	@HandleAfterSave
	@Transactional
	public void handleAfterSave(Station station){
		cacheService.updateStation(station.id);
	}
}