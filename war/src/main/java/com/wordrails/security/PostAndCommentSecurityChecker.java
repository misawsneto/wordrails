package com.wordrails.security;

import java.util.ArrayList;
import java.util.List;

import com.wordrails.auth.TrixAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wordrails.domain.Comment;
import com.wordrails.domain.Network;
import com.wordrails.domain.Person;
import com.wordrails.domain.Post;
import com.wordrails.domain.Station;
import com.wordrails.domain.StationRole;
import com.wordrails.persistence.NetworkRepository;
import com.wordrails.persistence.StationRolesRepository;
import com.wordrails.persistence.PostRepository;
import com.wordrails.persistence.StationRepository;

@Component
public class PostAndCommentSecurityChecker {

	private @Autowired StationRolesRepository personStationRolesRepository;
	private @Autowired NetworkRepository networkRepository;
	private @Autowired StationRepository stationRepository;
	private @Autowired PostRepository postRepository;
	@Autowired
	private TrixAuthenticationProvider authProvider;

	public boolean canWrite(Post post){
		boolean canWrite = false;

		Person personLogged = authProvider.getLoggedPerson();

		if(personLogged != null && personLogged.id.equals(post.author.id)){
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

	public boolean canEdit(Post post){
		boolean canEdit = false;

		Person personLogged = authProvider.getLoggedPerson();
		if(personLogged != null){
			StationRole personStationRoles = personStationRolesRepository.findByStationAndPerson(post.station, personLogged);
			if(post.author.id == personLogged.id || (personStationRoles != null && (personStationRoles.editor || personStationRoles.admin))){
				canEdit = true;
			}
		}
		return canEdit && canWrite(post);
	}

	public boolean canRead(Post post){
		boolean canRead = false;

		Person personLogged = authProvider.getLoggedPerson();
		if(personLogged != null){
			if(post.station.visibility.equals(Station.UNRESTRICTED)){
				canRead = true;
			}else if(post.station.visibility.equals(Station.RESTRICTED_TO_NETWORKS)){
				List<Integer> networksId = networkRepository.findIdsByStation(post.station.id);
				List<Network> belongsToNetworks = networkRepository.belongsToNetworks(personLogged.id, networksId);
				if(belongsToNetworks != null && belongsToNetworks.size() > 0){
					canRead = true;
				}
			}else{
				List<Integer> stationsId = new ArrayList<Integer>();
				List<Station> belongsToStations = stationRepository.belongsToStations(personLogged.id, stationsId);
				if(belongsToStations != null && belongsToStations.size() > 0){
					canRead = true;
				}
			}
		}
		return canRead;
	}

	public boolean canRemove(Post post){
		return canEdit(post);
	}

	public boolean canWrite(Comment comment){
		return canWrite(comment.post);
	}

	public boolean canEdit(Comment comment){
		return canEdit(comment.post);
	}

	public boolean canRead(Comment comment){
		return canRead(comment.post);
	}

	public boolean canRemove(Comment comment){
		return canEdit(comment.post);
	}

	public boolean canComment(Comment comment) {
		if((comment.post.station.allowComments || canWrite(comment.post)) && !comment.author.username.equals("wordrails"))
			return true;
		else
			return false;
	}

}