package com.wordrails.business;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "File")
public class File {

	public static final String INTERNAL = "I";
	public static final String EXTERNAL = "E";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer id;

	@NotNull
	@Size(min = 1, max = 1)
	public String type;

	public String mime;

	public String name;

	public String url;

	public String hash;

	public Long size;

	public Integer networkId;
}