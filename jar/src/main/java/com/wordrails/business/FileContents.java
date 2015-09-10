package com.wordrails.business;

import java.sql.Blob;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="File")
public class FileContents {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Integer id;

	public Blob contents;	
}