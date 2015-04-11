package com.wordrails.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import com.wordrails.business.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Integer>, QueryDslPredicateExecutor<Notification> {

}