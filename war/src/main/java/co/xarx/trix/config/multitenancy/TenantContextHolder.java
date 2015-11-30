package co.xarx.trix.config.multitenancy;

public class TenantContextHolder {

	private static final ThreadLocal<Integer> tenantIdStorage = new InheritableThreadLocal<>();
	private static final ThreadLocal<String> tenantSubdomainStorage = new InheritableThreadLocal<>();

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
