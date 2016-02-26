package co.xarx.trix.web.rest;

import co.xarx.trix.WordrailsService;
import co.xarx.trix.api.*;
import co.xarx.trix.config.multitenancy.TenantContextHolder;
import co.xarx.trix.converter.PostConverter;
import co.xarx.trix.domain.*;
import co.xarx.trix.dto.PersonCreateDto;
import co.xarx.trix.eventhandler.PersonEventHandler;
import co.xarx.trix.eventhandler.StationRoleEventHandler;
import co.xarx.trix.exception.BadRequestException;
import co.xarx.trix.exception.ConflictException;
import co.xarx.trix.exception.UnauthorizedException;
import co.xarx.trix.persistence.*;
import co.xarx.trix.security.NetworkSecurityChecker;
import co.xarx.trix.security.StationSecurityChecker;
import co.xarx.trix.security.auth.TrixAuthenticationProvider;
import co.xarx.trix.services.AmazonCloudService;
import co.xarx.trix.services.MobileService;
import co.xarx.trix.util.Logger;
import co.xarx.trix.util.ReadsCommentsRecommendsCount;
import co.xarx.trix.util.StringUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import org.jboss.resteasy.spi.HttpRequest;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.FieldError;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Path("/persons")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Component
public class PersonsResource {

	@Context
	private HttpServletRequest httpServletRequest;
	@Context
	private HttpRequest httpRequest;
	@Autowired
	private PersonRepository personRepository;
	@Autowired
	private MobileService mobileService;
	@Autowired
	private NetworkRolesRepository networkRolesRepository;
	@Autowired
	private StationRepository stationRepository;
	@Autowired
	private StationRolesRepository stationRolesRepository;
	@Autowired
	private NetworkRepository networkRepository;
	@Autowired
	private WordrailsService wordrailsService;
	@Autowired
	private PostRepository postRepository;
	@Autowired
	private PostConverter postConverter;
	@Autowired
	private RecommendRepository recommendRepository;
	@Autowired
	private PostReadRepository postReadRepository;
	@Autowired
	private CommentRepository commentRepository;
	@Autowired
	private NetworkSecurityChecker networkSecurityChecker;
	@Autowired
	private StationSecurityChecker stationSecurityChecker;
	@Autowired
	private QueryPersistence queryPersistence;
	@Autowired
	private PersonEventHandler personEventHandler;
	@Autowired
	private MenuEntryRepository menuEntryRepository;

	@Autowired
	@Qualifier("objectMapper")
	public ObjectMapper mapper;
	@Autowired
	@Qualifier("simpleMapper")
	public ObjectMapper simpleMapper;
	@Autowired
	public StationRoleEventHandler stationRoleEventHandler;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private TrixAuthenticationProvider authProvider;
	@Autowired
	private AmazonCloudService amazonCloudService;

	@Context
	private HttpServletRequest request;
	@Context
	private UriInfo uriInfo;
	@Context
	private HttpServletResponse response;

	private void forward() throws ServletException, IOException {
		String path = request.getServletPath() + uriInfo.getPath();
		request.getServletContext().getRequestDispatcher(path).forward(request, response);
	}

//    @GET
//    @Path("/findByUsername")
//    @Transactional
//    public Person findByUsername(@PathParam("username") String username) throws ServletException, IOException {
//        Person person = authProvider.getLoggedPerson();
//
//        Network network = wordrailsService.getNetworkFromHost(request.getHeader("Host"));
//
//        if(person.id.equals(id) || networkSecurityChecker.isNetworkAdmin(network)) {
//            forward();
//            return Response.status(Status.OK).build();
//        }else
//            return Response.status(Status.UNAUTHORIZED).build();
//    }

	@GET
	@Path("/{id}")
	@Transactional
	public Response findPerson(@PathParam("id") Integer id) throws ServletException, IOException {
		Person person = authProvider.getLoggedPerson();

		if(person.id.equals(id) || networkSecurityChecker.isNetworkAdmin()) {
			forward();
			return Response.status(Status.OK).build();
		}else
			return Response.status(Status.UNAUTHORIZED).build();
	}

