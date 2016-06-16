package co.xarx.trix.services.security;

import co.xarx.trix.api.v2.PermissionData;
import co.xarx.trix.api.v2.UserPermissionData;
import co.xarx.trix.config.multitenancy.TenantContextHolder;
import co.xarx.trix.config.security.Permissions;
import co.xarx.trix.domain.Person;
import co.xarx.trix.domain.Station;
import co.xarx.trix.persistence.PersonRepository;
import co.xarx.trix.persistence.StationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.springframework.security.acls.domain.BasePermission.CREATE;

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

	public UserPermissionData getPermissions(Sid sid) {
		List<Integer> stationIds = stationRepository.findIds(TenantContextHolder.getCurrentTenantId());

		UserPermissionData result = new UserPermissionData();
		Map<ObjectIdentity, MutableAcl> acls = aclService.findAcls(stationIds);
		for (ObjectIdentity oi : acls.keySet()) {
			Acl acl = acls.get(oi);
			int stationId = Math.toIntExact((long) oi.getIdentifier());
			List<AccessControlEntry> aces = aclService.findAce(acl.getEntries(), sid);

			if (aces != null) {
				for (AccessControlEntry ace : aces) {
					PermissionData permissionData = aclService.getPermissionData(ace.getPermission());
					result.getStationPermissions().add(result.new Permission(stationId, permissionData));
				}
			}
		}

		return result;
	}

	public List<Station> getStationsWithPermission(Sid sid, Permission p) {
		List<Station> result = new ArrayList<>();
		List<Station> stations = stationRepository.findAll();

		for (Station station : stations) {
			boolean hasPermission = aclService.hasPermission(Station.class, station.getId(), sid, p);
			boolean isPermissionOfCreate = Permissions.containsPermission(p, CREATE);
			if(isPermissionOfCreate)
				hasPermission = hasPermission || station.isWritable();

			if(hasPermission)
				result.add(station);
		}

		return result;
	}

	public UserPermissionData getPermissions(Sid sid, Integer stationId) {
		UserPermissionData result = new UserPermissionData();

		MutableAcl acl = aclService.findAcl(stationId);
		List<AccessControlEntry> aces = aclService.findAce(acl.getEntries(), sid);

		for (AccessControlEntry ace : aces) {
			PermissionData permissionData = aclService.getPermissionData(ace.getPermission());
			UserPermissionData.Permission permission = result.new Permission(stationId, permissionData);
			result.getStationPermissions().add(permission);
		}

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
