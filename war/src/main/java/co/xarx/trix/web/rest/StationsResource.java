package co.xarx.trix.web.rest;

import co.xarx.trix.WordrailsService;
import co.xarx.trix.security.auth.TrixAuthenticationProvider;
import co.xarx.trix.domain.*;
import co.xarx.trix.persistence.*;
import co.xarx.trix.services.CacheService;
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
public class StationsResource {

	private
	@Context
	HttpServletRequest request;
	@Autowired
	private TrixAuthenticationProvider authProvider;
	private
	@Autowired
	NetworkRolesRepository networkRolesRepository;

	@Autowired
	StationRolesRepository stationRolesRepository;

	private
	@Autowired
	StationRepository stationRepository;

	private
	@Autowired
	StationPerspectiveRepository stationPerspectiveRepository;

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
	@Path("/{stationId}/setDefaultPerspective")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response setDefaultPerspective(@PathParam("stationId") Integer stationId, @FormParam("perspectiveId") Integer perspectiveId) {
		Person person = authProvider.getLoggedPerson();
		Station station = stationRepository.findOne(stationId);

		Network network = wordrailsService.getNetworkFromHost(request.getHeader("Host"));
		NetworkRole role = networkRolesRepository.findByNetworkAndPerson(network, person);

		StationRole sRole =  stationRolesRepository.findByStationAndPersonId(station, person.id);

		if ((role.admin || sRole.admin) && stationPerspectiveRepository.findOne(perspectiveId).stationId.equals(station.id)) {
			queryPersistence.updateDefaultPerspective(station.id, perspectiveId);
			cacheService.updateStation(station.id);
			return Response.status(Status.OK).build();
		} else return Response.status(Status.UNAUTHORIZED).build();
	}

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