	@PUT
	@Path("/update")
	@Transactional
	public Response update(Person person){
		Person loggedPerson = authProvider.getLoggedPerson();

		Person loadedPerson = personRepository.findOne(person.id);

		if(person.id == null || !person.id.equals(loggedPerson.id))
			throw new UnauthorizedException();

		if(person.password != null && !person.password.isEmpty() && !person.password.equals(person.passwordConfirm))
			throw new BadRequestException("Password no equal");

		if((person.password != null && !person.password.isEmpty()) && person.password.length() < 5)
			throw new BadRequestException("Invalid Password");

		if(!StringUtil.isEmailAddr(person.email))
			throw new BadRequestException("Not email");

		if(person.username == null || person.username.isEmpty() || person.username.length() < 3 || !StringUtil.isFQDN(person.username))
			throw new BadRequestException("Invalid username");

		if(person.bio != null && !person.bio.isEmpty())
			loadedPerson.bio = person.bio;
		else
			loadedPerson.bio = null;

		loadedPerson.email = person.email;
		loadedPerson.name = person.name;

		User user = null;
		if(!person.username.equals(loggedPerson.username)){
			loadedPerson.user.username = person.username;
			loadedPerson.username = person.username;
			user = userRepository.findOne(loadedPerson.user.id);
			user.username = person.username;
			userRepository.save(user);
			personRepository.save(loadedPerson);
		}

		if((person.password != null && !person.password.isEmpty()) && !person.password.equals(loadedPerson.user.password)){
			loadedPerson.user.password = person.password;
			user = userRepository.findOne(loadedPerson.user.id);
			user.password = person.password;
			userRepository.save(user);
			personRepository.save(loadedPerson);
		}

		personRepository.save(loadedPerson);

		authProvider.updateLoggedPerson(loadedPerson);

		return Response.status(Status.OK).build();
	}

	@PUT
	@Path("/{id}")
	@Transactional
	public void updatePerson(@PathParam("id") Integer id) throws ServletException, IOException {
		Person person = authProvider.getLoggedPerson();

		if(person.id.equals(id) || networkSecurityChecker.isNetworkAdmin())
			forward();
		else
			throw new BadRequestException();
	}

	@Deprecated
	@PUT
	@Path("/me/regId")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response putRegId(@FormParam("regId") String regId, @FormParam("networkId") Integer networkId, @FormParam("lat") Double lat, @FormParam("lng") Double lng) {
		return updateMobile(regId, lat, lng, MobileDevice.Type.ANDROID);
	}

	@Deprecated
	@PUT
	@Path("/me/token")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response putToken(@Context HttpServletRequest request, @FormParam("token") String token, @FormParam("networkId") Integer networkId, @FormParam("lat") Double lat, @FormParam("lng") Double lng) {
		return updateMobile(token, lat, lng, MobileDevice.Type.APPLE);
	}

	public Response updateMobile(String token, Double lat, Double lng, MobileDevice.Type type) {
		Person person = authProvider.getLoggedPerson();
		Logger.info("Updating " + type.toString() + " device " + token + " for person " + person.id);
		mobileService.updateDevice(person, token, lat, lng, type);
		return Response.status(Response.Status.OK).build();
	}

	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response login(@Context HttpServletRequest request, @FormParam("username") String username, @FormParam("password") String password) {
		try{
			authProvider.passwordAuthentication(username, password);
			return Response.status(Status.OK).build();
		}catch(BadCredentialsException | UsernameNotFoundException e){
			return Response.status(Status.UNAUTHORIZED).build();
		}
	}

	@POST
	@Path("/tokenSignin")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response tokenSignin(@Context HttpServletRequest request, @FormParam("token") String token) {
		try{
			Network network = wordrailsService.getNetworkFromHost(request.getHeader("Host"));
			if(network.networkCreationToken == null || !network.networkCreationToken.equals(token))
				throw new BadRequestException("Invalid Token");

			List<NetworkRole> nr = personRepository.findNetworkAdmin();
			User user = nr.get(0).person.user;
			Set<GrantedAuthority> authorities = new HashSet<>();
			authorities.add(new SimpleGrantedAuthority("ROLE_NETWORK_ADMIN"));
			authProvider.passwordAuthentication(user, user.password);

			network.networkCreationToken = null;
			networkRepository.save(network);

			return Response.status(Status.OK).build();
		}catch(BadCredentialsException | UsernameNotFoundException e){
			return Response.status(Status.UNAUTHORIZED).build();
		}
	}

