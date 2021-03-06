package co.xarx.trix.web.rest.api.v1;

import co.xarx.trix.annotation.IgnoreMultitenancy;
import co.xarx.trix.api.PersonPermissions;
import co.xarx.trix.api.ThemeView;
import co.xarx.trix.domain.Network;
import co.xarx.trix.domain.Person;
import co.xarx.trix.exception.BadRequestException;
import co.xarx.trix.exception.ConflictException;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;

@Path("/networks")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface NetworkApi {

	@GET
	@Path("/")
	void getNetworks() throws IOException;

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
	@Path("/createNetwork")
	Response createNetwork (NetworkCreateDto networkCreateDto)
			throws ConflictException, BadRequestException, IOException;

	@GET
	@Path("/publicationsCount")
	Response publicationsCount() throws IOException;


	@GET
	@Path("/stats")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	Response networkStats(@QueryParam("date") String date,
						   @QueryParam("beggining") String beginning) throws IOException;

	@GET
	@Path("/invitationTemplate")
	/**
	 * Get the default invitation html template taking in to account the invitationMessage set by the admin at
	 * configuration screen.
	 */
	Response getNetworkInvitationTemplate();

	@GET
	@Path("/validationTemplate")
	/**
	 * Get the default validation html template taking in to account the validationMessage set by the admin at
	 * configuration screen.
	 */
	Response getNetworkValidationTemplate();

	 class NetworkCreateDto extends Network {
		public String newSubdomain;
		public Person person;
	}

}
