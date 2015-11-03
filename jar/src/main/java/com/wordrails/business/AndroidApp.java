package com.wordrails.business;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
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

	@NotNull
	@ManyToOne(cascade=CascadeType.REMOVE)
	public File logoActionBar;
	@NotNull
	@ManyToOne(cascade=CascadeType.REMOVE)
	public File logoLogin;
	@NotNull
	@ManyToOne(cascade=CascadeType.REMOVE)
	public File logoSplash;

	@NotNull
	@ManyToOne(cascade=CascadeType.REMOVE)
	public File launcherIconMDPI;           //48 × 48
	@NotNull
	@ManyToOne(cascade=CascadeType.REMOVE)
	public File launcherIconHDPI;           //72 × 72
	@NotNull
	@ManyToOne(cascade=CascadeType.REMOVE)
	public File launcherIconXHDPI;          //96 × 96
	@NotNull
	@ManyToOne(cascade=CascadeType.REMOVE)
	public File launcherIconXXHDPI;         //144 × 144
	@NotNull
	@ManyToOne(cascade=CascadeType.REMOVE)
	public File launcherIconXXXHDPI;        //192 × 192
	@NotNull
	@ManyToOne(cascade=CascadeType.REMOVE)
	public File launcherIconGooglePlayStore;//512 × 512
}
