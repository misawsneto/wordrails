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
import com.wordrails.persistence.PostRepository;
import com.wordrails.persistence.TermRepository;
import com.wordrails.util.WordrailsUtil;

@RepositoryEventHandler(Person.class)
@Component
public class PersonEventHandler {
	private @Autowired UserDetailsManager userDetailsManager;
	private @Autowired PersonRepository personRepository;
	private @Autowired TermRepository termRepository;
	private @Autowired EmailService passwordResetService;
	private @Autowired PostRepository postRepository;    

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
				new org.springframework.security.core.userdetails.User(person.username, password == null ? WordrailsUtil.generateRandomString(8, "a#") : password, authority);
		userDetailsManager.createUser(user);
	}

}