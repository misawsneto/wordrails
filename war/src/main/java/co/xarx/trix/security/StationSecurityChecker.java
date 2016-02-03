package co.xarx.trix.security;

import co.xarx.trix.domain.Person;
import co.xarx.trix.domain.Station;
import co.xarx.trix.domain.StationRole;
import co.xarx.trix.persistence.NetworkRepository;
import co.xarx.trix.persistence.PersonRepository;
import co.xarx.trix.persistence.StationRolesRepository;
import co.xarx.trix.security.auth.TrixAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StationSecurityChecker {

	@Autowired
	private TrixAuthenticationProvider authProvider;
	private @Autowired NetworkRepository networkRepository;
	private @Autowired StationRolesRepository stationRolesRepository;
	private @Autowired PersonRepository personRepository;
	
	public boolean canEdit(Station station){
		return isStationAdminOrEditor(station);
	}

	private boolean isNetworkAdmin(Person personLogged) {
		if (personLogged != null) {
			if (personLogged.networkAdmin) return true;
		}

		return false;
	}
	
	public boolean isStationAdmin(Station station){
		Person personLogged = authProvider.getLoggedPerson();

		boolean isAdmin = false;
		if(!isNetworkAdmin(personLogged) && personLogged != null) {
			StationRole personStationRole = stationRolesRepository.findByStationAndPerson(station, personLogged);
			if(personStationRole != null && personStationRole.admin){
				isAdmin = true;
			}
		}
		return isAdmin;
	}
	
	public boolean isStationsAdmin(List<Integer> stationIds){
		Person personLogged = authProvider.getLoggedPerson();

		boolean isAdmin = false;
		if (!isNetworkAdmin(personLogged) && personLogged != null) {
			List<StationRole> personStationRoles = stationRolesRepository.findByPersonAndStationIds(personLogged.id, stationIds);
			
			for (StationRole stationRole : personStationRoles) {
				if(!stationRole.admin)
					return false;
			}
			
			return true;
		}
		return isAdmin;
	}
	
	public boolean isStationAdminOrEditor(Station station){
		Person personLogged = authProvider.getLoggedPerson();

		boolean isEditorOrAdmin = false;
		if (!isNetworkAdmin(personLogged) && personLogged != null) {
			StationRole personStationRole = stationRolesRepository.findByStationAndPerson(station, personLogged);
			if(personStationRole != null && (personStationRole.editor || personStationRole.admin)){
				isEditorOrAdmin = true;
			}
		}
		return isEditorOrAdmin;
	}
	
	public boolean canVisualize(Station station){
		boolean canVisualize = false;
		
		Person personLogged = authProvider.getLoggedPerson();
		if(personLogged != null){
			if(station.visibility.equals(Station.UNRESTRICTED)){
				canVisualize = true;
			}else{
				StationRole personStationRole = stationRolesRepository.findByStationAndPerson(station, personLogged);
				if(personStationRole != null){
					canVisualize = true;
				}
			}
		}
		return canVisualize;
	}
}