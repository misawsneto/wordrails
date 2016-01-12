package co.xarx.trix.domain;


public interface MultiTenantEntity {

	String getTenantId();

	Integer getNetworkId();

	void setTenantId(String tenantId);

	void setNetworkId(Integer networkId);
}
