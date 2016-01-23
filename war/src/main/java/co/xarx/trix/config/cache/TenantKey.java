package co.xarx.trix.config.cache;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Objects;

public class TenantKey implements Serializable {

	@JsonProperty
	private String tenantContext;
	@JsonProperty
	private Object key;
	@JsonProperty
	private String cacheName;

	public TenantKey() {
	}

	/**
	 * null values are ok
	 */
	public TenantKey(final String tenantContext, Object key, String cacheName) {
		this.tenantContext = tenantContext;
		this.key = key;
		this.cacheName = cacheName;
	}

	public String getTenantContext() {
		return tenantContext;
	}

	public void setTenantContext(String tenantContext) {
		this.tenantContext = tenantContext;
	}

	public Object getKey() {
		return key;
	}

	public void setKey(Object key) {
		this.key = key;
	}

	public String getCacheName() {
		return cacheName;
	}

	public void setCacheName(String cacheName) {
		this.cacheName = cacheName;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof TenantKey)) {
			return false;
		}
		TenantKey that = (TenantKey) o;
		return Objects.equals(this.tenantContext, that.tenantContext) &&
				Objects.equals(this.key, that.key) &&
				Objects.equals(this.cacheName, that.cacheName);
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.tenantContext, this.key, this.cacheName);
	}
}