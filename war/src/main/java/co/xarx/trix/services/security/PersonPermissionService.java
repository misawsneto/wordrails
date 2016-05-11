package co.xarx.trix.services.security;

import co.xarx.trix.api.v2.PermissionData;
import co.xarx.trix.api.v2.UserPermissionData;
import co.xarx.trix.config.multitenancy.TenantContextHolder;
import co.xarx.trix.persistence.StationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.acls.model.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PersonPermissionService {

	private AccessControlListService aclService;
	private StationRepository stationRepository;

	@Autowired
	public PersonPermissionService(AccessControlListService aclService,
								   StationRepository stationRepository) {
		this.aclService = aclService;
		this.stationRepository = stationRepository;
	}

	public UserPermissionData getPermissions(Sid sid) {
		List<Integer> stationIds = stationRepository.findIds(TenantContextHolder.getCurrentTenantId());

		UserPermissionData result = new UserPermissionData();
		Map<ObjectIdentity, MutableAcl> acls = aclService.findAcls(stationIds);
		for (ObjectIdentity oi : acls.keySet()) {
			Acl acl = acls.get(oi);
			int stationId = Math.toIntExact((long) oi.getIdentifier());
			AccessControlEntry ace = aclService.findAce(acl.getEntries(), sid);

			if (ace != null) {
				PermissionData permissionData = aclService.getPermissionData(ace.getPermission());
				result.getStationPermissions().add(result.new Permission(stationId, permissionData));
			}
		}

		return result;
	}

	public UserPermissionData getPermissions(Sid sid, Integer stationId) {
		UserPermissionData result = new UserPermissionData();

		MutableAcl acl = aclService.findAcl(stationId);
		AccessControlEntry ace = aclService.findAce(acl.getEntries(), sid);

		PermissionData permissionData = aclService.getPermissionData(ace.getPermission());
		UserPermissionData.Permission permission = result.new Permission(stationId, permissionData);
		result.getStationPermissions().add(permission);

		return result;
	}
}
