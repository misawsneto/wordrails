package co.xarx.trix.persistence;

import co.xarx.trix.annotation.SdkExclude;
import co.xarx.trix.domain.Person;
import co.xarx.trix.domain.UserGrantedAuthority;
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

import java.util.Collection;
import java.util.List;
import java.util.Set;

@RepositoryRestResource(exported = true, excerptProjection = PersonProjection.class)
public interface PersonRepository extends DatabaseRepository<Person, Integer> {

	@Query(value = "SELECT * " +
			"FROM person p " +
			"JOIN authorities a on a.user_id = p.user_id " +
			"WHERE p.username IN (:usernames) OR a.authority IN (:roles) AND p.tenantId=:tenantId " +
			"GROUP BY p.id", nativeQuery = true)
	List<Person> findByUsernamesAndRoles(@Param("usernames") List<String> usernames, @Param("roles") List<String>
			roles, @Param("tenantId") String tenantId);

	@Cacheable(value = "person", key = "#p0")
	Person findByUsername(@Param("username") String username);

	Person findByUsernameAndTenantId(@Param("username") String username, @Param("tenantId") String tenantId);

	@RestResource(exported = false)
	Person findByEmail(@Param("email") String email);

	@RestResource(exported = false)
	List<Person> findByEmailIn(@Param("emails") Collection<String> emails);

	@RestResource(exported = true)
	@Query("select p from Person p where username like %:usernameOrEmailOrName% OR email like %:usernameOrEmailOrName% OR name like %:usernameOrEmailOrName%")
	List<Person> findPersons(@Param("usernameOrEmailOrName") String usernameOrEmail, Pageable pageable);

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

	@SdkExclude
	@RestResource(exported = false)
	List<Person> findByUsernameIn(List<String> usernames);
}