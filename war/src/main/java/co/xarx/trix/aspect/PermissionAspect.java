package co.xarx.trix.aspect;

import co.xarx.trix.config.multitenancy.TenantContextHolder;
import co.xarx.trix.domain.Comment;
import co.xarx.trix.domain.Post;
import co.xarx.trix.domain.Station;
import co.xarx.trix.security.acl.MultitenantPrincipalSid;
import co.xarx.trix.security.acl.TrixPermission;
import co.xarx.trix.services.auth.AuthService;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.model.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class PermissionAspect {

	private AuthService authService;
	private MutableAclService aclService;

	@Autowired
	public PermissionAspect(AuthService authService, MutableAclService aclService) {
		this.authService = authService;
		this.aclService = aclService;
	}

	private void savePermission(Class clazz, Integer id, Permission p) {
		String username = authService.getLoggedUsername();
		ObjectIdentity oi = new ObjectIdentityImpl(clazz, id);

		String tenantId = TenantContextHolder.getCurrentTenantId();
		Sid sid = new MultitenantPrincipalSid(username, tenantId);

		MutableAcl acl;
		try {
			acl = aclService.createAcl(oi);
		} catch (AlreadyExistsException e) {
			return;
		}

		acl.insertAce(acl.getEntries().size(), p, sid, true);
		aclService.updateAcl(acl);
	}

	private void saveWithParent(Class clazz, Integer id, Class parentClazz, Integer parentId) {
		ObjectIdentity oi = new ObjectIdentityImpl(clazz, id);

		MutableAcl acl;
		try {
			acl = aclService.createAcl(oi);
		} catch (AlreadyExistsException e) {
			return;
		}

		MutableAcl parentAcl;
		ObjectIdentity parentOi = new ObjectIdentityImpl(parentClazz, parentId);
		parentAcl = (MutableAcl) aclService.readAclById(parentOi);

		acl.setParent(parentAcl);
		aclService.updateAcl(acl);
	}

	@AfterReturning("within(co.xarx.trix.persistence.StationRepository+) && execution(* *..save(*)) && args(entity)")
	public void saveStation(Station entity) {
		savePermission(Station.class, entity.getId(), TrixPermission.ADMINISTRATION);
	}

	@AfterReturning("within(co.xarx.trix.persistence.PostRepository+) && execution(* *..save(*)) && args(entity)")
	public void savePost(Post entity) {
		saveWithParent(Post.class, entity.getId(), Station.class, entity.station.id);
	}

	@AfterReturning("within(co.xarx.trix.persistence.CommentRepository+) && execution(* *..save(*)) && args(entity)")
	public void saveComment(Comment entity) {
		saveWithParent(Comment.class, entity.getId(), Post.class, entity.post.id);
	}
}
