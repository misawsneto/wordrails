package co.xarx.trix.web.rest.resource.v1;

import co.xarx.trix.api.v2.OAuthResponseData;
import co.xarx.trix.config.multitenancy.TenantContextHolder;
import co.xarx.trix.domain.AuthCredential;
import co.xarx.trix.exception.BadRequestException;
import co.xarx.trix.persistence.AuthCredentialRepository;
import co.xarx.trix.services.PasswordService;
import co.xarx.trix.services.security.Authenticator;
import co.xarx.trix.web.rest.AbstractResource;
import co.xarx.trix.web.rest.api.v1.AuthApi;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.ws.rs.NotAllowedException;
import javax.ws.rs.core.Response;
import java.io.IOException;

@Component
@NoArgsConstructor
public class AuthResource extends AbstractResource implements AuthApi {

	private AuthCredentialRepository authCredentialRepository;
	private Authenticator authenticator;
	private PasswordService passwordService;

	@Autowired
	public AuthResource(AuthCredentialRepository authCredentialRepository, Authenticator authenticator, PasswordService passwordService) {
		this.authCredentialRepository = authCredentialRepository;
		this.authenticator = authenticator;
		this.passwordService = passwordService;
	}

	@Override
	@Transactional
	public Response signin(String providerId, String userId, String accessToken) throws IOException {
		if (userId == null || "".equals(userId)) {
			if ("facebook".equals(providerId)) {
				RestTemplate rq = new RestTemplate();
				OAuthResponseData authResponse = rq.getForObject("https://graph.facebook.com/me?access_token=" + accessToken,
						OAuthResponseData.class);
				userId = authResponse.id;
			}else if("google".equals(providerId)){
				RestTemplate rq = new RestTemplate();
				OAuthResponseData authResponse = rq.getForObject("https://www.googleapis.com/plus/v1/people/me?access_token=" + accessToken,
						OAuthResponseData.class);
				userId = authResponse.id;
			}
		}

		AuthCredential credential = authCredentialRepository.findAuthCredentialByTenantId(TenantContextHolder.getCurrentTenantId());
//		credential.network = null;

		if(credential == null) {
			throw new NotAllowedException("This network does not support social login");
		}

		boolean allowSocialLogin = true;
		String appId = null;
		String appSecret = null;
		if (providerId.equals("facebook")) {
			allowSocialLogin = credential.isFacebookLoginAllowed();
			appId = credential.getFacebookAppID();
			appSecret = credential.getFacebookAppSecret();
		} else if (providerId.equals("google")) {
			allowSocialLogin = credential.isGoogleLoginAllowed();
			appId = credential.getGoogleAppID();
			appSecret = credential.getGoogleAppSecret();
		}

		if (!allowSocialLogin) {
			return Response.status(Response.Status.NOT_ACCEPTABLE).build();
		}

		boolean authorized;

		try {
			authorized = authenticator.socialAuthentication(providerId, userId, appId, appSecret, accessToken);
		} catch (BadCredentialsException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
		}

		if (authorized) {
			return Response.status(Response.Status.OK).build();
		}

		return Response.status(Response.Status.UNAUTHORIZED).build();
	}

	@Override
	public Response resetPassword(String email) {
		if(passwordService.resetPassword(email) == null){
			return Response.status(Response.Status.NOT_FOUND).entity("User not found").build();
		}
		return Response.status(Response.Status.OK).build();
	}

	@Override
	public Response updatePassword(String hash, String password) {
		try {
			passwordService.updatePassword(hash, password);
			return Response.status(Response.Status.OK).build();
		} catch (BadRequestException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}
}
