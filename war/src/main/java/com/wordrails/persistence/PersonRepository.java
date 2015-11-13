package com.wordrails.persistence;

import com.wordrails.business.NetworkRole;
import com.wordrails.business.Person;
import com.wordrails.business.Post;
import com.wordrails.business.User;
import org.joda.time.DateTime;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;
import java.util.Set;

public interface PersonRepository extends JpaRepository<Person, Integer>, QueryDslPredicateExecutor<Person> {

	Set<Person> findByUsername(@Param("username") String username);

	@Query("SELECT person FROM Person person where person.username = :username and person.networkId = :networkId")
	Person findByUsernameAndNetworkId(@Param("username") String username, @Param("networkId") Integer networkId);

	@RestResource(exported = false)
	Person findByUser(@Param("user") User user);

	Person findByEmail(@Param("email") String email);

	@Query("SELECT person FROM Person person where person.email = :email and person.networkId = :networkId")
	Person findByEmailAndNetworkId(@Param("email") String email, @Param("networkId") Integer networkId);

	@RestResource(exported = false)
	Person findByWordpressId(@Param("wordpressId") Integer wordpressId);

	@RestResource(exported = false)
	@Query("select (select count(*) from PostRead pr where pr.post.author.id = p.id), (select count(*) from Comment comment where comment.post.author.id = p.id), (select count(*) from Recommend recommend where recommend.post.author.id = p.id) from Person p where p.id = :authorId")
	List<Object[]> findPersonStats(@Param("authorId") Integer authorId);

	@RestResource(exported = false)
	@Query("select person from Person person where person.user.network.id = :networkId")
	public List<Person> findAllByNetwork(@Param("networkId") Integer networkId);

	@Query("select person from Person person where person.networkId = :networkId")
	public List<Person> findAllByNetwork(@Param("networkId") Integer networkId, Pageable pageable);

	@Query("select person from Person person join fetch person.user u where person.networkId = :networkId and person.id <> :personId")
	public List<Person> findAllByNetworkExcludingPerson(@Param("networkId") Integer networkId, @Param("personId") Integer personId, Pageable pageable);

	@Query("select person from Person person where person.networkId = :networkId and (person.username = :query OR person.email = :query)")
	public List<Person> findAllByNetworkAndQuery(@Param("networkId") Integer networkId, @Param("query") String query, Pageable pageable);

	@Query("select person from Person person where person.networkId = :networkId and person.id <> :personId and (person.username = :query OR person.email = :query)")
	public List<Person> findAllByNetworkAndQueryExcludingPerson(@Param("networkId") Integer networkId, @Param("personId") Integer personId, @Param("query") String query, Pageable pageable);

	@RestResource(exported = false)
	@Query("select count(*) from Person person where person.user.network.id = :networkId")
	public Long countPersonsByNetwork(@Param("networkId") Integer networkId);

	@Query("select count(*) from StationRole sr, NetworkRole nr where (sr.person.id = :personId AND sr.admin = true) OR (nr.person.id = :personId AND nr.admin = true)")
	Long isAdmin(@Param("personId") Integer personId);

	@RestResource(exported = false)
	@Query("select person from Person person where person.id in (:personIds)")
	List<Person> findPersonsByIds(@Param("personIds") List<Integer> personIds);

	@RestResource(exported = false)

	@Query("select nr from NetworkRole nr join fetch nr.person person join fetch person.user u where u.network.id = :networkId")
	List<NetworkRole> findNetworkAdmin(@Param("networkId") Integer networkId);

	@RestResource(exported = false)
	@Query("SELECT person FROM Person person ORDER BY person.id DESC")
	List<Person> findAllPostsOrderByIdDesc();
}