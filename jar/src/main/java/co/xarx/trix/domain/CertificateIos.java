package co.xarx.trix.domain;

import javax.persistence.*;
import java.sql.Blob;


@Entity
@Table(name="Network")
public class CertificateIos {
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	public Integer id;

	@Column(name = "certificate_ios")
	public Blob certificateIos;

	@Column(name = "certificate_password")
	public String certificatePassword;

	public boolean isValid(){
		return this.certificateIos != null && this.certificatePassword != null;
	}

}
