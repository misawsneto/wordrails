package co.xarx.trix.web.rest.resource.v1;

import co.xarx.trix.domain.Person;
import co.xarx.trix.services.MobileService;
import co.xarx.trix.services.security.AuthService;
import co.xarx.trix.util.Constants;
import co.xarx.trix.util.Logger;
import co.xarx.trix.web.rest.AbstractResource;
import co.xarx.trix.web.rest.api.v1.MobileApi;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;

@Component
@NoArgsConstructor
public class MobileResource extends AbstractResource implements MobileApi {

	private AuthService authService;
	private MobileService mobileService;

	@Autowired
	public MobileResource(AuthService authService, MobileService mobileService) {
		this.authService = authService;
		this.mobileService = mobileService;
	}

	@Override
	public Response updateLocation(String token, String device, Double lat, Double lng) {
		String userAgent = request.getHeader("User-Agent");

		Constants.MobilePlatform platform;
		if (device.equals("apple") || userAgent.contains("WordRailsIOSClient"))
			platform = Constants.MobilePlatform.APPLE;
		else if(device.equals("android") || userAgent.contains("OkHttp"))
			platform = Constants.MobilePlatform.ANDROID;
		else
			return Response.status(Response.Status.BAD_REQUEST).entity("Invalid device").build();

		return updateMobile(token, lat, lng, platform);
	}

	private Response updateMobile(String token, Double lat, Double lng, Constants.MobilePlatform type) {
		Person person = authService.getLoggedPerson();
		Logger.info("Updating " + type.toString() + " device " + token + " for person " + person.id);
		mobileService.updateDevice(person.getId(), token, lat, lng, type);
		return Response.status(Response.Status.OK).build();
	}
}