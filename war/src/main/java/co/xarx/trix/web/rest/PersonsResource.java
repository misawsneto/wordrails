package co.xarx.trix.web.rest;

import co.xarx.trix.WordrailsService;
import co.xarx.trix.api.*;
import co.xarx.trix.converter.PostConverter;
import co.xarx.trix.domain.*;
import co.xarx.trix.eventhandler.PersonEventHandler;
import co.xarx.trix.eventhandler.StationRoleEventHandler;
import co.xarx.trix.exception.BadRequestException;
import co.xarx.trix.exception.ConflictException;
import co.xarx.trix.exception.UnauthorizedException;
import co.xarx.trix.persistence.*;
import co.xarx.trix.services.AmazonCloudService;
import co.xarx.trix.services.MobileService;
import co.xarx.trix.services.NetworkService;
import co.xarx.trix.services.PersonService;
import co.xarx.trix.services.auth.AuthService;
import co.xarx.trix.services.auth.StationPermissionService;
import co.xarx.trix.util.Logger;
import co.xarx.trix.util.ReadsCommentsRecommendsCount;
import co.xarx.trix.util.StringUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import org.apache.http.util.Asserts;
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
import org.springframework.security.access.prepost.PreAuthorize;
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

	public static class PersonCreateDto {

		public String name;
		public String username;
		public String email;
		public String password;
	}

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
	private NetworkService networkService;
	@Autowired
	private PersonService personService;
	@Autowired
	private PostRepository postRepository;
	@Autowired
	private PostConverter postConverter;
	@Autowired
	private PostReadRepository postReadRepository;
	@Autowired
	private CommentRepository commentRepository;
