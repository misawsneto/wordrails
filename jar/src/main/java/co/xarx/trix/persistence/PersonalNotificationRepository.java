package co.xarx.trix.persistence;

import co.xarx.trix.domain.Person;
import co.xarx.trix.domain.PersonalNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

public interface PersonalNotificationRepository extends JpaRepository<PersonalNotification, Integer>,
		QueryDslPredicateExecutor<PersonalNotification> {

	@Query("select n from PersonalNotification n where n.person.id = :personId")
	List<PersonalNotification> findByPersonId(@Param("personId") Integer personId);

	@RestResource(exported = false)
	@Modifying
	void deleteByPersonId(@Param("id") Integer id);
}