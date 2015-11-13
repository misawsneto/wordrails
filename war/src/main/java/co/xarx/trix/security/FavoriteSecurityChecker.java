package co.xarx.trix.security;

import java.util.List;

import co.xarx.trix.auth.TrixAuthenticationProvider;
import co.xarx.trix.persistence.NetworkRepository;
import co.xarx.trix.persistence.PostRepository;
import co.xarx.trix.persistence.StationRepository;
import co.xarx.trix.persistence.StationRolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.xarx.trix.domain.Bookmark;
import co.xarx.trix.domain.Network;
import co.xarx.trix.domain.Person;
import co.xarx.trix.domain.Post;
import co.xarx.trix.domain.Recommend;
import co.xarx.trix.domain.Station;
import co.xarx.trix.domain.StationRole;

@Component
public class FavoriteSecurityChecker {
	@Autowired
	private TrixAuthenticationProvider authProvider;
	private @Autowired
	NetworkRepository networkRepository;
	private @Autowired
	StationRepository stationRepository;
	private @Autowired
	PostRepository postRepository;
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
