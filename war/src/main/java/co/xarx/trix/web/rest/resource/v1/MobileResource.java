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
	public Response updateLocation(String token, String device, Double lat, Double lng, Boolean fcm) {
		String userAgent = request.getHeader("User-Agent");

		Constants.MobilePlatform platform;

		if (fcm != null && fcm && isAndroid2(device)){
			platform = Constants.MobilePlatform.FCM_ANDROID2;
		} else if (fcm != null && fcm && isIOs2(device)){
			platform = Constants.MobilePlatform.FCM_APPLE2;
		} else if (fcm != null && fcm && isAndroid(userAgent, device)){
			platform = Constants.MobilePlatform.FCM_ANDROID;
		} else if (fcm != null && fcm && isIOs(userAgent, device)) {
			platform = Constants.MobilePlatform.FCM_APPLE;
		} else if (isIOs(userAgent, device)) {
			platform = Constants.MobilePlatform.APPLE;
		} else if(isAndroid(userAgent, device)) {
			platform = Constants.MobilePlatform.ANDROID;
		} else {
			return Response.status(Response.Status.BAD_REQUEST).entity("Invalid device").build();
		}

		return updateMobile(token, lat, lng, platform);
	}

	private boolean isIOs(String userAgent, String device){
		return device.equals("apple") || device.equals("ios") || userAgent.contains("WordRailsIOSClient");
	}

	private boolean isAndroid(String userAgent, String device){
		return device.equals("android") || userAgent.contains("OkHttp");
	}

	private boolean isIOs2(String device){
		return device.equals("apple2") || device.equals("ios2");
	}

	private boolean isAndroid2(String device){
		return device.equals("android2");
	}

	private Response updateMobile(String token, Double lat, Double lng, Constants.MobilePlatform type) {
		Person person = authService.getLoggedPerson();
		Logger.info("Updating " + type.toString() + " device " + token + " for person " + person.id);
		mobileService.updateDevice(person.getId(), token, lat, lng, type);
		return Response.status(Response.Status.OK).build();
	}
}