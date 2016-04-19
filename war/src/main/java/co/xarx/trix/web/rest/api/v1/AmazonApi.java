package co.xarx.trix.web.rest.api.v1;

import org.springframework.security.access.prepost.PreAuthorize;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.io.IOException;

@Path("/amazon")
public interface AmazonApi {

	@GET
	@Path("/signedUrl")
	@PreAuthorize("permitAll()")
	Response generateSignedUrl(@QueryParam("hash") String hash,
							   @QueryParam("type") String type) throws IOException;
}