	@GET
	@Path("/{personId}/posts")
	public ContentResponse<List<PostView>> getPersonNetworkPosts(@Context HttpServletRequest request, @PathParam("personId") Integer personId, @QueryParam("networkId") Integer networkId,
																 @QueryParam("page") int page, @QueryParam("size") int size) throws ServletException, IOException {
		Pageable pageable = new PageRequest(page, size);

		String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();

		Person person = authProvider.getLoggedPerson();

		List<StationPermission> permissions = wordrailsService.getStationPermissions(baseUrl, person.id);

		List<Integer> stationIds = new ArrayList<>();
		if(permissions != null && permissions.size() > 0){
			for (StationPermission stationPermission : permissions) {
				stationIds.add(stationPermission.stationId);
			}
		}

		List<Post> posts = postRepository.findPostByPersonIdAndStations(personId, stationIds, pageable);

		ContentResponse<List<PostView>> response = new ContentResponse<List<PostView>>();
		response.content = postConverter.convertToViews(posts);
		return response;
	}

	@GET
	@Path("/getPostsByState")
	public ContentResponse<List<PostView>> getPersonNetworkPostsByState(@Context HttpServletRequest request, @QueryParam("personId") Integer personId, @QueryParam("state") String state,
																		@QueryParam("page") int page, @QueryParam("size") int size) throws ServletException, IOException {
		Pageable pageable = new PageRequest(page, size);

		String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();

		Person person;
		if(personId == null){
			person = authProvider.getLoggedPerson();
		}else{
			person = personRepository.findOne(personId);
		}

		List<StationPermission> permissions = wordrailsService.getStationPermissions(baseUrl, person.id);

		List<Integer> stationIds = new ArrayList<Integer>();
		if(permissions != null && permissions.size() > 0){
			for (StationPermission stationPermission : permissions) {
				stationIds.add(stationPermission.stationId);
			}
		}

		List<Post> posts = postRepository.findPostByPersonIdAndStationsAndState(person.id, state, stationIds, pageable);

		ContentResponse<List<PostView>> response = new ContentResponse<List<PostView>>();
		response.content = postConverter.convertToViews(posts);
		return response;
	}

	@GET
	@Path("/{personId}/recommends")
	public ContentResponse<List<PostView>> getPersonNetworkRecommendations(@Context HttpServletRequest request, @PathParam("personId") Integer personId, @QueryParam("networkId") Integer networkId,
																		   @QueryParam("page") int page, @QueryParam("size") int size) throws ServletException, IOException {
		Pageable pageable = new PageRequest(page, size);

		String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();

		Person person = authProvider.getLoggedPerson();

		List<StationPermission> permissions = wordrailsService.getStationPermissions(baseUrl, person.id);

		List<Integer> stationIds = new ArrayList<Integer>();
		if(permissions != null && permissions.size() > 0){
			for (StationPermission stationPermission : permissions) {
				stationIds.add(stationPermission.stationId);
			}
		}

		List<Post> posts = postRepository.findRecommendationsByPersonIdAndStations(personId, stationIds, pageable);

		ContentResponse<List<PostView>> response = new ContentResponse<List<PostView>>();
		response.content = postConverter.convertToViews(posts);
		return response;
	}

	@GET
	@Path("/logout")
	@Produces(MediaType.TEXT_PLAIN)
	public Response logout(){
		try{
			authProvider.logout();
			return Response.status(Status.OK).build();
		}catch(BadCredentialsException | UsernameNotFoundException e){
			return Response.status(Status.UNAUTHORIZED).build();
		}
	}

	@POST
	@Path("/testpost")
	@Produces(MediaType.TEXT_PLAIN)
	public Response testPost(){
		return Response.status(Status.OK).build();
	}

//	@PUT
//	@Path("/me/password")
//	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
//	public void putPassword(@FormParam("oldPassword") String oldPassword, @FormParam("newPassword") String newPassword) {
//
//		try{
//			org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//			String username = user.getUsername();
//			if(!username.equalsIgnoreCase("wordrails")) // don't allow users to change wordrails password
//				userDetailsManager.changePassword(oldPassword, newPassword);
//		}catch(Exception e){}
//	}

	@GET
	@Path("/me")
	public void getCurrentPerson() {
		Person person = authProvider.getLoggedPerson();
		String path = httpServletRequest.getServletPath() + "/persons/search/findByUsername?username=" + person.username;
		httpRequest.forward(path);
	}

