package co.xarx.trix.web.rest;

import co.xarx.trix.WordrailsService;
import co.xarx.trix.api.*;
import co.xarx.trix.auth.TrixAuthenticationProvider;
import co.xarx.trix.config.multitenancy.TenantContextHolder;
import co.xarx.trix.converter.PostConverter;
import co.xarx.trix.domain.*;
import co.xarx.trix.dto.PersonCreateDto;
import co.xarx.trix.eventhandler.PersonEventHandler;
import co.xarx.trix.eventhandler.StationRoleEventHandler;
import co.xarx.trix.exception.BadRequestException;
import co.xarx.trix.exception.ConflictException;
import co.xarx.trix.exception.UnauthorizedException;
import co.xarx.trix.mobile.notification.APNService;
import co.xarx.trix.mobile.notification.GCMService;
import co.xarx.trix.persistence.*;
import co.xarx.trix.security.NetworkSecurityChecker;
import co.xarx.trix.security.StationSecurityChecker;
import co.xarx.trix.util.ReadsCommentsRecommendsCount;
import co.xarx.trix.util.TrixUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
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
	private GCMService gcmService;
	@Autowired
	private APNService apnService;
	@Autowired
	private PostRepository postRepository;
	@Autowired
	private PostConverter postConverter;
	@Autowired
	private BookmarkRepository bookmarkRepository;
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
	@Qualifier("objectMapper")
	public ObjectMapper mapper;
	@Autowired
	@Qualifier("simpleMapper")
	public ObjectMapper simpleMapper;
	@Autowired
	public StationRoleEventHandler stationRoleEventHandler;
	@Autowired
	private SectionRepository sectionRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private TrixAuthenticationProvider authProvider;

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

	@PUT
	@Path("/{id}")
	@Transactional
	public Response findByUsername(@PathParam("id") Integer id) throws ServletException, IOException {
		Person person = authProvider.getLoggedPerson();

		Network network = wordrailsService.getNetworkFromHost(request.getHeader("Host"));

		if(person.id.equals(id) || networkSecurityChecker.isNetworkAdmin(network)) {
			forward();
			return Response.status(Status.OK).build();
		}else
			return Response.status(Status.UNAUTHORIZED).build();
	}

	@PUT
	@Path("/{id}")
	@Transactional
	public void updatePerson(@PathParam("id") Integer id) throws ServletException, IOException {
		Person person = authProvider.getLoggedPerson();

		Network network = wordrailsService.getNetworkFromHost(request.getHeader("Host"));

		if(person.id.equals(id) || networkSecurityChecker.isNetworkAdmin(network))
			forward();
		else
			throw new co.xarx.trix.exception.BadRequestException();
	}

	@PUT
	@Path("/me/regId")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response putRegId(@FormParam("regId") String regId, @FormParam("networkId") Integer networkId, @FormParam("lat") Double lat, @FormParam("lng") Double lng) {
		Network network = wordrailsService.getNetworkFromHost(request.getHeader("Host"));
		Person person = authProvider.getLoggedPerson();
		if(person.id == 0){
			gcmService.updateRegId(network, null, regId, lat, lng);
		} else {
			gcmService.updateRegId(network, person, regId, lat, lng);
		}
//		if(person.id == 0) person = null;
		System.out.println("regId: " + regId);
		return Response.status(Status.OK).build();
	}

	@PUT
	@Path("/me/token")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response putToken(@Context HttpServletRequest request, @FormParam("token") String token, @FormParam("networkId") Integer networkId, @FormParam("lat") Double lat, @FormParam("lng") Double lng) {
		Network network = wordrailsService.getNetworkFromHost(request.getHeader("Host"));
		Person person = authProvider.getLoggedPerson();
		if(person.id == 0){
			apnService.updateIosToken(network, null, token, lat, lng);
		} else {
			apnService.updateIosToken(network, person, token, lat, lng);
		}
//		if(person.id == 0) person = null;
		System.out.println("iOS token: " + token);
		return Response.status(Status.OK).build();
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
			authProvider.passwordAuthentication(user.username, user.password);

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

		List<StationPermission> permissions = wordrailsService.getStationPermissions(baseUrl, person.id, networkId);

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

		List<StationPermission> permissions = wordrailsService.getStationPermissions(baseUrl, person.id, TenantContextHolder.getCurrentTenantId());

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

		List<StationPermission> permissions = wordrailsService.getStationPermissions(baseUrl, person.id, networkId);

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
		if(personCreationObject != null){
			try{
				person = new Person();
				person.name = personCreationObject.name;
				person.username = personCreationObject.username;
				person.password = personCreationObject.password;
				person.email = personCreationObject.email;


				String password = person.password;

				if (password == null || password.trim().equals("")) {
					password = TrixUtil.generateRandomString(8, "a#");
				}

				user = new User();
				user.enabled = true;
				user.username = person.username;
				user.password = password;
				UserGrantedAuthority authority = new UserGrantedAuthority(user, UserGrantedAuthority.USER);
				authority.user = user;
				user.addAuthority(authority);

				person.user = user;

				if(person.email != null && !person.email.isEmpty()){
					Person personE = personRepository.findByEmail(person.email);
					if(personE != null){
						return Response.status(Status.CONFLICT).entity("{\"value\": \"" + person.email + "\"}").build();
					}
				}

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

					Person conflictingPerson = null;
					if(person.email != null && person.email.trim().equals(errorVal)){
						conflictingPerson = personRepository.findByEmail(person.email);
					}else if(person.username != null && person.username.trim().equals(errorVal)){
						conflictingPerson = personRepository.findOne(QPerson.person.user.username.eq(person.username));
					}

					if(conflictingPerson!=null && personCreationObject.stationRole !=null && personCreationObject.stationRole.station != null) {
//							conflictingPerson.id 
						StationRole str = stationRolesRepository.findByStationIdAndPersonId(personCreationObject.stationRole.station.id, conflictingPerson.id);
						if(str != null)
							return Response.status(Status.CONFLICT).entity("{\"value\": \"" + errorVal + "\", "
									+ "\"conflictingPerson\": " + mapper.writeValueAsString(conflictingPerson) + ", "
									+ "\"conflictingStationRole\": " + mapper.writeValueAsString(str) +"}").build();
					}

					return Response.status(Status.CONFLICT).entity("{\"value\": \"" + errorVal + "\", \"conflictingPerson\": " + mapper.writeValueAsString(conflictingPerson) +"}").build();
				}
				e.printStackTrace();
				throw new ConflictException();
			}

			NetworkRole networkRole = new NetworkRole();
			networkRole.network = networkRepository.findOne(TenantContextHolder.getCurrentTenantId());
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

			userRepository.save(user);

			return Response.status(Status.CREATED).entity(mapper.writeValueAsString(person)).build();
		}else{
			throw new BadRequestException();
		}
	}



	@Path("/count")
	@GET
	@Produces(MediaType.APPLICATION_FORM_URLENCODED)
	public Response countPersonsByNetwork(@QueryParam("networkId") Integer networkId){
		return Response.status(Status.OK).entity("{\"count\": " + personRepository.countPersons() + " }").build();
	}

	@PUT
	@Path("/deleteMany/network")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteMany (@Context HttpServletRequest request, List<Integer> personIds){
		Network network = wordrailsService.getNetworkFromHost(request.getHeader("Host"));
		List<Person> persons = personRepository.findPersonsByIds(personIds);

		if(persons != null && persons.size() > 0) {
			for (Person person : persons) {
				if (!person.user.networkId.equals(network.id)) return Response.status(Status.UNAUTHORIZED).build();
			}

			if (networkSecurityChecker.isNetworkAdmin(network)) {
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

		if(person != null && networkSecurityChecker.isNetworkAdmin(network) && person.user.networkId.equals(network.id)){
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
		Person person = personRepository.findOne(personId);
		person.user.enabled = false;
		personRepository.save(person);
		return Response.status(Status.CREATED).build();
	}

	@PUT
	@Path("/{personId}/enable")
	public Response enablePerson(@PathParam("personId") Integer personId){
		Person person = personRepository.findOne(personId);
		person.user.enabled = true;
		personRepository.save(person);
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
	public PersonData getAllInitData (@Context HttpServletRequest request, @Context HttpServletResponse response, @QueryParam("setAttributes") Boolean setAttributes) throws IOException {

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

	@Value("${amazon.publicCloudfrontUrl}")
	String publicCloudfrontUrl;

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
			networkPermissionDto.networkId = networkRole.id;
		else
			networkPermissionDto.admin = false;

		network.sections = sectionRepository.findByNetwork(network);

		List<StationDto> stationDtos = new ArrayList<>();
		List<Station> stations = stationRepository.findByPersonIdAndNetworkId(person.id, network.id);
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

		initData.publicCloudfrontUrl = publicCloudfrontUrl;
		initData.privateCloudfrontUrl = publicCloudfrontUrl;

		initData.person = mapper.readValue(mapper.writeValueAsString(person).getBytes("UTF-8"), PersonDto.class);
		initData.network = mapper.readValue(mapper.writeValueAsString(network).getBytes("UTF-8"), NetworkDto.class);

		List<SectionDto> sections = new ArrayList<>();
		for(Section section: network.sections){
			SectionDto sectionDto = mapper.readValue(mapper.writeValueAsString(section).getBytes("UTF-8"), SectionDto.class);
			sectionDto.links = wordrailsService.generateSelfLinks(baseUrl + "/api/sections/" + sectionDto.id);
			sections.add(sectionDto);
		}

		initData.sections = sections;

		initData.networkRole = mapper.readValue(mapper.writeValueAsString(networkRole).getBytes("UTF-8"), NetworkRoleDto.class);
		initData.stations = stationDtos;
		initData.personPermissions = personPermissions;

		initData.person.links = wordrailsService.generateSelfLinks(baseUrl + "/api/persons/" + person.id);
		initData.network.links = wordrailsService.generateSelfLinks(baseUrl + "/api/network/" + network.id);
		if(initData.networkRole != null)
			initData.networkRole.links = networkRole != null ? wordrailsService.generateSelfLinks(baseUrl + "/api/networkRoles/" + networkRole.id) : Arrays.asList(new Link());

		Pageable pageable2 = new PageRequest(0, 100, new Sort(Direction.DESC, "id"));
		if(initData.person != null && !initData.person.username.equals("wordrails")){
			List<Integer> postsRead = postRepository.findPostReadByPerson(initData.person.id, pageable2);
			List<Integer> bookmarks = bookmarkRepository.findBookmarkByPerson(initData.person.id, pageable2);
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

		if(bookmarkRepository.findBookmarkByPersonIdAndPostId(person.id, postId)!=null){
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

		String generalStatsJson = mapper.writeValueAsString(generalStatus != null && generalStatus.size() > 0 ? generalStatus.get(0) : null);
		String dateStatsJson = mapper.writeValueAsString(stats);
		return Response.status(Status.OK).entity("{\"generalStatsJson\": " + generalStatsJson + ", \"dateStatsJson\": " + dateStatsJson + "}").build();
	}
}