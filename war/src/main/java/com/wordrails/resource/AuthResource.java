package com.wordrails.resource;

import com.wordrails.auth.SocialUsersConnectionRepository;
import com.wordrails.auth.TrixAuthenticationProvider;
import com.wordrails.auth.TrixConnectionSignup;
import com.wordrails.business.*;
import com.wordrails.persistence.PersonRepository;
import com.wordrails.persistence.UserRepository;
import com.wordrails.util.WordrailsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.HashSet;

@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Component
public class AuthResource {

	@Autowired
	private TrixAuthenticationProvider authProvider;
	@Autowired
	private TrixConnectionSignup trixConnectionSignup;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PersonRepository personRepository;

	@POST
	@Path("/signup/{providerId}")
	public Person signup(@PathParam("providerId") String providerId, UserConnection userConnection) throws IOException {
		userConnection.providerId = providerId;

		int i = 1;
		String username = userConnection.providerUserId;
		Network network = authProvider.getNetwork();
		while(userRepository.existsByUsernameAndNetworkId(username, network.id)) {
			username = userConnection.providerUserId + i++;
		}

		User user = new User();
		Person person = new Person();
		person.name = userConnection.displayName;
		person.username = userConnection.providerUserId;
		person.email = userConnection.email;

		UserGrantedAuthority authority = new UserGrantedAuthority("ROLE_USER");
		authority.network = authProvider.getNetwork();

		user.enabled = true;
		user.username = person.username;
		user.network = authority.network;
		authority.user = user;
		user.addAuthority(authority);
		user.userConnections = new HashSet<>();
		user.userConnections.add(userConnection);

		person.user = user;

		personRepository.save(person);

		return person;
	}

	@POST
	@Path("/signin/{providerId}")
	public Person signin(@PathParam("providerId") String providerId, @FormParam("userId") String userId) throws IOException {
		authProvider.socialAuthentication(providerId, userId, authProvider.getNetwork());

		return authProvider.getLoggedPerson();
	}

}