	@POST
	@Path("/create")
	public Response create(PersonCreateDto personCreationObject, @Context HttpServletRequest request) throws ConflictException, BadRequestException, IOException{
		Person person = null;
		User user;
		Network network = wordrailsService.getNetworkFromHost(request.getHeader("Host"));
		if(personCreationObject != null){
			try{
				person = new Person();
				person.name = personCreationObject.name;
				person.username = personCreationObject.username;
				person.password = personCreationObject.password;
				person.email = personCreationObject.email;


				String password = person.password;

				if (password == null || password.trim().equals("")) {
					password = StringUtil.generateRandomString(8, "a#");
				}

				user = new User();
				user.enabled = true;
				user.username = person.username;
				user.password = password;
				UserGrantedAuthority authority = new UserGrantedAuthority(user, UserGrantedAuthority.USER);
				authority.user = user;
				user.addAuthority(authority);

				person.user = user;

				personRepository.save(person);
			}catch (javax.validation.ConstraintViolationException e){
				BadRequestException badRequest = new BadRequestException();

				for (ConstraintViolation violation : e.getConstraintViolations()) {
					FieldError error = new FieldError(violation.getInvalidValue()+"", violation.getInvalidValue()+"", violation.getMessage());
					badRequest.errors.add(error);
				}

				throw badRequest;
			}catch (org.springframework.dao.DataIntegrityViolationException e) {
				if(e.getCause() != null && e.getCause() instanceof org.hibernate.exception.ConstraintViolationException){
					String errorMsg = e.getCause().getCause().getLocalizedMessage();
					Pattern p = Pattern.compile("\'([^\']*)\'");
					Matcher m = p.matcher(errorMsg);
					String errorVal = "";
					while (m.find()) {
						errorVal = m.group(1);
						break;
					}

					if(errorVal.contains("-"))
						errorVal = errorVal.split("-")[0];

					Person conflictingPerson = null;
					if(person.email != null && person.email.trim().equals(errorVal))
						conflictingPerson = personRepository.findByEmail(person.email);

					if(conflictingPerson == null && person.username != null && person.username.trim().equals(errorVal)){
						conflictingPerson = personRepository.findOne(QPerson.person.user.username.eq(person.username));
					}

					if(conflictingPerson!=null && personCreationObject.stationRole !=null && personCreationObject.stationRole.station != null) {
						StationRole str = stationRolesRepository.findByStationIdAndPersonId(personCreationObject.stationRole.station.id, conflictingPerson.id);
						if(str != null) {
							Logger.debug("conflicting station name: " + str.station.name);
							return Response.status(Status.CONFLICT).entity("{\"value\": \"" + errorVal + "\", "
									+ "\"conflictingPerson\": " + mapper.writeValueAsString(conflictingPerson) + ", "
									+ "\"conflictingStationRole\": " + mapper.writeValueAsString(str) + "}").build();
						}
					}

					return Response.status(Status.CONFLICT).entity("{\"value\": \"" + errorVal + "\", \"conflictingPerson\": " + mapper.writeValueAsString(conflictingPerson) +"}").build();
				}
				e.printStackTrace();
				throw new ConflictException();
			}

			NetworkRole networkRole = new NetworkRole();
			networkRole.network = networkRepository.findOne(QNetwork.network.tenantId.eq(TenantContextHolder.getCurrentTenantId()));
			networkRole.person = person;
			networkRole.admin = false;
			networkRolesRepository.save(networkRole);

			if (networkRole.admin) {
				UserGrantedAuthority authority = new UserGrantedAuthority(user, UserGrantedAuthority.NETWORK_ADMIN);
				user.addAuthority(authority);
			}


			StationRole stRole = personCreationObject.stationRole;
			if(stRole !=null){
				if(stRole.station != null && stRole.station.id != null){
					stRole.station = stationRepository.findOne(stRole.station.id);
					stRole.person = person;
					stationRoleEventHandler.handleBeforeCreate(stRole);
					stationRolesRepository.save(stRole);

					if(stRole.admin) {
						UserGrantedAuthority authority = new UserGrantedAuthority(user, UserGrantedAuthority.STATION_ADMIN, stRole.station);
						user.addAuthority(authority);
					}
					if(stRole.editor) {
						UserGrantedAuthority authority = new UserGrantedAuthority(user, UserGrantedAuthority.STATION_EDITOR, stRole.station);
						user.addAuthority(authority);
					}
					if(stRole.writer) {
						UserGrantedAuthority authority = new UserGrantedAuthority(user, UserGrantedAuthority.STATION_WRITER, stRole.station);
						user.addAuthority(authority);
					}
				}else{
					throw new BadRequestException();
				}
			}

			if(network != null && network.addStationRolesOnSignup){
				List<Station> stations = stationRepository.findAll();
				for (Station station : stations) {
					StationRole sr = new StationRole();
					sr.person = person;
					sr.station = station;
					stationRolesRepository.save(sr);
				}
			}

			userRepository.save(user);

			return Response.status(Status.CREATED).entity(mapper.writeValueAsString(person)).build();
		}else{
			throw new BadRequestException();
		}
	}



