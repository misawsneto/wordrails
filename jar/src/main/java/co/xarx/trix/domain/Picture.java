package co.xarx.trix.domain;

import javax.persistence.*;
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

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer id;

	@Override
	public Integer getId() {
		return id;
	}

	public Integer height;

	public Integer width;

	public String sizeTag;

	@NotNull
	@ManyToOne(cascade=CascadeType.MERGE)
	public File file;
}
