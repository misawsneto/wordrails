package co.xarx.trix.persistence;

import co.xarx.trix.domain.NetworkRole;
import co.xarx.trix.domain.Person;
import co.xarx.trix.domain.User;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
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
	@RestResource(exported = false)
	@CachePut(value = "person", key = "#p0.user.username")
	Person save(Person person);

	@Query("SELECT person FROM Person person where person.username = :username and person.networkId = :networkId")
	Person findByUsernameAndNetworkId(@Param("username") String username, @Param("networkId") Integer networkId);

	@RestResource(exported = false)
	Person findByUser(@Param("user") User user);

	Person findByEmail(@Param("email") String email);

	@RestResource(exported = false)
	@Query("select (select count(*) from PostRead pr where pr.post.author.id = p.id), (select count(*) from Comment comment where comment.post.author.id = p.id), (select count(*) from Recommend recommend where recommend.post.author.id = p.id) from Person p where p.id = :authorId")
	List<Object[]> findPersonStats(@Param("authorId") Integer authorId);

	@Query("select person from Person person where person.networkId = :networkId")
	List<Person> findAllByNetwork(@Param("networkId") Integer networkId, Pageable pageable);

	@Query("select person from Person person join fetch person.user u where person.networkId = :networkId and person.id <> :personId")
	List<Person> findAllByNetworkExcludingPerson(@Param("networkId") Integer networkId, @Param("personId") Integer personId, Pageable pageable);

	@Query("select person from Person person where person.networkId = :networkId and (person.username = :query OR person.email = :query)")
	List<Person> findAllByNetworkAndQuery(@Param("networkId") Integer networkId, @Param("query") String query, Pageable pageable);

	@Query("select person from Person person where person.networkId = :networkId and person.id <> :personId and (person.username = :query OR person.email = :query)")
	List<Person> findAllByNetworkAndQueryExcludingPerson(@Param("networkId") Integer networkId, @Param("personId") Integer personId, @Param("query") String query, Pageable pageable);

	@RestResource(exported = false)
	@Query("select count(*) from Person person")
	Long countPersons();

	@Query("select count(*) from StationRole sr, NetworkRole nr where (sr.person.id = :personId AND sr.admin = true) OR (nr.person.id = :personId AND nr.admin = true)")
	Long isAdmin(@Param("personId") Integer personId);

	@RestResource(exported = false)
	@Query("select person from Person person where person.id in (:personIds)")
	List<Person> findPersonsByIds(@Param("personIds") List<Integer> personIds);

	@RestResource(exported = false)
	@Query("select nr from NetworkRole nr join fetch nr.person person join fetch person.user u")
	List<NetworkRole> findNetworkAdmin();

	@RestResource(exported = false)
	@Query("SELECT person FROM Person person ORDER BY person.id DESC")
	List<Person> findAllPostsOrderByIdDesc();
}