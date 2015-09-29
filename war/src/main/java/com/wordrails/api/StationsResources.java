package com.wordrails.api;

import com.wordrails.WordrailsService;
import com.wordrails.auth.TrixAuthenticationProvider;
import com.wordrails.business.Network;
import com.wordrails.business.NetworkRole;
import com.wordrails.business.Person;
import com.wordrails.business.Station;
import com.wordrails.persistence.NetworkRolesRepository;
import com.wordrails.persistence.QueryPersistence;
import com.wordrails.persistence.StationRepository;
import com.wordrails.services.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.HashSet;

@Path("/stations")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Component
public class StationsResources {

	private
	@Context
	HttpServletRequest request;
	@Autowired
	private TrixAuthenticationProvider authProvider;
	private
	@Autowired
	NetworkRolesRepository networkRolesRepository;
	private
	@Autowired
	StationRepository stationRepository;
	private
	@Autowired
	WordrailsService wordrailsService;
	private
	@Autowired
	CacheService cacheService;
	private
	@Autowired
	QueryPersistence queryPersistence;

	@PUT
	@Path("/{stationId}/setMainStation")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response setMainStation(@PathParam("stationId") Integer stationId, @FormParam("value") boolean value) {
		Person person = authProvider.getLoggedPerson();
		Network network = wordrailsService.getNetworkFromHost(request.getHeader("Host"));
		NetworkRole role = networkRolesRepository.findByNetworkAndPerson(network, person);

		if (role.admin) {
			network.stations = new HashSet<>(stationRepository.findByNetworkId(network.id));
			for (Station station : network.stations) {
				if (station.id.equals(stationId)) station.main = value;
				else station.main = false;

				queryPersistence.updateMainStation(station.id, station.main);
				cacheService.updateStation(station.id);
			}
			return Response.status(Status.OK).build();
		} else return Response.status(Status.UNAUTHORIZED).build();
	}
}
