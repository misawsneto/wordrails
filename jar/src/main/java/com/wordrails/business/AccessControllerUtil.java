package com.wordrails.business;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import com.wordrails.persistence.PersonRepository;
import com.wordrails.services.CacheService;

@Component
public class AccessControllerUtil {
	private @Autowired PersonRepository personRepository;
	private @Autowired UserDetailsService userDetailsService;
	private @Autowired @Qualifier("myAuthenticationManager") AuthenticationManager authenticationManager;
	private @Autowired CacheService cacheService;

	public Person getLoggedPerson() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		org.springframework.security.core.userdetails.User user;
		if(authentication == null || 
				(authentication != null && authentication.getPrincipal() instanceof String)
				&& authentication.getPrincipal().equals("anonymousUser")){

			user = new org.springframework.security.core.userdetails.User("wordrails", "wordrails", true, true, true, true, Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"))); 
			authentication = new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
			authenticationManager.authenticate(authentication);
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}else{
			user = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
		}
		try {
			return cacheService.getPersonByUsername(user.getUsername());
		} catch (ExecutionException e) {
			return personRepository.findByUsername(user.getUsername());
		}
	}

	public boolean isLogged(){
		org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		try {
			return (cacheService.getPersonByUsername(user.getUsername()) != null ? true : false);
		} catch (ExecutionException e) {
			return (personRepository.findByUsername(user.getUsername()) != null ? true : false);
		}
	}

	public void authenticate(String username, String password) {
		UserDetails details = userDetailsService.loadUserByUsername(username);
		org.springframework.security.core.userdetails.User user = new org.springframework.security.core.userdetails.User(username, password, true, true, true, true, details.getAuthorities()); 
		UsernamePasswordAuthenticationToken usernameAndPassword = new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
		// Authenticate
		Authentication auth = authenticationManager.authenticate(usernameAndPassword);
		// Store the authentication object in the security context
		SecurityContextHolder.getContext().setAuthentication(auth);
	}

	public boolean areYouLogged(Integer personId){
		boolean areYouLogged = false;
		Person person = getLoggedPerson();
		if(person != null && person.id == personId){
			areYouLogged = true;
		}
		return areYouLogged;
	}
}