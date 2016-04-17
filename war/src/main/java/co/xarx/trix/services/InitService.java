package co.xarx.trix.services;

import co.xarx.trix.annotations.IgnoreMultitenancy;
import co.xarx.trix.api.*;
import co.xarx.trix.converter.PostConverter;
import co.xarx.trix.domain.*;
import co.xarx.trix.eventhandler.StationRoleEventHandler;
import co.xarx.trix.exception.UnauthorizedException;
import co.xarx.trix.persistence.MenuEntryRepository;
import co.xarx.trix.persistence.PostRepository;
import co.xarx.trix.persistence.StationRepository;
import co.xarx.trix.persistence.StationRolesRepository;
import co.xarx.trix.services.security.AuthService;
import co.xarx.trix.util.StringUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class InitService {

	@Autowired
	private StationRepository stationRepository;
	@Autowired
	private StationRolesRepository stationRolesRepository;
	@Autowired
	private PostRepository postRepository;
	@Autowired
	private PostConverter postConverter;
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
	private AuthService authProvider;

	@PersistenceContext(type=javax.persistence.PersistenceContextType.EXTENDED)
	EntityManager em;


	@Value("${trix.amazon.cloudfront}")
	String cloudfrontUrl;

	public void systemInit() {
//		List<Station> stations = em.createQuery("SELECT s from Station s").getResultList();
//		for(Station station: stations){
//			station.stationSlug = StringUtil.toSlug(station.name);
//			em.persist(station);
//		}
	}

	public PersonData getData(PersonData personData, Integer stationId) throws IOException {
		StationDto defaultStation = getDefaultStation(personData, stationId);

		if (defaultStation != null) {
			personData.defaultStation = defaultStation;

			Pageable pageable = new PageRequest(0, 10);

			List<Post> popular = postRepository.findPopularPosts(defaultStation.id, pageable);
			List<Post> recent = postRepository.findPostsOrderByDateDesc(defaultStation.id, pageable);
			personData.popular = postConverter.convertToViews(popular);
			personData.recent = postConverter.convertToViews(recent);
		} else {
			personData.noVisibleStation = true;
		}

		return personData;
	}

	public PersonData getInitialData(String baseUrl, Network network) throws IOException {

		Person person = authProvider.getLoggedPerson();

		if (person == null) {
			throw new UnauthorizedException("User is not authorized");
		}


		PersonPermissions personPermissions = new PersonPermissions();


		List<StationDto> stationDtos = new ArrayList<>();
		List<Station> stations = stationRepository.findByPersonId(person.id);
		for (Station station : stations) {
			StationDto stationDto = mapper.readValue(mapper.writeValueAsString(station).getBytes("UTF-8"), StationDto.class);
			stationDto.links = generateSelfLinks(baseUrl + "/api/stations/" + station.id);
			stationDtos.add(stationDto);
		}

		personPermissions.stationPermissions = getStationPermissions(stations, person.id);
		personPermissions.personId = person.id;
		personPermissions.username = person.username;
		personPermissions.personName = person.name;

		PersonData initData = new PersonData();
		initData.isAdmin = person.networkAdmin;

		if (person.user != null && (person.password == null || person.password.equals(""))) {
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
				sectionDto.links = generateSelfLinks(baseUrl + "/api/menuEntries/" + sectionDto.id);
				menuEntries.add(sectionDto);

			}
		}
		initData.menuEntries = menuEntries;
		initData.sections = menuEntries;

		initData.person.links = generateSelfLinks(baseUrl + "/api/persons/" + person.id);
		initData.network.links = generateSelfLinks(baseUrl + "/api/network/" + network.id);

		Pageable pageable2 = new PageRequest(0, 100, new Sort(Sort.Direction.DESC, "id"));
		if (initData.person != null && !initData.person.username.equals("wordrails")) {
			List<Integer> postsRead = postRepository.findPostReadByPerson(initData.person.id, pageable2);
			List<Integer> bookmarks = new ArrayList(person.getBookmarkPosts());
			List<Integer> recommends = new ArrayList(person.getRecommendPosts());
			initData.postsRead = postsRead;
			initData.bookmarks = bookmarks;
			initData.recommends = recommends;
		}

		return initData;
	}

	public Integer getStationIdFromCookie(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null)
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("stationId")) {
					return Integer.parseInt(cookie.getValue());
				}
			}
		return null;
	}

	private StationDto getDefaultStation(PersonData personData, Integer currentStationId) {
		List<StationPermission> stationPermissions = personData.personPermissions.stationPermissions;

		Integer stationId = 0;

		if (stationPermissions == null)
			return null;

		for (StationPermission stationPermission : stationPermissions) {
			if ((currentStationId != null && currentStationId.equals(stationPermission.stationId)) || stationPermission.main)
				stationId = stationPermission.stationId;
		}

		if (stationId == 0)
			for (StationPermission stationPermission : stationPermissions) {
				if (stationPermission.visibility.equals(Station.UNRESTRICTED))
					stationId = stationPermission.stationId;
			}

		if (stationId == 0)
			for (StationPermission stationPermission : stationPermissions) {
				if (stationPermission.visibility.equals(Station.RESTRICTED_TO_NETWORKS))
					stationId = stationPermission.stationId;
			}

		if (stationId == 0)
			for (StationPermission stationPermission : stationPermissions) {
				if (stationPermission.visibility.equals(Station.RESTRICTED))
					stationId = stationPermission.stationId;
			}

		for (StationDto station : personData.stations) {
			if (stationId.equals(station.id)) {
				return station;
			}
		}

		return null;
	}


	private List<Link> generateSelfLinks(String self) {
		Link link = new Link();
		link.href = self;
		link.rel = "self";
		return Collections.singletonList(link);
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
}
