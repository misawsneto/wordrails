package co.xarx.trix.web.rest.api.v2;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/v2/terms")
@Produces(MediaType.APPLICATION_JSON)
public interface V2TermsApi {

    @PUT
    @Path("/{id}/seen")
    Response setTermSeen(@PathParam("id") Integer id,
                      @QueryParam("timeReading") Integer timeReading,
                      @QueryParam("date") Long timestamp);
}
