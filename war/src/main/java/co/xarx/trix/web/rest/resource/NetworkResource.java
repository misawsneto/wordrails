package co.xarx.trix.web.rest.resource;

import co.xarx.trix.annotations.IgnoreMultitenancy;
import co.xarx.trix.api.PersonPermissions;
import co.xarx.trix.api.StationPermission;
import co.xarx.trix.api.ThemeView;
import co.xarx.trix.config.multitenancy.TenantContextHolder;
import co.xarx.trix.domain.*;
import co.xarx.trix.eventhandler.PostEventHandler;
import co.xarx.trix.exception.BadRequestException;
import co.xarx.trix.exception.ConflictException;
import co.xarx.trix.persistence.*;
import co.xarx.trix.services.analytics.StatisticsService;
import co.xarx.trix.services.security.AuthService;
import co.xarx.trix.util.StatsJson;
import co.xarx.trix.web.rest.AbstractResource;
import co.xarx.trix.web.rest.api.NetworkApi;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;

import javax.validation.ConstraintViolation;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.io.IOException;
import java.util.*;

@Component
public class NetworkResource extends AbstractResource implements NetworkApi {

	@Autowired
	public ObjectMapper objectMapper;
	@Autowired
	private StationRepository stationRepository;
	@Autowired
	private StationRolesRepository stationRolesRepository;
	@Autowired
	private AuthService authProvider;
	@Autowired
	private NetworkRepository networkRepository;
	@Autowired
	private TaxonomyRepository taxonomyRepository;
	@Autowired
	private PersonRepository personRepository;
	@Autowired
	private TermRepository termRepository;
	@Autowired
	private PostRepository postRepository;
	@Autowired
	private PostEventHandler postEventHandler;
	@Autowired
	private PostReadRepository postReadRepository;
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
	@Autowired
	private StatisticsService statisticsService;

	@Override
	public void putNetwork(Integer id) throws IOException {
		forward();
	}

	@Override
	public PersonPermissions getNetworkPersonPermissions(Integer id) {
		PersonPermissions personPermissions = new PersonPermissions();
		Person person = authProvider.getLoggedPerson();


		//Stations Permissions
		List<Station> stations = stationRepository.findByPersonId(person.id);
		List<StationPermission> stationPermissionDtos = new ArrayList<>(stations.size());
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
			if (stationRole != null) {
				stationPermissionDto.admin = stationRole.admin;
				stationPermissionDto.editor = stationRole.editor;
				stationPermissionDto.writer = stationRole.writer;
			}

			stationPermissionDtos.add(stationPermissionDto);
		}

		personPermissions.stationPermissions = stationPermissionDtos;
		personPermissions.personId = person.id;
		personPermissions.username = person.username;
		personPermissions.personName = person.name;

