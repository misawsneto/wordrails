package com.wordrails.business;

import java.sql.Blob;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name="File")
public class FileContents {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer id;

	@NotNull
	@Size(min = 1, max = 1)
	public String type;

	public String directory;

	public String mime;

	public String name;

	public String url;

	public String hash;

	public Long size;

	public Integer networkId;

	public Blob contents;
}