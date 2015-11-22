package co.xarx.trix.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
public class Picture extends BaseEntity implements Serializable {

	public Picture() {
	}

	public Picture(String sizeTag, File file) {
		this.sizeTag = sizeTag;
		this.file = file;
	}

	public Integer height;

	public Integer width;

	public String sizeTag;

	@NotNull
	@ManyToOne(cascade=CascadeType.MERGE)
	public File file;
}
