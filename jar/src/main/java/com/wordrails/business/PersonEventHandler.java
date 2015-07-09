package com.wordrails.business;

import com.wordrails.auth.TrixAuthenticationProvider;
import com.wordrails.persistence.PersonRepository;
import com.wordrails.persistence.TermRepository;
import com.wordrails.persistence.UserGrantedAuthorityRepository;
import com.wordrails.persistence.UserRepository;
import com.wordrails.services.CacheService;
import com.wordrails.util.WordrailsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.HandleBeforeDelete;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wordrails.persistence.ImageRepository;
import com.wordrails.persistence.NetworkRolesRepository;
import com.wordrails.persistence.PersonRepository;
import com.wordrails.persistence.PostRepository;
import com.wordrails.persistence.TermRepository;
import com.wordrails.services.CacheService;
import com.wordrails.util.WordrailsUtil;

@RepositoryEventHandler(Person.class)
@Component
public class PersonEventHandler {
	
	private @Autowired NetworkRolesRepository networkRolesRepository;
	private @Autowired ImageRepository imageRepository;

	@Autowired
	private ImageRepository bookmarksRepository;
	@Autowired
	private PersonRepository personRepository;
	@Autowired
	private TermRepository termRepository;
	@Autowired
	private CacheService cacheService;
	@Autowired
	private TrixAuthenticationProvider authProvider;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserGrantedAuthorityRepository userGrantedAuthorityRepository;

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
		UserGrantedAuthority authority = new UserGrantedAuthority("ROLE_USER");
		authority.network = authProvider.getNetwork();

		String password = person.password;

		if (password == null || password.trim().equals("")) {
			password = WordrailsUtil.generateRandomString(8, "a#");
		}

		User user = new User();
		user.username = person.username;
		user.password = password;
		user.network = authority.network;
		user.addAuthority(authority);

		userGrantedAuthorityRepository.save(authority);
		userRepository.save(user);
		person.user = user;
	}
	
	@HandleBeforeDelete
	public void handleBeforeDelete(Person person){
		networkRolesRepository.deleteByPerson(person);
		imageRepository.deleteByPerson(person);
		bookmarksRepository.deleteByPerson(person);
		System.out.println(person.id+ "");

	}
	
	@HandleAfterSave
	@Transactional
	public void handleAfterSave(Person person){
		cacheService.updatePerson(person.id);
		cacheService.updatePerson(person.username);
	}

}