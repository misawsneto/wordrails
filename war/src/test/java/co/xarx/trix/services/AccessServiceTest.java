package co.xarx.trix.services;

import co.xarx.trix.config.multitenancy.TenantProvider;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AccessServiceTest {

	TenantProvider demoTenantProvider = () -> "demo";
	TenantProvider cesarTenantProvider = () -> "cesar";

	String[] tenants = {"demo"};
	String[] profiles = {"dev"};

	AccessService demoDevAS = new AccessService("dev", demoTenantProvider);
	AccessService cesarDevAS = new AccessService("dev", cesarTenantProvider);

	AccessService demoProdAS = new AccessService("prod", demoTenantProvider);

	public void assertPermissions(boolean hasPermission, boolean hasNotPermission) {
		assertTrue("Should have permission", hasPermission);
		assertFalse("Should not have permission", hasNotPermission);
	}

	@Test
	public void testAccessGrantedForTenant() throws Exception {
		boolean hasPermissionForDemo = demoDevAS.hasPermissionOnTenant(true, tenants);
		boolean hasPermissionForCesar = cesarDevAS.hasPermissionOnTenant(true, tenants);

		assertPermissions(hasPermissionForDemo, hasPermissionForCesar);
	}

	@Test
	public void testAccessDeniedForTenantDemo() throws Exception {
		boolean hasPermissionForDemo = demoDevAS.hasPermissionOnTenant(false, tenants);
		boolean hasPermissionForCesar = cesarDevAS.hasPermissionOnTenant(false, tenants);

		assertPermissions(hasPermissionForCesar, hasPermissionForDemo);
	}

	@Test
	public void testAccessGrantedForProfileDev() throws Exception {
		boolean hasPermissionForDev = demoDevAS.hasPermissionOnProfile(true, profiles);
		boolean hasPermissionForProd = demoProdAS.hasPermissionOnProfile(true, profiles);

		assertPermissions(hasPermissionForDev, hasPermissionForProd);
	}

	@Test
	public void testAccessDeniedForProfileDev() throws Exception {
		boolean hasPermissionForDev = demoDevAS.hasPermissionOnProfile(false, profiles);
		boolean hasPermissionForProd = demoProdAS.hasPermissionOnProfile(false, profiles);

		assertPermissions(hasPermissionForProd, hasPermissionForDev);
	}
}