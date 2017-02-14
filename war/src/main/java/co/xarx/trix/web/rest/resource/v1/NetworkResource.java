package co.xarx.trix.web.rest.resource.v1;

import co.xarx.trix.annotation.IgnoreMultitenancy;
import co.xarx.trix.api.*;
import co.xarx.trix.config.multitenancy.TenantContextHolder;
import co.xarx.trix.domain.*;
import co.xarx.trix.eventhandler.PostEventHandler;
import co.xarx.trix.exception.BadRequestException;
import co.xarx.trix.exception.ConflictException;
import co.xarx.trix.persistence.*;
import co.xarx.trix.services.NetworkService;
import co.xarx.trix.services.analytics.StatisticsService;
import co.xarx.trix.services.security.AuthService;
import co.xarx.trix.services.security.Authenticator;
import co.xarx.trix.services.security.PersonPermissionService;
import co.xarx.trix.services.security.StationPermissionService;
import co.xarx.trix.util.StringUtil;
import co.xarx.trix.web.rest.AbstractResource;
import co.xarx.trix.web.rest.api.v1.NetworkApi;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NoArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.FieldError;

import javax.validation.ConstraintViolation;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.io.IOException;
import java.util.*;

import static org.springframework.security.acls.domain.BasePermission.READ;

@Component
@NoArgsConstructor
public class NetworkResource extends AbstractResource implements NetworkApi {

	@Autowired
	public ObjectMapper objectMapper;
	@Autowired
	private StationRepository stationRepository;
	@Autowired
	private AuthService authProvider;
	@Autowired
	private Authenticator authenticatorService;
	@Autowired
	private NetworkRepository networkRepository;
	@Autowired
	private TaxonomyRepository taxonomyRepository;
	@Autowired
	private PersonRepository personRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private TermRepository termRepository;
	@Autowired
	private PostEventHandler postEventHandler;
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
	@Autowired
	private NetworkService networkService;
	@Autowired
	private StationPermissionService stationPermissionService;
	@Autowired
	private PersonPermissionService personPermissionService;
	@Autowired
	private AuthCredentialRepository authCredentialRepository;

	@Override
	public void getNetworks() throws IOException {
		forward();
	}

	@Override
	public void putNetwork(Integer id) throws IOException {
		forward();
	}

	@Override
	public PersonPermissions getNetworkPersonPermissions(Integer id) {
		PersonPermissions personPermissions = new PersonPermissions();
		Person person = authProvider.getLoggedPerson();


		//Stations Permissions
		List<Station> stations = personPermissionService.getStationsWithPermission(READ);
		List<StationPermission> stationPermissionDtos = stationPermissionService.getStationPermissions(stations);

		personPermissions.stationPermissions = stationPermissionDtos;
		personPermissions.personId = person.id;
		personPermissions.username = person.username;
		personPermissions.personName = person.name;

		return personPermissions;
	}

