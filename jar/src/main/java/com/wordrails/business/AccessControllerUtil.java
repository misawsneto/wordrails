package com.wordrails.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.wordrails.persistence.PersonRepository;

@Component
public class AccessControllerUtil {
	private @Autowired PersonRepository personRepository;

	public Person getLoggedPerson() {
		org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
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