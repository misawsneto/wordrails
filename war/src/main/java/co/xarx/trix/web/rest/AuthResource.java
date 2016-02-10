package co.xarx.trix.web.rest;

import co.xarx.trix.config.multitenancy.TenantContextHolder;
import co.xarx.trix.domain.AuthCredential;
import co.xarx.trix.domain.Network;
import co.xarx.trix.persistence.NetworkRepository;
import co.xarx.trix.services.auth.AuthService;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.FacebookApi;
import org.scribe.builder.api.GoogleApi;
import org.scribe.model.Token;
import org.scribe.oauth.OAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

@Path("/auth")
@Component
public class AuthResource {

	@Autowired
	private NetworkRepository networkRepository;
	@Autowired
	private AuthService authProvider;

	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Path("/signin")
	public Response signin(@FormParam("provider") String providerId, @FormParam("userId") String userId, @FormParam("accessToken") String accessToken) throws IOException {
		Network network = networkRepository.findByTenantId(TenantContextHolder.getCurrentTenantId());

		if(network.authCredential == null) {
			throw new NotAllowedException("This network does not support social login");
		}

		boolean allowSocialLogin = true;
		AuthCredential oauthCredential = network.authCredential;
		OAuthService service = null;
		Token token = null;
		if (providerId.equals("facebook")) {
			allowSocialLogin = oauthCredential.isFacebookLoginAllowed();
			service = new ServiceBuilder()
					.provider(FacebookApi.class)
					.apiKey(oauthCredential.getFacebookAppID())
					.apiSecret(oauthCredential.getFacebookAppSecret())
					.build();
			token = new Token(accessToken, oauthCredential.getFacebookAppSecret());
		} else if (providerId.equals("googleweb")) {
			allowSocialLogin = oauthCredential.isGoogleWebLoginAllowed();
			service = new ServiceBuilder()
					.provider(GoogleApi.class)
					.apiKey(oauthCredential.getGoogleWebAppID())
					.apiSecret(oauthCredential.getGoogleWebAppSecret())
					.build();
			token = new Token(accessToken, oauthCredential.getGoogleWebAppSecret());
		} else if (providerId.equals("googleandroid")) {
			allowSocialLogin = oauthCredential.isGoogleAndroidLoginAllowed();
			service = new ServiceBuilder()
					.provider(GoogleApi.class)
					.apiKey(oauthCredential.getGoogleAndroidAppID())
					.apiSecret(oauthCredential.getGoogleAndroidAppSecret())
					.build();
			token = new Token(accessToken, oauthCredential.getGoogleAndroidAppSecret());
		} else if (providerId.equals("googleios")) {
			allowSocialLogin = oauthCredential.isGoogleAppleLoginAllowed();
			service = new ServiceBuilder()
					.provider(GoogleApi.class)
					.apiKey(oauthCredential.getGoogleAppleAppID())
					.apiSecret(oauthCredential.getGoogleAppleAppSecret())
					.build();
			token = new Token(accessToken, oauthCredential.getGoogleAppleAppSecret());
		}

		if (!allowSocialLogin) {
			return Response.status(Response.Status.NOT_ACCEPTABLE).build();
		}

		boolean authorized = authProvider.socialAuthentication(providerId, service, userId, token);

		if (authorized) {
			return Response.status(Response.Status.OK).build();
		}

		return Response.status(Response.Status.UNAUTHORIZED).build();
	}
}
