package com.wordrails.api;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wordrails.WordrailsService;
import com.wordrails.business.AccessControllerUtil;
import com.wordrails.business.Network;
import com.wordrails.business.NetworkRole;
import com.wordrails.business.Person;
import com.wordrails.business.Station;
import com.wordrails.persistence.NetworkRolesRepository;
import com.wordrails.persistence.QueryPersistence;
import com.wordrails.persistence.StationRepository;
import com.wordrails.services.CacheService;

@Path("/stations")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Component
public class StationsResources {
	
	private @Context HttpServletRequest request;
	private @Autowired AccessControllerUtil accessControllerUtil;
	private @Autowired NetworkRolesRepository networkRolesRepository;
	private @Autowired StationRepository stationRepository;
	private @Autowired WordrailsService wordrailsService;
	private @Autowired CacheService cacheService;
	private @Autowired QueryPersistence queryPersistence;

	@PUT
	@Path("/{stationId}/setMainStation")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response setMainStation(@PathParam("stationId") Integer stationId, @FormParam("value") boolean value){
		Person person = accessControllerUtil.getLoggedPerson();
		Network network = wordrailsService.getNetworkFromHost(request);
		NetworkRole role = networkRolesRepository.findByNetworkAndPerson(network, person);
		
		if(role.admin){
			network.stations = new HashSet<Station>(stationRepository.findByNetworkId(network.id));
			for (Station station : network.stations) {
				if(station.id.equals(stationId))
					station.main = value;
				else
					station.main = false;
				
				queryPersistence.updateMainStation(station.id, station.main);
				cacheService.updateStation(station.id);
			}
			return Response.status(Status.OK).build();
		}else
			return Response.status(Status.UNAUTHORIZED).build();
	}
}
