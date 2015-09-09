package com.wordrails.resource;

import com.wordrails.auth.TrixAuthenticationProvider;
import com.wordrails.business.Network;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

@Path("/auth")
@Component
public class AuthResource {

	@Autowired
	private TrixAuthenticationProvider authProvider;

	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Path("/signin")
	public Response signin(@FormParam("provider") String providerId, @FormParam("userId") String userId, @FormParam("accessToken") String accessToken) throws IOException {
		Network network = authProvider.getNetwork();

		if (!network.allowSocialLogin) {
			return Response.status(Response.Status.NOT_ACCEPTABLE).build();
		}

		boolean authorized = authProvider.socialAuthentication(providerId, userId, accessToken, network);

		if (authorized) {
			return Response.status(Response.Status.OK).build();
		}

		return Response.status(Response.Status.UNAUTHORIZED).build();
	}

	final String TWITTER_CONSUMER_KEY      = "xNbHA2FU662x6I2HUEz09quRf";
	final String TWITTER_CONSUMER_SECRET   = "F7OpmJd20QUAYTW3irutJ3NpPvKFlw4pm9bfoACbDfyzqcXJ3R";
	final String TWITTER_REQUEST_URL       = "https://api.twitter.com/oauth/request_token";
	final String TWITTER_ACCESS_URL        = "https://api.twitter.com/oauth/access_token";
	final String TWITTER_AUTHORIZE_URL     = "https://api.twitter.com/oauth/authorize";

//	@POST
//	@Consumes
}
