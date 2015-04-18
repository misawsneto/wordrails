package com.wordrails.business;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.wordrails.persistence.PersonRepository;

@Component
public class AccessControllerUtil {
	private @Autowired PersonRepository personRepository;

	public Person getLoggedPerson() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		org.springframework.security.core.userdetails.User user;
		if(authentication == null || (authentication != null && authentication.getPrincipal() instanceof String) && authentication.getPrincipal().equals("anonymousUser")){
			user = new org.springframework.security.core.userdetails.User("wordrails", "wordrails", true, true, true, true, Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"))); 
			authentication = new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
		}else{
			user = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
		}
		return personRepository.findByUsername(user.getUsername());
	}

	public boolean isLogged(){
		org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return (personRepository.findByUsername(user.getUsername()) != null ? true : false);
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