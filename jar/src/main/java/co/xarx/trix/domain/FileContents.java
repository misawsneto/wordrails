package co.xarx.trix.domain;

import lombok.AccessLevel;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Blob;

@lombok.Getter
@lombok.Setter
@Deprecated
@Entity
@Table(name="File")
public class FileContents extends BaseEntity {

	@Id
	@Setter(AccessLevel.NONE)
	@GeneratedValue(strategy = GenerationType.AUTO)
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

	public Blob contents;

	public String getExtension() {
		if (mime != null && mime.split("/").length == 2) return mime.split("/")[1];

		return null;
	}
}