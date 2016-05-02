package co.xarx.trix.web.rest.resource.v1;

import co.xarx.trix.api.*;
import co.xarx.trix.converter.PostConverter;
import co.xarx.trix.domain.*;
import co.xarx.trix.eventhandler.PersonEventHandler;
import co.xarx.trix.eventhandler.StationRoleEventHandler;
import co.xarx.trix.exception.BadRequestException;
import co.xarx.trix.exception.ConflictException;
import co.xarx.trix.exception.UnauthorizedException;
import co.xarx.trix.persistence.*;
import co.xarx.trix.services.InitService;
import co.xarx.trix.services.MobileService;
import co.xarx.trix.services.NetworkService;
import co.xarx.trix.services.PersonService;
import co.xarx.trix.services.analytics.StatisticsService;
import co.xarx.trix.services.security.AuthService;
import co.xarx.trix.services.security.StationPermissionService;
import co.xarx.trix.util.Constants;
import co.xarx.trix.util.Logger;
import co.xarx.trix.util.StatsJson;
import co.xarx.trix.util.StringUtil;
import co.xarx.trix.web.rest.AbstractResource;
import co.xarx.trix.web.rest.api.v1.PersonsApi;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import lombok.NoArgsConstructor;
import org.apache.http.util.Asserts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletException;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@NoArgsConstructor
public class PersonsResource extends AbstractResource implements PersonsApi {

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
	@Autowired
	private QueryPersistence queryPersistence;
	@Autowired
	private PersonEventHandler personEventHandler;
	@Autowired
	private InitService initService;
	@Autowired
	private StationPermissionService stationPermissionService;
	@Autowired
	private StatisticsService statisticsService;

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

	@Value("${trix.amazon.cloudfront}")
	String cloudfrontUrl;

	@Override
	public void getPersons() throws IOException {
		forward();
	}

	@Override
	@Transactional
	public Response findPerson(Integer id) throws IOException {
		Person person = authProvider.getLoggedPerson();

		if(person.id.equals(id) || person.networkAdmin) {
			forward();
			return Response.status(Status.OK).build();
		}else
			return Response.status(Status.UNAUTHORIZED).build();
	}

	@Override
	public Response findPersonByUsername() throws IOException {
		forward();
		return null;
	}

	@Override
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

