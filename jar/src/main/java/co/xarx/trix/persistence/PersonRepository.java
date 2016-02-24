package co.xarx.trix.persistence;

import co.xarx.trix.annotation.SdkExclude;
import co.xarx.trix.domain.NetworkRole;
import co.xarx.trix.domain.Person;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

public interface PersonRepository extends JpaRepository<Person, Integer>, QueryDslPredicateExecutor<Person> {

	@Cacheable(value = "person", key = "#p0")
	Person findByUsername(@Param("username") String username);

	@Override
	@SdkExclude
	@CacheEvict(value = "person", key = "#p0.username")
	Person save(Person person);

	@Override
	@SdkExclude
	@CacheEvict(value = "person", key = "#p0.username")
	void delete(Person person);

	@RestResource(exported = false)
	Person findByEmail(@Param("email") String email);

	@RestResource(exported = false)
	@Query("select (select count(*) from PostRead pr where pr.post.author.id = p.id), (select count(*) from Comment comment where comment.post.author.id = p.id), (select count(*) from Recommend recommend where recommend.post.author.id = p.id) from Person p where p.id = :authorId")
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

	@RestResource(exported = false)
	@Query("select nr from NetworkRole nr join fetch nr.person person join fetch person.user u")
	List<NetworkRole> findNetworkAdmin();
}