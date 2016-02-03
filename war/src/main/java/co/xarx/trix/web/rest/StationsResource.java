package co.xarx.trix.web.rest;

import co.xarx.trix.api.ContentResponse;
import co.xarx.trix.domain.Person;
import co.xarx.trix.domain.Station;
import co.xarx.trix.domain.StationRole;
import co.xarx.trix.persistence.*;
import co.xarx.trix.security.auth.TrixAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.List;

@Path("/stations")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Component
public class StationsResource {

	@Context
	private HttpServletRequest request;
	@Autowired
	private TrixAuthenticationProvider authProvider;
	@Autowired
	private StationRolesRepository stationRolesRepository;
	@Autowired
	private StationRepository stationRepository;
	@Autowired
	private StationPerspectiveRepository stationPerspectiveRepository;
	@Autowired
	private QueryPersistence queryPersistence;

	@PUT
	@Path("/{stationId}/setDefaultPerspective")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response setDefaultPerspective(@PathParam("stationId") Integer stationId, @FormParam("perspectiveId") Integer perspectiveId) {
		Person person = authProvider.getLoggedPerson();
		Station station = stationRepository.findOne(stationId);

		StationRole sRole =  stationRolesRepository.findByStationAndPersonId(station, person.id);

		if ((person.networkAdmin || sRole.admin) && stationPerspectiveRepository.findOne(perspectiveId).stationId.equals(station.id)) {
			queryPersistence.updateDefaultPerspective(station.id, perspectiveId);
			return Response.status(Status.OK).build();
		} else return Response.status(Status.UNAUTHORIZED).build();
	}

	@PUT
	@Path("/{stationId}/setMainStation")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response setMainStation(@PathParam("stationId") Integer stationId, @FormParam("value") boolean value) {
		Person person = authProvider.getLoggedPerson();

		if (person.networkAdmin) {
			for (Station station : stationRepository.findAll()) {
				station.main = station.id.equals(stationId) && value;

				stationRepository.save(station);
			}
			return Response.status(Status.OK).build();
		} else return Response.status(Status.UNAUTHORIZED).build();
	}

	@GET
	@Path("/stats/roles/count")
	public ContentResponse<Integer> countRolesByStationIds(@QueryParam("stationIds") List<Integer> stationIds, @QueryParam("q") String q){
		ContentResponse<Integer> resp = new ContentResponse<>();
		resp.content = 0;
		if(stationIds != null && !stationIds.isEmpty()) {
			if (q != null && !q.isEmpty()) {
				resp.content = stationRolesRepository.countRolesByStationIdsAndNameOrUsernameOrEmail(stationIds, q).intValue();
			}else {
				resp.content = stationRolesRepository.countRolesByStationIds(stationIds).intValue();
			}
		}else {
			throw new co.xarx.trix.exception.BadRequestException();
		}
		return resp;
	}
}
