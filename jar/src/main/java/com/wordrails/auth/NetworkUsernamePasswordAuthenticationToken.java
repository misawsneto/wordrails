package com.wordrails.auth;

import com.wordrails.business.Network;
import com.wordrails.business.Person;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;


public class NetworkUsernamePasswordAuthenticationToken extends UsernamePasswordAuthenticationToken {

	private Network network;

	public NetworkUsernamePasswordAuthenticationToken(Person person, String credentials, Network network, Collection<GrantedAuthority> authorities) {
		super(person, credentials, authorities);
		this.network = network;
	}

	public Network getNetwork() {
		return this.network;
	}

	public Person getPerson() {
		return (Person) this.getPrincipal();
	}

	public boolean isAnonymous() {
		return getPerson().username.equals("wordrails");
	}
}
