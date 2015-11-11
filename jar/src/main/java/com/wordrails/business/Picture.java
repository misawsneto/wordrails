package com.wordrails.business;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
public class Picture implements MultiTenantEntity, Serializable {

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	public Integer id;

	public Integer height;

	public Integer width;

	public String sizeTag;

	@NotNull
	@ManyToOne(cascade=CascadeType.PERSIST)
	public File file;

	@ManyToOne
	public Image image;

	@NotNull
	public Integer networkId;

	@Override
	public Integer getNetworkId() {
		return networkId;
	}

	@Override
	public void setNetworkId(Integer networkId) {
		this.networkId = networkId;
	}
}
