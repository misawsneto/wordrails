package co.xarx.trix;

import co.xarx.trix.api.*;
import co.xarx.trix.domain.Network;
import co.xarx.trix.domain.Station;
import co.xarx.trix.domain.StationRole;
import co.xarx.trix.domain.Term;
import co.xarx.trix.persistence.*;
import co.xarx.trix.services.CacheService;
import co.xarx.trix.util.StringUtil;
import co.xarx.trix.web.rest.PerspectiveResource;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.NotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Component("wordrailsService")
public class WordrailsService {

	Logger log = Logger.getLogger(WordrailsService.class.getName());

	@Autowired
	public ObjectMapper objectMapper;
	@Autowired
	public TaxonomyRepository taxonomyRepository;
	@Autowired
	public PersonRepository personRepository;
	@Autowired
	private NetworkRepository networkRepository;
	@Autowired
	private PerspectiveResource perspectiveResource;
	@Autowired
	private CacheService cacheService;
	@Autowired
	private StationRepository stationRepository;
	@Autowired
	private StationRolesRepository stationRolesRepository;

	private LoadingCache<PermissionId, StationsPermissions> stationsPermissions;

	@PostConstruct
	public void init(){
		// ------------- init person cache
		stationsPermissions = CacheBuilder.newBuilder().maximumSize(1000)
				.expireAfterWrite(1, TimeUnit.MINUTES)
						//	       .removalListener(MY_LISTENER)
				.build(
						new CacheLoader<PermissionId, StationsPermissions>() {
							public StationsPermissions load(PermissionId id) {
								List<StationPermission> permissions = getStationPermissions(id.baseUrl, id.personId);
								if (permissions != null) {
									StationsPermissions stationsPermissions = new StationsPermissions();
									stationsPermissions.stationPermissionDtos = permissions;
									return stationsPermissions;
								} else {
									return null;
								}
							}
						});
	}

	public StationsPermissions getPersonPermissions(PermissionId id) throws ExecutionException{
		return stationsPermissions.get(id);
	}

	public List<Link> generateSelfLinks(String self){
		Link link = new Link();
		link.href = self;
		link.rel = "self";
		return Collections.singletonList(link);
	}

	@Autowired
	private HttpServletResponse response;

	public Network getNetworkFromHost(String host) {
		Network network = null;
		String subdomain = StringUtil.getSubdomainFromHost(host);
		if (subdomain != null && !subdomain.isEmpty()) {
			if (subdomain.equals("settings")) {
				network = new Network();
				network.name = "Settings";
				network.tenantId = "settings";
				network.id = 0;
			}

			try {
				network = networkRepository.findByTenantId(subdomain);
			} catch (Exception e) {
				// no network found in cache or db.
				e.printStackTrace();
			}
			if (network != null) return network;
		}

		try {
			network = networkRepository.findByDomain(host);
			if (network != null) return network;
			response.setStatus(400);
			return null;
		} catch (Exception e) {
			throw new NotFoundException();
		}
	}

	public StationDto getDefaultStation(PersonData personData, Integer currentStationId){
		List<StationPermission> stationPermissions = personData.personPermissions.stationPermissions;

		Integer stationId = 0;

		if(stationPermissions == null)
			return null;

		for (StationPermission stationPermission : stationPermissions) {
			if((currentStationId != null && currentStationId.equals(stationPermission.stationId)) || stationPermission.main)
				stationId = stationPermission.stationId;
		}

		if(stationId == 0)
			for (StationPermission stationPermission : stationPermissions) {
				if(stationPermission.visibility.equals(Station.UNRESTRICTED))
					stationId = stationPermission.stationId;
			}

		if(stationId == 0)
			for (StationPermission stationPermission : stationPermissions) {
				if(stationPermission.visibility.equals(Station.RESTRICTED_TO_NETWORKS))
					stationId = stationPermission.stationId;
			}

		if(stationId == 0)
			for (StationPermission stationPermission : stationPermissions) {
				if(stationPermission.visibility.equals(Station.RESTRICTED))
					stationId = stationPermission.stationId;
			}

		for (StationDto station : personData.stations) {
			if(stationId.equals(station.id)){
				return station;
			}
		}

		return null;
	}

	public TermPerspectiveView getDefaultPerspective(Integer stationPerspectiveId, int size) {
		return perspectiveResource.getTermPerspectiveView(null, null, stationPerspectiveId, 0, size);
	}

	public Integer getStationIdFromCookie(HttpServletRequest request){
		Cookie[] cookies = request.getCookies();
		if(cookies != null)
			for (Cookie cookie : cookies) {
				if(cookie.getName().equals("stationId")){
					return Integer.parseInt(cookie.getValue());
				}
			}
		return null;
	}

	public List<Term> createTermTree(List<Term> allTerms){
		List<Term> roots = getRootTerms(allTerms);
		for (Term term : roots) {
			getChilds(term, allTerms);
		}
		return roots;
	}

	public Set<Term> getChilds(Term parent, List<Term> allTerms){
		cleanTerm(parent);
		parent.children = new HashSet<Term>();
		for (Term term : allTerms) {
			if(term.parent != null && parent.id.equals(term.parent.id)){
				parent.children.add(term);
				term.parent = null;
				term.cells = null;
				term.termPerspectives = null;
				term.posts = null;
			}
		}

		for (Term term: parent.children) {
			getChilds(term, allTerms);
		}

		return parent.children;
	}

	private List<Term> getRootTerms(List<Term> allTerms){
		List<Term> roots = new ArrayList<Term>();
		for (Term term : allTerms) {
			if(term.parent == null){
				roots.add(term);
			}
		}
		return roots;
	}

	private void cleanTerm(Term term){
		term.posts = null;
		term.rows = null;
		term.termPerspectives = null;
		term.cells = null;
		term.taxonomy = null;
	}

	public List<StationPermission> getStationPermissions(String baseUrl, Integer personId) {
		//Stations Permissions
		List<StationPermission> stationPermissionDtos = new ArrayList<>();
		try {
			List<Station> stations = stationRepository.findByPersonId(personId);
			for (Station station : stations) {
				StationPermission stationPermissionDto = new StationPermission();
				StationDto stationDto = new StationDto();
				stationDto.links = generateSelfLinks(baseUrl + "/api/stations/" + station.id);

				//Station Fields
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

				stationDto = objectMapper.readValue(objectMapper.writeValueAsString(station).getBytes("UTF-8"), StationDto.class);
				stationDto.links = generateSelfLinks(baseUrl + "/api/stations/" + station.id);
				//StationRoles Fields
				StationRole stationRole = stationRolesRepository.findByStationAndPersonId(station, personId);
				if(stationRole != null){
					stationPermissionDto.admin = stationRole.admin;
					stationPermissionDto.editor = stationRole.editor;
					stationPermissionDto.writer = stationRole.writer;
				}

				stationPermissionDtos.add(stationPermissionDto);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return stationPermissionDtos;
	}

	public List<Integer> getReadableStationIds(StationsPermissions permissions) {
		List<Integer> ids = new ArrayList<Integer>();
		if(permissions.stationPermissionDtos != null)
			for (StationPermission sp : permissions.stationPermissionDtos) {
				//if(sp.visibility.equals(Station.UNRESTRICTED) || sp.writer)
				ids.add(sp.stationId);
			}
		return ids;
	}
}
