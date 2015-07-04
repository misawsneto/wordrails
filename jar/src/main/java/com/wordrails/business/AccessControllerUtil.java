package com.wordrails.business;

import com.wordrails.auth.NetworkUsernamePasswordAuthenticationToken;
import com.wordrails.persistence.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

@Component
public class AccessControllerUtil {

	@Autowired
	private PersonRepository personRepository;

	@Autowired
	@Qualifier("trixAuthenticationManager")
	private AuthenticationManager authenticationManager;

	public Person getLoggedPerson() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (auth == null || auth instanceof AnonymousAuthenticationToken) {
			Person person = personRepository.findByUsername("wordrails");
			String password = person.password;
			person.password = null;

			Collection<GrantedAuthority> authorities = new HashSet<>();
			authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
			auth = new NetworkUsernamePasswordAuthenticationToken(person, password, null, authorities);

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
		Person person = personRepository.findByUsername(username);

		//These exceptions are not being thrown, not in a visible way at least
		if (person == null) {
			throw new BadCredentialsException("Username not found");
		}

		Collection<GrantedAuthority> authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

		NetworkUsernamePasswordAuthenticationToken auth = new NetworkUsernamePasswordAuthenticationToken(person, password, network, authorities);

		Authentication validAuth = authenticationManager.authenticate(auth);
		SecurityContextHolder.getContext().setAuthentication(validAuth);
	}

	public boolean areYouLogged(Integer personId) {
		Person person = getLoggedPerson();

		return person != null && Objects.equals(personId, person.id);
	}
}