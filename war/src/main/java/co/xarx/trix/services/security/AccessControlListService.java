package co.xarx.trix.services.security;

import co.xarx.trix.api.v2.PermissionData;
import co.xarx.trix.config.security.Permissions;
import co.xarx.trix.domain.Station;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.model.*;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

import static co.xarx.trix.config.security.Permissions.MODERATION;
import static co.xarx.trix.config.security.Permissions.SPONSOR;
import static org.springframework.security.acls.domain.BasePermission.*;

@Component
public class AccessControlListService {

	private MutableAclService aclService;
	private PermissionGrantingStrategy grantingStrategy;

	@Autowired
	public AccessControlListService(MutableAclService aclService, PermissionGrantingStrategy grantingStrategy) {
		this.aclService = aclService;
		this.grantingStrategy = grantingStrategy;
	}

	public boolean hasPermission(Class clazz, Integer objectId, Sid sid, Permission... permission) {
		ObjectIdentityImpl oi = new ObjectIdentityImpl(clazz, objectId);
		Acl acl = aclService.readAclById(oi);

		return grantingStrategy.isGranted(acl, Arrays.asList(permission), Collections.singletonList(sid), false);
	}


	List<AccessControlEntry> findAce(List<AccessControlEntry> entries, Sid sid) {
		return entries.stream()
				.filter(entry -> entry.getSid().equals(sid))
				.collect(Collectors.toList());
	}

	PermissionData getPermissionData(Permission p) {
		PermissionData permissionData = new PermissionData();
		permissionData.setRead(Permissions.containsPermission(p, READ));
		permissionData.setWrite(Permissions.containsPermission(p, WRITE));
		permissionData.setCreate(Permissions.containsPermission(p, CREATE));
		permissionData.setDelete(Permissions.containsPermission(p, DELETE));
		permissionData.setAdministration(Permissions.containsPermission(p, ADMINISTRATION));
		permissionData.setModerate(Permissions.containsPermission(p, MODERATION));
		permissionData.setSponsor(Permissions.containsPermission(p, SPONSOR));

		return permissionData;
	}

	Map<ObjectIdentity, MutableAcl> findAcls(List<Integer> stationIds) {
		List<ObjectIdentity> ois = stationIds
				.stream()
				.map(stationId -> new ObjectIdentityImpl(Station.class, stationId))
				.collect(Collectors.toList());

		Map<ObjectIdentity, Acl> acls = aclService.readAclsById(ois);

		return getAclMap(acls);
	}

	MutableAcl findAcl(Integer stationId) {
		ObjectIdentity oi = new ObjectIdentityImpl(Station.class, stationId);

		MutableAcl acl;
		try {
			acl = (MutableAcl) aclService.readAclById(oi);
		} catch (NotFoundException e) {
			acl = aclService.createAcl(oi);
		}

		return acl;
	}

	Map<ObjectIdentity, MutableAcl> findAcls(List<Integer> stationIds, List<Sid> sids) {
		List<ObjectIdentity> ois = stationIds
				.stream()
				.map(stationId -> new ObjectIdentityImpl(Station.class, stationId))
				.collect(Collectors.toList());

		Map<ObjectIdentity, Acl> acls = aclService.readAclsById(ois, sids);

		return getAclMap(acls);
	}

	private Map<ObjectIdentity, MutableAcl> getAclMap(Map<ObjectIdentity, Acl> acls) {
		Map<ObjectIdentity, MutableAcl> result = new HashMap();
		for (ObjectIdentity oi : acls.keySet()) {
			result.put(oi, (MutableAcl) acls.get(oi));
		}
		return result;
	}


	public void updateAcl(MutableAcl acl) {
		aclService.updateAcl(acl);
	}
}