	@Override
	public Response updateTheme(ThemeView themeView){
		Network network = networkRepository.findByTenantId(TenantContextHolder.getCurrentTenantId());
		network = networkRepository.findOne(network.id);

		if(themeView.primaryColors == null || themeView.primaryColors.size() < 14 ||
			themeView.secondaryColors == null || themeView.primaryColors.size() < 14 ||
			themeView.alertColors == null || themeView.primaryColors.size() < 14 ||
			themeView.backgroundColors == null || themeView.primaryColors.size() < 14)
				throw new BadRequestException("Invalid Theme");

		network.navbarColor = themeView.primaryColors != null ? themeView.primaryColors.get("500") : "#30307E";
		network.mainColor = themeView.secondaryColors != null ? themeView.secondaryColors.get("300") : " #BC26C5 ";
		network.backgroundColor = themeView.backgroundColors != null ? themeView.backgroundColors.get("500") :
				"#EFEFEF";

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
	@Transactional
	public Response createNetwork (NetworkCreateDto networkCreate)  throws ConflictException, BadRequestException, IOException {
		try {
			Network network = new Network();
			network.name = networkCreate.name;
			network.setTenantId(networkCreate.newSubdomain);

			TenantContextHolder.setCurrentTenantId(networkCreate.newSubdomain);

			//Station Default Taxonomy

			try {
				network.networkCreationToken = UUID.randomUUID().toString();

				networkRepository.save(network);

				AuthCredential authCredential = new AuthCredential();
				authCredential.network = network;
				authCredentialRepository.save(authCredential);

			} catch (javax.validation.ConstraintViolationException e) {

				List<FieldError> errors = new ArrayList<>();
				for (ConstraintViolation violation : e.getConstraintViolations()) {
					FieldError error = new FieldError(violation.getRootBean().getClass().getName()+"", violation.getPropertyPath()+"", violation.getMessage());
					errors.add(error);
				}

				e.printStackTrace();

				return Response.status(Status.BAD_REQUEST).entity("{\"errors\": " + objectMapper.writeValueAsString(errors) +"}").build();
			} catch (org.springframework.dao.DataIntegrityViolationException e){

				if (e.getCause() != null && e.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
					org.hibernate.exception.ConstraintViolationException ex = (ConstraintViolationException) e.getCause();

					return Response.status(Status.BAD_REQUEST).entity("{\"error\": {" + "\"message\": \"" + ex.getCause().getMessage() + "\"}" + "}").build();
				}

				e.printStackTrace();

				throw e;
			}

			// Create Person ------------------------------

			Person person = networkCreate.person;
			person.setTenantId(network.getTenantId());
			User user;

			try {
				user = new User();
				user.setTenantId(network.getTenantId());
				user.enabled = true;
				user.username = person.username;
				user.password = person.password;

				UserGrantedAuthority authority = new UserGrantedAuthority(user, "ROLE_USER");
				UserGrantedAuthority nauthority = new UserGrantedAuthority(user, "ROLE_ADMIN");

				authority.setTenantId(network.getTenantId());
				nauthority.setTenantId(network.getTenantId());

				user.addAuthority(authority);
				user.addAuthority(nauthority);

				userRepository.save(user);

				person.user = user;
				person.networkAdmin = true;

				personRepository.save(person);
				authenticatorService.passwordAuthentication(user, user.password);
			} catch (javax.validation.ConstraintViolationException e) {

				List<FieldError> errors = new ArrayList<FieldError>();
				for (ConstraintViolation violation : e.getConstraintViolations()) {
					FieldError error = new FieldError(violation.getRootBean().getClass().getName()+"", violation.getPropertyPath()+"", violation.getMessage());
					errors.add(error);
				}

				e.printStackTrace();

				networkRepository.delete(network);

				return Response.status(Status.BAD_REQUEST).entity("{\"errors\": " + objectMapper.writeValueAsString(errors) +"}").build();
			} catch (org.springframework.dao.DataIntegrityViolationException e){
				networkRepository.delete(network);

				if (e.getCause() != null && e.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
					org.hibernate.exception.ConstraintViolationException ex = (ConstraintViolationException) e.getCause();

					return Response.status(Status.BAD_REQUEST).entity("{\"error\": {" + "\"mensage\": \"" + ex.getCause().getMessage() + "\"}" + "}").build();
				}

				e.printStackTrace();

				throw e;
			}catch (Exception e){
				networkRepository.delete(network);
				e.printStackTrace();
			}

			// End Create Person ------------------------------

//			Set<Taxonomy> nTaxonomies = new HashSet<Taxonomy>();
//			nTaxonomies.add(nTaxonomy);
//			taxonomyRepository.save(nTaxonomy);
//			network.ownedTaxonomies = nTaxonomies;
//			network.categoriesTaxonomyId = nTaxonomy.id;
//			networkRepository.save(network);

			Station station = new Station();
			station.setTenantId(network.getTenantId());
			station.name = network.name;
			station.main = true;
//			station.networks = new HashSet<Network>();
//			station.networks.add(network);
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

			station.stationSlug = StringUtil.toSlug(station.name);
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

			station.defaultPerspectiveId = new ArrayList<>(station.stationPerspectives).get(0).id;
			stationRepository.save(station);

			try {
				TermPerspective tp = new TermPerspective();
				tp.setTenantId(network.getTenantId());
				tp.term = null;
				tp.perspective = stationPerspective;
				tp.stationId = station.id;

				tp.rows = new ArrayList<>();

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
			postEventHandler.handleBeforeSave(post);

			networkService.addTenant(network.getTenantId()	, network.id);

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
	public Response networkStats(String date, String beginning) throws JsonProcessingException {
		return Response
				.status(Status.OK)
				.entity(objectMapper.writeValueAsString(statisticsService.getNetworkStats(date, beginning)))
				.build();
	}

	@Override
	/**
	 * Get the default invitation html template taking in to account the invitationMessage set by the admin at
	 * configuration screen.
	 */
	public Response getNetworkInvitationTemplate(){
		String template;
		try {
			template = networkService.getNetworkInvitationTemplate();
			StringResponse stringResponse = new StringResponse();
			stringResponse.response = template;
			return Response.status(Status.OK).entity(objectMapper.writeValueAsString(stringResponse)).build();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return Response.status(Status.NOT_IMPLEMENTED).build();
		} catch (IOException e) {
			e.printStackTrace();
			return Response.status(Status.SERVICE_UNAVAILABLE).build();
		}
	}

	@Override
	/**
	 * Get the default validation html template taking in to account the validationMessage set by the admin at
	 * configuration screen.
	 */
	public Response getNetworkValidationTemplate(){
		String template;
		try {
			template = networkService.getNetworkValidationTemplate();
			StringResponse stringResponse = new StringResponse();
			stringResponse.response = template;
			return Response.status(Status.OK).entity(objectMapper.writeValueAsString(stringResponse)).build();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return Response.status(Status.NOT_IMPLEMENTED).build();
		} catch (IOException e) {
			e.printStackTrace();
			return Response.status(Status.SERVICE_UNAVAILABLE).build();
		}
	}
}