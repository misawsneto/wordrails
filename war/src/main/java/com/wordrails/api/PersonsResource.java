package com.wordrails.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.wordrails.notification.NotificationManager;
import org.jboss.resteasy.spi.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wordrails.GCMService;
import com.wordrails.WordrailsService;
import com.wordrails.business.AccessControllerUtil;
import com.wordrails.business.Network;
import com.wordrails.business.NetworkRole;
import com.wordrails.business.Person;
import com.wordrails.business.Post;
import com.wordrails.business.UnauthorizedException;
import com.wordrails.converter.PostConverter;
import com.wordrails.persistence.BookmarkRepository;
import com.wordrails.persistence.NetworkRepository;
import com.wordrails.persistence.NetworkRolesRepository;
import com.wordrails.persistence.PersonNetworkRegIdRepository;
import com.wordrails.persistence.PersonRepository;
import com.wordrails.persistence.PostRepository;
import com.wordrails.persistence.RecommendRepository;
import com.wordrails.persistence.StationRepository;
import com.wordrails.persistence.StationRolesRepository;
import com.wordrails.persistence.TaxonomyRepository;

@Path("/persons")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Component
public class PersonsResource {
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
	private @Autowired PersonNetworkRegIdRepository pnrRepository;
//	private @Autowired GCMService gcmService;
	private @Autowired NotificationManager notificationManager;
	private @Autowired PostRepository postRepository;
	private @Autowired PostConverter postConverter;

	private @Autowired BookmarkRepository bookmarkRepository;
	private @Autowired RecommendRepository recommendRepository;

	public @Autowired @Qualifier("objectMapper") ObjectMapper mapper;

	@PUT
	@Path("/me/regId")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response putRegId(@FormParam("regId") String regId, @FormParam("networkId") Integer networkId, @FormParam("lat") Double lat, @FormParam("lng") Double lng) {
		Network network = networkRepository.findOne(networkId);
		Person person = accessControllerUtil.getLoggedPerson();
//		gcmService.updateRegId(network, person, regId);
		notificationManager.updateRegId(network, person, regId);
		return Response.status(Status.OK).build();
	}

	@PUT
	@Path("/me/token")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response putToken(@FormParam("token") String regId, @FormParam("networkId") Integer networkId, @FormParam("lat") Double lat, @FormParam("lng") Double lng) {
		//		Network network = networkRepository.findOne(networkId);
		//		Person person = accessControllerUtil.getLoggedPerson();
		//		gcmService.updateRegId(network, person, regId);
		return Response.status(Status.OK).build();
	}

	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response login(@FormParam("username") String username, @FormParam("password") String password){
		try{
			accessControllerUtil.authenticate(username, password);
			return Response.status(Status.OK).build();
		}catch(BadCredentialsException e){
			return Response.status(Status.UNAUTHORIZED).build();
		}
	}

	@GET
	@Path("/{personId}/posts")
	public ContentResponse<List<PostView>> getPersonNetworkPosts(@Context HttpServletRequest request, @PathParam("personId") Integer personId, @QueryParam("networkId") Integer networkId,
			@QueryParam("page") int page, @QueryParam("size") int size) throws ServletException, IOException {
		Pageable pageable = new PageRequest(page, size);

		String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();

		Person person = accessControllerUtil.getLoggedPerson();

		List<StationPermission> permissions = wordrailsService.getStationPermissions(baseUrl, person.id, networkId);

		List<Integer> stationIds = new ArrayList<Integer>();
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
	@Path("/{personId}/recommends")
	public ContentResponse<List<PostView>> getPersonNetworkRecommendations(@Context HttpServletRequest request, @PathParam("personId") Integer personId, @QueryParam("networkId") Integer networkId,
			@QueryParam("page") int page, @QueryParam("size") int size) throws ServletException, IOException {
		Pageable pageable = new PageRequest(page, size);

		String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();

		Person person = accessControllerUtil.getLoggedPerson();

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

	@PUT
	@Path("/me/password")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void putPassword(@FormParam("oldPassword") String oldPassword, @FormParam("newPassword") String newPassword) {

		try{
			org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			String username = user.getUsername();
			if(!username.equalsIgnoreCase("wordrails")) // don't allow users to change wordrails password
				userDetailsManager.changePassword(oldPassword, newPassword);
		}catch(Exception e){}
	}

	@GET
	@Path("/me")
	public void getCurrentPerson() {
		org.springframework.security.core.userdetails.User user = null;
		user = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = user.getUsername();		
		String path = httpServletRequest.getServletPath() + "/persons/search/findByUsername?username=" + username;
		httpRequest.forward(path);
	}

	@POST
	@Path("/create")
	public Response create(Person person){
		// TODO create user
		return Response.status(Status.CREATED).build();
	}

	@GET
	@Path("/allInit")
	public PersonData getAllInitData (@Context HttpServletRequest request, @Context HttpServletResponse response, @QueryParam("setAttributes") Boolean setAttributes) throws JsonParseException, JsonMappingException, JsonProcessingException, IOException{

		Integer stationId = wordrailsService.getStationIdFromCookie(request);
		PersonData personData = getInitialData(request);

		StationDto defaultStation = wordrailsService.getDefaultStation(personData, stationId);
		Integer stationPerspectiveId = defaultStation.defaultPerspectiveId;

		TermPerspectiveView termPerspectiveView = wordrailsService.getDefaultPerspective(stationPerspectiveId, 10);

		Pageable pageable = new PageRequest(0, 15);
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

	@GET
	@Path("/init")
	public PersonData getInitialData (@Context HttpServletRequest request) throws JsonParseException, JsonMappingException, JsonProcessingException, IOException{
		String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();

		Person person = accessControllerUtil.getLoggedPerson();

		if(person == null){
			throw new UnauthorizedException("User is not authorized");
		}

		Network network = wordrailsService.getNetworkFromHost(request);

		PersonPermissions personPermissions = new PersonPermissions();
		NetworkRole networkRole = networkRolesRepository.findByNetworkIdAndPersonId(network.id, person.id);
		List<StationDto> stationDtos = new ArrayList<StationDto>();

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

		initData.person = mapper.readValue(mapper.writeValueAsString(person).getBytes(), PersonDto.class);
		initData.network = mapper.readValue(mapper.writeValueAsString(network).getBytes(), NetworkDto.class); 
		initData.networkRole = mapper.readValue(mapper.writeValueAsString(networkRole).getBytes(), NetworkRoleDto.class);
		initData.stations = stationDtos;
		initData.personPermissions = personPermissions;

		initData.person.links = wordrailsService.generateSelfLinks(baseUrl + "/api/persons/" + person.id);
		initData.network.links = wordrailsService.generateSelfLinks(baseUrl + "/api/stations/" + network.id);
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
		Person person = accessControllerUtil.getLoggedPerson();
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
}
