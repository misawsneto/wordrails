package co.xarx.trix.security;

import co.xarx.trix.security.auth.TrixAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.xarx.trix.domain.Network;
import co.xarx.trix.domain.NetworkRole;
import co.xarx.trix.domain.Person;
import co.xarx.trix.persistence.NetworkRepository;
import co.xarx.trix.persistence.NetworkRolesRepository;

@Component
public class NetworkSecurityChecker {

	@Autowired
	private TrixAuthenticationProvider authProvider;
	private @Autowired NetworkRepository networkRepository;
	private @Autowired NetworkRolesRepository personNetworkRolesRepository;
	
	public boolean isNetworkAdmin(Network network){
		boolean isAdmin = false;
		Person personLogged = authProvider.getLoggedPerson();
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
		Person personLogged = authProvider.getLoggedPerson();
		if(personLogged != null){
			NetworkRole personNetworkRole = personNetworkRolesRepository.findByNetworkAndPerson(network, personLogged);
			if(personNetworkRole != null){
				belongsToNetwork = true;
			}
		}
		return belongsToNetwork;
	}
}