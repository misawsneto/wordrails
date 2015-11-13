package com.wordrails.persistence;

import com.wordrails.domain.CertificateIos;
import com.wordrails.domain.GlobalParameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.rest.core.annotation.RestResource;


public interface GlobalParameterRepository extends JpaRepository<GlobalParameter, Integer>, QueryDslPredicateExecutor<CertificateIos> {

	@RestResource(exported = false)
	GlobalParameter findByParameterName(String key);
}
