package co.xarx.trix.web.rest;

import co.xarx.trix.config.multitenancy.TenantContextHolder;
import co.xarx.trix.domain.File;
import co.xarx.trix.persistence.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;

@Path("/web")
@Component
public class WebResource {

	@Context
	private HttpServletRequest request;
	@Autowired
	private FileRepository personRepository;

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
