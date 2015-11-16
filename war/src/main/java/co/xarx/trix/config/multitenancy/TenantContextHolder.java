package co.xarx.trix.config.multitenancy;

public class TenantContextHolder {

	private static final ThreadLocal<Integer> tenantIdStorage = new ThreadLocal<>();
	private static final ThreadLocal<String> tenantSubdomainStorage = new ThreadLocal<>();

	public static Integer getCurrentTenantId() {
		return tenantIdStorage.get();
	}

	public static String getCurrentTenantSubdomain() {
		return tenantSubdomainStorage.get();
	}

	public static void setCurrentTenantId(Integer tenantId) {
		tenantIdStorage.set(tenantId);
	}

	public static void setCurrentTenantSubdomain(String tenantSubdomain) {
		tenantSubdomainStorage.set(tenantSubdomain);
	}
}
