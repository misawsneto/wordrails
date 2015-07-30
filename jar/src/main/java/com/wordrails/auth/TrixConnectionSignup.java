package com.wordrails.auth;

import com.wordrails.business.Network;
import com.wordrails.business.Person;
import com.wordrails.business.User;
import com.wordrails.business.UserGrantedAuthority;
import com.wordrails.persistence.PersonRepository;
import com.wordrails.persistence.UserRepository;
import com.wordrails.util.WordrailsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UserProfile;
import org.springframework.stereotype.Component;


/**
 * This is executed when someones signup using any social network and an user is not found
 * Then we must create a new user
 */
@Deprecated
@Component
public class TrixConnectionSignup implements ConnectionSignUp {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PersonRepository personRepository;
	@Autowired
	private TrixAuthenticationProvider authProvider;

	@Override
	public String execute(Connection<?> connection) {
		UserProfile profile = connection.fetchUserProfile();

		int i = 1;
		String username = profile.getUsername();
		Network network = authProvider.getNetwork();
		while(userRepository.existsByUsernameAndNetworkId(username, network.id)) {
			username = profile.getUsername() + i++;
		}

		User user = new User();
		Person person = new Person();
		person.name = profile.getName();
		person.username = profile.getUsername();
		person.email = profile.getEmail();

		UserGrantedAuthority authority = new UserGrantedAuthority("ROLE_USER");
		authority.network = network;

		user.enabled = true;
		user.username = person.username;
		user.network = authority.network;
		authority.user = user;
		user.addAuthority(authority);

		person.user = user;

		personRepository.save(person);

		return null;
	}
}
