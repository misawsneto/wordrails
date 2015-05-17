package com.wordrails.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.hibernate.search.MassIndexer;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.jboss.resteasy.spi.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wordrails.WordrailsService;
import com.wordrails.business.AccessControllerUtil;
import com.wordrails.business.Invitation;
import com.wordrails.business.Network;
import com.wordrails.business.NotImplementedException;
import com.wordrails.business.Person;
import com.wordrails.business.PersonNetworkRegId;
import com.wordrails.business.Post;
import com.wordrails.business.PostRead;
import com.wordrails.business.Station;
import com.wordrails.business.StationPerspective;
import com.wordrails.business.TermPerspective;
import com.wordrails.business.UnauthorizedException;
import com.wordrails.persistence.InvitationRepository;
import com.wordrails.persistence.NetworkRepository;
import com.wordrails.persistence.NetworkRolesRepository;
import com.wordrails.persistence.PersonNetworkRegIdRepository;
import com.wordrails.persistence.PersonRepository;
import com.wordrails.persistence.PostReadRepository;
import com.wordrails.persistence.PostRepository;
import com.wordrails.persistence.QueryPersistence;
import com.wordrails.persistence.StationPerspectiveRepository;
import com.wordrails.persistence.StationRepository;
import com.wordrails.persistence.StationRolesRepository;
import com.wordrails.persistence.TaxonomyRepository;
import com.wordrails.persistence.TermPerspectiveRepository;
import com.wordrails.util.WordrailsUtil;

@Path("/util")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Component
public class UtilResource {
	private @Context HttpServletRequest httpServletRequest;
	private @Context HttpRequest httpRequest;

	private @Autowired UserDetailsManager userDetailsManager;
	private @Autowired PersonRepository personRepository;

	private @Autowired NetworkRolesRepository networkRolesRepository;
	private @Autowired StationRepository stationRepository;
	private @Autowired StationRolesRepository stationRolesRepository;
	private @Autowired AccessControllerUtil accessControllerUtil;
	private @Autowired NetworkRepository networkRepository;
	private @Autowired WordrailsService wordrailsService;
	private @Autowired TaxonomyRepository taxonomyRepository;
	private @Autowired PostRepository postRepository;
	
	private @Autowired TermPerspectiveRepository termPerspectiveRepository;
	private @Autowired StationPerspectiveRepository stationPerspectiveRepository;
	
	private @Autowired InvitationRepository invitationRepository;
	
	public @Autowired @Qualifier("objectMapper") ObjectMapper mapper;

	private @PersistenceContext EntityManager manager;

	/**
	 * Method to manually update the Full Text Index. This is not required if inserting entities
	 * using this Manager as they will automatically be indexed. Useful though if you need to index
	 * data inserted using a different method (e.g. pre-existing data, or test data inserted via
	 * scripts or DbUnit).
	 */
	@GET
	@Path("/updateIndex")
	public Response updateFullTextIndex(@Context HttpServletRequest request) throws Exception {

		String host = request.getHeader("Host");

		if(host.contains("0:0:0:0:0:0:0") || host.contains("0.0.0.0") || host.contains("localhost") || host.contains("127.0.0.1")){

			FullTextEntityManager ftem = Search.getFullTextEntityManager(manager);
			ftem.createIndexer().startAndWait();
			ftem.flushToIndexes();
			return Response.status(Status.OK).entity("Updating...").build();
		}

		throw new UnauthorizedException();
	}

