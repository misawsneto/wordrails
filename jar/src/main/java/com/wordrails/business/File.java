package com.wordrails.business;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "File")
public class File implements Serializable, MultiTenantEntity {
	private static final long serialVersionUID = 7828358342575034733L;

	public static final String INTERNAL = "I";
	public static final String EXTERNAL = "E";

	public static final String DIR_IMAGES = "images";
	public static final String DIR_VIDEO = "videos";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer id;

	@NotNull
	@Size(min = 1, max = 1)
	public String type;

	public String directory;

	public String mime;

	public String name;

	public String url;

	public String hash;

	public Long size;

	public Integer networkId;

	@Override
	public Integer getNetworkId() {
		return networkId;
	}

	@Override
	public void setNetworkId(Integer networkId) {
		this.networkId = networkId;
	}

	public String getExtension() {
		if (mime != null && mime.split("/").length == 2)
			return mime.split("/")[1];

		return null;
	}
}