		return personPermissions;
	}

	@Override
	public Response updateTheme(ThemeView themeView){
		Network network = networkRepository.findByTenantId(TenantContextHolder.getCurrentTenantId());
		if(themeView.primaryColors == null || themeView.primaryColors.size() < 14 ||
			themeView.secondaryColors == null || themeView.primaryColors.size() < 14 ||
			themeView.alertColors == null || themeView.primaryColors.size() < 14 ||
			themeView.backgroundColors == null || themeView.primaryColors.size() < 14)
				throw new BadRequestException("Invalid Theme");

		network.primaryColors = themeView.primaryColors;
		network.secondaryColors = themeView.secondaryColors;
		network.alertColors = themeView.alertColors;
		network.backgroundColors = themeView.backgroundColors;
		network.backgroundColor = themeView.backgroundColors.get("500");

		networkRepository.save(network);
		return Response.status(Status.OK).build();
	}

	@Override
	@IgnoreMultitenancy
	public Response createNetwork (NetworkCreateDto networkCreate)  throws ConflictException, BadRequestException, IOException {
		try {
			Network network = new Network();
			network.name = networkCreate.name;
			network.setTenantId(networkCreate.newSubdomain);

			TenantContextHolder.setCurrentTenantId(networkCreate.newSubdomain);

			//Station Default Taxonomy
			Taxonomy nTaxonomy = new Taxonomy();
			nTaxonomy.setTenantId(network.getTenantId());

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
			person.setTenantId(network.getTenantId());
			User user = null;

			try {
				user = new User();
				user.setTenantId(network.getTenantId());
				user.enabled = true;
				user.username = person.username;
				user.password = person.password;

				UserGrantedAuthority authority = new UserGrantedAuthority(user, "ROLE_USER");
				UserGrantedAuthority nauthority = new UserGrantedAuthority(user, "ROLE_NETWORK_ADMIN");

				authority.setTenantId(network.getTenantId());
				nauthority.setTenantId(network.getTenantId());

				user.addAuthority(authority);
				user.addAuthority(nauthority);

				person.user = user;
				person.networkAdmin = true;

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

			// End Create Person ------------------------------

			nTaxonomy.owningNetwork = network;
			taxonomyRepository.save(nTaxonomy);

			Term nterm1 = new Term();
			nterm1.setTenantId(network.getTenantId());
			nterm1.name = "Categoria 1";

			Term nterm2 = new Term();
			nterm2.setTenantId(network.getTenantId());
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
			station.setTenantId(network.getTenantId());
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
			stationPerspective.setTenantId(network.getTenantId());
			stationPerspective.station = station;
			stationPerspective.name = station.name + " (Default)";
			perspectives.add(stationPerspective);
			station.stationPerspectives = perspectives;

			Set<Taxonomy> taxonomies = new HashSet<Taxonomy>();

			//Station Default Taxonomy
			Taxonomy sTaxonomy = new Taxonomy();
			sTaxonomy.setTenantId(network.getTenantId());
			sTaxonomy.name = "Station: " + station.name;
			sTaxonomy.owningStation = station;
			sTaxonomy.type = Taxonomy.STATION_TAXONOMY;
			taxonomies.add(sTaxonomy);
			station.ownedTaxonomies = taxonomies;
			stationPerspective.taxonomy = sTaxonomy;

			stationRepository.save(station);

			taxonomies = station.ownedTaxonomies;
			Term defaultPostTerm = null;
			Term term1 = null;
			Term term2 = null;
			for (Taxonomy tax: taxonomies){
				if(tax.type.equals(Taxonomy.STATION_TAXONOMY)){
					if(station.categoriesTaxonomyId == null) {
						station.categoriesTaxonomyId = tax.id;
						// ---- create sample terms...
						term1 = new Term();
						term1.setTenantId(network.getTenantId());
						term1.name = "Categoria 1";

						defaultPostTerm = term1;

						term2 = new Term();
						term2.setTenantId(network.getTenantId());
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
			role.setTenantId(network.getTenantId());
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
				tp.setTenantId(network.getTenantId());
				tp.term = null;
				tp.perspective = stationPerspective;
				tp.stationId = station.id;

				tp.rows = new ArrayList<Row>();

				Row row1 = new Row();
				row1.setTenantId(network.getTenantId());
				row1.term = term1;
				row1.type = Row.ORDINARY_ROW;
				row1.index = 0;
				tp.rows.add(row1);

				Row row2 = new Row();
				row2.setTenantId(network.getTenantId());
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
			post.setTenantId(network.getTenantId());

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

	@Override
	public Response publicationsCount()throws IOException {
		List<Integer> ids = new ArrayList<>();

		for (Station station: stationRepository.findAll()){
			ids.add(station.id);
		}
		List<Object[]> counts =  queryPersistence.getStationsPublicationsCount(ids);
		return Response.status(Status.OK).entity("{\"publicationsCounts\": " + (counts.size() > 0 ? objectMapper.writeValueAsString(counts.get(0)) : null) + "}").build();
	}

	@Override
	public StatsJson networkStats(String date, String beginning, Integer postId) throws JsonProcessingException {
		return statisticsService.networkStats(date, beginning);
	}
}