package co.xarx.trix.web.rest;

import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.io.IOException;

@Path("/web")
@Component
public class WebResource {

	@GET
	@Path("/pages")
	public Response getPages() throws IOException {


		return Response.ok().build();
	}

	@POST
	@Path("/page")
	public Response postPage() {

		return Response.ok().build();
	}
}
