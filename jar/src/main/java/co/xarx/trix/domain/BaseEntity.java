package co.xarx.trix.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;

@MappedSuperclass
public abstract class BaseEntity implements MultiTenantEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	public Integer id;

	@Version
	@Column(columnDefinition = "integer DEFAULT 0", nullable = false)
	private int version;

	@JsonFormat(shape = JsonFormat.Shape.NUMBER)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
	private Date updatedAt;

	@JsonFormat(shape = JsonFormat.Shape.NUMBER)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private Date createdAt;

	@Column(columnDefinition = "int(11) DEFAULT 0")
	private Integer networkId;

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
