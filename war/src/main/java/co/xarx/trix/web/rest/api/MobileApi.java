package co.xarx.trix.web.rest.api;

import org.springframework.security.access.prepost.PreAuthorize;

import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/mobile")
@Consumes(MediaType.WILDCARD)
public interface MobileApi {

	@PUT
	@Path("/location")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@PreAuthorize("permitAll()")
	Response updateLocation(@NotNull @FormParam("deviceCode") String token, @NotNull @FormParam("device") String device,
							@FormParam("lat") Double lat, @FormParam("lng") Double lng);
}