	/**
	 * Regenerates all the indexed class indexes
	 *
	 * @param async true if the reindexing will be done as a background thread
	 * @param sess the hibernate session
	 */
	@GET
	@Path("/reindex")
	public Response reindexAll(@Context HttpServletRequest request) {

		String host = request.getHeader("Host");

		if(host.contains("0:0:0:0:0:0:0") || host.contains("0.0.0.0") || host.contains("localhost") || host.contains("127.0.0.1")){
			FullTextEntityManager ftem = Search.getFullTextEntityManager(manager);
			MassIndexer massIndexer = ftem.createIndexer();
			massIndexer.purgeAllOnStart(true);
			//		massIndexer.start;
			try {
				massIndexer.startAndWait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//		ftem.flushToIndexes();
			return Response.status(Status.OK).entity("Reindexing...").build();
		}
		throw new UnauthorizedException();
	}

	@GET
	@Path("/updateDefaultStationPerspective")
	public void updateDefaultStationPerspective(@Context HttpServletRequest request) {
		String host = request.getHeader("Host");

		if(host.contains("0:0:0:0:0:0:0") || host.contains("0.0.0.0") || host.contains("localhost") || host.contains("127.0.0.1")){
			List<Station> stations = stationRepository.findAll();
			for (Station station : stations) {
				Set	<StationPerspective> perspectives = station.stationPerspectives != null ? station.stationPerspectives : new HashSet<StationPerspective>();
				if(perspectives.size() > 0){
					station.defaultPerspectiveId = ((StationPerspective)perspectives.toArray()[0]).id;
				}
			}
			stationRepository.save(stations);
		}
	}
	
	@GET
	@Path("/updatePostFields")
	public void updatePostFields(@Context HttpServletRequest request) {
		String host = request.getHeader("Host");

		if(host.contains("0:0:0:0:0:0:0") || host.contains("0.0.0.0") || host.contains("localhost") || host.contains("127.0.0.1")){
			List<Post> posts = postRepository.findAll();
			for (Post post : posts) {
				if(post.featuredImage != null && post.featuredImage.original != null){
					post.imageId = post.featuredImage.original.id;
					post.imageSmallId = post.featuredImage.small.id;
					post.imageMediumId = post.featuredImage.medium.id;
					post.imageLargeId= post.featuredImage.large.id;
				}else{
					post.imageId = null;
					post.imageSmallId = null;
					post.imageMediumId = null;
					post.imageLargeId = null;
				}
				if(post.comments != null){
					post.commentsCount = post.comments.size();
				}
			}
			postRepository.save(posts);
		}
	}
	
	@GET
	@Path("/updateTermPerspectivesStationIds")
	public void updateTermPerspectivesStationIds(@Context HttpServletRequest request) {
		String host = request.getHeader("Host");

		if(host.contains("0:0:0:0:0:0:0") || host.contains("0.0.0.0") || host.contains("localhost") || host.contains("127.0.0.1")){
			List<TermPerspective> termPerspectives = termPerspectiveRepository.findAll();
			for (TermPerspective termPerspective : termPerspectives) {
				termPerspective.stationId = termPerspective.perspective.station.id;
			}
			termPerspectiveRepository.save(termPerspectives);
			
			List<StationPerspective> stationPerspectives = stationPerspectiveRepository.findAll();
			for (StationPerspective stationPerspective : stationPerspectives) {
				stationPerspective.stationId = stationPerspective.station.id;
			}
			stationPerspectiveRepository.save(stationPerspectives);
		}
	}
	
	@GET
	@Path("/updatePersonFields")
	public void updatePersonFields(@Context HttpServletRequest request) {
		String host = request.getHeader("Host");

		if(host.contains("0:0:0:0:0:0:0") || host.contains("0.0.0.0") || host.contains("localhost") || host.contains("127.0.0.1")){
			List<Person> persons = personRepository.findAll();
			for (Person person : persons) {
				if(person.image != null && person.image.original != null){
					person.imageId = person.image.original.id;
					person.imageSmallId = person.image.small.id;
					person.imageMediumId = person.image.medium.id;
					person.imageLargeId = person.image.large.id;
				}else{
					person.imageId = null;
					person.imageSmallId = null;
					person.imageMediumId = null;
					person.imageLargeId = null;
				}
				
				if(person.cover != null && person.cover.original != null){
					person.coverMediumId = person.cover.medium.id;
					person.coverLargeId = person.cover.large.id;
				}
			}
			
			personRepository.save(persons);
		}
	}
	
	@GET
	@Path("/recalculateSlug")
	public void recalculateSlug(@Context HttpServletRequest request){
		String host = request.getHeader("Host");

		if(host.contains("0:0:0:0:0:0:0") || host.contains("0.0.0.0") || host.contains("localhost") || host.contains("127.0.0.1")){
			List<Post> posts = postRepository.findAll();
			for (Post post : posts) {
				doSlug(post);
			}
		}
	}
	
	@POST
	@Path("/generateInvitations")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void generate(@FormParam("subdomain") String subdomain, @FormParam("stationId") Integer stationId, @FormParam("count") Integer count){
		
		Network network = networkRepository.findOneBySubdomain(subdomain);
		Station station = stationId != null ? stationRepository.findOne(stationId) : null;
		
		List<Invitation> invites = new ArrayList<Invitation>();
		
		for (int i = 0; i < count; i++) {
			Invitation invitation =  new Invitation();
			invitation.network = network;
			invitation.station = station;
			invitation.active = true;
			invitation.hash = WordrailsUtil.generateRandomString(8, "aA#");
			invites.add(invitation);
		}
		
		invitationRepository.save(invites);
	}
	
	private void doSlug(Post post){
		String originalSlug = WordrailsUtil.toSlug(post.title);
		post.originalSlug = originalSlug;
		try {
			post.slug = originalSlug;
			postRepository.save(post);
		} catch (org.springframework.dao.DataIntegrityViolationException ex) {
			String hash = WordrailsUtil.generateRandomString(5, "!Aau");
			post.slug = originalSlug + "-" + hash;
		}
	}
	
	@GET
	@Path("/updateAllResources")
	public void updateAllResources(@Context HttpServletRequest request){
		String host = request.getHeader("Host");

		if(host.contains("0:0:0:0:0:0:0") || host.contains("0.0.0.0") || host.contains("localhost") || host.contains("127.0.0.1")){
			reindexAll(request);
			updateDefaultStationPerspective(request);
			updatePostFields(request);
			updateTermPerspectivesStationIds(request);
			updatePersonFields(request);
			recalculateSlug(request);
			updateRegDate(request);
		}
	}
	
	@Autowired private PersonNetworkRegIdRepository personNetworkRegIdRepository; 
	
	@GET
	@Path("/updateRegDate")
	private void updateRegDate(HttpServletRequest request) {
		String host = request.getHeader("Host");

		if(host.contains("0:0:0:0:0:0:0") || host.contains("0.0.0.0") || host.contains("localhost") || host.contains("127.0.0.1")){
			List<PersonNetworkRegId> ids = personNetworkRegIdRepository.findAll();
			for (PersonNetworkRegId personNetworkRegId : ids) {
				personNetworkRegId.createdAt = new Date();
				personNetworkRegId.updatedAt = new Date();
			}
			personNetworkRegIdRepository.save(ids);
		}
	}

	@Autowired private QueryPersistence qp;
	@Autowired private PersonNetworkRegIdRepository reg;
	@Autowired private PostReadRepository postReadRepository;
	
	@GET
	@Path("/test")
	public void test(@Context HttpServletRequest request){
		String host = request.getHeader("Host");
		if(host.contains("0:0:0:0:0:0:0") || host.contains("0.0.0.0") || host.contains("localhost") || host.contains("127.0.0.1")){
			reg.findRegIdByStationId(2);
		}
	}
}
