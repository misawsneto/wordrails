package co.xarx.trix.domain;

import co.xarx.trix.annotation.SdkExclude;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Blob;


@lombok.Getter
@lombok.Setter
@SdkExclude
@Entity
@Table(uniqueConstraints = {
		@UniqueConstraint(columnNames = {"tenantId"}),
		@UniqueConstraint(columnNames = {"network_id"})
})
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

	@OneToOne
	@NotNull
	public Network network;

	@NotNull
	public String password;
}
