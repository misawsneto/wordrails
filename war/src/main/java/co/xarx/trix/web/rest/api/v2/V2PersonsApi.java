package co.xarx.trix.web.rest.api.v2;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by misael on 4/24/2016.
 */
@Path("/v2/persons")
@Produces(MediaType.APPLICATION_JSON)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public interface V2PersonsApi {
	@GET
	@Path("/search")
	Response searchPersons(@QueryParam("q") String query,
						 @QueryParam("stationId") List<Integer> stations,
						 @QueryParam("page") @DefaultValue("0") Integer page,
						 @QueryParam("size") @DefaultValue("10") Integer size,
						 @QueryParam("order") List<String> orders,
						 @QueryParam("embed") List<String> embeds);
}
