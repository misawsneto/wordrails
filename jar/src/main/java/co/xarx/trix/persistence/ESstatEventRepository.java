package co.xarx.trix.persistence;

import co.xarx.trix.domain.ESstatEvent;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

public interface ESstatEventRepository extends ESRepository<ESstatEvent> {

    @RestResource(exported = false)
    List<ESstatEvent> findByPersonId(Integer personId);
}
