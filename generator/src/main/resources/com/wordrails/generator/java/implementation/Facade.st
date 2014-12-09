Facade(package, facade, entities) ::=

<<package $package$;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

$entities:{entity | import $entity.fullName$;}; separator="\n"$
$entities:{entity | import $entity.repositoryFullName$;}; separator="\n"$

@Path("/batch/api")
@Component
public class $facade$ {
	private @Context HttpServletRequest request;
	private @Context UriInfo uriInfo;
	private @Context HttpServletResponse response;
	
	$entities:{entity | private @Autowired $entity.repositoryName$ $entity.nameLowercase$Dao;}; separator="\n"$

	private void foward() {
		String path = request.getServletPath() + uriInfo.getPath();		
		try {
			request.getServletContext().getRequestDispatcher(path).forward(request, response);
		} catch (ServletException | IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@POST
	@Path("/batch") 
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public Response postBatch(List<Command> batch) {
		List<Response> results = new ArrayList<>();
		for (Command command : batch) {
			Response result = command.execute(results, this);
			results.add(result);
			if (result.getStatus() == Status.NOT_FOUND.getStatusCode()) {
				return Response.status(Status.NOT_FOUND).build();
			}
		}
		return Response.status(Status.OK).build();
	}

$entities:FacadeEntityMethods(); separator="\n\n"$
}>>