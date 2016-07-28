package co.xarx.trix.persistence.custom;

import co.xarx.trix.config.multitenancy.TenantContextHolder;
import co.xarx.trix.domain.QStation;
import co.xarx.trix.domain.Station;
import co.xarx.trix.persistence.StationRepository;
import co.xarx.trix.services.security.PermissionFilterService;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StationRepositoryImpl implements StationRepositoryCustom {

	//DON'T REFACTOR THIS CLASS TO USE AUTOWIRED CONSTRUCTOR
	//BECAUSE IT WILL CAUSE A CIRCULAR REFERENCE EXCEPTION
	@Autowired
	private StationRepository stationRepository;
	@Autowired
	private PermissionFilterService permissionFilterService;

	@Override
	public Page<Station> findAll(Pageable pageable) {
		List<Integer> ids = getIdsWithReadPermission();
		return stationRepository.findAll(QStation.station.id.in(ids), pageable);
//		Session session = em.unwrap(Session.class);
//		List<Integer> postIdsWithReadPermission = getPostIdsWithReadPermission();
//		enableFilter(session, postIdsWithReadPermission);
//		return postRepository.findAll(pageable);
	}

	public void enableFilter(Session session, List<Integer> ids) {
		Filter filter = session.enableFilter("idFilter");
		filter.setParameterList("ids", ids);
	}

	public List<Integer> getIdsWithReadPermission() {
		List<Integer> ids = stationRepository.findIds(TenantContextHolder.getCurrentTenantId());
		ids = permissionFilterService.filterIds(ids, Station.class, "read");
		return ids;
	}
}
