package co.xarx.trix.web.rest.resource;

import co.xarx.trix.config.multitenancy.TenantContextHolder;
import co.xarx.trix.domain.AuthCredential;
import co.xarx.trix.domain.Network;
import co.xarx.trix.persistence.NetworkRepository;
import co.xarx.trix.services.PasswordService;
import co.xarx.trix.services.auth.AuthService;
import co.xarx.trix.web.rest.AbstractResource;
import co.xarx.trix.web.rest.api.AuthApi;
import lombok.NoArgsConstructor;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.FacebookApi;
import org.scribe.builder.api.GoogleApi;
import org.scribe.model.Token;
import org.scribe.oauth.OAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.NotAllowedException;
import javax.ws.rs.core.Response;
import java.io.IOException;

@Component
@NoArgsConstructor
public class AuthResource extends AbstractResource implements AuthApi {

	private NetworkRepository networkRepository;
	private AuthService authProvider;
	private PasswordService passwordService;

	@Autowired
	public AuthResource(NetworkRepository networkRepository, AuthService authProvider, PasswordService passwordService) {
		this.networkRepository = networkRepository;
		this.authProvider = authProvider;
		this.passwordService = passwordService;
	}

	@Override
	public Response signin(String providerId, String userId, String accessToken) throws IOException {
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

	@Override
	public Response resetPassword(String email) {
		passwordService.resetPassword(email);
		return Response.status(Response.Status.OK).build();
	}

	@Override
	public Response updatePassword(String hash, String password) {
		passwordService.updatePassword(hash, password);
		return Response.status(Response.Status.OK).build();
	}
}
