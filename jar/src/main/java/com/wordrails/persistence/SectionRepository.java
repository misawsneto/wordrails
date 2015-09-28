package com.wordrails.persistence;

import com.wordrails.business.Network;
import com.wordrails.business.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import java.util.List;
import java.util.Set;

/**
 * Created by misael on 28/09/2015.
 */
public interface SectionRepository extends JpaRepository<Section, Integer>, QueryDslPredicateExecutor<Section> {
    Set<Section> findByNetwork(Network network);
}
