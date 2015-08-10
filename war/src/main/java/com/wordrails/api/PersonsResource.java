package com.wordrails.api;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wordrails.GCMService;
import com.wordrails.WordrailsService;
import com.wordrails.auth.TrixAuthenticationProvider;
import com.wordrails.business.BadRequestException;
import com.wordrails.business.*;
import com.wordrails.converter.PostConverter;
import com.wordrails.persistence.*;
import com.wordrails.security.NetworkSecurityChecker;
import com.wordrails.security.StationSecurityChecker;
import com.wordrails.util.PersonCreateDto;
import com.wordrails.util.ReadsCommentsRecommendsCount;
import com.wordrails.util.WordrailsUtil;
import org.jboss.resteasy.spi.HttpRequest;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.authentication.BadCredentialsException;
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
	private @Context HttpServletRequest httpServletRequest;
	private @Context HttpRequest httpRequest;

	private @Autowired PersonRepository personRepository;

	private @Autowired NetworkRolesRepository networkRolesRepository;
	private @Autowired StationRepository stationRepository;
	private @Autowired StationRolesRepository stationRolesRepository;
	private @Autowired NetworkRepository networkRepository;
	private @Autowired WordrailsService wordrailsService;
	private @Autowired GCMService gcmService;
	private @Autowired PostRepository postRepository;
	private @Autowired PostConverter postConverter;

	private @Autowired BookmarkRepository bookmarkRepository;
	private @Autowired RecommendRepository recommendRepository;
	private @Autowired PostReadRepository postReadRepository;
	private @Autowired CommentRepository commentRepository;

	private @Autowired NetworkSecurityChecker networkSecurityChecker;
	private @Autowired StationSecurityChecker stationSecurityChecker;
	private @Autowired QueryPersistence queryPersistence;
	private @Autowired PersonEventHandler personEventHandler;

	@Autowired
	private UserRepository userRepository;

	public @Autowired @Qualifier("objectMapper") ObjectMapper mapper;
	public @Autowired StationRoleEventHandler stationRoleEventHandler;

	private @Autowired TrixAuthenticationProvider authProvider;

	private
	@Context
	HttpServletRequest request;
	private
	@Context
	UriInfo uriInfo;
	private
	@Context
	HttpServletResponse response;

	private void forward() throws ServletException, IOException {
		String path = request.getServletPath() + uriInfo.getPath();
		request.getServletContext().getRequestDispatcher(path).forward(request, response);
	}

	@PUT
	@Path("/{id}")
	public void updatePerson(@PathParam("id") Integer id) throws ServletException, IOException {
		Person person = authProvider.getLoggedPerson();

		Network network = wordrailsService.getNetworkFromHost(request);

		if(person.id.equals(id) || networkSecurityChecker.isNetworkAdmin(network))
			forward();
		else
			throw new BadRequestException();
	}

	@PUT
	@Path("/me/regId")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response putRegId(@FormParam("regId") String regId, @FormParam("networkId") Integer networkId, @FormParam("lat") Double lat, @FormParam("lng") Double lng) {
		Network network = networkRepository.findOne(networkId);
		Person person = authProvider.getLoggedPerson();
//		if(person.id == 0) person = null;
		System.out.println("regId: " + regId);
		gcmService.updateRegId(network, person, regId, lat, lng);
		return Response.status(Status.OK).build();
	}

	@PUT
	@Path("/me/token")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response putToken(@FormParam("token") String token, @FormParam("networkId") Integer networkId, @FormParam("lat") Double lat, @FormParam("lng") Double lng) {
		Network network = networkRepository.findOne(networkId);
		Person person = authProvider.getLoggedPerson();
//		if(person.id == 0) person = null;
		System.out.println("iOS token: " + token);
		gcmService.updateIosToken(network, person, token, lat, lng);
		return Response.status(Status.OK).build();
	}

	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response login(@Context HttpServletRequest request, @FormParam("username") String username, @FormParam("password") String password) {
		try{
			authProvider.passwordAuthentication(username, password, authProvider.getNetwork());
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

		Person person = null;
		if(personId == null){
			person = authProvider.getLoggedPerson();
		}else{
			person = personRepository.findOne(personId);
		}

		Network network = authProvider.getNetwork();

		List<StationPermission> permissions = wordrailsService.getStationPermissions(baseUrl, person.id, network.id);

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
		return Response.status(Status.OK).build();
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
	public Response create(PersonCreateDto personCreationObject, @Context HttpServletRequest request) throws ConflictException, BadRequestException, JsonProcessingException{
		Network network = authProvider.getNetwork();

		Person person = null;
		User user = null;
		if(personCreationObject != null){
			try{
				person = new Person();
				person.name = personCreationObject.name;
				person.username = personCreationObject.username;
				person.password = personCreationObject.password;
				person.email = personCreationObject.email;

				UserGrantedAuthority authority = new UserGrantedAuthority("ROLE_USER");
				authority.network = network;

				String password = person.password;

				if (password == null || password.trim().equals("")) {
					password = WordrailsUtil.generateRandomString(8, "a#");
				}

				user = new User();
				user.enabled = true;
				user.username = person.username;
				user.password = password;
				user.network = authority.network;
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

					Person conflictingPerson = null;
					if(person.email != null && person.email.trim().equals(errorVal)){
						conflictingPerson = personRepository.findByEmailAndNetworkId(person.email, network.id);
					}else if(person.username != null && person.username.trim().equals(errorVal)){
						conflictingPerson = personRepository.findByUsernameAndNetworkId(person.username, network.id);
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

			if(network != null ){
				NetworkRole networkRole = new NetworkRole();
				networkRole.network = networkRepository.findOne(network.id);
				networkRole.person = person;
				networkRole.admin = false;
				networkRolesRepository.save(networkRole);

				if(networkRole.admin) {
					UserGrantedAuthority authority = new UserGrantedAuthority(user, "ROLE_NETWORK_ADMIN", network);
					user.addAuthority(authority);
				}
			}

			StationRole stRole = personCreationObject.stationRole;
			if(stRole !=null){
				if(stRole.station != null && stRole.station.id != null){
					stRole.station = stationRepository.findOne(stRole.station.id);
					stRole.person = person;
					stationRoleEventHandler.handleBeforeCreate(stRole);
					stationRolesRepository.save(stRole);

					if(stRole.admin) {
						UserGrantedAuthority authority = new UserGrantedAuthority(user, "ROLE_STATION_ADMIN", network, stRole.station);
						user.addAuthority(authority);
					}
					if(stRole.editor) {
						UserGrantedAuthority authority = new UserGrantedAuthority(user, "ROLE_STATION_EDITOR", network, stRole.station);
						user.addAuthority(authority);
					}
					if(stRole.writer) {
						UserGrantedAuthority authority = new UserGrantedAuthority(user, "ROLE_STATION_WRITER", network, stRole.station);
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

	@GET
	@Path("/allInit")
	public PersonData getAllInitData (@Context HttpServletRequest request, @Context HttpServletResponse response, @QueryParam("setAttributes") Boolean setAttributes) throws JsonParseException, JsonMappingException, JsonProcessingException, IOException{

		Integer stationId = wordrailsService.getStationIdFromCookie(request);
		PersonData personData = getInitialData(request);

		StationDto defaultStation = wordrailsService.getDefaultStation(personData, stationId);
		Integer stationPerspectiveId = defaultStation.defaultPerspectiveId;

		TermPerspectiveView termPerspectiveView = wordrailsService.getDefaultPerspective(stationPerspectiveId, 10);

		Pageable pageable = new PageRequest(0, 10);
		//			Pageable pageable2 = new PageRequest(0, 100, new Sort(Direction.DESC, "id"));

		if(defaultStation != null){
			//				if(personData.person != null && !personData.person.username.equals("wordrails")){
			//					List<Integer> postsRead = postRepository.findPostReadByPerson(personData.person.id, pageable2);
			//					List<Integer> bookmarks = bookmarkRepository.findBookmarkByPerson(personData.person.id, pageable2);
			//					List<Integer> recommends = recommendRepository.findRecommendByPerson(personData.person.id, pageable2);
			//					personData.postsRead = postsRead;
			//					personData.bookmarks = bookmarks;
			//					personData.recommends = recommends;
			//				}

			List<Post> popular = postRepository.findPopularPosts(defaultStation.id, pageable);
			List<Post> recent = postRepository.findPostsOrderByDateDesc(defaultStation.id, pageable);
			personData.popular = postConverter.convertToViews(popular);
			personData.recent = postConverter.convertToViews(recent);

		}

		if(setAttributes != null && setAttributes){
			request.setAttribute("personData", mapper.writeValueAsString(personData));
			request.setAttribute("termPerspectiveView", mapper.writeValueAsString(termPerspectiveView));
			request.setAttribute("networkName", personData.network.name);
			request.setAttribute("networkDesciption", "");
			request.setAttribute("networkKeywords", "");
		}

		return personData;
	}

	@Path("/count")
	@GET
	@Produces(MediaType.APPLICATION_FORM_URLENCODED)
	public Response countPersonsByNetwork(@QueryParam("networkId") Integer networkId){
		return Response.status(Status.OK).entity("{\"count\": " + personRepository.countPersonsByNetwork(networkId) + " }").build();
	}

	@PUT
	@Path("/deleteMany/network")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteMany (@Context HttpServletRequest request, List<Integer> personIds){
		Network network = wordrailsService.getNetworkFromHost(request);
		List<Person> persons = personRepository.findPersonsByIds(personIds);

		if(persons != null && persons.size() > 0) {
			for (Person person : persons) {
				if (!person.user.network.id.equals(network.id)) return Response.status(Status.UNAUTHORIZED).build();
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
	public Response deletePersonFromNetwork (@Context HttpServletRequest request, @PathParam("personId") Integer personId) throws JsonParseException, JsonMappingException, JsonProcessingException, IOException{
		Network network = wordrailsService.getNetworkFromHost(request);
		Person person = personRepository.findOne(personId);

		if(person != null && networkSecurityChecker.isNetworkAdmin(network) && person.user.network.id.equals(network.id)){
			personEventHandler.handleBeforeDelete(person);
			personRepository.delete(person.id);
			return Response.status(Status.OK).build();
		}else{
			return Response.status(Status.UNAUTHORIZED).build();
		}

	}

	@PUT
	@Path("/deletePersonStationRoles")
	@Consumes(MediaType.APPLICATION_JSON)
	@Transactional(readOnly = false)
	public Response deletePersonStationRoles(List<Integer> stationRolesIds) throws JsonParseException, JsonMappingException, JsonProcessingException, IOException{

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

//	@PUT
//	@Path("/{id}")
//	@Consumes(MediaType.APPLICATION_JSON)
//	@Transactional()
//	public Response updatePerson(@PathParam("id") Integer personId, Person person){
//		personRepository.save(sa)
//		return Response.status(Status.OK).build();
//	}

	@GET
	@Path("/init")
	public PersonData getInitialData (@Context HttpServletRequest request) throws IOException{
		String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();

		Person person = authProvider.getLoggedPerson();

		if(person == null){
			throw new UnauthorizedException("User is not authorized");
		}

		Network network = wordrailsService.getNetworkFromHost(request);

		PersonPermissions personPermissions = new PersonPermissions();
		NetworkRole networkRole = networkRolesRepository.findByNetworkIdAndPersonId(network.id, person.id);
		List<StationDto> stationDtos = new ArrayList<>();

		//Network Permissions
		NetworkPermission networkPermissionDto = new NetworkPermission();
		if(networkRole != null)
			networkPermissionDto.networkId = networkRole.id;
		else
			networkPermissionDto.admin = false;

		personPermissions.networkPermission = networkPermissionDto;
		personPermissions.stationPermissions = wordrailsService.getStationPermissions(baseUrl, person.id, network.id, stationDtos);
		personPermissions.personId = person.id;
		personPermissions.username = person.username;
		personPermissions.personName = person.name;

		PersonData initData = new PersonData();

		initData.person = mapper.readValue(mapper.writeValueAsString(person).getBytes("UTF-8"), PersonDto.class);
		initData.network = mapper.readValue(mapper.writeValueAsString(network).getBytes("UTF-8"), NetworkDto.class);
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


	@GET
	@Path("/me/bookmarkedRecommended")
	public ContentResponse<List<BooleanResponse>> checkBookmarkedRecommendedByMe(@QueryParam("postId") Integer postId){
		Person person = authProvider.getLoggedPerson();
		List<BooleanResponse> resp = new ArrayList<BooleanResponse>();

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

		ContentResponse<List<BooleanResponse>> response = new ContentResponse<List<BooleanResponse>>();
		response.content = resp;
		return response;
	}

	@GET
	@Path("/me/publicationsCount")
	public Response publicationsCount(@QueryParam("personId") Integer personId)throws JsonProcessingException {
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
	public Response personStats(@QueryParam("date") String date, @QueryParam("postId") Integer postId) throws JsonProcessingException{
		if(date == null)
			throw new BadRequestException("Invalid date. Expected yyyy-MM-dd");

		org.joda.time.format.DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");

		Person person = null;
		if(postId == null || postId == 0) {
			person = authProvider.getLoggedPerson();
		}

		TreeMap<Long, ReadsCommentsRecommendsCount> stats = new TreeMap<Long, ReadsCommentsRecommendsCount>();
		DateTime firstDay = formatter.parseDateTime(date);

		// create date slots
		DateTime lastestDay = firstDay;
		while (firstDay.minusDays(30).getMillis() < lastestDay.getMillis()){
			stats.put(lastestDay. getMillis(), new ReadsCommentsRecommendsCount());
			lastestDay = lastestDay.minusDays(1);
		}

		List<Object[]> postReadCounts = new ArrayList<Object[]>();
		List<Object[]> recommendsCounts = new ArrayList<Object[]>();
		List<Object[]> commentsCounts = new ArrayList<Object[]>();
		List<Object[]> generalStatus = new ArrayList<Object[]>();

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
