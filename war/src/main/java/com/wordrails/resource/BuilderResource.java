package com.wordrails.resource;

import com.wordrails.WordrailsService;
import com.wordrails.builder.AndroidBuilder;
import com.wordrails.business.AndroidApp;
import com.wordrails.business.Network;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

@Path("/builder")
@Component
public class BuilderResource {

	@Autowired
	private WordrailsService wordrailsService;
	@Autowired
	private AndroidBuilder builder;

	@GET
	@Path("/generateApk")
	public Response getApk(@Context HttpServletRequest request) {
		Network network = wordrailsService.getNetworkFromHost(request.getHeader("Host"));

		AndroidApp androidApp = network.androidApp;

		try {
			builder.run("/opt/trix_android", androidApp, network.subdomain);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return Response.ok().build();
	}
}
