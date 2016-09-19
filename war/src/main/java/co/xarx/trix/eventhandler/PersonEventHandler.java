package co.xarx.trix.eventhandler;

import co.xarx.trix.domain.*;
import co.xarx.trix.persistence.*;
import co.xarx.trix.services.ElasticSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.*;
import org.springframework.stereotype.Component;

import java.util.List;

@RepositoryEventHandler(Person.class)
@Component
public class PersonEventHandler {

	@Autowired
	private PersonRepository personRepository;
	@Autowired
	private TermRepository termRepository;
	@Autowired
	private ElasticSearchService elasticSearchService;
	@Autowired
	private UserRepository userRepository;

	@HandleBeforeSave
	public void handleBeforeSave(Person person) {
		Person originalPerson = personRepository.findOne(person.id);
		person.user = userRepository.findOne(originalPerson.user.id);
		if (originalPerson != null && !originalPerson.name.equals(person.name)) {
			termRepository.updateTermsNamesAuthorTaxonomies(person.name, originalPerson.name);
		}
	}

	@HandleAfterSave
	public void handleAfterSave(Person person) {
		elasticSearchService.mapThenSave(person, ESPerson.class);
	}

	@HandleAfterCreate
	public void handleAfterCreate(Person person) {
		elasticSearchService.mapThenSave(person, ESPerson.class);
	}

}