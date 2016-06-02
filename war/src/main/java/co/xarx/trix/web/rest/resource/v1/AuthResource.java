package co.xarx.trix.web.rest.resource.v1;

import co.xarx.trix.config.multitenancy.TenantContextHolder;
import co.xarx.trix.domain.AuthCredential;
import co.xarx.trix.persistence.AuthCredentialRepository;
import co.xarx.trix.services.PasswordService;
import co.xarx.trix.services.security.AuthService;
import co.xarx.trix.web.rest.AbstractResource;
import co.xarx.trix.web.rest.api.v1.AuthApi;
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

	private AuthCredentialRepository authCredentialRepository;
	private AuthService authProvider;
	private PasswordService passwordService;

	@Autowired
	public AuthResource(AuthCredentialRepository authCredentialRepository, AuthService authProvider, PasswordService passwordService) {
		this.authCredentialRepository = authCredentialRepository;
		this.authProvider = authProvider;
		this.passwordService = passwordService;
	}

	@Override
	public Response signin(String providerId, String userId, String accessToken) throws IOException {
		AuthCredential credential = authCredentialRepository.findAuthCredentialByTenantId(TenantContextHolder.getCurrentTenantId());

		if(credential == null) {
			throw new NotAllowedException("This network does not support social login");
		}


		boolean allowSocialLogin = true;
		OAuthService service = null;
		Token token = null;
		if (providerId.equals("facebook")) {
			allowSocialLogin = credential.isFacebookLoginAllowed();
			service = new ServiceBuilder()
					.provider(FacebookApi.class)
					.apiKey(credential.getFacebookAppID())
					.apiSecret(credential.getFacebookAppSecret())
					.build();
			token = new Token(accessToken, credential.getFacebookAppSecret());
		} else if (providerId.equals("google")) {
			allowSocialLogin = credential.isGoogleLoginAllowed();
			service = new ServiceBuilder()
					.provider(GoogleApi.class)
					.apiKey(credential.getGoogleAppID())
					.apiSecret(credential.getGoogleAppSecret())
					.build();
			token = new Token(accessToken, credential.getGoogleAppSecret());
		}

		if (!allowSocialLogin) {
			return Response.status(Response.Status.NOT_ACCEPTABLE).build();
		}

		if(token == null) {
			return Response.status(Response.Status.BAD_REQUEST).entity("Invalid provider ID").build();
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
