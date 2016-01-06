package co.xarx.trix.config.multitenancy;

public class TenantContextHolder {

	private static final ThreadLocal<String> tenantIdStorage = new ThreadLocal<>();

	public static String getCurrentTenantId() {
		return tenantIdStorage.get();
	}

	public static void setCurrentTenantId(String tenantSubdomain) {
		tenantIdStorage.set(tenantSubdomain);
	}
}
