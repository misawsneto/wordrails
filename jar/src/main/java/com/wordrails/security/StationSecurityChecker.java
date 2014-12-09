package com.wordrails.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wordrails.business.AccessControllerUtil;
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
	
	private @Autowired AccessControllerUtil accessControllerUtil;
	private @Autowired NetworkRepository networkRepository;
	private @Autowired StationRolesRepository stationRolesRepository;
	private @Autowired NetworkRolesRepository networkRolesRepository;
	
	public boolean canCreate(Station station){
		Person personLogged = accessControllerUtil.getLoggedPerson();
		NetworkRole networkRole = networkRolesRepository.findByNetworkAndPerson(station.networks.iterator().next(), personLogged);
		return (networkRole != null && networkRole.admin);
	}
	
	public boolean canEdit(Station station){
		return isStationAdminOrEditor(station);
	}
	
	public boolean isStationAdmin(Station station){
		boolean isAdmin = false;
		Person personLogged = accessControllerUtil.getLoggedPerson();
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
	
	public boolean isStationAdminOrEditor(Station station){
		boolean isEditorOrAdmin = false;
		Person personLogged = accessControllerUtil.getLoggedPerson();
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
		
		Person personLogged = accessControllerUtil.getLoggedPerson();
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
		for (Network network : station.networks) {
			NetworkRole networkRole = networkRolesRepository.findByNetworkAndPerson(network, person);
			if(networkRole.admin){
				isAdmin = true;
				break;
			}
		}
		return isAdmin;
	}
}