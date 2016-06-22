package co.xarx.trix.persistence;

import co.xarx.trix.domain.Person;
import co.xarx.trix.domain.PersonValidation;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

public interface PersonValidationRepository extends DatabaseRepository<PersonValidation, Integer> {

	@RestResource(path = "findBValidationHash")
	PersonValidation findByHash(@Param("hash") String hash);

	@RestResource(exported = false)
	PersonValidation findByPerson(@Param("person") Person person);
}
