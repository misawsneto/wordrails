package com.wordrails.api;

import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wordrails.business.Station;

@Component
public class PersonDataFilter implements Filter{
	
	private @Autowired PersonsResource personsResource;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest request = (HttpServletRequest) req;
		
		String requestUrl = request.getRequestURI();
		
		PersonData personData = personsResource.getInitialData(request);
		HttpSession session = request.getSession();
		
		req.setAttribute("personData", personsResource.mapper.writeValueAsString(personData));
		selectDefaultStation(personData);
		chain.doFilter(req, res);
	}

	@Override
	public void destroy() {
	}
	

//	this.selectCurrentStation = function(stations, changeToStationId){
//		var ret = null;
//		if(stations){
//			stations.forEach(function(station){
//				if(station.main && !changeToStationId){
//					ret = station;
//					return;
//				}
//			});
//
//			if(!ret){
//				for (var i = 0; i < stations.length; i++) {
//					var station = stations[i]
//					if(changeToStationId){
//						if(station.id == changeToStationId){
//							ret = station;
//						break; // exit foreach
//					}
//				}else{
//					ret = station;
//					break;
//				}
//			};}
//		}
//		return ret;
//	}
	
	private TermPerspectiveView selectDefaultStation(PersonData personData){
		
		/*List<StationPermission> stationPermissions = personData.personPermissions.stationPermissions;
		
		TermPerspectiveView termPerspectiveView;
		Station station;
		station.
		
		if(stationPermissions == null)
			return null;
			
		for (StationPermission stationPermission : stationPermissions) {
			if(stationPermission.main)
				return stationPermission.stationId;
		}
		
		for (StationPermission stationPermission : stationPermissions) {
			if(stationPermission.visibility.equals(Station.UNRESTRICTED))
				return stationPermission.stationId;
		}
		
		for (StationPermission stationPermission : stationPermissions) {
			if(stationPermission.visibility.equals(Station.RESTRICTED_TO_NETWORKS))
				return stationPermission.stationId;
		}
		
		for (StationPermission stationPermission : stationPermissions) {
			if(stationPermission.visibility.equals(Station.RESTRICTED))
				return stationPermission.stationId;
		}*/
		
		return null;
	}
	
	
}
