package co.xarx.trix;

import co.xarx.trix.api.*;
import co.xarx.trix.domain.*;
import co.xarx.trix.persistence.*;
import co.xarx.trix.services.CacheService;
import co.xarx.trix.services.LogService;
import co.xarx.trix.web.rest.PerspectiveResource;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.apache.log4j.Logger;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.NotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Component
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
	private PostReadRepository postReadRepository;
	@Autowired
	private QueryPersistence queryPersistence;
	@Autowired
	private PostRepository postRepository;
	@Autowired
	private PerspectiveResource perspectiveResource;
	@Autowired
	private CacheService cacheService;
	@Autowired
	private StationRepository stationRepository;
	@Autowired
	private StationRolesRepository stationRolesRepository;

	@Autowired
	private LogService logService;

	private LoadingCache<PermissionId, StationsPermissions> stationsPermissions;

	public void init(){
		// ------------- init person cache
		stationsPermissions = CacheBuilder.newBuilder().maximumSize(1000)
				.expireAfterWrite(1, TimeUnit.MINUTES)
						//	       .removalListener(MY_LISTENER)
				.build(
						new CacheLoader<PermissionId, StationsPermissions>() {
							public StationsPermissions load(PermissionId id) {
								List<StationPermission> permissions = getStationPermissions(id.baseUrl, id.personId, id.networkId);
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

	public String getSubdomainFromHost(String host) {
		String[] names = host.split("\\.");
		String topDomain = names[names.length - 2] + "." + names[names.length - 1];
		return !topDomain.equals(host) ? host.split("." + topDomain)[0] : null;
	}

    @Autowired
    private HttpServletResponse response;

	public Network getNetworkFromHost(String host){
		Network network = null;
        String subdomain = getSubdomainFromHost(host);
        if(subdomain != null && !subdomain.isEmpty()){
            try {
                network = cacheService.getNetworkBySubdomain(subdomain);
            } catch (Exception e) {
                // no network found in cache or db.
            }
            if (network != null)
                return network;
        }

		try {
            network = cacheService.getNetworkByDomain(host);
            if(network != null)
			    return network;
            response.setStatus(400);
            return null;
		} catch (Exception e) {
            throw new NotFoundException();
		}
	}

	@Async
	@Transactional(readOnly = false)
	public void countPostRead(Post post, Person person, String sessionId){
		try {
			PostRead postRead = new PostRead();
			postRead.person = person;
			postRead.post = post;
			postRead.sessionid = "0"; // constraint fails if null
			if(postRead.person != null && postRead.person.username.equals("wordrails")) { // if user wordrails, include session to uniquely identify the user.
				postRead.person = null;
				postRead.sessionid = sessionId;
			}

			try {
				postReadRepository.save(postRead);
				queryPersistence.incrementReadsCount(post.id);
				logService.postRead(postRead);
			} catch (ConstraintViolationException | DataIntegrityViolationException e) {
				log.info("user already read this post");
			}
		} catch (Exception ex) {
            co.xarx.trix.util.Logger.error(ex.getMessage());
		}
	}

	@Async
	@Transactional(noRollbackFor = org.springframework.dao.DataIntegrityViolationException.class)
	public void countPostRead(Integer postId, Person person, String sessionId){
		PostRead postRead = new PostRead();
		postRead.person = person;
		postRead.post = postRepository.findOne(postId);
		postRead.sessionid = "0"; // constraint fails if null
		if(postRead.person != null && postRead.person.username.equals("wordrails")) { // if user wordrails, include session to uniquely identify the user.
			postRead.person = null;
			postRead.sessionid = sessionId;
		}
		try {
			postReadRepository.save(postRead);
			queryPersistence.incrementReadsCount(postId);
		} catch (org.springframework.dao.DataIntegrityViolationException ex) {
			ex.printStackTrace();
		}
	}

	@Async
	@org.springframework.transaction.annotation.Transactional(readOnly=true)
	public void sendResetEmail(PasswordReset passwordReset) {

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

	public List<StationPermission> getStationPermissions(String baseUrl, Integer personId, Integer networkId) {
		//Stations Permissions
		List<StationPermission> stationPermissionDtos = new ArrayList<>();
		try {
			List<Station> stations = stationRepository.findByPersonIdAndNetworkId(personId, networkId);
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

	public List<Integer> getWritableStationIds(StationsPermissions permissions) {
		List<Integer> ids = new ArrayList<Integer>();
		if(permissions.stationPermissionDtos != null)
			for (StationPermission sp : permissions.stationPermissionDtos) {
				if(sp.writable || sp.writer || sp.editor)
					ids.add(sp.stationId);
			}
		return ids;
	}

	@Async
	@Transactional
	public void updateLastLogin(String username) {
		queryPersistence.updateLastLogin(username);
	}

	@Async
	@Transactional
	public void deleteTaxonomyNetworks(Taxonomy taxonomy){
		taxonomyRepository.deleteTaxonomyNetworks(taxonomy.id);
	}
}
