package co.xarx.trix.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
public class Picture extends BaseEntity implements Serializable {

	public Picture() {
	}

	public Picture(Integer networkId, String sizeTag, File file, Image image) {
		this.sizeTag = sizeTag;
		this.file = file;
		this.image = image;
	}

	public Integer height;

	public Integer width;

	public String sizeTag;

	@NotNull
	@ManyToOne(cascade=CascadeType.MERGE)
	public File file;

	@ManyToOne
	public Image image;
}
