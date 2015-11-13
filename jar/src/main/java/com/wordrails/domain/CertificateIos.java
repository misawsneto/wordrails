package com.wordrails.domain;

import javax.persistence.*;
import java.sql.Blob;

/**
 * Created by jonas on 10/08/15.
 */
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
		if(this.certificateIos != null && this.certificatePassword != null){
			return true;
		}
		return false;
	}

}
