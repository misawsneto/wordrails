package co.xarx.trix.persistence;

import co.xarx.trix.annotation.SdkExclude;
import co.xarx.trix.domain.Person;
import co.xarx.trix.domain.projection.PersonProjection;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

@RepositoryRestResource(exported = true, excerptProjection = PersonProjection.class)
public interface PersonRepository extends DatabaseRepository<Person, Integer> {

	@Cacheable(value = "person", key = "#p0")
	Person findByUsername(@Param("username") String username);

	@RestResource(exported = false)
	Person findByEmail(@Param("email") String email);

	@RestResource(exported = true)
	@Query("select p from Person p where username like %:usernameOrEmailOrName% OR email like %:usernameOrEmailOrName% OR name like %:usernameOrEmailOrName%")
	List<Person> findPersons(@Param("usernameOrEmailOrName") String usernameOrEmail, Pageable pageable);

	@RestResource(exported = false)
	@Query("select (select count(*) from PostRead pr where pr.post.author.id = p.id), " +
			"(select count(*) from Comment comment where comment.post.author.id = p.id) " +
			"from Person p where p.id = :authorId")
	List<Object[]> findPersonStats(@Param("authorId") Integer authorId);

	@RestResource(exported = false)
	@Query("select count(*) from Person person")
	Long countPersons();

	@RestResource(exported = false)
	@Query("select count(*) from Person person where person.name = :q OR person.username = :q OR person.email = :q")
	Long countPersonsByString(@Param("q") String q);

	@RestResource(exported = false)
	@Query("select person from Person person where person.id in (:personIds)")
	List<Person> findPersonsByIds(@Param("personIds") List<Integer> personIds);

	@Override
	@SdkExclude
	@RestResource(exported = true)
	@CacheEvict(value = "person", key = "#p0.username")
	Person save(Person person);

	@Override
	@SdkExclude
	@CacheEvict(value = "person", key = "#p0.username")
	void delete(Person person);

	@Override
	@SdkExclude
	@RestResource(exported = true)
	Person findOne(Integer id);

	@Override
	@SdkExclude
	@RestResource(exported = true)
	List<Person> findAll();

	@Override
	@SdkExclude
	@RestResource(exported = true)
	List<Person> findAll(Sort sort);

	@Override
	@SdkExclude
	@RestResource(exported = true)
	Page<Person> findAll(Pageable pageable);
}