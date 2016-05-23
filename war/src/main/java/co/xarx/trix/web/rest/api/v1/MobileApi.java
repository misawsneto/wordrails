package co.xarx.trix.web.rest.api.v1;

import org.springframework.security.access.prepost.PreAuthorize;

import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

@Path("/")
@Consumes(MediaType.WILDCARD)
public interface MobileApi {

	@PUT
	@Path("/mobile/location")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@PreAuthorize("permitAll()")
	Response updateLocation(@NotNull @FormParam("deviceCode") String token, @NotNull @FormParam("device") String device,
							@FormParam("lat") Double lat, @FormParam("lng") Double lng);

	@PUT
	@Path("/persons/me/location")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@PreAuthorize("permitAll()")
	void updateLocation2(@NotNull @FormParam("deviceCode") String token, @NotNull @FormParam("device") String
			device,
							@FormParam("lat") Double lat, @FormParam("lng") Double lng) throws IOException;
}
