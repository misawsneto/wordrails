package com.wordrails.persistence;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import com.wordrails.domain.Notification;
import com.wordrails.domain.Post;
import com.wordrails.domain.Station;

public interface NotificationRepository extends JpaRepository<Notification, Integer>, QueryDslPredicateExecutor<Notification> {

	@Override
	@RestResource(exported = false)
	public <S extends Notification> S save(S arg0);

	@Query("select notification from Notification notification where notification.person.id = :personId order by notification.id desc")
	public List<Notification> findNotificationsByPersonIdOrderByDate(@Param("personId") Integer personId, Pageable pageable);

	@RestResource(exported = false)
	public void deleteByPost(Post post);
	
	@RestResource(exported = false)
	public void deleteByHash(String notificationHash);

	@RestResource(exported = false)
	public List<Notification> findByStation(Station station);

	@RestResource(exported = false)
	@Modifying
	void deleteByPersonId(Integer id);

}