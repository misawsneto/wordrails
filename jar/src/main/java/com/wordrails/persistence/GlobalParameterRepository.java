package com.wordrails.persistence;

import com.wordrails.business.CertificateIos;
import com.wordrails.business.GlobalParameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;


public interface GlobalParameterRepository extends JpaRepository<GlobalParameter, Integer>, QueryDslPredicateExecutor<CertificateIos> {

	@RestResource(exported = false)
	GlobalParameter findByParameterName(String key);
}
