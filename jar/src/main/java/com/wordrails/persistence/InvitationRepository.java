package com.wordrails.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import com.wordrails.business.Invitation;

public interface InvitationRepository extends JpaRepository<Invitation, Integer>, QueryDslPredicateExecutor<Invitation> {
	@RestResource(exported=true)
	public Invitation findByInvitationHash(@Param("hash") String hash);
	
	@Override
	@RestResource(exported=false)
	public <S extends Invitation> S save(S arg0);
	
	@Override
	@RestResource(exported=false)
	public void delete(Integer arg0);
	
	@Override
	@RestResource(exported=false)
	public List<Invitation> findAll();
	
	@Override
	@RestResource(exported=false)
	public Invitation findOne(Integer arg0);
}