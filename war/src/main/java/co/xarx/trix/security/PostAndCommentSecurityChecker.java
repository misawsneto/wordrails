package co.xarx.trix.security;

import co.xarx.trix.domain.*;
import co.xarx.trix.persistence.*;
import co.xarx.trix.services.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PostAndCommentSecurityChecker {

	@Autowired
	private StationRolesRepository personStationRolesRepository;
	@Autowired
	private
	StationRepository stationRepository;
	@Autowired
	private AuthService authProvider;

	public boolean canWrite(Post post){
		boolean canWrite = false;

		Person personLogged = authProvider.getLoggedPerson();

		if(personLogged != null && personLogged.id.equals(post.author.id)){
			Station station = post.station;
			if(station.visibility.equals(Station.UNRESTRICTED) && station.writable){
				canWrite = true;
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
			if(personLogged.networkAdmin || (post.author.id.equals(personLogged.id) || (personStationRoles != null && (personStationRoles.editor || personStationRoles.admin)))){
				canEdit = true;
			}
		}
		return canEdit;
	}

	public boolean canRead(Post post){
		boolean canRead = false;

		Person personLogged = authProvider.getLoggedPerson();
		if(personLogged != null){
			if(post.station.visibility.equals(Station.UNRESTRICTED)){
				canRead = true;
			}else{
				List<Integer> stationsId = new ArrayList<>();
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