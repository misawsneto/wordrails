package co.xarx.trix.domain;

import co.xarx.trix.annotation.SdkExclude;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Blob;


@lombok.Getter
@lombok.Setter
@SdkExclude
@Entity
public class AppleCertificate extends BaseEntity {

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	public Integer id;

	@Override
	public Integer getId() {
		return id;
	}

	@NotNull
	public Blob file;

	@NotNull
	public String password;
}
