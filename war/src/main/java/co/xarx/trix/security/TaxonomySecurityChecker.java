package co.xarx.trix.security;

import co.xarx.trix.config.multitenancy.TenantContextHolder;
import co.xarx.trix.security.auth.TrixAuthenticationProvider;
import co.xarx.trix.domain.*;
import co.xarx.trix.persistence.NetworkRolesRepository;
import co.xarx.trix.persistence.NetworkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.xarx.trix.persistence.StationRolesRepository;

@Component
public class TaxonomySecurityChecker {
	
	private @Autowired StationRolesRepository personStationRolesRepository;
	private @Autowired
	NetworkRolesRepository personNetworkRolesRepository;
	private @Autowired NetworkSecurityChecker networkSecurityChecker;
	private @Autowired NetworkRepository networkRepository;
	@Autowired
	private TrixAuthenticationProvider authProvider;
	
	public boolean canCreate(Taxonomy taxonomy){
		Person person = authProvider.getLoggedPerson();
		Network network = networkRepository.findOne(person.networkId);
//		Network network = networkRepository.findOne(TenantContextHolder.getCurrentNetworkId());

		boolean canCreate = false;
		if(person != null){
			if(taxonomy.type.equals(Taxonomy.STATION_TAXONOMY)){
				StationRole personStationRoles = personStationRolesRepository.findByStationAndPerson(taxonomy.owningStation, person);
				if(personStationRoles != null && (personStationRoles.admin || personStationRoles.editor))
					canCreate = true;
				else {
					NetworkRole personNetworkRole = personNetworkRolesRepository.findByNetworkAndPerson(network, person);
					if(personNetworkRole != null && personNetworkRole.admin)
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
		return canCreate(taxonomy);
	}
}