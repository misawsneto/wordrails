package co.xarx.trix.security;

import co.xarx.trix.domain.Person;
import co.xarx.trix.domain.StationRole;
import co.xarx.trix.domain.Taxonomy;
import co.xarx.trix.persistence.StationRolesRepository;
import co.xarx.trix.services.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TaxonomySecurityChecker {

	@Autowired
	private StationRolesRepository personStationRolesRepository;
	@Autowired
	private AuthService authProvider;
	
	public boolean canCreate(Taxonomy taxonomy){
		Person person = authProvider.getLoggedPerson();

		boolean canCreate = false;
		if(person != null){
			if (person.networkAdmin) return true;

			if(taxonomy.type.equals(Taxonomy.STATION_TAXONOMY)){
				StationRole personStationRoles = personStationRolesRepository.findByStationAndPerson(taxonomy.owningStation, person);
				if(personStationRoles != null && (personStationRoles.admin || personStationRoles.editor))
					canCreate = true;
			}
		}

		return canCreate;
	}
	
	public boolean canEdit(Taxonomy taxonomy){
		return canCreate(taxonomy);
	}
}