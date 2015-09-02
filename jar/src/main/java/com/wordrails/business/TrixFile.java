package com.wordrails.business;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Blob;

@Entity
@Table(name = "File")
public class TrixFile {
	public static final String INTERNAL_FILE = "I";
	public static final String EXTERNAL_FILE = "E";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer id;

	@NotNull
	@Size(min = 1, max = 1)
	public String type;

	public String mime;

	public String name;

	public String url;

	public Blob contents;
}