	@Path("/stats/count")
	@GET
	public ContentResponse<Integer> countPersonsByNetwork(@QueryParam("q") String q){
		ContentResponse<Integer> resp = new ContentResponse<Integer>();
		resp.content = 0;
		if(q != null && !q.isEmpty()) {
			resp.content = personRepository.countPersonsByString(q).intValue();
		}else {
			resp.content = personRepository.countPersons().intValue();
		}
		return resp;
	}

	@PUT
	@Path("/deleteMany/network")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteMany (@Context HttpServletRequest request, List<Integer> personIds){
//		Network network = wordrailsService.getNetworkFromHost(request.getHeader("Host"));
		List<Person> persons = personRepository.findPersonsByIds(personIds);

		if(persons != null && persons.size() > 0) {
//			for (Person person : persons) {
//				if (!person.user.getTenantId().equals(network.getTenantId())) return Response.status(Status.UNAUTHORIZED).build();
//			}

			if (networkSecurityChecker.isNetworkAdmin()) {
				for (Person person : persons) {
					personEventHandler.handleBeforeDelete(person);
				}

				personRepository.delete(persons);
			} else {
				return Response.status(Status.UNAUTHORIZED).build();
			}
		}

		return Response.status(Status.OK).build();
	}

	@DELETE
	@Path("/{personId}")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response deletePersonFromNetwork (@Context HttpServletRequest request, @PathParam("personId") Integer personId) throws IOException {
		Network network = wordrailsService.getNetworkFromHost(request.getHeader("Host"));
		Person person = personRepository.findOne(personId);

		if(person != null && networkSecurityChecker.isNetworkAdmin() && person.user.getTenantId().equals(network.getTenantId())){
			personEventHandler.handleBeforeDelete(person);
			personRepository.delete(person.id);
			return Response.status(Status.OK).build();
		}else{
			return Response.status(Status.UNAUTHORIZED).build();
		}

	}

	@PUT
	@Path("/{personId}/disable")
	public Response disablePerson(@PathParam("personId") Integer personId){
		Person self = authProvider.getLoggedPerson();
		Person person = personRepository.findOne(personId);
		if(!self.id.equals(person.id)) {
			person.user.enabled = false;
			personRepository.save(person);
		}
		return Response.status(Status.CREATED).build();
	}

	@PUT
	@Path("/{personId}/enable")
	public Response enablePerson(@PathParam("personId") Integer personId){
		Person self = authProvider.getLoggedPerson();
		Person person = personRepository.findOne(personId);
		if(!self.id.equals(person.id)) {
			person.user.enabled = true;
			personRepository.save(person);
		}
		return Response.status(Status.CREATED).build();
	}

	@PUT
	@Path("/updateStationRoles")
	@Transactional
	public Response updateStationRoles(StationRolesUpdate stationRolesUpdate){
		Person self = authProvider.getLoggedPerson();
		if(stationRolesUpdate != null && stationRolesUpdate.personsIds != null && stationRolesUpdate.stationsIds != null){

			stationRolesUpdate.personsIds.remove(self.id);

			QStationRole q = QStationRole.stationRole;

			Iterable<StationRole> isr = stationRolesRepository.findAll(q.person.id.in(stationRolesUpdate.personsIds).and(q.station.id.in(stationRolesUpdate.stationsIds)));
			ArrayList<StationRole> roles = Lists.newArrayList(isr);

			for (StationRole sr: roles){
				sr.admin = stationRolesUpdate.admin;
				sr.editor = stationRolesUpdate.editor;
				sr.writer = stationRolesUpdate.writer;
			}

			stationRolesRepository.save(roles);

			List<Person> persons = personRepository.findAll(stationRolesUpdate.personsIds);
			List<Station> stations = stationRepository.findAll(stationRolesUpdate.stationsIds);

			ArrayList<StationRole> allRoles = new ArrayList<StationRole>();

			for (Station station : stations) {
				for (Person person: persons){

					boolean skip = false;
					for(StationRole sr: roles){
						if(sr.person != null && sr.station != null && sr.station.id.equals(station.id) && sr.person.id.equals(person.id)) {
							skip = true;
							continue;
						}
					}

					if(skip)
						continue;

					StationRole sRole = new StationRole();
					sRole.station = station;
					sRole.person = person;
					sRole.admin = stationRolesUpdate.admin;
					sRole.editor = stationRolesUpdate.editor;
					sRole.writer = stationRolesUpdate.writer;

					allRoles.add(sRole);
				}
			}

			stationRolesRepository.save(allRoles);
		}
		return Response.status(Status.CREATED).build();
	}

