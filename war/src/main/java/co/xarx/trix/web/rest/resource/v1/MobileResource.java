package co.xarx.trix.web.rest.resource.v1;

import co.xarx.trix.domain.MobileDevice;
import co.xarx.trix.domain.Person;
import co.xarx.trix.services.MobileService;
import co.xarx.trix.services.security.AuthService;
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
		return updateMobile(token, lat, lng, device.equals("apple") ? MobileDevice.Type.APPLE : MobileDevice.Type.ANDROID);
	}

	private Response updateMobile(String token, Double lat, Double lng, MobileDevice.Type type) {
		Person person = authService.getLoggedPerson();
		Logger.info("Updating " + type.toString() + " device " + token + " for person " + person.id);
		mobileService.updateDevice(person, token, lat, lng, type);
		return Response.status(Response.Status.OK).build();
	}
}