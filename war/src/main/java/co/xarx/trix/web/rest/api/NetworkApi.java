package co.xarx.trix.web.rest.api;

import co.xarx.trix.annotations.IgnoreMultitenancy;
import co.xarx.trix.api.PersonPermissions;
import co.xarx.trix.api.ThemeView;
import co.xarx.trix.domain.Network;
import co.xarx.trix.domain.Person;
import co.xarx.trix.exception.BadRequestException;
import co.xarx.trix.exception.ConflictException;
import co.xarx.trix.util.ReadsCommentsRecommendsCount;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.TreeMap;

@Path("/networks")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface NetworkApi {

	@PUT
	@Path("/{id}")
	void putNetwork(@PathParam("id") Integer id) throws IOException;

	@Path("/{id}/permissions")
	@GET
	PersonPermissions getNetworkPersonPermissions(@PathParam("id") Integer id);

	@Path("/updateTheme")
	@PUT
	Response updateTheme (ThemeView themeView);

	@POST
	@IgnoreMultitenancy
	@Path("/createNetwork")
	Response createNetwork (NetworkCreateDto networkCreateDto)
			throws ConflictException, BadRequestException, IOException;

	@GET
	@Path("/publicationsCount")
	Response publicationsCount() throws IOException;

	@GET
	@Path("/stats")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	JsonStats networkStats(@QueryParam("date") String date,
										   @QueryParam("beggining") String beginning,
										   @QueryParam("postId") Integer postId) throws IOException;

	public class JsonStats {
		public Object generalStatsJson;
		public TreeMap<Long, ReadsCommentsRecommendsCount> dateStatsJson;
	}

	public class NetworkCreateDto extends Network {
		public String newSubdomain;
		public Person person;
	}

}
