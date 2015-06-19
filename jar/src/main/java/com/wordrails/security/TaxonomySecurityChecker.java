package com.wordrails.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wordrails.business.AccessControllerUtil;
import com.wordrails.business.NetworkRole;
import com.wordrails.business.Person;
import com.wordrails.business.StationRole;
import com.wordrails.business.Taxonomy;
import com.wordrails.persistence.NetworkRolesRepository;
import com.wordrails.persistence.StationRolesRepository;

@Component
public class TaxonomySecurityChecker {
	
	private @Autowired StationRolesRepository personStationRolesRepository;
	private @Autowired NetworkRolesRepository personNetworkRolesRepository;
	private @Autowired AccessControllerUtil accessControllerUtil;
	
	public boolean canCreate(Taxonomy taxonomy){
		Person person = accessControllerUtil.getLoggedPerson();
		boolean canCreate = false;
		if(person != null){
			if(taxonomy.type.equals(Taxonomy.STATION_TAXONOMY) || taxonomy.type.equals(Taxonomy.STATION_TAG_TAXONOMY) || taxonomy.type.equals(Taxonomy.STATION_AUTHOR_TAXONOMY)){
				StationRole personStationRoles = personStationRolesRepository.findByStationAndPerson(taxonomy.owningStation, person);
				if(personStationRoles != null && (personStationRoles.admin || personStationRoles.editor)){
					canCreate = true;
				}
			}else if(taxonomy.type.equals(Taxonomy.NETWORK_TAXONOMY)){
				NetworkRole personNetworkRole = personNetworkRolesRepository.findByNetworkAndPerson(taxonomy.owningNetwork, person);
				if(personNetworkRole != null && personNetworkRole.admin){
					canCreate = true;
				}
			}
		}
		return canCreate;
	}
	
	public boolean canEdit(Taxonomy taxonomy){
		Person person = accessControllerUtil.getLoggedPerson();
		if(taxonomy.type.equals(Taxonomy.STATION_TAG_TAXONOMY)){
			StationRole personStationRoles = personStationRolesRepository.findByStationAndPerson(taxonomy.owningStation, person);
			if(personStationRoles != null && (personStationRoles.writer)){
				return true;
			}
		}
		return canCreate(taxonomy);
	}
}