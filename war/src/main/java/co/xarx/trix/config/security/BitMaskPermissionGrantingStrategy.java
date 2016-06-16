package co.xarx.trix.config.security;


import org.springframework.security.acls.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BitMaskPermissionGrantingStrategy implements PermissionGrantingStrategy {

	@Override
	public boolean isGranted(Acl acl, List<Permission> permission, List<Sid> sids, boolean administrativeMode) {

		List<Permission> permissionsToCheckOnParent = new ArrayList<>();
		permissionsToCheckOnParent.addAll(permission);
		boolean hasPermission = false;
		p: for (Permission p : permission) {
			List<AccessControlEntry> entries = filterAceByPermissionAndSids(acl.getEntries(), p, sids);

			if (entries == null)
				continue;

			for (AccessControlEntry entry : entries) {

				boolean toDeny = !entry.isGranting();
				if (toDeny) {
					hasPermission = false;
					permissionsToCheckOnParent.remove(p); //don't need to check on parent, since this permission is
					// already denied
					continue p; //for this permission it is denied. let's go to next to see if it can have a
					// permission that is allowed on all sids
				} else {
					hasPermission = true;
				}
			}

			if (hasPermission)
				break;
		}

		boolean hasParent = acl.isEntriesInheriting() && (acl.getParentAcl() != null);
		if (hasParent) {
			hasPermission &= acl.getParentAcl().isGranted(permissionsToCheckOnParent, sids, false);
		}

		return hasPermission;
	}


	private List<AccessControlEntry> filterAceByPermissionAndSids(List<AccessControlEntry> entries,
																  Permission p, List<Sid> sids) {
		return entries.stream()
				.filter(entry -> sids.contains(entry.getSid()) && Permissions.containsPermission(entry.getPermission(), p))
				.collect(Collectors.toList());
	}
}