	@PUT
	@Path("/enable/all")
	public Response enablePerson(IdsList idsList){
		Person self = authProvider.getLoggedPerson();
		if(idsList != null && idsList.ids != null){
			List<Person> persons = personRepository.findAll(idsList.ids);
			for (Person person: persons) {
				if(self.id.equals(person.id))
					continue;
				person.user.enabled = true;
			}

			personRepository.save(persons);
		}
		return Response.status(Status.CREATED).build();
	}

	@PUT
	@Path("/disable/all")
	public Response disablePerson(IdsList idsList){
		Person self = authProvider.getLoggedPerson();
		if(idsList != null && idsList.ids != null){
			List<Person> persons = personRepository.findAll(idsList.ids);
			for (Person person: persons) {
				if(self.id.equals(person.id))
					continue;
				person.user.enabled = false;
			}

			personRepository.save(persons);
		}
		return Response.status(Status.CREATED).build();
	}

	@PUT
	@Path("/deletePersonStationRoles")
	@Consumes(MediaType.APPLICATION_JSON)
	@Transactional(readOnly = false)
	public Response deletePersonStationRoles(List<Integer> stationRolesIds) throws IOException{

		List<Station> stations = stationRepository.findByStationRolesIds(stationRolesIds);

		Set<Integer> stationIds = new HashSet<Integer>();

		for (Station station : stations) {
			stationIds.add(station.id);
		}

		if(stationSecurityChecker.isStationsAdmin(new ArrayList<Integer>(stationIds))){
			stationRolesRepository.deleteByIds(stationRolesIds);
			return Response.status(Status.OK).build();
		}else{
			return Response.status(Status.UNAUTHORIZED).build();
		}
	}

	@GET
	@Path("/allInit")
	public PersonData   getAllInitData (@Context HttpServletRequest request, @Context HttpServletResponse response, @QueryParam("setAttributes") Boolean setAttributes) throws IOException {

		Network network = wordrailsService.getNetworkFromHost(request.getHeader("Host"));
		Integer stationId = wordrailsService.getStationIdFromCookie(request);
		PersonData personData = getInitialData(request);

		StationDto defaultStation = wordrailsService.getDefaultStation(personData, stationId);

			if(defaultStation != null){
			Integer stationPerspectiveId = defaultStation.defaultPerspectiveId;

			TermPerspectiveView termPerspectiveView = wordrailsService.getDefaultPerspective(stationPerspectiveId, 10);

			Pageable pageable = new PageRequest(0, 10);
			//			Pageable pageable2 = new PageRequest(0, 100, new Sort(Direction.DESC, "id"));

			List<Post> popular = postRepository.findPopularPosts(defaultStation.id, pageable);
			List<Post> recent = postRepository.findPostsOrderByDateDesc(defaultStation.id, pageable);
			personData.popular = postConverter.convertToViews(popular);
			personData.recent = postConverter.convertToViews(recent);


			if(setAttributes != null && setAttributes){
				request.setAttribute("personData", simpleMapper.writeValueAsString(personData));
				request.setAttribute("termPerspectiveView", simpleMapper.writeValueAsString(termPerspectiveView));
				request.setAttribute("networkName", personData.network.name);
				request.setAttribute("networkId", personData.network.id);
				if(network.favicon != null)
					request.setAttribute("faviconLink", amazonCloudService.getPublicImageURL(network.getFaviconHash()));
				request.setAttribute("networkDesciption", "");
				request.setAttribute("networkKeywords", "");
			}
		}else {
			personData.noVisibleStation = true;
			request.setAttribute("personData", simpleMapper.writeValueAsString(personData));
			request.setAttribute("networkName", personData.network.name);
		}

		return personData;
	}

	@Value("${trix.amazon.cloudfront}")
	String cloudfrontUrl;

