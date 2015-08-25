package com.wordrails.persistence;

import com.wordrails.business.CertificateIos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * Created by jonas on 12/08/15.
 */
@RepositoryRestResource(exported=false)
public interface CertificateIosRepository extends JpaRepository<CertificateIos, Integer>, QueryDslPredicateExecutor<CertificateIos>{

}
