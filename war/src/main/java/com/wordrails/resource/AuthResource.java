package com.wordrails.resource;

import com.wordrails.auth.TrixAuthenticationProvider;
import com.wordrails.business.Network;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
	public Response signin(@FormParam("providerId") String providerId, @FormParam("userId") String userId, @FormParam("accessToken") String accessToken) throws IOException {
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
}