	@Override
	@Transactional
	public Response updateAuthData(PersonAuthDto person){

		Person loadedPerson = personRepository.findOne(person.id);

		if(person.password != null && !person.password.isEmpty() && !person.password.equals(person.passwordConfirm))
			throw new BadRequestException("Password no equal");

		if((person.password != null && !person.password.isEmpty()) && person.password.length() < 5)
			throw new BadRequestException("Invalid Password");

		if(!StringUtil.isEmailAddr(person.email))
			throw new BadRequestException("Not email");

		if(person.username == null || person.username.isEmpty() || person.username.length() < 3 || !StringUtil.isFQDN
				(person.username + ".com"))
			throw new BadRequestException("Invalid username");


		loadedPerson.email = person.email;

		User user = null;
		if(!person.username.equals(loadedPerson.username)){
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

		return Response.status(Status.OK).build();
	}

	@Override
	@Transactional
	public void updatePerson(Integer id) throws ServletException, IOException {
		Person person = authProvider.getLoggedPerson();

		if(person.id.equals(id) || person.networkAdmin)
			forward();
		else
			throw new BadRequestException();
	}

	@Override
	@Deprecated
	public Response putRegId(String regId, Integer networkId, Double lat, Double lng) {
		return updateMobile(regId, lat, lng, Constants.MobilePlatform.ANDROID);
	}

	@Override
	@Deprecated
	public Response putToken(String token, Integer networkId, Double lat, Double lng) {
		return updateMobile(token, lat, lng, Constants.MobilePlatform.APPLE);
	}

	private Response updateMobile(String token, Double lat, Double lng, Constants.MobilePlatform type) {
		Person person = authProvider.getLoggedPerson();
		Logger.info("Updating " + type.toString() + " device " + token + " for person " + person.id);
		mobileService.updateDevice(person, token, lat, lng, type);
		return Response.status(Response.Status.OK).build();
	}

	@Override
	public Response tokenSignin(String token) {
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

	@Override
	public ContentResponse<List<PostView>> getPersonNetworkPosts(Integer personId,
																 Integer networkId,
																 int page,
																 int size) throws ServletException, IOException {
		Pageable pageable = new PageRequest(page, size);

		List<Integer> stationsWithPermission = stationPermissionService.findStationsWithPermission();

		List<Post> posts = postRepository.findPostByPersonIdAndStations(personId, stationsWithPermission, pageable);

		ContentResponse<List<PostView>> response = new ContentResponse<>();
		response.content = postConverter.convertToViews(posts);
		return response;
	}

	@Override
	public ContentResponse<List<PostView>> getPersonNetworkPostsByState(Integer personId,
																		String state,
																		int page,
																		int size) throws ServletException, IOException {
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

	@Override
	public void getCurrentPerson() throws ServletException, IOException {
		Person person = authProvider.getLoggedPerson();
		forward("/persons/search/findByUsername?username=" + person.username);
	}

	@Override
	@Transactional
	public void putPassword(String oldPassword,
							String newPassword) {
		Asserts.notEmpty(oldPassword, "Old password it empty or null");
		Asserts.notEmpty(newPassword, "New password it empty or null");

		if(newPassword.length() < 5)throw new BadRequestException("Password too short");

		Person loggedPerson = authProvider.getLoggedPerson();
		if (!oldPassword.equals(loggedPerson.user.password)) throw new UnauthorizedException("Wrong password");

		loggedPerson.user.password = newPassword;
		personRepository.save(loggedPerson);
	}

	@Override
	public Response signUp(PersonCreateDto dto) throws ConflictException, BadRequestException, IOException {
		Person person = personService.create(dto.name, dto.username, dto.password, dto.email, dto.stationsRole);

		if (person != null) {
			return Response.status(Status.CREATED).entity(mapper.writeValueAsString(person)).build();
		} else {
			throw new BadRequestException();
		}
	}

	@Override
	public Response invitePerson(PersonInviteDto dto) throws ConflictException, BadRequestException, IOException {
		personService.invite(dto);
		return Response.status(Status.CREATED).build();
	}


	@Override
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

	@Override
	public Response deleteMany(List<Integer> personIds){
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

	@Override
	public Response disablePerson(Integer personId){
		Person self = authProvider.getLoggedPerson();
		Person person = personRepository.findOne(personId);
		if(!self.id.equals(person.id)) {
			person.user.enabled = false;
			personRepository.save(person);
		}
		return Response.status(Status.CREATED).build();
	}

	@Override
	public Response enablePerson(Integer personId){
		Person self = authProvider.getLoggedPerson();
		Person person = personRepository.findOne(personId);
		if(!self.id.equals(person.id)) {
			person.user.enabled = true;
			personRepository.save(person);
		}
		return Response.status(Status.CREATED).build();
	}

	@Override
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

			for (StationRole sr: roles) {
				stationRolesRepository.save(sr);
			}


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

			for (StationRole sr : allRoles ) {
				stationRolesRepository.save(sr);
			}

		}
		return Response.status(Status.CREATED).build();
	}

	@Override
	public Response enablePerson(IdsList idsList){
		Person self = authProvider.getLoggedPerson();
		if(idsList != null && idsList.ids != null){
			List<Person> persons = personRepository.findAll(idsList.ids);
			for (Person person: persons) {
				if(self.id.equals(person.id))
					continue;
				person.user.enabled = true;
			}

			for (Person person: persons) {
				personRepository.save(person);
			}

		}
		return Response.status(Status.CREATED).build();
	}

	@Override
	public Response disablePerson(IdsList idsList){
		Person self = authProvider.getLoggedPerson();
		if(idsList != null && idsList.ids != null){
			List<Person> persons = personRepository.findAll(idsList.ids);
			for (Person person: persons) {
				if(self.id.equals(person.id))
					continue;
				person.user.enabled = false;
			}

			for (Person person : persons) {
				personRepository.save(person);
			}
		}
		return Response.status(Status.CREATED).build();
	}

	@Override
	public PersonData getAllInitData() throws IOException {
		String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
		Network network = networkService.getNetworkFromHost(request.getHeader("Host"));
		Integer stationId = initService.getStationIdFromCookie(request);
		PersonData personData = initService.getInitialData(baseUrl, network);

		PersonData data = initService.getData(personData, stationId);
		request.setAttribute("personData", simpleMapper.writeValueAsString(data));
		request.setAttribute("networkName", data.network.name);
		return data;
	}

	@Override
	public PersonData getInitialData() throws IOException{
		String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
		Network network = networkService.getNetworkFromHost(request.getHeader("Host"));
		return initService.getInitialData(baseUrl, network);
	}

	@Override
	public Response publicationsCount(Integer personId)throws IOException {
		Person person = null;
		if(personId != null){
			person = personRepository.findOne(personId);
		}else{
			person = authProvider.getLoggedPerson();
		}

		List<Object[]> counts =  queryPersistence.getPersonPublicationsCount(person.id);
		return Response.status(Status.OK).entity("{\"publicationsCounts\": " + (counts.size() > 0 ? mapper.writeValueAsString(counts.get(0)) : null) + "}").build();
	}

	@Override
	public StatsJson personStats(String date, Integer postId) throws JsonProcessingException {
		if(postId == null){
			Person person = authProvider.getLoggedPerson();
			return statisticsService.personStats(date, person.getId(), null);
		} else{
			return statisticsService.postStats(date, postId, null);
		}
	}

//	@Override
//	public Response personStatsOld(String date, Integer postId) throws IOException{
//		if(date == null)
//			throw new BadRequestException("Invalid date. Expected yyyy-MM-dd");
//
//		org.joda.time.format.DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
//
//		Person person = null;
//		if(postId == null || postId == 0) {
//			person = authProvider.getLoggedPerson();
//		}
//
//		TreeMap<Long, ReadsCommentsRecommendsCount> stats = new TreeMap<>();
//		DateTime firstDay = formatter.parseDateTime(date);
//
//		// create date slots
//		DateTime lastestDay = firstDay;
//		while (firstDay.minusDays(30).getMillis() < lastestDay.getMillis()){
//			stats.put(lastestDay. getMillis(), new ReadsCommentsRecommendsCount());
//			lastestDay = lastestDay.minusDays(1);
//		}
//
//		List<Object[]> postReadCounts;
//		List<Object[]> commentsCounts;
//		List<Object[]> generalStatus;
//
//		if(person == null) {
//			postReadCounts = postReadRepository.countByPostAndDate(postId, firstDay.minusDays(30).toDate(), firstDay.toDate());
//			commentsCounts = commentRepository.countByPostAndDate(postId, firstDay.minusDays(30).toDate(), firstDay.toDate());
//			generalStatus = postRepository.findPostStats(postId);
//		}else {
//			postReadCounts = postReadRepository.countByAuthorAndDate(person.id, firstDay.minusDays(30).toDate(), firstDay.toDate());
//			commentsCounts = commentRepository.countByAuthorAndDate(person.id, firstDay.minusDays(30).toDate(), firstDay.toDate());
//			generalStatus = personRepository.findPersonStats(person.id);
//		}
//
//		// check date and map counts
//		Iterator it = stats.entrySet().iterator();
//		checkDateAndMapCounts(postReadCounts, it);
//
//		it = stats.entrySet().iterator();
//		checkDateAndMapCounts(commentsCounts, it);
//
//		String generalStatsJson = mapper.writeValueAsString(generalStatus != null && generalStatus.size() > 0 ? generalStatus.get(0) : null);
//		String dateStatsJson = mapper.writeValueAsString(stats);
//		return Response.status(Status.OK).entity("{\"generalStatsJson\": " + generalStatsJson + ", \"dateStatsJson\": " + dateStatsJson + "}").build();
//	}
//
//	private void checkDateAndMapCounts(List<Object[]> countList, Iterator it) {
//		while (it.hasNext()){
//			Map.Entry<Long,ReadsCommentsRecommendsCount> pair = (Map.Entry<Long,ReadsCommentsRecommendsCount>)it.next();
//			long key = (Long)pair.getKey();
//			for(Object[] counts: countList){
//				long dateLong = ((java.sql.Date) counts[0]).getTime();
//				long count = (long) counts[1];
//				if(new DateTime(key).withTimeAtStartOfDay().equals(new DateTime(dateLong).withTimeAtStartOfDay()))
//					pair.getValue().commentsCount = count;
//			}
//		}
//	}


	@Override
	/**
	 * {@link co.xarx.trix.persistence.PersonRepository#findPersons(String, Pageable)}
	 */
	public void findPersons() throws IOException {
		forward();
	}
}