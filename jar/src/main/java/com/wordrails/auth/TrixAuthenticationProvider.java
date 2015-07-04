package com.wordrails.auth;

import com.wordrails.business.Network;
import com.wordrails.business.Person;
import com.wordrails.persistence.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashSet;


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

		if (!password.equals(person.password)) {
			throw new BadCredentialsException("Wrong password");
		}

		if (person.network == null) {
			if(network != null) {
				person.network = network;
				personRepository.save(person);
			}
		}

		Collection<GrantedAuthority> authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

		person.password = null; //let's not store the password in a visible way. for security

		return new NetworkUsernamePasswordAuthenticationToken(person, password, network, authorities);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return (NetworkUsernamePasswordAuthenticationToken.class.equals(authentication));
	}
}
