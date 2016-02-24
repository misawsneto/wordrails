package co.xarx.trix.domain;


public interface MultiTenantEntity {

	String getTenantId();

	void setTenantId(String tenantId);
}
