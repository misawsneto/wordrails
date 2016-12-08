package co.xarx.trix.services;

import co.xarx.trix.api.*;
import co.xarx.trix.config.multitenancy.TenantContextHolder;
import co.xarx.trix.converter.TermConverter;
import co.xarx.trix.domain.*;
import co.xarx.trix.exception.UnauthorizedException;
import co.xarx.trix.persistence.MenuEntryRepository;
import co.xarx.trix.persistence.PostRepository;
import co.xarx.trix.persistence.TermRepository;
import co.xarx.trix.services.security.AuthService;
import co.xarx.trix.services.security.PersonPermissionService;
import co.xarx.trix.services.security.StationPermissionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

import static org.springframework.security.acls.domain.BasePermission.READ;

@Service
public class InitService {

	@Autowired
	@Qualifier("objectMapper")
	public ObjectMapper mapper;
	@Autowired
	@Qualifier("simpleMapper")
	public ObjectMapper simpleMapper;
	@Value("${trix.amazon.cloudfront}")
	String cloudfrontUrl;
	@Autowired
	private PostRepository postRepository;
	@Autowired
	private StationPermissionService stationPermissionService;
	@Autowired
	private PersonPermissionService personPermissionService;
	@Autowired
	private MenuEntryRepository menuEntryRepository;
	@Autowired
	private TermRepository termRepository;
	@Autowired
	private AuthService authProvider;
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private TermConverter termConverter;

	@Autowired
	private NetworkService networkService;

	@Value("${spring.profiles.active:'dev'}")
	String profile;

	@Autowired
	private HttpServletRequest request;

	public PersonData getData(PersonData personData, Integer stationId) throws IOException {
		StationView defaultStation = getDefaultStation(personData, stationId);

		if (defaultStation != null) {
			personData.defaultStation = defaultStation;
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


		List<StationView> stationViews = new ArrayList<>();
		List<Station> stations = personPermissionService.getStationsWithPermission(READ);
		for (Station station : stations) {
			StationView stationView = modelMapper.map(station, StationView.class);
			List<Term> terms = termRepository.findByTaxonomyId(station.categoriesTaxonomyId);
			stationView.categories = termConverter.convertToViews(terms);
//			stationDto.links = generateSelfLinks(baseUrl + "/api/stations/" + station.id);
//			stationDtos.add(stationDto);
			Set<String> perspectives = new HashSet<>();
			for (String perspective : stationView.stationPerspectives) {
				perspectives.add(baseUrl + "/api/stationPerspectives/" + perspective);
			}
			stationView.stationPerspectives = perspectives;
			stationViews.add(stationView);
		}

		personPermissions.stationPermissions = stationPermissionService.getStationPermissions(stations);
		personPermissions.personId = person.id;
		personPermissions.username = person.username;
		personPermissions.personName = person.name;

		PersonData initData = new PersonData();
		initData.isAdmin = person.networkAdmin;
		if (authProvider.isAnonymous()) {
			initData.permissions = personPermissionService.getPermissions(new GrantedAuthoritySid("ROLE_ANONYMOUS"));
		} else {
			initData.permissions = personPermissionService.getPermissions(new PrincipalSid(person.username));
		}

		if (person.user != null && (person.password == null || person.password.equals(""))) {
			initData.noPassword = true;
		}

		initData.publicCloudfrontUrl = cloudfrontUrl;
		initData.privateCloudfrontUrl = cloudfrontUrl;

		initData.person = mapper.readValue(mapper.writeValueAsString(person).getBytes("UTF-8"), PersonDto.class);
		initData.network = mapper.readValue(mapper.writeValueAsString(network).getBytes("UTF-8"), NetworkData.class);
		initData.network.facebookUrlRedirect = network.domain == null ? String.format("http://%s."
				+ (profile.equals ("dev") ? "xarx" : "trix") + ".rocks/", network.tenantId) : java.lang.String.format
				("http://%s/", network.domain);
		initData.network.logoImageId = network.getLogoImageId();
		initData.network.loginImageId = network.getLoginImageId();
		initData.network.splashImageId = network.getSplashImageId();
		initData.network.faviconId = network.getFaviconId();
		initData.network.subdomain = TenantContextHolder.getCurrentTenantId();

		if(networkService.getDeviceFromRequest(request) != null && network.authCredential != null){
			initData.network.facebookAppSecret = network.authCredential.facebookAppSecret;
			initData.network.googleAppSecret = network.authCredential.googleAppSecret;
		}

		initData.stations = stationViews;
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

		Pageable pageable2 = new PageRequest(0, 100, new Sort(Sort.Direction.DESC, "id"));
		if (initData.person != null && !initData.person.username.equals("wordrails")) {
			List<Integer> bookmarks = new ArrayList(person.getBookmarkPosts());
			List<Integer> recommends = new ArrayList(person.getRecommendPosts());
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

	private StationView getDefaultStation(PersonData personData, Integer currentStationId) {
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

		for (StationView station : personData.stations) {
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


}
