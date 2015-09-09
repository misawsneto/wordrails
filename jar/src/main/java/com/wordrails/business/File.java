package com.wordrails.business;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Blob;

@Entity
public class File {
	public static final String INTERNAL_FILE = "I";
	public static final String EXTERNAL_FILE = "E";	
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)				
	public Integer id;
	
	@NotNull
	@Size(min=1, max=1)
	public String type;	

	public String mime;

	public String name;	
	
	public String url;
}