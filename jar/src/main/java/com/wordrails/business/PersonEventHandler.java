package com.wordrails.business;

import com.wordrails.persistence.ImageRepository;
import com.wordrails.persistence.NetworkRolesRepository;
import com.wordrails.persistence.PersonRepository;
import com.wordrails.persistence.TermRepository;
import com.wordrails.services.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.HandleBeforeDelete;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RepositoryEventHandler(Person.class)
@Component
public class PersonEventHandler {

	@Autowired
	private NetworkRolesRepository networkRolesRepository;
	@Autowired
	private ImageRepository imageRepository;

	@Autowired
	private ImageRepository bookmarksRepository;
	@Autowired
	private PersonRepository personRepository;
	@Autowired
	private TermRepository termRepository;
	@Autowired
	private CacheService cacheService;


	@HandleBeforeSave
	@Transactional
	public void handleBeforeSave(Person person) {
		Person originalPerson = personRepository.findOne(person.id);
		if (originalPerson != null && !originalPerson.name.equals(person.name)) {
			termRepository.updateTermsNamesAuthorTaxonomies(person.name, originalPerson.name);
		}
	}

	@HandleBeforeDelete
	public void handleBeforeDelete(Person person) {
		networkRolesRepository.deleteByPerson(person);
		imageRepository.deleteByPerson(person);
		bookmarksRepository.deleteByPerson(person);
		System.out.println(person.id + "");

	}

	@HandleAfterSave
	@Transactional
	public void handleAfterSave(Person person) {
		cacheService.updatePerson(person.id);
		cacheService.updatePerson(person.username);
	}

}