	@GET
	@Path("/init")
	public PersonData getInitialData (@Context HttpServletRequest request) throws IOException{
		String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();

		Person person = authProvider.getLoggedPerson();

		if(person == null){
			throw new UnauthorizedException("User is not authorized");
		}

		Network network = wordrailsService.getNetworkFromHost(request.getHeader("Host"));

		PersonPermissions personPermissions = new PersonPermissions();
		NetworkRole networkRole = networkRolesRepository.findByNetworkIdAndPersonId(network.id, person.id);

		//Network Permissions
		NetworkPermission networkPermissionDto = new NetworkPermission();
		if(networkRole != null)
			networkPermissionDto.networkId = network.id;
		else
			networkPermissionDto.admin = false;

		List<StationDto> stationDtos = new ArrayList<>();
		List<Station> stations = stationRepository.findByPersonId(person.id);
		for(Station station : stations) {
			StationDto stationDto = mapper.readValue(mapper.writeValueAsString(station).getBytes("UTF-8"), StationDto.class);
			stationDto.links = wordrailsService.generateSelfLinks(baseUrl + "/api/stations/" + station.id);
			stationDtos.add(stationDto);
		}

		personPermissions.networkPermission = networkPermissionDto;
		personPermissions.stationPermissions = getStationPermissions(stations, person.id);
		personPermissions.personId = person.id;
		personPermissions.username = person.username;
		personPermissions.personName = person.name;

		PersonData initData = new PersonData();

		if(person.user != null && (person.password == null || person.password.equals(""))){
			initData.noPassword = true;
		}

		initData.publicCloudfrontUrl = cloudfrontUrl;
		initData.privateCloudfrontUrl = cloudfrontUrl;

		initData.person = mapper.readValue(mapper.writeValueAsString(person).getBytes("UTF-8"), PersonDto.class);
		initData.network = mapper.readValue(mapper.writeValueAsString(network).getBytes("UTF-8"), NetworkDto.class);

		initData.networkRole = mapper.readValue(mapper.writeValueAsString(networkRole).getBytes("UTF-8"), NetworkRoleDto.class);
		initData.stations = stationDtos;
		initData.personPermissions = personPermissions;

		List<MenuEntry> entries = menuEntryRepository.findAll();
		List<MenuEntryDto> menuEntries = new ArrayList<>();
		if (entries != null) {
			for (MenuEntry menuEntry : entries) {
				MenuEntryDto sectionDto = mapper.readValue(mapper.writeValueAsString(menuEntry).getBytes("UTF-8"), MenuEntryDto.class);
				sectionDto.links = wordrailsService.generateSelfLinks(baseUrl + "/api/menuEntries/" + sectionDto.id);
				menuEntries.add(sectionDto);

			}
		}
		initData.menuEntries = menuEntries;
		initData.sections = menuEntries;

		initData.person.links = wordrailsService.generateSelfLinks(baseUrl + "/api/persons/" + person.id);
		initData.network.links = wordrailsService.generateSelfLinks(baseUrl + "/api/network/" + network.id);
		if(initData.networkRole != null)
			initData.networkRole.links = networkRole != null ? wordrailsService.generateSelfLinks(baseUrl + "/api/networkRoles/" + networkRole.id) : Arrays.asList(new Link());

		Pageable pageable2 = new PageRequest(0, 100, new Sort(Direction.DESC, "id"));
		if(initData.person != null && !initData.person.username.equals("wordrails")){
			List<Integer> postsRead = postRepository.findPostReadByPerson(initData.person.id, pageable2);
			List<Integer> bookmarks = new ArrayList(person.getBookmarkPosts());
			List<Integer> recommends = recommendRepository.findRecommendByPerson(initData.person.id, pageable2);
			initData.postsRead = postsRead;
			initData.bookmarks = bookmarks;
			initData.recommends = recommends;
		}

		return initData;
	}

	private List<StationPermission> getStationPermissions(List<Station> stations, Integer personId) {
		List<StationPermission> stationPermissionDtos = new ArrayList<>();
		for (Station station : stations) {
			StationPermission stationPermissionDto = new StationPermission();

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
			StationRole stationRole = stationRolesRepository.findByStationAndPersonId(station, personId);
			if (stationRole != null) {
				stationPermissionDto.admin = stationRole.admin;
				stationPermissionDto.editor = stationRole.editor;
				stationPermissionDto.writer = stationRole.writer;
			}

			stationPermissionDtos.add(stationPermissionDto);
		}

		return stationPermissionDtos;
	}


