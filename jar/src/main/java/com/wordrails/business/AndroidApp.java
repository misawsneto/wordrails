package com.wordrails.business;


import javax.persistence.*;
import java.io.Serializable;

@Entity
public class AndroidApp implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer id;

	public String projectName;
	public String appName;
	public String keyAlias;
	public String packageSuffix;
	public String host;

	@Lob
	public String shortDescription;

	@Lob
	public String fullDescription;
	public String videoUrl;

	public String apkUrl;
}
