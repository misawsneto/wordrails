package co.xarx.trix.web.rest.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/builder")
public interface BuilderApi {

	@GET
	@Path("/generateApk")
	Response getApk();
}
