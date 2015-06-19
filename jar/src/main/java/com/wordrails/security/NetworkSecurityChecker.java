package com.wordrails.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wordrails.business.AccessControllerUtil;
import com.wordrails.business.Network;
import com.wordrails.business.NetworkRole;
import com.wordrails.business.Person;
import com.wordrails.persistence.NetworkRepository;
import com.wordrails.persistence.NetworkRolesRepository;

@Component
public class NetworkSecurityChecker {
	
	private @Autowired AccessControllerUtil accessControllerUtil;
	private @Autowired NetworkRepository networkRepository;
	private @Autowired NetworkRolesRepository personNetworkRolesRepository;
	
	public boolean isNetworkAdmin(Network network){
		boolean isAdmin = false;
		Person personLogged = accessControllerUtil.getLoggedPerson();
		if(personLogged != null){
			NetworkRole personNetworkRole = personNetworkRolesRepository.findByNetworkAndPerson(network, personLogged);
			if(personNetworkRole != null && personNetworkRole.admin){
				isAdmin = true;
			}
		}
		return isAdmin;
	}
	
	public boolean belongsToNetwork(Network network){
		boolean belongsToNetwork = false;
		Person personLogged = accessControllerUtil.getLoggedPerson();
		if(personLogged != null){
			NetworkRole personNetworkRole = personNetworkRolesRepository.findByNetworkAndPerson(network, personLogged);
			if(personNetworkRole != null){
				belongsToNetwork = true;
			}
		}
		return belongsToNetwork;
	}
}