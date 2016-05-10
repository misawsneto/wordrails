package co.xarx.trix.services.security;

import co.xarx.trix.api.v2.PermissionData;
import co.xarx.trix.api.v2.UserPermissionData;
import co.xarx.trix.config.multitenancy.TenantContextHolder;
import co.xarx.trix.config.security.Permissions;
import co.xarx.trix.domain.Person;
import co.xarx.trix.persistence.PersonRepository;
import co.xarx.trix.persistence.StationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.AccessControlEntry;
import org.springframework.security.acls.model.Acl;
import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class PersonPermissionService {

	private AccessControlListService aclService;
	private StationRepository stationRepository;
	private PersonRepository personRepository;

	@Autowired
	public PersonPermissionService(AccessControlListService aclService, StationRepository stationRepository, PersonRepository personRepository) {
		this.aclService = aclService;
		this.stationRepository = stationRepository;
		this.personRepository = personRepository;
	}

	public UserPermissionData getPermissions(String username) {
		List<Integer> stationIds = stationRepository.findIds(TenantContextHolder.getCurrentTenantId());

		UserPermissionData result = new UserPermissionData(username);
		Map<ObjectIdentity, MutableAcl> acls = aclService.findAcls(stationIds);
		for (ObjectIdentity oi : acls.keySet()) {
			Acl acl = acls.get(oi);
			int stationId = Math.toIntExact((long) oi.getIdentifier());
			AccessControlEntry ace = aclService.findAce(acl.getEntries(), username);

			PermissionData permissionData = aclService.getPermissionData(ace.getPermission());
			result.getStationPermissions().add(result.new Permission(stationId, permissionData));
		}

		return result;
	}

	public UserPermissionData getPermissions(String username, Integer stationId) {
		UserPermissionData result = new UserPermissionData(username);

		MutableAcl acl = aclService.findAcl(stationId);
		AccessControlEntry ace = aclService.findAce(acl.getEntries(), username);

		PermissionData permissionData = aclService.getPermissionData(ace.getPermission());
		UserPermissionData.Permission permission = result.new Permission(stationId, permissionData);
		result.getStationPermissions().add(permission);

		return result;
	}

	public Iterable<Person> getPersonFromStation(Integer stationId) {
		MutableAcl acl = aclService.findAcl(stationId);
		List<Person> persons = new ArrayList<>();
		for (AccessControlEntry entry : acl.getEntries()) {
			boolean isNotUserPermission = !(entry.getSid() instanceof PrincipalSid);

			if (isNotUserPermission) {
				continue;
			}

			if (!entry.isGranting() && Permissions.containsPermission(entry.getPermission(), Permissions.READ)) {
				continue;
			}

			PrincipalSid sid = (PrincipalSid) entry.getSid();
			persons.add(personRepository.findByUsername(sid.getPrincipal()));
		}
		return persons;
	}
}
