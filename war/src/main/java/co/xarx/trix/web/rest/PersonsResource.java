package co.xarx.trix.web.rest;

import co.xarx.trix.WordrailsService;
import co.xarx.trix.api.*;
import co.xarx.trix.converter.PostConverter;
import co.xarx.trix.domain.*;
import co.xarx.trix.dto.PersonCreateDto;
import co.xarx.trix.eventhandler.PersonEventHandler;
import co.xarx.trix.eventhandler.StationRoleEventHandler;
import co.xarx.trix.exception.BadRequestException;
import co.xarx.trix.exception.ConflictException;
import co.xarx.trix.exception.UnauthorizedException;
import co.xarx.trix.persistence.*;
import co.xarx.trix.security.StationSecurityChecker;
import co.xarx.trix.services.AmazonCloudService;
import co.xarx.trix.services.PersonService;
import co.xarx.trix.services.auth.AuthService;
import co.xarx.trix.services.EmailService;
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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import java.io.IOException;
import java.util.*;

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
	private StationRepository stationRepository;
	@Autowired
	private StationRolesRepository stationRolesRepository;
	@Autowired
	private NetworkRepository networkRepository;
	@Autowired
	private WordrailsService wordrailsService;
	@Autowired
	private PersonService personService;
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
	private StationSecurityChecker stationSecurityChecker;
	@Autowired
	private QueryPersistence queryPersistence;
	@Autowired
	private PersonEventHandler personEventHandler;
	@Autowired
	private EmailService emailService;

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
	private AuthService authProvider;
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

		if(person.id.equals(id) || person.networkAdmin) {
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

		if(person.id.equals(id) || person.networkAdmin)
			forward();
		else
			throw new BadRequestException();
	}

	@PUT
	@Path("/me/regId")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response putRegId(@FormParam("regId") String regId, @FormParam("networkId") Integer networkId, @FormParam("lat") Double lat, @FormParam("lng") Double lng) {
		Person person = authProvider.getLoggedPerson();
		Logger.info("Updating android device " + regId + " for person " + person.id);
		mobileService.updateDevice(person, regId, lat, lng, MobileDevice.Type.ANDROID);
		return Response.status(Status.OK).build();
	}

	@PUT
	@Path("/me/token")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response putToken(@Context HttpServletRequest request, @FormParam("token") String token, @FormParam("networkId") Integer networkId, @FormParam("lat") Double lat, @FormParam("lng") Double lng) {
		Person person = authProvider.getLoggedPerson();
		Logger.info("Updating apple device " + token + " for person " + person.id);
		mobileService.updateDevice(person, token, lat, lng, MobileDevice.Type.APPLE);
		return Response.status(Status.OK).build();
	}

//	@POST
//	@Path("/login")
//	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
//	public Response login(@Context HttpServletRequest request, @FormParam("username") String username, @FormParam("password") String password) {
//		try{
//			authProvider.passwordAuthentication(username, password);
//			return Response.status(Status.OK).build();
//		}catch(BadCredentialsException | UsernameNotFoundException e){
//			return Response.status(Status.UNAUTHORIZED).build();
//		}
//	}

	@POST
	@Path("/tokenSignin")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response tokenSignin(@Context HttpServletRequest request, @FormParam("token") String token) {
		try{
			Network network = wordrailsService.getNetworkFromHost(request.getHeader("Host"));
			if(network.networkCreationToken == null || !network.networkCreationToken.equals(token))
				throw new BadRequestException("Invalid Token");

			Person person = authProvider.getLoggedPerson();
			User user = person.user;
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
	public Response create(PersonCreateDto dto) throws ConflictException, BadRequestException, IOException{
		Person person = personService.create(dto.name, dto.username, dto.password, dto.email);

		if (person != null) {
			return Response.status(Status.CREATED).entity(mapper.writeValueAsString(person)).build();
		} else {
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
		Person person = authProvider.getLoggedPerson();
		List<Person> persons = personRepository.findPersonsByIds(personIds);

		if(persons != null && persons.size() > 0) {
//			for (Person person : persons) {
//				if (!person.user.getTenantId().equals(network.getTenantId())) return Response.status(Status.UNAUTHORIZED).build();
//			}

			if (person.networkAdmin) {
				for (Person p : persons) {
					personEventHandler.handleBeforeDelete(p);
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

		if(person != null && person.networkAdmin && person.user.getTenantId().equals(network.getTenantId())){
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

			ArrayList<StationRole> allRoles = new ArrayList<>();

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
					request.setAttribute("faviconLink", amazonCloudService.getPublicImageURL(network.favicon.get(Image.SIZE_MEDIUM)));
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

	@Value("${amazon.cloudfrontUrl}")
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

		//Network Permissions
		NetworkPermission networkPermissionDto = new NetworkPermission();

		networkPermissionDto.admin = person.networkAdmin;

		network.sections = sectionRepository.findByNetwork(network);

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

		List<SectionDto> sections = new ArrayList<>();
		for(Section section: network.sections){
			SectionDto sectionDto = mapper.readValue(mapper.writeValueAsString(section).getBytes("UTF-8"), SectionDto.class);
			sectionDto.links = wordrailsService.generateSelfLinks(baseUrl + "/api/sections/" + sectionDto.id);
			sections.add(sectionDto);
		}

		initData.sections = sections;

		initData.stations = stationDtos;
		initData.personPermissions = personPermissions;

		initData.person.links = wordrailsService.generateSelfLinks(baseUrl + "/api/persons/" + person.id);
		initData.network.links = wordrailsService.generateSelfLinks(baseUrl + "/api/network/" + network.id);

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