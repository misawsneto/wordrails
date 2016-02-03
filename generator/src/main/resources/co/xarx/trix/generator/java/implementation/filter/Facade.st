Facade(package, facade, entities) ::=

<<package $package$;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.jboss.resteasy.spi.HttpRequest;
import org.springframework.stereotype.Component;

@Component
public abstract class AbstractAuthorizationFilter {
	private @Context HttpServletRequest httpServletRequest;
	private @Context UriInfo uriInfo;	
	private @Context HttpRequest httpRequest;
	
	private Response forward() {
		String path = httpServletRequest.getServletPath() + uriInfo.getPath();		
		httpRequest.forward(path);
		return null;
	}
	
	private Response forbidden() {
		return Response.status(Status.FORBIDDEN).build();
	}

$entities:FacadeEntityMethods(); separator="\n\n"$
}>>