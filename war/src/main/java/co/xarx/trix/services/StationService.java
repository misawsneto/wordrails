package co.xarx.trix.services;

import co.xarx.trix.api.v2.ImageData;
import co.xarx.trix.api.v2.StationData;
import co.xarx.trix.config.security.Permissions;
import co.xarx.trix.domain.Station;
import co.xarx.trix.persistence.StationRepository;
import co.xarx.trix.services.security.AccessControlListService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StationService {

	private CacheManager cacheManager;
	private ModelMapper mapper;
	private AccessControlListService aclService;
	private StationRepository stationRepository;

	@Autowired
	public StationService(CacheManager cacheManager, ModelMapper mapper,
						  AccessControlListService aclService, StationRepository stationRepository) {
		this.cacheManager = cacheManager;
		this.mapper = mapper;
		this.aclService = aclService;
		this.stationRepository = stationRepository;
	}

	public StationData findStation(Integer stationId) {
		Station station = stationRepository.findOne(stationId);

		return getStationData(station);
	}

	public List<StationData> findStations() {
		List<Station> stations = stationRepository.findAll();
		List<StationData> result = new ArrayList<>();

		for (Station station : stations) {
			StationData data = getStationData(station);
			result.add(data);
		}

		return result;
	}

	private StationData getStationData(Station station) {
		StationData data = new StationData();

		data.setId(data.getId());
		data.setName(station.getName());
		data.setSlug(station.getStationSlug());
		if (station.getLogo() != null) {
			data.setLogo(mapper.map(station.getLogo(), ImageData.class));
		}
		data.setMain(station.isMain());
		data.setWritable(station.isWritable());
		data.setReadableForAnonymous(aclService.hasPermission(Station.class, station.getId(), new
				GrantedAuthoritySid("ROLE_ANONYMOUS"), Permissions.READ));
		data.setAllowComments(station.isAllowComments());
		data.setAllowSocialShare(station.isAllowSocialShare());
		data.setAllowWritersToNotify(station.isAllowWritersToNotify());
		data.setTopper(station.isTopper());
		data.setSubheading(station.isSubheading());
		data.setShowAuthorData(station.isShowAuthorData());
		data.setShowAuthorSocialData(station.isShowAuthorSocialData());

		cacheManager.getCache("stations").put(data.getId(), data);
		return data;
	}
}
