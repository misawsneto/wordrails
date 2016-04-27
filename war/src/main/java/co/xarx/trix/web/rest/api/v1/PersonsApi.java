package co.xarx.trix.web.rest.api.v1;

import co.xarx.trix.api.*;
import co.xarx.trix.domain.Person;
import co.xarx.trix.domain.StationRole;
import co.xarx.trix.exception.ConflictException;
import co.xarx.trix.util.StatsJson;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;

@Path("/persons")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface PersonsApi {

	public static class PersonCreateDto {
		public String name;
		public String username;
		public String email;
		public String password;
		public boolean emailNotification;
		public List<StationRole> stationsRole;
	}

	@GET
	@Path("/")
	@Transactional
	void getPersons() throws  IOException;

	@GET
	@Path("/{id}")
	@Transactional
	Response findPerson(@PathParam("id") Integer id) throws IOException;

	@GET
	@Path("/search/findByUsername")
	@Transactional
	Response findPersonByUsername() throws IOException;

	@PUT
	@Path("/update")
	@Transactional
	Response update(Person person);

	@PUT
	@Path("/{id}")
	@Transactional
	void updatePerson(@PathParam("id") Integer id) throws ServletException, IOException;

	@Deprecated
	@PUT
	@Path("/me/regId")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	Response putRegId(@FormParam("regId") String regId,
					  @FormParam("networkId") Integer networkId,
					  @FormParam("lat") Double lat,
					  @FormParam("lng") Double lng);

	@Deprecated
	@PUT
	@Path("/me/token")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	Response putToken(@FormParam("token") String token,
					  @FormParam("networkId") Integer networkId,
					  @FormParam("lat") Double lat,
					  @FormParam("lng") Double lng);

	@POST
	@Path("/tokenSignin")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	Response tokenSignin(@FormParam("token") String token);

	@GET
	@Path("/{personId}/posts")
	ContentResponse<List<PostView>>getPersonNetworkPosts(@PathParam("personId") Integer personId,
														 @QueryParam("networkId") Integer networkId,
														 @QueryParam("page") int page,
														 @QueryParam("size") int size) throws ServletException, IOException;

	@GET
	@Path("/getPostsByState")
	ContentResponse<List<PostView>> getPersonNetworkPostsByState(@QueryParam("personId") Integer personId,
																 @QueryParam("state") String state,
																 @QueryParam("page") int page,
																 @QueryParam("size") int size) throws ServletException, IOException;

	@GET
	@Path("/me")
	void getCurrentPerson() throws ServletException, IOException;

	@PUT
	@Transactional
	@Path("/me/password")
	@PreAuthorize("isAuthenticated()")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	void putPassword(@FormParam("oldPassword") String oldPassword, @FormParam("newPassword") String newPassword);

	@POST
	@Path("/create")
	Response signUp(PersonCreateDto dto) throws ConflictException, BadRequestException, IOException;

	@GET
	@Path("/stats/count")
	ContentResponse<Integer> countPersonsByNetwork(@QueryParam("q") String q);

	@PUT
	@Path("/deleteMany/network")
	@Consumes(MediaType.APPLICATION_JSON)
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	Response deleteMany(List<Integer> personIds);

	@PUT
	@Path("/{personId}/disable")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	Response disablePerson(@PathParam("personId") Integer personId);

	@PUT
	@Path("/{personId}/enable")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	Response enablePerson(@PathParam("personId") Integer personId);

	@PUT
	@Transactional
	@Path("/updateStationRoles")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	Response updateStationRoles(StationRolesUpdate stationRolesUpdate);

	@PUT
	@Path("/enable/all")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	Response enablePerson(IdsList idsList);

	@PUT
	@Path("/disable/all")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	Response disablePerson(IdsList idsList);

	@GET
	@Path("/allInit")
	PersonData getAllInitData() throws IOException;

	@GET
	@Path("/init")
	PersonData getInitialData() throws IOException;

	@GET
	@Path("/me/publicationsCount")
	@PreAuthorize("isAuthenticated()")
	Response publicationsCount(@QueryParam("personId") Integer personId) throws IOException;

	@GET
	@Path("/me/stats")
	@PreAuthorize("isAuthenticated()")
	StatsJson personStats(@QueryParam("date") String date, @QueryParam("postId") Integer postId) throws IOException;
}
