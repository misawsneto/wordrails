package co.xarx.trix.config.security;

import co.xarx.trix.domain.Post;
import co.xarx.trix.domain.Station;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.acls.domain.AccessControlEntryImpl;
import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.acls.model.Sid;
import org.springframework.util.Assert;

import java.util.Arrays;

import static co.xarx.trix.config.security.Permissions.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BitMaskPermissionGrantingStrategyTest {


	private Sid user1; //reader on station1, writer on station2
	private Sid user2; //roleAdmin on station1, forbidden to read station2
	private Sid roleAnon; //can read station1 but not station2
	private Sid roleAdmin; //roleAdmin on both stations
	private Sid roleUser;

	private IMAcl station1; //public
	private IMAcl post1; //belongs to station1

	private IMAcl station2; //private
	private IMAcl post2; //belongs to station2
	private BitMaskPermissionGrantingStrategy grantingStrategy;

	@Before
	public void setUp() throws Exception {
		grantingStrategy = new BitMaskPermissionGrantingStrategy();

		station1 = new IMAcl(Station.class, 1);
		station2 = new IMAcl(Station.class, 2);
		post1 = new IMAcl(Post.class, 1);
		post2 = new IMAcl(Post.class, 2);
		post1.setParent(station1);
		post2.setParent(station2);

		user1 = new PrincipalSid("user1");
		AccessControlEntryImpl user1St1 = ace(station1, user1, getReader(), true);
		AccessControlEntryImpl user1St2 = ace(station2, user1, getWriter(), true);
		AccessControlEntryImpl user1Post1 = ace(post1, user1, getReader(), false);

		user2 = new PrincipalSid("user2");
		AccessControlEntryImpl user2St1 = ace(station1, user2, getAdmin(), true);
		AccessControlEntryImpl user2St2 = ace(station2, user2, getAdmin(), false);
		AccessControlEntryImpl user2Post2 = ace(post2, user2, getOwner(), true);

		roleUser = new GrantedAuthoritySid("ROLE_USER");
		AccessControlEntryImpl userSt1 = ace(station1, roleUser, getReader(), true);

		roleAnon = new GrantedAuthoritySid("ROLE_ANONYMOUS");
		AccessControlEntryImpl anonSt1 = ace(station1, roleAnon, getReader(), true);
		AccessControlEntryImpl anonSt2 = ace(station2, roleAnon, getReader(), false);

		roleAdmin = new GrantedAuthoritySid("ROLE_ADMIN");
		AccessControlEntryImpl adminSt1 = ace(station1, roleAdmin, getAdmin(), true);
		AccessControlEntryImpl adminSt2 = ace(station2, roleAdmin, getAdmin(), true);

		station1.addEntry(user1St1);
		station1.addEntry(user2St1);
		station1.addEntry(anonSt1);
		station1.addEntry(adminSt1);
		station1.addEntry(userSt1);

		station2.addEntry(user1St2);
		station2.addEntry(user2St2);
		station2.addEntry(anonSt2);
		station2.addEntry(adminSt2);

		post1.addEntry(user1Post1);
		post1.addEntry(user2Post2);
	}

	private AccessControlEntryImpl ace(IMAcl acl, Sid sid, Permission permission, boolean granting) {
		return new AccessControlEntryImpl(null, acl, sid, permission, granting, false, false);
	}

	private class AllowedPermissions {

		private final Sid[] sids;
		private final IMAcl acl;

		public AllowedPermissions(IMAcl acl, Sid... sids) {
			this.acl = acl;
			this.sids = sids;
		}

		private boolean isAllowed(Permission... permission) {
			Assert.notEmpty(permission);
			return grantingStrategy.isGranted(this.acl,
					Arrays.asList(permission),
					Arrays.asList(this.sids), false);
		}

		private void isAllAllowed() {
			assertTrue(isAllowed(READ));
			assertTrue(isAllowed(WRITE));
			assertTrue(isAllowed(CREATE));
			assertTrue(isAllowed(MODERATION));
			assertTrue(isAllowed(DELETE));
			assertTrue(isAllowed(ADMINISTRATION));
		}

		private void isAllDenied() {
			assertFalse(isAllowed(READ));
			assertFalse(isAllowed(WRITE));
			assertFalse(isAllowed(CREATE));
			assertFalse(isAllowed(MODERATION));
			assertFalse(isAllowed(DELETE));
			assertFalse(isAllowed(ADMINISTRATION));
		}
	}

	@Test
	public void User1OnStation1() throws Exception {
		AllowedPermissions permissions = new AllowedPermissions(station1, user1);

		assertTrue(permissions.isAllowed(READ));
		assertFalse(permissions.isAllowed(CREATE));
		assertTrue(permissions.isAllowed(READ, CREATE));
	}

	@Test
	public void User1OnStation2() throws Exception {
		AllowedPermissions permissions = new AllowedPermissions(station2, user1);

		assertTrue(permissions.isAllowed(READ));
		assertTrue(permissions.isAllowed(WRITE));
		assertTrue(permissions.isAllowed(CREATE));
	}

	@Test
	public void User1OnPost1() throws Exception {
		AllowedPermissions permissions = new AllowedPermissions(post1, user1);

		permissions.isAllDenied();
		assertFalse(permissions.isAllowed(READ, CREATE, WRITE, DELETE, MODERATION));
	}

	@Test
	public void User2OnStation1() throws Exception {
		AllowedPermissions permissions = new AllowedPermissions(station1, user2);

		permissions.isAllAllowed();
	}

	@Test
	public void User2OnStation2() throws Exception {
		AllowedPermissions permissions = new AllowedPermissions(station2, user2);

		permissions.isAllDenied();
	}

	@Test
	public void User2OnPost1() throws Exception {
		AllowedPermissions permissions = new AllowedPermissions(post1, user2);

		permissions.isAllAllowed();
	}

	@Test
	public void User2OnPost2() throws Exception {
		AllowedPermissions permissions = new AllowedPermissions(post2, user2);

		permissions.isAllDenied();
	}

	@Test
	public void AnonOnStation1() throws Exception {
		AllowedPermissions permissions = new AllowedPermissions(station1, roleAnon);

		assertTrue(permissions.isAllowed(READ));
		assertFalse(permissions.isAllowed(CREATE, WRITE, DELETE, MODERATION));
	}

	@Test
	public void AnonOnStation2() throws Exception {
		AllowedPermissions permissions = new AllowedPermissions(station2, roleAnon);

		permissions.isAllDenied();
	}

	@Test
	public void AnonOnPost2() throws Exception {
		AllowedPermissions permissions = new AllowedPermissions(post2, roleAnon);

		permissions.isAllDenied();
	}

	@Test
	public void AdminOnStation1() throws Exception {
		AllowedPermissions permissions = new AllowedPermissions(station1, roleAdmin);

		permissions.isAllAllowed();
	}

	@Test
	public void AdminOnStation2() throws Exception {
		AllowedPermissions permissions = new AllowedPermissions(station2, roleAdmin);

		permissions.isAllAllowed();
	}

	@Test
	public void UserOnStation1() throws Exception {
		AllowedPermissions permissions = new AllowedPermissions(station1, roleUser);

		assertTrue(permissions.isAllowed(READ));
	}

	@Test
	public void UserOnStation2() throws Exception {
		AllowedPermissions permissions = new AllowedPermissions(station2, roleUser);

		assertFalse(permissions.isAllowed(READ));
	}
}