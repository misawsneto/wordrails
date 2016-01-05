package co.xarx.trix.security;

import co.xarx.trix.domain.NetworkRole;
import co.xarx.trix.domain.Person;
import co.xarx.trix.domain.StationRole;
import co.xarx.trix.domain.Taxonomy;
import co.xarx.trix.persistence.NetworkRolesRepository;
import co.xarx.trix.persistence.StationRolesRepository;
import co.xarx.trix.security.auth.TrixAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TaxonomySecurityChecker {

	@Autowired
	private StationRolesRepository personStationRolesRepository;
	@Autowired
	private NetworkRolesRepository personNetworkRolesRepository;
	@Autowired
	private TrixAuthenticationProvider authProvider;
	
	public boolean canCreate(Taxonomy taxonomy){
		Person person = authProvider.getLoggedPerson();

		boolean canCreate = false;
		if(person != null){
			if(taxonomy.type.equals(Taxonomy.STATION_TAXONOMY) || taxonomy.type.equals(Taxonomy.STATION_TAG_TAXONOMY) || taxonomy.type.equals(Taxonomy.STATION_AUTHOR_TAXONOMY)){
				StationRole personStationRoles = personStationRolesRepository.findByStationAndPerson(taxonomy.owningStation, person);
				if(personStationRoles != null && (personStationRoles.admin || personStationRoles.editor))
					canCreate = true;
				else {
					NetworkRole personNetworkRole = personNetworkRolesRepository.findByPerson(person);
					if(personNetworkRole != null && personNetworkRole.admin)
						canCreate = true;
				}

			}else if(taxonomy.type.equals(Taxonomy.NETWORK_TAXONOMY)){
				NetworkRole personNetworkRole = personNetworkRolesRepository.findByPerson(person);
				if(personNetworkRole != null && personNetworkRole.admin){
					canCreate = true;
				}
			}
		}
		return canCreate;
	}
	
	public boolean canEdit(Taxonomy taxonomy){
		Person person = authProvider.getLoggedPerson();
		if(taxonomy.type.equals(Taxonomy.STATION_TAG_TAXONOMY)){
			StationRole personStationRoles = personStationRolesRepository.findByStationAndPerson(taxonomy.owningStation, person);
			if(personStationRoles != null && (personStationRoles.writer)){
				return true;
			}
		}
		return canCreate(taxonomy);
	}
}