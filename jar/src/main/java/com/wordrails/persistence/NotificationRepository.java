package com.wordrails.persistence;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import com.wordrails.business.Notification;
import com.wordrails.business.Post;

public interface NotificationRepository extends JpaRepository<Notification, Integer>, QueryDslPredicateExecutor<Notification> {

	@Override
	@RestResource(exported = false)
	public <S extends Notification> S save(S arg0);
	
	public List<Notification> findNotificationsByPersonIdOrderByDate(@Param("personId") Integer personId, Pageable pageable);

	@RestResource(exported = false)
	public void deleteByPost(Post post);
}