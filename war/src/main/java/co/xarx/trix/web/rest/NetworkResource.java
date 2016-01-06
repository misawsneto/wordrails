package co.xarx.trix.web.rest;

import co.xarx.trix.WordrailsService;
import co.xarx.trix.api.NetworkPermission;
import co.xarx.trix.api.PersonPermissions;
import co.xarx.trix.api.StationPermission;
import co.xarx.trix.aspect.annotations.IgnoreMultitenancy;
import co.xarx.trix.domain.*;
import co.xarx.trix.dto.NetworkCreateDto;
import co.xarx.trix.eventhandler.PostEventHandler;
import co.xarx.trix.exception.BadRequestException;
import co.xarx.trix.exception.ConflictException;
import co.xarx.trix.persistence.*;
import co.xarx.trix.security.auth.TrixAuthenticationProvider;
import co.xarx.trix.util.ReadsCommentsRecommendsCount;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.exception.ConstraintViolationException;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.io.IOException;
import java.util.*;

@Path("/networks")
@Component
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class NetworkResource {

	@Autowired
	public ObjectMapper objectMapper;
	@Autowired
	private NetworkRolesRepository networkRolesRepository;
	@Autowired
	private StationRepository stationRepository;
	@Autowired
	private StationRolesRepository stationRolesRepository;
	@Autowired
	private TrixAuthenticationProvider authProvider;
	@Autowired
	private NetworkRepository networkRepository;
	@Autowired
	private TaxonomyRepository taxonomyRepository;
	@Autowired
	private PersonRepository personRepository;
	@Autowired
	private TermRepository termRepository;
	@Autowired
	private WordrailsService wordrailsService;
	@Autowired
	private PostRepository postRepository;
	@Autowired
	private PostEventHandler postEventHandler;
	@Autowired
	private PostReadRepository postReadRepository;
	@Autowired
	private RecommendRepository recommendRepository;
	@Autowired
	private CommentRepository commentRepository;
	@Autowired
	private QueryPersistence queryPersistence;
	@Autowired
	private StationPerspectiveRepository stationPerspectiveRepository;
	@Autowired
	private TermPerspectiveRepository termPerspectiveRepository;
	@Autowired
	private RowRepository rowRepository;

	@Path("/{id}/permissions")
	@GET
	public PersonPermissions getNetworkPersonPermissions(@PathParam("id") Integer id){
		PersonPermissions personPermissions = new PersonPermissions();
		Person person = authProvider.getLoggedPerson();

		NetworkRole networkRole = networkRolesRepository.findByNetworkIdAndPersonId(id, person.id);
		if(networkRole != null){
			//Network Permissions
			NetworkPermission networkPermissionDto = new NetworkPermission();
			networkPermissionDto.networkId = networkRole.id;
			networkPermissionDto.admin = networkRole.admin;

			//Stations Permissions
			List<Station> stations = stationRepository.findByPersonId(person.id);
			List<StationPermission> stationPermissionDtos = new ArrayList<StationPermission>(stations.size());
			for (Station station : stations) {
				StationPermission stationPermissionDto = new StationPermission();

				//Station Fields
				stationPermissionDto.stationId = station.id;
				stationPermissionDto.stationName = station.name;
				stationPermissionDto.writable = station.writable;
				stationPermissionDto.main = station.main;
				stationPermissionDto.visibility = station.visibility;
				stationPermissionDto.defaultPerspectiveId = station.defaultPerspectiveId;

				stationPermissionDto.subheading = station.subheading;
				stationPermissionDto.sponsored = station.sponsored;
				stationPermissionDto.topper = station.topper;

				stationPermissionDto.allowComments = station.allowComments;
				stationPermissionDto.allowSocialShare = station.allowSocialShare;

				//StationRoles Fields
				StationRole stationRole = stationRolesRepository.findByStationAndPerson(station, person);
				if(stationRole != null){
					stationPermissionDto.admin = stationRole.admin;
					stationPermissionDto.editor = stationRole.editor;
					stationPermissionDto.writer = stationRole.writer;
				}

				stationPermissionDtos.add(stationPermissionDto);
			}
			personPermissions.networkPermission = networkPermissionDto;
			personPermissions.stationPermissions = stationPermissionDtos;
			personPermissions.personId = person.id;
			personPermissions.username = person.username;
			personPermissions.personName = person.name;

		}
		return personPermissions;
	}

	@POST
	@IgnoreMultitenancy
	@Path("/createNetwork")
	public Response createNetwork (NetworkCreateDto networkCreate)  throws ConflictException, BadRequestException, IOException {
		try {
			Network network = new Network();
			network.name = networkCreate.name;
			network.setTenantId(networkCreate.subdomain);

			//Station Default Taxonomy
			Taxonomy nTaxonomy = new Taxonomy();

			nTaxonomy.name = "Categoria da Rede " + network.name;
			nTaxonomy.type = Taxonomy.NETWORK_TAXONOMY;
			taxonomyRepository.save(nTaxonomy);

			try {
				network.networkCreationToken = UUID.randomUUID().toString();
				networkRepository.save(network);
			} catch (javax.validation.ConstraintViolationException e) {

				List<FieldError> errors = new ArrayList<>();
				for (ConstraintViolation violation : e.getConstraintViolations()) {
					FieldError error = new FieldError(violation.getRootBean().getClass().getName()+"", violation.getPropertyPath()+"", violation.getMessage());
					errors.add(error);
				}

				taxonomyRepository.delete(nTaxonomy);
				return Response.status(Status.BAD_REQUEST).entity("{\"errors\": " + objectMapper.writeValueAsString(errors) +"}").build();
			} catch (org.springframework.dao.DataIntegrityViolationException e){
				taxonomyRepository.delete(nTaxonomy);

				if (e.getCause() != null && e.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
					org.hibernate.exception.ConstraintViolationException ex = (ConstraintViolationException) e.getCause();

					return Response.status(Status.BAD_REQUEST).entity("{\"error\": {" + "\"message\": \"" + ex.getCause().getMessage() + "\"}" + "}").build();
				}

				throw e;
			}

			// Create Person ------------------------------

			Person person = networkCreate.person;
			User user = null;

			try {
				user = new User();
				user.enabled = true;
				user.username = person.username;
				user.password = person.password;

				UserGrantedAuthority authority = new UserGrantedAuthority(user, "ROLE_USER");
				UserGrantedAuthority nauthority = new UserGrantedAuthority(user, "ROLE_NETWORK_ADMIN");

				user.addAuthority(authority);
				user.addAuthority(nauthority);

				person.user = user;

				personRepository.save(person);
			} catch (javax.validation.ConstraintViolationException e) {

				List<FieldError> errors = new ArrayList<FieldError>();
				for (ConstraintViolation violation : e.getConstraintViolations()) {
					FieldError error = new FieldError(violation.getRootBean().getClass().getName()+"", violation.getPropertyPath()+"", violation.getMessage());
					errors.add(error);
				}

				taxonomyRepository.delete(nTaxonomy);
				networkRepository.delete(network);

				return Response.status(Status.BAD_REQUEST).entity("{\"errors\": " + objectMapper.writeValueAsString(errors) +"}").build();
			} catch (org.springframework.dao.DataIntegrityViolationException e){
				taxonomyRepository.delete(nTaxonomy);
				networkRepository.delete(network);

				if (e.getCause() != null && e.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
					org.hibernate.exception.ConstraintViolationException ex = (ConstraintViolationException) e.getCause();

					return Response.status(Status.BAD_REQUEST).entity("{\"error\": {" + "\"mensage\": \"" + ex.getCause().getMessage() + "\"}" + "}").build();
				}

				throw e;
			}catch (Exception e){
				taxonomyRepository.delete(nTaxonomy);
				networkRepository.delete(network);
				e.printStackTrace();
			}

			NetworkRole networkRole = new NetworkRole();
			networkRole.network = network;
			networkRole.person = person;
			networkRole.admin = true;
			networkRolesRepository.save(networkRole);

			// End Create Person ------------------------------

			nTaxonomy.owningNetwork = network;
			taxonomyRepository.save(nTaxonomy);

			Term nterm1 = new Term();
			nterm1.name = "Categoria 1";

			Term nterm2 = new Term();
			nterm2.name = "Categoria 2";

			nterm1.taxonomy = nTaxonomy;
			nterm2.taxonomy = nTaxonomy;

			nTaxonomy.terms = new HashSet<Term>();
			nTaxonomy.terms.add(nterm1);
			nTaxonomy.terms.add(nterm2);
			termRepository.save(nterm1);
			termRepository.save(nterm2);
			Set<Taxonomy> nTaxonomies = new HashSet<Taxonomy>();
			nTaxonomies.add(nTaxonomy);
			taxonomyRepository.save(nTaxonomy);
			network.ownedTaxonomies = nTaxonomies;
			network.categoriesTaxonomyId = nTaxonomy.id;
			networkRepository.save(network);

			Station station = new Station();
			station.name = network.name;
			station.main = true;
//			station.networks = new HashSet<Network>();
//			station.networks.add(network);
			station.network  = network;
			station.visibility = Station.UNRESTRICTED;
			station.writable = false;

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

			stationRepository.save(station);

			taxonomies = station.ownedTaxonomies;
			Term defaultPostTerm = null;
			Term term1 = null;
			Term term2 = null;
			for (Taxonomy tax: taxonomies){
				if(tax.type.equals(Taxonomy.STATION_TAG_TAXONOMY)){
					if(station.tagsTaxonomyId == null)
						station.tagsTaxonomyId = tax.id;
				}
				if(tax.type.equals(Taxonomy.STATION_TAXONOMY)){
					if(station.categoriesTaxonomyId == null) {
						station.categoriesTaxonomyId = tax.id;
						// ---- create sample terms...
						term1 = new Term();
						term1.name = "Categoria 1";

						defaultPostTerm = term1;

						term2 = new Term();
						term2.name = "Categoria 2";

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

			StationRole role = new StationRole();
			role.person = person;
			role.station = station;
			role.writer = true;
			role.admin = true;
			role.editor = true;
			stationRolesRepository.save(role);
			station.defaultPerspectiveId = new ArrayList<StationPerspective>(station.stationPerspectives).get(0).id;
			stationRepository.save(station);

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
				row2.index = 1;
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

			Post post = new Post();

			post.title = "Bem Vindo a TRIX";
			post.body = "<p>Trix é uma plataforma para a criação e gestão de redes de informação e pensada primeiramente para dispositivos móveis. Através do editor é possível criar conteúdos baseados em textos, imagens, áudios e vídeos.</p><p>Adicione usuários com permissão de leitura, escrita, edição ou administração e através das funções de administração personalize a sua rede.</p>";
			post.author = person;
			post.terms = new HashSet<>();
			post.terms.add(defaultPostTerm);
			post.station = station;
			postEventHandler.savePost(post);

			return Response.status(Status.CREATED).entity("{\"token\": \"" + network.networkCreationToken + "\"}").build();
		}catch (Exception e){
			e.printStackTrace();
		}
		return Response.status(Status.INTERNAL_SERVER_ERROR).build();
	}

	@GET
	@Path("/publicationsCount")
	public Response publicationsCount(@Context HttpServletRequest request)throws IOException {
		List<Integer> ids = new ArrayList<>();

		for (Station station: stationRepository.findAll()){
			ids.add(station.id);
		}
		List<Object[]> counts =  queryPersistence.getStationsPublicationsCount(ids);
		return Response.status(Status.OK).entity("{\"publicationsCounts\": " + (counts.size() > 0 ? objectMapper.writeValueAsString(counts.get(0)) : null) + "}").build();
	}

	@GET
	@Path("/stats")
	public Response networkStats(@Context HttpServletRequest request, @QueryParam("date") String date, @QueryParam("beggining") String beginning, @QueryParam("networkId") Integer postId) throws IOException {
		if (date == null)
			throw new BadRequestException("Invalid date. Expected yyyy-MM-dd");

		org.joda.time.format.DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");

		TreeMap<Long, ReadsCommentsRecommendsCount> stats = new TreeMap<>();
		DateTime firstDay = formatter.parseDateTime(date);
		DateTime beginningDate;

		int dateDiference = 30;

		if (beginning != null && !beginning.isEmpty()){
			beginningDate = formatter.parseDateTime(date);
			dateDiference = Days.daysBetween(beginningDate.toLocalDate(), firstDay.toLocalDate()).getDays();
		}

		// create date slots
		DateTime lastestDay = firstDay;
		while (firstDay.minusDays(dateDiference).getMillis() < lastestDay.getMillis()){
			stats.put(lastestDay.getMillis(), new ReadsCommentsRecommendsCount());
			lastestDay = lastestDay.minusDays(1);
		}

		List<Object[]> postReadCounts;
		List<Object[]> recommendsCounts;
		List<Object[]> commentsCounts;
		List<Object[]> generalStatus;

		if (postId != null && postId > 0) {
			postReadCounts = postReadRepository.countByPostAndDate(postId, firstDay.minusDays(dateDiference).toDate(), firstDay.toDate());
			recommendsCounts = recommendRepository.countByPostAndDate(postId, firstDay.minusDays(dateDiference).toDate(), firstDay.toDate());
			commentsCounts = commentRepository.countByPostAndDate(postId, firstDay.minusDays(dateDiference).toDate(), firstDay.toDate());
			generalStatus = postRepository.findPostStats(postId);
		}else {
			postReadCounts = postReadRepository.countByDate(firstDay.minusDays(dateDiference).toDate(), firstDay.toDate());
			recommendsCounts = recommendRepository.countByDate(firstDay.minusDays(dateDiference).toDate(), firstDay.toDate());
			commentsCounts = commentRepository.countByDate(firstDay.minusDays(dateDiference).toDate(), firstDay.toDate());
			generalStatus = networkRepository.findStats();
		}

		// check date and map counts
		Iterator it = stats.entrySet().iterator();
		while (it.hasNext()){
			Map.Entry<Long,ReadsCommentsRecommendsCount> pair = (Map.Entry<Long,ReadsCommentsRecommendsCount>)it.next();
			long key = (Long)pair.getKey();
			for(Object[] counts: postReadCounts){
				long dateLong = ((java.sql.Date) counts[0]).getTime();
				long count = (long) counts[1];
				if(new DateTime(key).withTimeAtStartOfDay().equals(new DateTime(dateLong).withTimeAtStartOfDay()))
					pair.getValue().readsCount = count;
			}
		}

		it = stats.entrySet().iterator();
		while (it.hasNext()){
			Map.Entry<Long,ReadsCommentsRecommendsCount> pair = (Map.Entry<Long,ReadsCommentsRecommendsCount>)it.next();
			long key = (Long)pair.getKey();
			for(Object[] counts: recommendsCounts){
				long dateLong = ((java.sql.Date) counts[0]).getTime();
				long count = (long) counts[1];
				if(new DateTime(key).withTimeAtStartOfDay().equals(new DateTime(dateLong).withTimeAtStartOfDay()))
					pair.getValue().recommendsCount = count;
			}
		}

		it = stats.entrySet().iterator();
		while (it.hasNext()){
			Map.Entry<Long,ReadsCommentsRecommendsCount> pair = (Map.Entry<Long,ReadsCommentsRecommendsCount>)it.next();
			long key = (Long)pair.getKey();
			for(Object[] counts: commentsCounts){
				long dateLong = ((java.sql.Date) counts[0]).getTime();
				long count = (long) counts[1];
				if(new DateTime(key).withTimeAtStartOfDay().equals(new DateTime(dateLong).withTimeAtStartOfDay()))
					pair.getValue().commentsCount = count;
			}
		}

		String generalStatsJson = objectMapper.writeValueAsString(generalStatus != null && generalStatus.size() > 0 ? generalStatus.get(0) : null);
		String dateStatsJson = objectMapper.writeValueAsString(stats);
		return Response.status(Status.OK).entity("{\"generalStatsJson\": " + generalStatsJson + ", \"dateStatsJson\": " + dateStatsJson + "}").build();
	}
}