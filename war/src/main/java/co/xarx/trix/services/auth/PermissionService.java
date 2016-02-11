package co.xarx.trix.services.auth;

import co.xarx.trix.domain.Station;
import co.xarx.trix.persistence.PersonRepository;
import co.xarx.trix.security.acl.TrixPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.*;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

@Service
public class PermissionService {

	private AuthService authService;
	private MutableAclService aclService;
	private PersonRepository personRepository;

	@Autowired
	public PermissionService(AuthService authService, MutableAclService aclService, PersonRepository personRepository) {
		this.authService = authService;
		this.aclService = aclService;
		this.personRepository = personRepository;
	}

	public void updateStationsPermissions(List<String> usernames, List<Integer> stationIds, boolean publisher, boolean editor, boolean admin) {
		Assert.notEmpty(usernames, "Usernames must have elements");
		Assert.notEmpty(stationIds, "Station ids must have elements");


		int permissionMask = TrixPermission.READ.getMask();

		//in simple words, the three next lines adds the following permissions to the permission list
		if (publisher) permissionMask |= TrixPermission.PUBLISHER.getMask(); //I never thought I'd use the |= operator
		if (editor) permissionMask |= TrixPermission.EDITOR.getMask();
		if (admin) permissionMask |= TrixPermission.ADMINISTRATION.getMask();

		Permission p = new TrixPermission(permissionMask);

		List<MutableAcl> acls = findAcls(stationIds);
		for (MutableAcl acl : acls) {
			List<AccessControlEntry> entries = acl.getEntries();
			for (String username : usernames) {
				AccessControlEntry ace = findAce(entries, username, acl.getObjectIdentity());
				if(ace == null) {
					Sid sid = new PrincipalSid(username);
					acl.insertAce(acl.getEntries().size(), p, sid, true);
				} else {
					acl.updateAce(entries.indexOf(ace), p);
				}

				aclService.updateAcl(acl);
			}
		}
	}

	private AccessControlEntry findAce(List<AccessControlEntry> entries, String username, ObjectIdentity objectIdentity) {
		for (AccessControlEntry entry : entries) {
			if(entry.getAcl().getObjectIdentity().equals(objectIdentity) && entry.getSid().equals(new PrincipalSid(username))) {
				return entry;
			}
		}

		return null;
	}

	private List<MutableAcl> findAcls(List<Integer> stationIds) {
		List<MutableAcl> acls = new ArrayList<>();
		for (Integer stationId : stationIds) {
			ObjectIdentity oi = new ObjectIdentityImpl(Station.class, stationId);

			MutableAcl acl;
			try {
				acl = (MutableAcl) aclService.readAclById(oi);
			} catch (NotFoundException e) {
				acl = aclService.createAcl(oi);
			}

			acls.add(acl);
		}

		return acls;
	}

	public void deleteStationPermissions(List<String> usernames, List<Integer> stationIds) {
		List<MutableAcl> acls = findAcls(stationIds);
		for (MutableAcl acl : acls) {
			for (int i = 0; i < acl.getEntries().size(); i++) {
				AccessControlEntry ace = acl.getEntries().get(i);
				if (usernames.contains(ace.getSid().toString())) {
					acl.deleteAce(i);
				}
			}
		}
	}
}
