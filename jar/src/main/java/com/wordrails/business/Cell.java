package com.wordrails.business;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
public class Cell {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Integer id;	
	
	@Min(0)
	@Column(name="\"index\"")
	public int index;

	@Column(columnDefinition = "boolean default false", nullable = false)
	public boolean featured;
	
	@NotNull
	@ManyToOne
	public Row row;
	
	@ManyToOne
	public Term term;
	
	@NotNull
	@ManyToOne
	public Post post;
}