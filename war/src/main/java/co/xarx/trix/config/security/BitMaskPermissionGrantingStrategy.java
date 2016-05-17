package co.xarx.trix.config.security;


import org.springframework.security.acls.domain.AuditLogger;
import org.springframework.security.acls.model.*;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.List;

public class BitMaskPermissionGrantingStrategy implements PermissionGrantingStrategy {

	private final transient AuditLogger auditLogger;

	public BitMaskPermissionGrantingStrategy(AuditLogger auditLogger) {
		Assert.notNull(auditLogger, "auditLogger cannot be null");
		this.auditLogger = auditLogger;
	}

	private Integer getObjectId(Serializable identifier) {
		Integer result = 0;

		if(identifier instanceof Long) {
			result = ((Long) identifier).intValue();
		} else if(identifier instanceof Integer) {
			result = ((Integer) identifier);
		}

		return result;
	}

	@Override
	public boolean isGranted(Acl acl, List<Permission> permission, List<Sid> sids, boolean administrativeMode) {
		final List<AccessControlEntry> aces = acl.getEntries();

		AccessControlEntry firstRejection = null;

		for (Permission p : permission) {
			g:for (Sid sid : sids) {
				for (AccessControlEntry ace : aces) {
					boolean belongsToCurrentSid = ace.getSid().equals(sid);
					if(!belongsToCurrentSid) {
						continue;
					}

					Permission cumulativePermission = ace.getPermission();
					boolean isAdmin = containsPermission(cumulativePermission, Permissions.ADMINISTRATION);
					if(isAdmin && ace.isGranting()) {
						return true;
					}

					//Bit-wise comparison
					if (containsPermission(cumulativePermission, p)) {
						// Found a matching ACE, so its authorization decision will prevail
						if (ace.isGranting()) {
							// Success
							if (!administrativeMode) {
								auditLogger.logIfNeeded(true, ace);
							}

							return true;
						}

						// Failure for this permission, so stop search
						// We will see if they have a different permission
						// (this permission is 100% rejected for this SID)
						// Store first rejection for auditing reasons
						if (firstRejection == null) {
							firstRejection = ace;
						}

						break g; // exit sids loop
					}
				}
			}
		}

		if (firstRejection != null) {
			// We found an ACE to reject the request at this point, as no
			// other ACEs were found that granted a different permission
			if (!administrativeMode) {
				auditLogger.logIfNeeded(false, firstRejection);
			}

			return false;
		}

		// No matches have been found so far
		if (acl.isEntriesInheriting() && (acl.getParentAcl() != null)) {
			// We have a parent, so let them try to find a matching ACE
			return acl.getParentAcl().isGranted(permission, sids, false);
		} else {
			// We either have no parent, or we're the uppermost parent
			throw new NotFoundException("Unable to locate a matching ACE for passed permissions and SIDs");
		}
	}

	private boolean containsPermission(Permission cumulativePermission, Permission singlePermission) {
		return (cumulativePermission.getMask() & singlePermission.getMask()) == singlePermission.getMask();
	}
}