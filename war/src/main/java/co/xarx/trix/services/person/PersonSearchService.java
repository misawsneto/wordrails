package co.xarx.trix.services.person;

import co.xarx.trix.api.v2.PersonData;
import co.xarx.trix.domain.page.query.statement.PersonStatement;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonSearchService {

	public List<PersonData> search(PersonStatement params, Integer page, Integer size) {
		return null;
	}
}
