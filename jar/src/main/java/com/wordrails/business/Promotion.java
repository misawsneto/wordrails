package com.wordrails.business;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
public class Promotion {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)				
	public Integer id;
	
	@JsonFormat(shape=JsonFormat.Shape.NUMBER)
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	public Date date;
	
	@NotNull
	@ManyToOne	
	public Post post;
	
	@NotNull
	@ManyToOne
	public Person promoter;
	
	@NotNull
	@ManyToOne
	public Station station;	
	
	@Size(min=1, max=500)
	public String comment;
}