package com.wordrails.business;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wordrails.persistence.PersonRepository;
import com.wordrails.persistence.TermRepository;

@RepositoryEventHandler(Person.class)
@Component
public class PersonEventHandler {
	private @Autowired UserDetailsManager userDetailsManager;
	private @Autowired PersonRepository personRepository;
	private @Autowired TermRepository termRepository;
	private @Autowired EmailService passwordResetService;

	@HandleBeforeSave
	@Transactional
	public void handleBeforeSave(Person person) {
		Person originalPerson = personRepository.findOne(person.id);
		if(originalPerson != null && !originalPerson.name.equals(person.name)){
			termRepository.updateTermsNamesAuthorTaxonomies(person.name, originalPerson.name);
		}
	}
	
	@HandleAfterCreate
	@Transactional
	public void handleAfterCreate(Person person) {
		Collection<GrantedAuthority> authority = new ArrayList<GrantedAuthority>(1);
		SimpleGrantedAuthority roleUser = new SimpleGrantedAuthority("ROLE_USER");
		authority.add(roleUser);
		
		String password = person.password;
		
		if(password != null && password.trim().equals("")){
			password = null;
		}
		
		org.springframework.security.core.userdetails.User user = 
				new org.springframework.security.core.userdetails.User(person.username, password == null ? generateRandomString(8, "a#") : password, authority);
		userDetailsManager.createUser(user);
	}

	private String generateRandomString(int length, String chars){
		String mask = "";
		if (chars.contains("a")) mask += "abcdefghijklmnopqrstuvwxyz";
		if (chars.contains("A")) mask += "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		if (chars.contains("#")) mask += "0123456789";
		if (chars.contains("!")) mask += "~`!@#$%^&*()_+-={}[]:\";\'<>?,./|\\";
		String result = "";
		for (int i = length; i > 0; --i){
			int index = (int) Math.round(Math.random() * (mask.length() - 1));
			result += mask.charAt(index);
		}

		return result;
	}
	
}