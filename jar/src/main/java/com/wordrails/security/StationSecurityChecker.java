package com.wordrails.security;

import java.util.ArrayList;
import java.util.List;

import com.wordrails.auth.TrixAuthenticationProvider;
import com.wordrails.persistence.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wordrails.business.Network;
import com.wordrails.business.NetworkRole;
import com.wordrails.business.Person;
import com.wordrails.business.Station;
import com.wordrails.business.StationRole;
import com.wordrails.persistence.NetworkRepository;
import com.wordrails.persistence.NetworkRolesRepository;
import com.wordrails.persistence.StationRolesRepository;

@Component
public class StationSecurityChecker {

	@Autowired
	private TrixAuthenticationProvider authProvider;
	private @Autowired NetworkRepository networkRepository;
	private @Autowired StationRolesRepository stationRolesRepository;
	private @Autowired NetworkRolesRepository networkRolesRepository;
	private @Autowired PersonRepository personRepository;
	
	public boolean canCreate(Station station){
		Person personLogged = authProvider.getLoggedPerson();
		NetworkRole networkRole = networkRolesRepository.findByNetworkAndPerson(station.network, personLogged);
		return (networkRole != null && networkRole.admin);
	}
	
	public boolean canEdit(Station station){
		return isStationAdminOrEditor(station);
	}
	
	public boolean isStationAdmin(Station station){
		boolean isAdmin = false;
		Person personLogged = authProvider.getLoggedPerson();
		if(personLogged != null){
			StationRole personStationRole = stationRolesRepository.findByStationAndPerson(station, personLogged);
			if(personStationRole != null && personStationRole.admin){
				isAdmin = true;
			}else{
				isAdmin = isNetworkAdmin(station, personLogged);
			}
		}
		return isAdmin;
	}
	
	public boolean isStationsAdmin(List<Integer> stationIds){
		boolean isAdmin = false;
		Person personLogged = authProvider.getLoggedPerson();
		if(personLogged != null){
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
		boolean isEditorOrAdmin = false;
		Person personLogged = authProvider.getLoggedPerson();
		if(personLogged != null){
			StationRole personStationRole = stationRolesRepository.findByStationAndPerson(station, personLogged);
			if(personStationRole != null && (personStationRole.editor || personStationRole.admin)){
				isEditorOrAdmin = true;
			}else{
				isEditorOrAdmin = isNetworkAdmin(station, personLogged);
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
			}else if(station.visibility.equals(Station.RESTRICTED_TO_NETWORKS)){
				List<Integer> networksId = networkRepository.findIdsByStation(station.id);
				List<Network> belongsToNetworks = networkRepository.belongsToNetworks(personLogged.id, networksId);
				if(belongsToNetworks != null && belongsToNetworks.size() > 0){
					canVisualize = true;
				}
			}else{
				StationRole personStationRole = stationRolesRepository.findByStationAndPerson(station, personLogged);
				if(personStationRole != null){
					canVisualize = true;
				}
			}
		}
		return canVisualize;
	}
	
	private boolean isNetworkAdmin(Station station, Person person){
		boolean isAdmin = false;
//		for (Network network : station.networks) {
			NetworkRole networkRole = networkRolesRepository.findByNetworkAndPerson(station.network, person);
			if(networkRole != null && networkRole.admin){
				isAdmin = true;
//				break;
			}
//		}
		return isAdmin;
	}

	public boolean isAdmin() {
		Person person = authProvider.getLoggedPerson();
		return personRepository.isAdmin(person.id) > 0;
	}
}