package co.xarx.trix.services.person;

import co.xarx.trix.api.v2.PersonData;
import co.xarx.trix.domain.page.query.statement.PersonStatement;
import co.xarx.trix.persistence.repository.SQLPersonRepository;
import co.xarx.trix.util.RestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonSearchService {

	private SQLPersonRepository personRepository;

	@Autowired
	public PersonSearchService(SQLPersonRepository personRepository) {
		this.personRepository = personRepository;
	}

	public List<PersonData> search(PersonStatement params, Integer page, Integer size) {
		return personRepository.findAll(size, page, RestUtil.getSort(params.getOrders()));
	}
}
