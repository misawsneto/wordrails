package co.xarx.trix.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.Filters;
import org.hibernate.annotations.ParamDef;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.Date;

@MappedSuperclass
@FilterDef(name = "networkFilter", parameters = @ParamDef(name = "networkId", type = "integer"))
@Filters(@Filter(name = "networkFilter", condition = "networkId = :networkId"))
public abstract class BaseEntity implements MultiTenantEntity, Identifiable {

	@JsonFormat(shape = JsonFormat.Shape.NUMBER)
	@Temporal(TemporalType.TIMESTAMP)
	@LastModifiedDate
	public Date updatedAt;

	@JsonFormat(shape = JsonFormat.Shape.NUMBER)
	@Temporal(TemporalType.TIMESTAMP)
	@CreatedDate
	public Date createdAt;

	@JsonIgnore
	@Column(columnDefinition = "int(11) DEFAULT 0")
	public Integer networkId;

	@Version
	@JsonIgnore
	@Column(columnDefinition = "int(11) DEFAULT 0", nullable = false)
	private int version;

	@JsonIgnore
	String tenantId;

	@Override
	public String getTenantId() {
		return tenantId;
	}

	@Override
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	@Override
	public Integer getNetworkId() {
		return networkId;
	}

	@Override
	public void setNetworkId(Integer networkId) {
		this.networkId = networkId;
	}

	public int getVersion() {
		return version;
	}

	@SuppressWarnings("unused")
	private void setVersion(int version) {
		this.version = version;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	@SuppressWarnings("unused")
	private void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	@SuppressWarnings("unused")
	private void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
}