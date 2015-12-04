package co.xarx.trix.config.multitenancy;

public class TenantContextHolder {

	private static final ThreadLocal<Integer> networkIdStorage = new InheritableThreadLocal<>();
	private static final ThreadLocal<String> tenantIdStorage = new InheritableThreadLocal<>();

	public static Integer getCurrentNetworkId() {
		return networkIdStorage.get();
	}

	public static String getCurrentTenantId() {
		return tenantIdStorage.get();
	}

	public static void setCurrentNetworkId(Integer tenantId) {
		networkIdStorage.set(tenantId);
	}

	public static void setCurrentTenantId(String tenantSubdomain) {
		tenantIdStorage.set(tenantSubdomain);
	}
}
