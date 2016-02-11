package co.xarx.trix.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.Filters;
import org.hibernate.annotations.ParamDef;
import org.javers.core.metamodel.annotation.DiffIgnore;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@MappedSuperclass
@FilterDef(name = "tenantFilter", parameters = @ParamDef(name = "tenantId", type = "string"))
@Filters(@Filter(name = "tenantFilter", condition = "tenantId = :tenantId"))
public abstract class BaseEntity implements MultiTenantEntity, Identifiable, Versionable {

	@DiffIgnore
	@JsonFormat(shape = JsonFormat.Shape.NUMBER)
	@Temporal(TemporalType.TIMESTAMP)
	@LastModifiedDate
	public Date updatedAt;

	@DiffIgnore
	@JsonFormat(shape = JsonFormat.Shape.NUMBER)
	@Temporal(TemporalType.TIMESTAMP)
	@CreatedDate
	public Date createdAt;

	@Version
	@JsonIgnore
	@Column(columnDefinition = "int(11) DEFAULT 0", nullable = false)
	private int version;

	@JsonIgnore
	@NotNull
	public String tenantId;

	@Override
	public String getTenantId() {
		return tenantId;
	}

	@Override
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public Integer getVersion() {
		return version;
	}

	@SuppressWarnings("unused")
	private void setVersion(Integer version) {
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

	@Override
	public boolean equals(Object obj) {
		if(obj == null || !(obj instanceof BaseEntity)) {
			return false;
		}

		return ((BaseEntity) obj).getId().equals(getId());

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
		return result;
	}
}