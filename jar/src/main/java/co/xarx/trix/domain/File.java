package co.xarx.trix.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "File")
public class File extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 7828358342575034733L;

	public static final String INTERNAL = "I";
	public static final String EXTERNAL = "E";

	public static final String DIR_IMAGES = "images";
	public static final String DIR_VIDEO = "videos";

	@NotNull
	@Size(min = 1, max = 1)
	public String type;

	public String directory;

	public String mime;

	public String name;

	public String url;

	public String hash;

	public Long size;

	public String getExtension() {
		if (mime != null && mime.split("/").length == 2)
			return mime.split("/")[1];

		return null;
	}
}