//	@Autowired
//	private StationSecurityChecker stationSecurityChecker;
	@Autowired
	private QueryPersistence queryPersistence;
	@Autowired
	private PersonEventHandler personEventHandler;
	@Autowired
	private MenuEntryRepository menuEntryRepository;
	@Autowired
	private StationPermissionService stationPermissionService;

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

		authProvider.updateLoggedPerson(loadedPerson.user);

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

	private Response updateMobile(String token, Double lat, Double lng, MobileDevice.Type type) {
		Person person = authProvider.getLoggedPerson();
		Logger.info("Updating " + type.toString() + " device " + token + " for person " + person.id);
		mobileService.updateDevice(person, token, lat, lng, type);
		return Response.status(Response.Status.OK).build();
	}

	@POST
	@Path("/tokenSignin")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response tokenSignin(@Context HttpServletRequest request, @FormParam("token") String token) {
		try{
			Network network = networkService.getNetworkFromHost(request.getHeader("Host"));
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

		List<Integer> stationsWithPermission = stationPermissionService.findStationsWithPermission();

		List<Post> posts = postRepository.findPostByPersonIdAndStations(personId, stationsWithPermission, pageable);

		ContentResponse<List<PostView>> response = new ContentResponse<>();
		response.content = postConverter.convertToViews(posts);
		return response;
	}

	@GET
	@Path("/getPostsByState")
	public ContentResponse<List<PostView>> getPersonNetworkPostsByState(@Context HttpServletRequest request, @QueryParam("personId") Integer personId, @QueryParam("state") String state,
																		@QueryParam("page") int page, @QueryParam("size") int size) throws ServletException, IOException {
		Pageable pageable = new PageRequest(page, size);

		Person person;
		if(personId == null){
			person = authProvider.getLoggedPerson();
		}else{
			person = personRepository.findOne(personId);
		}

		List<Integer> stationsWithPermission = stationPermissionService.findStationsWithPermission();

		List<Post> posts = postRepository.findPostByPersonIdAndStationsAndState(person.id, state, stationsWithPermission, pageable);

		ContentResponse<List<PostView>> response = new ContentResponse<>();
		response.content = postConverter.convertToViews(posts);
		return response;
	}

	@GET
	@Path("/me")
	public void getCurrentPerson() {
		Person person = authProvider.getLoggedPerson();
		String path = httpServletRequest.getServletPath() + "/persons/search/findByUsername?username=" + person.username;
		httpRequest.forward(path);
	}

	@PUT
	@Transactional
	@Path("/me/password")
	@PreAuthorize("isAuthenticated()")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void putPassword(@FormParam("oldPassword") String oldPassword, @FormParam("newPassword") String newPassword) {
		Asserts.notEmpty(oldPassword, "Old password it empty or null");
		Asserts.notEmpty(newPassword, "New password it empty or null");

		if(newPassword.length() < 5)throw new BadRequestException("Password too short");

		Person loggedPerson = authProvider.getLoggedPerson();
		if (!oldPassword.equals(loggedPerson.user.password)) throw new UnauthorizedException("Wrong password");

		loggedPerson.user.password = newPassword;
		personRepository.save(loggedPerson);
	}

	@POST
	@Path("/create")
	public Response signUp(PersonCreateDto dto) throws ConflictException, BadRequestException, IOException{
		Person person = personService.create(dto.name, dto.username, dto.password, dto.email);

		if (person != null) {
			return Response.status(Status.CREATED).entity(mapper.writeValueAsString(person)).build();
		} else {
			throw new BadRequestException();
		}
	}

	@GET
	@Path("/stats/count")
	public ContentResponse<Integer> countPersonsByNetwork(@QueryParam("q") String q){
		ContentResponse<Integer> resp = new ContentResponse<>();
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
	@PreAuthorize("hasRole('ROLE_ADMIN')")
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

	@PUT
	@Path("/{personId}/disable")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
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
	@PreAuthorize("hasRole('ROLE_ADMIN')")
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
	@Transactional
	@Path("/updateStationRoles")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
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
	@PreAuthorize("hasRole('ROLE_ADMIN')")
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
	@PreAuthorize("hasRole('ROLE_ADMIN')")
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

	@GET
	@Path("/allInit")
	public PersonData   getAllInitData (@Context HttpServletRequest request, @Context HttpServletResponse response, @QueryParam("setAttributes") Boolean setAttributes) throws IOException {

		Network network = networkService.getNetworkFromHost(request.getHeader("Host"));
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

		Network network = networkService.getNetworkFromHost(request.getHeader("Host"));

		PersonPermissions personPermissions = new PersonPermissions();


		List<StationDto> stationDtos = new ArrayList<>();
		List<Station> stations = stationRepository.findByPersonId(person.id);
		for(Station station : stations) {
			StationDto stationDto = mapper.readValue(mapper.writeValueAsString(station).getBytes("UTF-8"), StationDto.class);
			stationDto.links = wordrailsService.generateSelfLinks(baseUrl + "/api/stations/" + station.id);
			stationDtos.add(stationDto);
		}

		personPermissions.stationPermissions = getStationPermissions(stations, person.id);
		personPermissions.personId = person.id;
		personPermissions.username = person.username;
		personPermissions.personName = person.name;

		PersonData initData = new PersonData();
		initData.isAdmin = person.networkAdmin;

		if(person.user != null && (person.password == null || person.password.equals(""))){
			initData.noPassword = true;
		}

		initData.publicCloudfrontUrl = cloudfrontUrl;
		initData.privateCloudfrontUrl = cloudfrontUrl;

		initData.person = mapper.readValue(mapper.writeValueAsString(person).getBytes("UTF-8"), PersonDto.class);
		initData.network = mapper.readValue(mapper.writeValueAsString(network).getBytes("UTF-8"), NetworkDto.class);

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

		Pageable pageable2 = new PageRequest(0, 100, new Sort(Direction.DESC, "id"));
		if(initData.person != null && !initData.person.username.equals("wordrails")){
			List<Integer> postsRead = postRepository.findPostReadByPerson(initData.person.id, pageable2);
			List<Integer> bookmarks = new ArrayList(person.getBookmarkPosts());
			List<Integer> recommends = new ArrayList(person.getRecommendPosts());
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
	@Path("/me/publicationsCount")
	@PreAuthorize("isAuthenticated()")
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
	@PreAuthorize("isAuthenticated()")
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
		List<Object[]> commentsCounts;
		List<Object[]> generalStatus;

		if(person == null) {
			postReadCounts = postReadRepository.countByPostAndDate(postId, firstDay.minusDays(30).toDate(), firstDay.toDate());
			commentsCounts = commentRepository.countByPostAndDate(postId, firstDay.minusDays(30).toDate(), firstDay.toDate());
			generalStatus = postRepository.findPostStats(postId);
		}else {
			postReadCounts = postReadRepository.countByAuthorAndDate(person.id, firstDay.minusDays(30).toDate(), firstDay.toDate());
			commentsCounts = commentRepository.countByAuthorAndDate(person.id, firstDay.minusDays(30).toDate(), firstDay.toDate());
			generalStatus = personRepository.findPersonStats(person.id);
		}

		// check date and map counts
		Iterator it = stats.entrySet().iterator();
		checkDateAndMapCounts(postReadCounts, it);

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