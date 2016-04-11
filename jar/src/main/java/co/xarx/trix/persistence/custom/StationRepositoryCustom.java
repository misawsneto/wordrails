package co.xarx.trix.persistence.custom;

import co.xarx.trix.domain.Station;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StationRepositoryCustom {

	Page<Station> findAll(Pageable pageable);
}
