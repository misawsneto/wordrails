package co.xarx.trix.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
public class Picture implements MultiTenantEntity, Serializable {

	public Picture() {
	}

	public Picture(Integer networkId, String sizeTag, File file, Image image) {
		this.networkId = networkId;
		this.sizeTag = sizeTag;
		this.file = file;
		this.image = image;
	}

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

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	public Integer id;

	public Integer height;

	public Integer width;

	public String sizeTag;

	@NotNull
	@ManyToOne(cascade=CascadeType.MERGE)
	public File file;

	@ManyToOne
	public Image image;
}
