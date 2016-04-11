package co.xarx.trix;

import co.xarx.trix.api.*;
import co.xarx.trix.domain.Station;
import co.xarx.trix.domain.Term;
import co.xarx.trix.persistence.PersonRepository;
import co.xarx.trix.persistence.TaxonomyRepository;
import co.xarx.trix.web.rest.PerspectiveResource;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Component("wordrailsService")
public class WordrailsService {

	@Autowired
	public ObjectMapper objectMapper;
	@Autowired
	public TaxonomyRepository taxonomyRepository;
	@Autowired
	public PersonRepository personRepository;
	@Autowired
	private PerspectiveResource perspectiveResource;

	public List<Link> generateSelfLinks(String self){
		Link link = new Link();
		link.href = self;
		link.rel = "self";
		return Collections.singletonList(link);
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

	public List<Integer> getReadableStationIds(StationsPermissions permissions) {
		List<Integer> ids = new ArrayList<>();
		if(permissions.stationPermissionDtos != null)
			for (StationPermission sp : permissions.stationPermissionDtos) {
				//if(sp.visibility.equals(Station.UNRESTRICTED) || sp.writer)
				ids.add(sp.stationId);
			}
		return ids;
	}
}
