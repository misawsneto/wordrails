package com.wordrails.auth;

import com.wordrails.business.Network;
import com.wordrails.business.Person;
import com.wordrails.business.User;
import com.wordrails.persistence.NetworkRepository;
import com.wordrails.persistence.PersonRepository;
import com.wordrails.persistence.UserRepository;
import com.wordrails.services.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ExecutionException;


@Component
public class TrixAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private PersonRepository personRepository;
	@Autowired
	private NetworkRepository networkRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private CacheService cacheService;


	@Override
	public Authentication authenticate(Authentication auth) throws AuthenticationException {
		User user = (User) auth.getPrincipal();
		String password = (String) auth.getCredentials();

		if (user == null) {
			throw new BadCredentialsException("Person is null");
		}

		if (password == null || !password.equals(user.password)) {
			throw new BadCredentialsException("Wrong password");
		}

		return new UsernamePasswordAuthenticationToken(user, password, user.getAuthorities());
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return (UsernamePasswordAuthenticationToken.class.equals(authentication));
	}

	public User getUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		return (User) auth.getPrincipal();
	}

	public Network getNetwork() {
		return getUser().network;
	}

	public Integer getNetworkId() {
		if(getUser().isAnonymous() || getUser().network == null)
			return 0;

		return getUser().network.id;
	}

	public Person getLoggedPerson() {
		User user = getUser();

		Person person;
		if (user.isAnonymous()) {
			person = new Person();
			person.username = "wordrails";

			return person;
		}

		try {
			person = cacheService.getPersonByUsernameAndNetworkId(user.username, getNetworkId());
		} catch (ExecutionException e) {
			person = personRepository.findByUser(user);
		}

		Authentication auth = new UsernamePasswordAuthenticationToken(user, user.password, user.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(auth);

		return person;
	}

	public boolean isLogged() {
		return !getUser().isAnonymous();
	}

	public Authentication authenticate(String username, String password, Integer networkId) throws BadCredentialsException {
		Set<User> users;
		try {
			users = cacheService.getUsersByUsername(username);
		} catch (ExecutionException e) {
			users = userRepository.findByUsernameAndEnabled(username, true);
		}

		if (users != null && users.size() > 0) {
			User user = null;
			for (User u : users) {
				if (u.network != null && Objects.equals(u.network.id, networkId)) { //find by network
					if (password.equals(u.password)) { //if this is the user for this network, is the password right?
						user = u;
						break;
					}
				} else if (password.equals(u.password)) { //find by password, if it enters here, the network is not set
					user = u;
					break;
				}
			}

			if (user == null) { //didn't find by password or network.
				throw new BadCredentialsException("Wrong password");
			}

			if (user.network == null || Objects.equals(user.network.id, 0)) {
				user.network = getNetwork();
				userRepository.save(user);
			}

			UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user, password, user.getAuthorities());

			Authentication validAuth = authenticate(auth);
			SecurityContextHolder.getContext().setAuthentication(validAuth);

			return auth;
		}

		throw new BadCredentialsException("Wrong username");
	}

	public boolean areYouLogged(Integer personId) {
		Person person = getLoggedPerson();

		return person != null && Objects.equals(personId, person.id);
	}
}
