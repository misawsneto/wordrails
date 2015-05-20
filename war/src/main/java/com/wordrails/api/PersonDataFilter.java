package com.wordrails.api;

import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wordrails.business.Post;
import com.wordrails.business.Station;
import com.wordrails.converter.PostConverter;
import com.wordrails.persistence.PostRepository;
import com.wordrails.persistence.TermPerspectiveRepository;

@Component
public class PersonDataFilter implements Filter{

	private @Autowired PersonsResource personsResource;

	private @Autowired TermPerspectiveRepository termPerspectiveRepository;

	private @Autowired PerspectiveResource perspectiveResource;
	
	private @Autowired PostRepository postRepository;
	
	private @Autowired PostConverter postConverter;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	@Transactional(readOnly=true)
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) req;

		Integer stationPerspectiveId = getPerspectiveFromCookie(request);
		PersonData personData = personsResource.getInitialData(request);
		
		StationDto defaultStation = getDefaultStation(personData);
		if(stationPerspectiveId == null){
			stationPerspectiveId = defaultStation.defaultPerspectiveId;
		}

		TermPerspectiveView termPerspectiveView = getDefaultPerspective(stationPerspectiveId);
		
		Pageable pageable = new PageRequest(0, 15);

		List<Post> unread = postRepository.findUnreadByStationAndPerson(1, 1, pageable);
		List<Post> popular = postRepository.findPopularPosts(defaultStation.id, pageable);
		List<Post> recent = postRepository.findPostsOrderByDateDesc(defaultStation.id, pageable);
		
		personData.unread = postConverter.convertToViews(unread);
		personData.popular = postConverter.convertToViews(popular);
		personData.recent = postConverter.convertToViews(recent);

		req.setAttribute("personData", personsResource.mapper.writeValueAsString(personData));
		req.setAttribute("termPerspectiveView", personsResource.mapper.writeValueAsString(termPerspectiveView));
		req.setAttribute("networkName", personData.network.name);
		
		chain.doFilter(req, res);
	}

	@Override
	public void destroy() {
	}
	
	private StationDto getDefaultStation(PersonData personData){
		List<StationPermission> stationPermissions = personData.personPermissions.stationPermissions;

		Integer stationId = 0;

		if(stationPermissions == null)
			return null;

		for (StationPermission stationPermission : stationPermissions) {
			if(stationPermission.main)
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
			if(stationId == station.id){
				return station;
			}
		}
		
		return null;
	}

	private TermPerspectiveView getDefaultPerspective(Integer stationPerspectiveId) {
		return perspectiveResource.getTermPerspectiveView(null, null, stationPerspectiveId, 0, 15);
	}

	private Integer getPerspectiveFromCookie(HttpServletRequest request){
		Cookie[] cookies = request.getCookies();
		if(cookies != null)
			for (Cookie cookie : cookies) {
				if(cookie.getName().equals("stationPerspectiveId")){
					return Integer.parseInt(cookie.getValue());
				}
			}
		return null;
	}
}
