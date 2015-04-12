package com.wordrails.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wordrails.business.AccessControllerUtil;
import com.wordrails.business.Favorite;
import com.wordrails.business.Network;
import com.wordrails.business.Person;
import com.wordrails.business.Post;
import com.wordrails.business.Station;
import com.wordrails.business.StationRole;
import com.wordrails.persistence.NetworkRepository;
import com.wordrails.persistence.PostRepository;
import com.wordrails.persistence.StationRepository;
import com.wordrails.persistence.StationRolesRepository;

@Component
public class FavoriteSecurityChecker {
	private @Autowired AccessControllerUtil accessControllerUtil;
	private @Autowired NetworkRepository networkRepository;
	private @Autowired StationRepository stationRepository;
	private @Autowired PostRepository postRepository;
	private @Autowired StationRolesRepository personStationRolesRepository;

	public boolean canWrite(Favorite favorite) {
		boolean canWrite = false;
		Post post = favorite.post;

		Person personLogged = accessControllerUtil.getLoggedPerson();
		if(personLogged != null && personLogged.id == favorite.person.id){
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
