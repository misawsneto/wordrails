package co.xarx.trix.web.rest.api.v2;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/v2/persons")
@Produces(MediaType.APPLICATION_JSON)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public interface V2PersonsApi {

	class StationsDto {
		public List<Integer> stationsIds;
	}

	@GET
	@Path("/search")
	Response searchPersons(@QueryParam("q") String query,
						   @QueryParam("username") List<String> usernames,
						   @QueryParam("email") List<String> emails,
						   @QueryParam("page") @DefaultValue("0") Integer page,
						   @QueryParam("size") @DefaultValue("10") Integer size,
						   @QueryParam("order") List<String> orders,
						   @QueryParam("embed") List<String> embeds);

	@GET
	@Path("/{username}/permissions/station/{stationId}")
	Response getPermissions(@PathParam("username") String username,
							@PathParam("stationId") Integer stationId);

	@GET
	@Path("/{username}/permissions/station")
	Response getPermissions(@PathParam("username") String username);

	@DELETE
	@Path("/{email}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	Response deletePerson(@PathParam("email") String username);

	@GET
	@Path("/{username}/timeline")
	Response getPersonTimeline(@PathParam("username") String username);

	@PUT
	@Path("/notify/stations")
	Response notifyStations(StationsDto stationsDto);

	@PUT
	@Path("/notify/all")
	Response notifyAllStaitons();

//	@GET
//	@Path("{username}/permissions/station")
//	Response getPermissions(@PathParam("username") String username);
}
