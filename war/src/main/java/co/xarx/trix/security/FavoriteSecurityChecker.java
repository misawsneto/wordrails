package co.xarx.trix.security;

import co.xarx.trix.domain.*;
import co.xarx.trix.persistence.NetworkRepository;
import co.xarx.trix.persistence.StationRepository;
import co.xarx.trix.persistence.StationRolesRepository;
import co.xarx.trix.security.auth.TrixAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FavoriteSecurityChecker {
	@Autowired
	private TrixAuthenticationProvider authProvider;
	private @Autowired
	NetworkRepository networkRepository;
	private @Autowired
	StationRepository stationRepository;
	private @Autowired
	StationRolesRepository personStationRolesRepository;

	public boolean canWriteBookmark(Bookmark bookmark) {
		boolean canWrite = false;
		Post post = bookmark.post;

		Person personLogged = authProvider.getLoggedPerson();
		if(personLogged != null && personLogged.id.equals(bookmark.person.id)){
			Station station = post.station;
			if(station.visibility.equals(Station.UNRESTRICTED) && station.writable){
				canWrite = true;
			}else if(station.visibility.equals(Station.RESTRICTED_TO_NETWORKS) && station.writable){
				List<Integer> networksId = networkRepository.findIdsByStation(post.station.id);
				List<Network> belongsToNetworks = networkRepository.belongsToNetworks(personLogged.id, networksId);
				if(belongsToNetworks != null && belongsToNetworks.size() > 0){
					canWrite = true;
				}
			}else if(station.visibility.equals(Station.RESTRICTED) && station.writable){
				Station belongsToStation = stationRepository.belongsToStation(personLogged.id, post.station.id);
				if(belongsToStation != null){
					canWrite = true;
				}
			}else{
				StationRole personStationRoles = personStationRolesRepository.findByStationAndPerson(post.station, personLogged);
				if(personStationRoles != null && personStationRoles.writer){
					canWrite = true;
				}
			}
		}
		return canWrite;
	}

	public boolean canWriteRecommend(Recommend recommend) {
		boolean canWrite = false;
		Post post = recommend.post;

		Person personLogged = authProvider.getLoggedPerson();
		if(personLogged != null && personLogged.id == recommend.person.id){
			Station station = post.station;
			if(station.visibility.equals(Station.UNRESTRICTED) && station.writable){
				canWrite = true;
			}else if(station.visibility.equals(Station.RESTRICTED_TO_NETWORKS) && station.writable){
				List<Integer> networksId = networkRepository.findIdsByStation(post.station.id);
				List<Network> belongsToNetworks = networkRepository.belongsToNetworks(personLogged.id, networksId);
				if(belongsToNetworks != null && belongsToNetworks.size() > 0){
					canWrite = true;
				}
			}else if(station.visibility.equals(Station.RESTRICTED) && station.writable){
				Station belongsToStation = stationRepository.belongsToStation(personLogged.id, post.station.id);
				if(belongsToStation != null){
					canWrite = true;
				}
			}else{
				StationRole personStationRoles = personStationRolesRepository.findByStationAndPerson(post.station, personLogged);
				if(personStationRoles != null && personStationRoles.writer){
					canWrite = true;
				}
			}
		}
		return canWrite;
	}

}
