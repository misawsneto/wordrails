package co.xarx.trix.web.rest;

import co.xarx.trix.config.multitenancy.TenantContextHolder;
import co.xarx.trix.domain.Network;
import co.xarx.trix.persistence.NetworkRepository;
import co.xarx.trix.security.auth.TrixAuthenticationProvider;
import co.xarx.trix.services.PasswordService;
import org.hibernate.metamodel.relational.IllegalIdentifierException;
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
	private TrixAuthenticationProvider authProvider;
	@Autowired
	private PasswordService passwordService;

	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Path("/signin")
	public Response signin(@FormParam("provider") String providerId, @FormParam("userId") String userId, @FormParam("accessToken") String accessToken) throws IOException {
		Network network = networkRepository.findByTenantId(TenantContextHolder.getCurrentTenantId());

		boolean allowSocialLogin = true;
		OAuthService service = null;
		Token token = null;
		switch (providerId) {
			case "facebook":
				allowSocialLogin = network.isFacebookLoginAllowed();
				service = new ServiceBuilder()
						.provider(FacebookApi.class)
						.apiKey(network.facebookAppID)
						.apiSecret(network.facebookAppSecret)
						.build();
				token = new Token(accessToken, network.facebookAppSecret);
				break;
			case "google":
				allowSocialLogin = network.isGoogleLoginAllowed();
				service = new ServiceBuilder()
						.provider(GoogleApi.class)
						.apiKey(network.googleAppID)
						.apiSecret(network.googleAppSecret)
						.build();
				token = new Token(accessToken, network.googleAppSecret);
				break;
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

	@POST
	@Path("/forgotPassword")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response resetePassword(@FormParam("email") String email){
		try{
			passwordService.resetPassword(email);
		} catch (IllegalArgumentException e){
			return Response.status(Response.Status.BAD_REQUEST).build();
		}

		return Response.status(Response.Status.OK).build();
	}

	@PUT
	@Path("/{hash}")
	public Response updatePassword(@PathParam("hash") String hash, @FormParam("password") String password){
		try{
			passwordService.updatePassword(hash, password);
		} catch (IllegalIdentifierException e){
			return Response.status(Response.Status.BAD_REQUEST).build();
		}

		return Response.status(Response.Status.OK).build();
	}
}
