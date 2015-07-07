package com.wordrails.auth;

import com.wordrails.business.Person;
import com.wordrails.business.User;
import com.wordrails.persistence.PersonRepository;
import com.wordrails.persistence.UserRepository;
import com.wordrails.services.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.ExecutionException;


@Component
public class TrixAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private PersonRepository personRepository;
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

		if (user.isAnonymous()) {
			return auth;
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

	public Person getLoggedPerson() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		User user;
		if (auth == null || auth instanceof AnonymousAuthenticationToken) {
			try {
				user = cacheService.getUserByUsernameAndNetworkId("wordrails", 0);
			} catch (ExecutionException e) {
				user = userRepository.findByUsernameAndEnabledAndNetworkId("wordrails", true, 0);
			}
		} else {
			user = (User) auth.getPrincipal();
		}

		Person person;
		try {
			person = cacheService.getPersonByUser(user);
		} catch (ExecutionException e) {
			person = personRepository.findByUser(user);
		}

		auth = new UsernamePasswordAuthenticationToken(user, user.password, user.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(auth);

		return person;
	}

	public boolean isLogged() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		return authentication != null && (authentication.getPrincipal() instanceof String || !((User) authentication.getPrincipal()).isAnonymous());
	}

	public void authenticate(String username, String password, Integer networkId) throws BadCredentialsException {
		User user;
		try {
			user = cacheService.getUserByUsernameAndNetworkId(username, networkId);
		} catch (ExecutionException e) {
			user = userRepository.findByUsernameAndEnabledAndNetworkId(username, true, networkId);
		}

		//These exceptions are not being thrown, not in a visible way at least
		if (user == null) {
			throw new BadCredentialsException("Username not found");
		}

		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user, password, user.getAuthorities());

		Authentication validAuth = authenticate(auth);
		SecurityContextHolder.getContext().setAuthentication(validAuth);
	}

	public boolean areYouLogged(Integer personId) {
		Person person = getLoggedPerson();

		return person != null && Objects.equals(personId, person.id);
	}
}
