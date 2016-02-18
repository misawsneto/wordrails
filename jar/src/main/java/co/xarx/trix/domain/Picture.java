package co.xarx.trix.domain;

import co.xarx.trix.annotation.SdkExclude;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@SdkExclude
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class Picture extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -6407170001176054415L;

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
