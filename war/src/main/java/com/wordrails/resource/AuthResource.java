package com.wordrails.resource;

import com.fasterxml.jackson.databind.JsonNode;
import com.wordrails.auth.TrixAuthenticationProvider;
import com.wordrails.business.*;
import com.wordrails.persistence.PersonRepository;
import com.wordrails.persistence.UserConnectionRepository;
import com.wordrails.persistence.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.ImageType;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.social.support.URIBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.URI;
import java.util.HashSet;

@Path("/auth")
@Component
public class AuthResource {

	@Value("facebook.app.id")
	String facebookAppId;
	@Value("facebook.app.secret")
	String facebookAppSecret;
	@Autowired
	private TrixAuthenticationProvider authProvider;
	@Autowired
	private UserRepository userRepository;
	@Qualifier("userConnectionRepository")
	@Autowired
	private UserConnectionRepository userConnectionRepository;
	@Autowired
	private PersonRepository personRepository;

	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Path("/signin")
	public Response signin(@FormParam("providerId") String providerId, @FormParam("userId") String userId, @FormParam("accessToken") String accessToken) throws IOException {

		Network network = authProvider.getNetwork();

		UserConnection userConnection = userConnectionRepository.findByProviderIdAndProviderUserId(providerId, userId, network.id);

		Facebook facebook = new FacebookTemplate(accessToken);
		if (userConnection == null) {
			if (providerId.equals("facebook")) {
				Person person = getFacebookUser(facebook, userId, network);
				personRepository.save(person);
			}
		} else {
			if (providerId.equals("facebook")) {
				org.springframework.social.facebook.api.User profile = facebook.userOperations().getUserProfile(userId);

				if (profile == null) {
					return Response.status(Response.Status.UNAUTHORIZED).build();
				}
			}
		}

		return Response.status(Response.Status.OK).build();
	}

	private Person getFacebookUser(Facebook facebook, String userId, Network network) {
		org.springframework.social.facebook.api.User profile = facebook.userOperations().getUserProfile(userId);

		int i = 1;
		String username = profile.getId();
		while (userRepository.existsByUsernameAndNetworkId(username, network.id)) {
			username = profile.getId() + i++;
		}

		User user = new User();
		Person person = new Person();
		person.name = profile.getName();
		person.username = username;
		person.email = profile.getEmail();


		UserGrantedAuthority authority = new UserGrantedAuthority("ROLE_USER");
		authority.network = network;

		user.enabled = true;
		user.username = username;
		user.password = "";
		user.network = network;
		authority.user = user;
		user.addAuthority(authority);

		UserConnection userConnection = new UserConnection();
		userConnection.providerId = "facebook";
		userConnection.providerUserId = userId;
		userConnection.email = profile.getEmail();
		userConnection.user = user;
		userConnection.displayName = profile.getName();
		userConnection.profileUrl = profile.getLink();
		userConnection.imageUrl = fetchPictureUrl(userId, facebook, ImageType.LARGE);
		user.userConnections = new HashSet<>();
		user.userConnections.add(userConnection);

		person.user = user;

		return person;
	}

	private static final String GRAPH_API_URL = "http://graph.facebook.com/";

	public String fetchPictureUrl(String userId, Facebook facebook, ImageType imageType) {
		URI uri = URIBuilder.fromUri(GRAPH_API_URL + userId + "/picture" +
				"?type=" + imageType.toString().toLowerCase() + "&redirect=false").build();

		RestTemplate restTemplate = new RestTemplate();
		JsonNode response = restTemplate.getForObject(uri, JsonNode.class);
		return response.get("data").get("url").textValue();
	}
}
