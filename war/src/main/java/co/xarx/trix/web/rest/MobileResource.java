package co.xarx.trix.web.rest;

import co.xarx.trix.domain.MobileDevice;
import co.xarx.trix.domain.Person;
import co.xarx.trix.security.auth.TrixAuthenticationProvider;
import co.xarx.trix.services.MobileService;
import co.xarx.trix.util.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/mobile")
@Consumes(MediaType.WILDCARD)
@Component
public class MobileResource {

	@Autowired
	private TrixAuthenticationProvider authService;
	@Autowired
	private MobileService mobileService;

	@PUT
	@Path("/location")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response putToken(@FormParam("deviceCode") String token, @FormParam("lat") Double lat, @FormParam("lng") Double lng) {
		return updateMobile(token, lat, lng, MobileDevice.Type.APPLE);
	}

	public Response updateMobile(String token, Double lat, Double lng, MobileDevice.Type type) {
		Person person = authService.getLoggedPerson();
		Logger.info("Updating " + type.toString() + " device " + token + " for person " + person.id);
		mobileService.updateDevice(person, token, lat, lng, type);
		return Response.status(Response.Status.OK).build();
	}
}