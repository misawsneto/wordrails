package co.xarx.trix.config.multitenancy;

//@Component
//@Scope(value = "session")
public class SessionTenantProvider implements TenantProvider {

	private String tenantId;

	@Override
	public String getTenantId() {
		return tenantId;
	}

//	@Override
//	public void setTenantId(String tenantId) {
//		this.tenantId = tenantId;
//	}
}
