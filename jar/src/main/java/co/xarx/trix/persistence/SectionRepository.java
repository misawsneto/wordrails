package co.xarx.trix.persistence;

import co.xarx.trix.domain.Section;
import co.xarx.trix.domain.Network;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.Set;

/**
 * Created by misael on 28/09/2015.
 */
public interface SectionRepository extends JpaRepository<Section, Integer>, QueryDslPredicateExecutor<Section> {
    @RestResource(exported = false)
    Set<Section> findByNetwork(Network network);
}
