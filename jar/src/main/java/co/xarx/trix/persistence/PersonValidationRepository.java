package co.xarx.trix.persistence;

import co.xarx.trix.domain.Person;
import co.xarx.trix.domain.PersonValidation;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

public interface PersonValidationRepository extends DatabaseRepository<PersonValidation, Integer> {

	@RestResource(path = "findByValidationHash")
	@Query("SELECT pv from PersonValidation pv WHERE pv.hash =:hash")
	PersonValidation findByValidationHash(@Param("hash") String hash);

	@RestResource(exported = false)
	PersonValidation findByPerson(@Param("person") Person person);

	@RestResource(exported = false)
	@Modifying
//	@Query("delete from PersonValidation WHERE person.id = :personId")
	void deleteByPerson(@Param("person") Person person);
}
