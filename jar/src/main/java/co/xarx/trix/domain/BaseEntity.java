package co.xarx.trix.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
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

@Getter
@Setter
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
	@JsonProperty
	public Date createdAt;

	@Version
	@JsonIgnore
	@Column(columnDefinition = "int(11) DEFAULT 0", nullable = false)
	private int version;

	@JsonIgnore
	@NotNull
	public String tenantId;
}