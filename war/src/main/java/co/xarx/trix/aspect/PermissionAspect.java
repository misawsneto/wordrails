package co.xarx.trix.aspect;

import co.xarx.trix.annotation.IntegrationTestBean;
import co.xarx.trix.config.multitenancy.TenantContextHolder;
import co.xarx.trix.config.security.MultitenantPrincipalSid;
import co.xarx.trix.config.security.Permissions;
import co.xarx.trix.domain.Comment;
import co.xarx.trix.domain.Post;
import co.xarx.trix.domain.Station;
import co.xarx.trix.services.security.AuthService;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.model.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
@IntegrationTestBean
public class PermissionAspect {

	private AuthService authService;
	private MutableAclService aclService;

	@Autowired
	public PermissionAspect(AuthService authService, MutableAclService aclService) {
		this.authService = authService;
		this.aclService = aclService;
	}

	@AfterReturning("within(co.xarx.trix.persistence.StationRepository+) && execution(* *..save(*)) && args(entity)")
	public void saveStation(Station entity) {
		MutableAcl acl = savePermission(Station.class, entity.getId(), Permissions.getAdmin());
		if(acl == null) {
			acl = getAcl(Station.class, entity.getId());
		}
		boolean anonymousCanRead = entity.visibility.equals(Station.UNRESTRICTED);
		if(acl != null) {
			setPermission(Permissions.READ, new GrantedAuthoritySid("ROLE_ANONYMOUS"), acl, anonymousCanRead);
			setPermission(Permissions.getAdmin(), new GrantedAuthoritySid("ROLE_ADMIN"), acl, true);
		}

		if (acl != null) {
			aclService.updateAcl(acl);
		}
	}

	@AfterReturning("within(co.xarx.trix.persistence.PostRepository+) && execution(* *..save(*)) && args(entity)")
	public void savePost(Post entity) {
		Permission permission = Permissions.getOwner();
		MutableAcl acl = savePermissionWithParent(Post.class, entity.getId(), permission, Station.class, entity.station.id);

		if (acl != null) {
			aclService.updateAcl(acl);
		}
	}

	@AfterReturning("within(co.xarx.trix.persistence.CommentRepository+) && execution(* *..save(*)) && args(entity)")
	public void saveComment(Comment entity) {
		Permission permission = Permissions.getOwner();
		MutableAcl acl = savePermissionWithParent(Comment.class, entity.getId(), permission, Post.class, entity.post.id);

		if (acl != null) {
			aclService.updateAcl(acl);
		}
	}

	private MutableAcl savePermission(Class clazz, Integer id, Permission permission) {
		MutableAcl acl = createAcl(clazz, id);
		if(acl != null)
			setPermission(permission, getSid(), acl, true);

		return acl;
	}

	private MutableAcl savePermissionWithParent(Class clazz, Integer id, Permission permission, Class parentClazz, Integer parentId) {
		MutableAcl acl = getAcl(clazz, id);

		if (acl != null) {
			setParent(parentClazz, parentId, acl);
			setPermission(permission, getSid(), acl, true);
		}

		return acl;
	}

	private MutableAcl createAcl(Class clazz, Integer id) {
		ObjectIdentity oi = new ObjectIdentityImpl(clazz, id);

		try {
			return aclService.createAcl(oi);
		} catch (AlreadyExistsException e) {
			return null;
		}
	}

	private MutableAcl getAcl(Class clazz, Integer id) {
		ObjectIdentity oi = new ObjectIdentityImpl(clazz, id);

		try {
			return (MutableAcl) aclService.readAclById(oi);
		} catch (NotFoundException e) {
			return aclService.createAcl(oi);
		}
	}

	private Sid getSid() {
		String username = authService.getLoggedUsername();
		String tenantId = TenantContextHolder.getCurrentTenantId();
		return new MultitenantPrincipalSid(username, tenantId);
	}

	private void setPermission(Permission permission, Sid sid, MutableAcl acl, boolean granting) {
		for (int i = 0; i < acl.getEntries().size(); i++) {
			AccessControlEntry ace = acl.getEntries().get(i);
			if (ace.isGranting() == granting && ace.getSid().equals(sid)) {
				if(permission.getMask() == ace.getPermission().getMask()) {
					return;
				}

				acl.updateAce(i, permission);
				return;
			}
		}

		acl.insertAce(acl.getEntries().size(), permission, sid, granting);
	}

	private void setParent(Class parentClazz, Integer parentId, MutableAcl acl) {
		MutableAcl parentAcl;
		ObjectIdentity parentOi = new ObjectIdentityImpl(parentClazz, parentId);
		parentAcl = (MutableAcl) aclService.readAclById(parentOi);

		acl.setParent(parentAcl);
		aclService.updateAcl(acl);
	}
}
