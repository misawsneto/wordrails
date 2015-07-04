package com.wordrails.auth;

import com.wordrails.business.Network;
import com.wordrails.business.Person;
import com.wordrails.persistence.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;


@Component
public class TrixAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private PersonRepository personRepository;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		NetworkUsernamePasswordAuthenticationToken auth = (NetworkUsernamePasswordAuthenticationToken) authentication;

		if (auth.isAnonymous()) {
			return auth;
		}

		Person person = auth.getPerson();
		Network network = auth.getNetwork();
		String password = (String) auth.getCredentials();

		if (person == null) {
			throw new BadCredentialsException("Person is null");
		}

		if (password == null || !password.equals(person.password)) {
			throw new BadCredentialsException("Wrong password");
		}

		return new NetworkUsernamePasswordAuthenticationToken(person, password, network);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return (NetworkUsernamePasswordAuthenticationToken.class.equals(authentication));
	}

	public Person getLoggedPerson() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (auth == null || auth instanceof AnonymousAuthenticationToken) {
			Person person = personRepository.findByUsernameAndNetwork("wordrails", null);
			String password = person.password;

			auth = new NetworkUsernamePasswordAuthenticationToken(person, password, null);

			SecurityContextHolder.getContext().setAuthentication(auth);

			return person;
		}

		return (Person) auth.getPrincipal();
	}

	public boolean isLogged() {
		NetworkUsernamePasswordAuthenticationToken authentication = (NetworkUsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

		return authentication != null && !authentication.isAnonymous();
	}

	public void authenticate(String username, String password, Network network) throws BadCredentialsException {
		Person person = personRepository.findByUsernameAndNetwork(username, network);

		//These exceptions are not being thrown, not in a visible way at least
		if (person == null) {
			throw new BadCredentialsException("Username not found");
		}

		NetworkUsernamePasswordAuthenticationToken auth = new NetworkUsernamePasswordAuthenticationToken(person, password, network);

		Authentication validAuth = authenticate(auth);
		SecurityContextHolder.getContext().setAuthentication(validAuth);
	}

	public boolean areYouLogged(Integer personId) {
		Person person = getLoggedPerson();

		return person != null && Objects.equals(personId, person.id);
	}
}
