package co.xarx.trix.config.security;

import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.model.*;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

class IMAcl implements Acl {

	private Class type;
	private Serializable id;
	private Acl parent;
	private Sid owner;
	private final List<Acl> children = new ArrayList<>();
	private final List<AccessControlEntry> aces = new ArrayList<>();
	private PermissionGrantingStrategy grantingStrategy = new BitMaskPermissionGrantingStrategy();

	IMAcl(Class type, Serializable id) {
		this.type = type;
		this.id = id;
	}

	@Override
	public List<AccessControlEntry> getEntries() {
		return aces;
	}

	public void addEntry(AccessControlEntry ace) {
		aces.add(ace);
	}

	@Override
	public ObjectIdentity getObjectIdentity() {
		return new ObjectIdentityImpl(type, id);
	}

	@Override
	public Sid getOwner() {
		return owner;
	}

	public void setParent(Acl parent) {
		Assert.isTrue(parent == null || !parent.equals(this),
				"Cannot be the parent of yourself");
		this.parent = parent;
		if(parent instanceof IMAcl) {
			((IMAcl) parent).children.add(this);
		}
	}

	@Override
	public Acl getParentAcl() {
		return parent;
	}

	@Override
	public boolean isEntriesInheriting() {
		return true;
	}

	@Override
	public boolean isGranted(List<Permission> permission, List<Sid> sids, boolean administrativeMode) throws NotFoundException, UnloadedSidException {
		return grantingStrategy.isGranted(this, permission, sids, administrativeMode);
	}

	@Override
	public boolean isSidLoaded(List<Sid> sids) {
		return true;
	}

	public void setOwner(Sid owner) {
		this.owner = owner;
	}
}
