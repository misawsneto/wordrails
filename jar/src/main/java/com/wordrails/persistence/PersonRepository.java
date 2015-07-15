package com.wordrails.persistence;

import com.wordrails.business.Network;
import com.wordrails.business.Person;
import com.wordrails.business.User;
import org.joda.time.DateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;
import java.util.Set;

public interface PersonRepository extends JpaRepository<Person, Integer>, QueryDslPredicateExecutor<Person> {

	Set<Person> findByUsername(@Param("username") String username);

	@Query("SELECT person FROM Person person where person.username = :username and person.user.network.id = :networkId")
	Person findByUsernameAndNetworkId(@Param("username") String username, @Param("networkId") Integer networkId);

	@RestResource(exported = false)
	Person findByUser(@Param("user") User user);

	Person findByEmail(@Param("email") String email);

	@Query("SELECT person FROM Person person where person.email = :email and person.user.network.id = :networkId")
	Person findByEmailAndNetworkId(@Param("email") String email, @Param("networkId") Integer networkId);

	@RestResource(exported = false)
	Person findByWordpressId(@Param("wordpressId") Integer wordpressId);

	@RestResource(exported = false)
	@Query("select (select count(*) from PostRead pr where pr.post.author.id = p.id), (select count(*) from Comment comment where comment.post.author.id = p.id), (select count(*) from Recommend recommend where recommend.post.author.id = p.id) from Person p where p.id = :authorId")
	public List<Object[]> findPersonStats(@Param("authorId") Integer authorId);

}