package co.xarx.trix.aspect;

import co.xarx.trix.domain.Comment;
import co.xarx.trix.domain.Post;
import co.xarx.trix.domain.Station;
import co.xarx.trix.security.TrixPermission;
import co.xarx.trix.services.auth.AuthService;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
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
		Sid sid = new PrincipalSid(username);

		MutableAcl acl;
		try {
			acl = aclService.createAcl(oi);
		} catch (AlreadyExistsException e) {
			return;
		}

		acl.insertAce(acl.getEntries().size(), p, sid, true);
		aclService.updateAcl(acl);
	}

	private void saveOwner(Class clazz, Integer id) {
		String username = authService.getLoggedUsername();
		ObjectIdentity oi = new ObjectIdentityImpl(clazz, id);
		Sid sid = new PrincipalSid(username);

		MutableAcl acl;
		try {
			acl = aclService.createAcl(oi);
		} catch (AlreadyExistsException e) {
			return;
		}

		acl.setOwner(sid);
		aclService.updateAcl(acl);
	}

	@AfterReturning("within(co.xarx.trix.persistence.StationRepository+) && execution(* *..save(*)) && args(entity)")
	public void saveStation(Station entity) {
		savePermission(Station.class, entity.getId(), TrixPermission.ADMINISTRATION);
	}

	@AfterReturning("within(co.xarx.trix.persistence.PostRepository+) && execution(* *..save(*)) && args(entity)")
	public void savePost(Post entity) {
		saveOwner(Post.class, entity.getId());
	}

	@AfterReturning("within(co.xarx.trix.persistence.CommentRepository+) && execution(* *..save(*)) && args(entity)")
	public void saveComment(Comment entity) {
		saveOwner(Comment.class, entity.getId());
	}
}
