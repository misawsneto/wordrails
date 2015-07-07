package com.wordrails.business;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.wordrails.persistence.UserRepository;
import com.wordrails.services.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
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
import com.wordrails.services.CacheService;
import com.wordrails.util.WordrailsUtil;

@RepositoryEventHandler(Person.class)
@Component
public class PersonEventHandler {
//	private @Autowired UserDetailsManager userDetailsManager;
	private @Autowired PersonRepository personRepository;
	private @Autowired TermRepository termRepository;
	private @Autowired EmailService passwordResetService;
	private @Autowired
	UserRepository userRepository;
	@Autowired
	private CacheService cacheService;

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
		Set<String> authority = new HashSet<>(1);
		authority.add("ROLE_USER");

		String password = person.password;
		
		if(password != null && password.trim().equals("")){
			password = null;
		}

		person.user = new User();
		person.user.username = person.username;
		person.user.password = password == null ? WordrailsUtil.generateRandomString(8, "a#") : password;
		person.user.authorities = authority;

	}
	
	@HandleAfterSave
	@Transactional
	public void handleAfterSave(Person person){
		cacheService.updatePerson(person.id);
		cacheService.updatePerson(person.username);
	}

}