	@GET
	@Path("/me/bookmarkedRecommended")
	public ContentResponse<List<BooleanResponse>> checkBookmarkedRecommendedByMe(@QueryParam("postId") Integer postId){
		Person person = authProvider.getLoggedPerson();
		List<BooleanResponse> resp = new ArrayList<>();

		if(person.getBookmarkPosts().contains(postId)){
			BooleanResponse bool = new BooleanResponse();
			bool.response = true;
			resp.add(bool);
		}else{
			BooleanResponse bool = new BooleanResponse();
			bool.response = false;
			resp.add(bool);
		}

		if(recommendRepository.findRecommendByPersonIdAndPostId(person.id, postId)!=null){
			BooleanResponse bool = new BooleanResponse();
			bool.response = true;
			resp.add(bool);
		}else{
			BooleanResponse bool = new BooleanResponse();
			bool.response = false;
			resp.add(bool);
		}

		ContentResponse<List<BooleanResponse>> response = new ContentResponse<>();
		response.content = resp;
		return response;
	}

	@GET
	@Path("/me/publicationsCount")
	public Response publicationsCount(@QueryParam("personId") Integer personId)throws IOException {
		Person person = null;
		if(personId != null){
			person = personRepository.findOne(personId);
		}else{
			person = authProvider.getLoggedPerson();
		}

		List<Object[]> counts =  queryPersistence.getPersonPublicationsCount(person.id);
		return Response.status(Status.OK).entity("{\"publicationsCounts\": " + (counts.size() > 0 ? mapper.writeValueAsString(counts.get(0)) : null) + "}").build();
	}

	@GET
	@Path("/me/stats")
	public Response personStats(@QueryParam("date") String date, @QueryParam("postId") Integer postId) throws IOException{
		if(date == null)
			throw new BadRequestException("Invalid date. Expected yyyy-MM-dd");

		org.joda.time.format.DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");

		Person person = null;
		if(postId == null || postId == 0) {
			person = authProvider.getLoggedPerson();
		}

		TreeMap<Long, ReadsCommentsRecommendsCount> stats = new TreeMap<>();
		DateTime firstDay = formatter.parseDateTime(date);

		// create date slots
		DateTime lastestDay = firstDay;
		while (firstDay.minusDays(30).getMillis() < lastestDay.getMillis()){
			stats.put(lastestDay. getMillis(), new ReadsCommentsRecommendsCount());
			lastestDay = lastestDay.minusDays(1);
		}

		List<Object[]> postReadCounts;
		List<Object[]> recommendsCounts;
		List<Object[]> commentsCounts;
		List<Object[]> generalStatus;

		if(person == null) {
			postReadCounts = postReadRepository.countByPostAndDate(postId, firstDay.minusDays(30).toDate(), firstDay.toDate());
			recommendsCounts = recommendRepository.countByPostAndDate(postId, firstDay.minusDays(30).toDate(), firstDay.toDate());
			commentsCounts = commentRepository.countByPostAndDate(postId, firstDay.minusDays(30).toDate(), firstDay.toDate());
			generalStatus = postRepository.findPostStats(postId);
		}else {
			postReadCounts = postReadRepository.countByAuthorAndDate(person.id, firstDay.minusDays(30).toDate(), firstDay.toDate());
			recommendsCounts = recommendRepository.countByAuthorAndDate(person.id, firstDay.minusDays(30).toDate(), firstDay.toDate());
			commentsCounts = commentRepository.countByAuthorAndDate(person.id, firstDay.minusDays(30).toDate(), firstDay.toDate());
			generalStatus = personRepository.findPersonStats(person.id);
		}

		// check date and map counts
		Iterator it = stats.entrySet().iterator();
		checkDateAndMapCounts(postReadCounts, it);

		it = stats.entrySet().iterator();
		checkDateAndMapCounts(recommendsCounts, it);

		it = stats.entrySet().iterator();
		checkDateAndMapCounts(commentsCounts, it);

		String generalStatsJson = mapper.writeValueAsString(generalStatus != null && generalStatus.size() > 0 ? generalStatus.get(0) : null);
		String dateStatsJson = mapper.writeValueAsString(stats);
		return Response.status(Status.OK).entity("{\"generalStatsJson\": " + generalStatsJson + ", \"dateStatsJson\": " + dateStatsJson + "}").build();
	}

	public void checkDateAndMapCounts(List<Object[]> countList, Iterator it) {
		while (it.hasNext()){
			Map.Entry<Long,ReadsCommentsRecommendsCount> pair = (Map.Entry<Long,ReadsCommentsRecommendsCount>)it.next();
			long key = (Long)pair.getKey();
			for(Object[] counts: countList){
				long dateLong = ((java.sql.Date) counts[0]).getTime();
				long count = (long) counts[1];
				if(new DateTime(key).withTimeAtStartOfDay().equals(new DateTime(dateLong).withTimeAtStartOfDay()))
					pair.getValue().commentsCount = count;
			}
		}
	}
}