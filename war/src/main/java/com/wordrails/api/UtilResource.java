package com.wordrails.api;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
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
import com.wordrails.business.Person;
import com.wordrails.business.Post;
import com.wordrails.business.Station;
import com.wordrails.business.StationPerspective;
import com.wordrails.business.TermPerspective;
import com.wordrails.business.UnauthorizedException;
import com.wordrails.persistence.NetworkRepository;
import com.wordrails.persistence.NetworkRolesRepository;
import com.wordrails.persistence.PersonRepository;
import com.wordrails.persistence.PostRepository;
import com.wordrails.persistence.StationPerspectiveRepository;
import com.wordrails.persistence.StationRepository;
import com.wordrails.persistence.StationRolesRepository;
import com.wordrails.persistence.TaxonomyRepository;
import com.wordrails.persistence.TermPerspectiveRepository;

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
	@Path("/setDefaultStationPerspective")
	public void setDefaultStationPerspective(@Context HttpServletRequest request) {
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
	@Path("/setPostImageIds")
	public void setPostImageIds(@Context HttpServletRequest request) {
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
			}
			postRepository.save(posts);
		}
	}
	
	@GET
	@Path("/setStationIds")
	public void setStationIds(@Context HttpServletRequest request) {
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
	@Path("/updatePersonImage")
	public void updatePersonImage(@Context HttpServletRequest request) {
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
			}
			
			personRepository.save(persons);
		}
	}
}