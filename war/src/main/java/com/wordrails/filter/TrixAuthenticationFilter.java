package com.wordrails.filter;

import com.wordrails.WordrailsService;
import com.wordrails.auth.NetworkUsernamePasswordAuthenticationToken;
import com.wordrails.business.Network;
import com.wordrails.business.Person;
import com.wordrails.persistence.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;
import java.util.Set;

public class TrixAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	@Autowired
	private WordrailsService wordrailsService;
	@Autowired
	private PersonRepository personRepository;

	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, BadCredentialsException {
		String username = super.obtainUsername(request);
		String password = super.obtainPassword(request);
		Network network = wordrailsService.getNetworkFromHost(request);
		Set<Person> persons = personRepository.findByUsername(username);

		if (persons != null && persons.size() > 0) {
			Person person = null;
			for (Person p : persons) { //find by network
				if (p.network != null && Objects.equals(p.network.id, network.id)) {
					person = p;
					break;
				}
			}

			if (person == null) { //if network wasnt found, it must be null.
				for (Person p : persons) { //find by password
					if (password.equals(p.password)) {
						person = p;
						break;
					}
				}
			}

			if (person == null) { //didn't find by password or network.
				throw new BadCredentialsException("Wrong password");
			}

			if (person.network == null) {
				if (network != null) {
					person.network = network;
					personRepository.save(person);
				}
			}

			return new NetworkUsernamePasswordAuthenticationToken(person, password, network);
		}

		throw new BadCredentialsException("Wrong username");
	}
}
