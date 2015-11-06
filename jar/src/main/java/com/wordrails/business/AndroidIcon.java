package com.wordrails.business;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
public class AndroidIcon implements Serializable {

	public static final Integer MDPI_SIZE = 48;
	public static final Integer HDPI_SIZE = 72;
	public static final Integer XHDPI_SIZE = 96;
	public static final Integer XXHDPI_SIZE = 144;
	public static final Integer XXXHDPI_SIZE = 192;
	public static final Integer ORIGINAL_SIZE = 512;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer id;

	@NotNull
	@ManyToOne(cascade = CascadeType.REMOVE)
	public File icon;

	@Transient
	public File mdpi;
	@Transient
	public File hdpi;
	@Transient
	public File xhdpi;
	@Transient
	public File xxhdpi;
	@Transient
	public File xxxhdpi;
}