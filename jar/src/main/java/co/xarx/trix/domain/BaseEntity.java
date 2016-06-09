package co.xarx.trix.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.*;
import org.javers.core.metamodel.annotation.DiffIgnore;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
@MappedSuperclass
@FilterDefs({
		@FilterDef(
				name = "tenantFilter",
				parameters = @ParamDef(name = "tenantId", type = "string")
		),
		@FilterDef(
				name = "idFilter",
				parameters = @ParamDef(name = "ids", type = "int")
		)
})

@Filters({
		@Filter(name = "tenantFilter", condition = "tenantId = :tenantId"),
		@Filter(name = "idFilter", condition = "id in (:ids)")
})
public abstract class BaseEntity implements MultiTenantEntity, Identifiable, Versionable {

	@DiffIgnore
	@JsonFormat(shape = JsonFormat.Shape.NUMBER)
	@Temporal(TemporalType.TIMESTAMP)
	@LastModifiedDate
	private Date updatedAt;

	@DiffIgnore
	@JsonFormat(shape = JsonFormat.Shape.NUMBER)
	@Temporal(TemporalType.TIMESTAMP)
	@CreatedDate
	private Date createdAt;

	@Version
	@JsonIgnore
	@Column(columnDefinition = "int DEFAULT 0", nullable = false)
	private int version;

	@JsonIgnore
	@NotNull
	public String tenantId;
}