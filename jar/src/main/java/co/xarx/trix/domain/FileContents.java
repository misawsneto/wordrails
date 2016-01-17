package co.xarx.trix.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Blob;

@Deprecated
@Entity
@Table(name="File")
public class FileContents extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer id;

	@Override
	public Integer getId() {
		return id;
	}

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
}