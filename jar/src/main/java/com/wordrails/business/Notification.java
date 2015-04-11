package com.wordrails.business;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
public class Notification {
	
	enum Type{
		STATION_ADD, STATION_REMOVE, POST_COMMENT, POST_DELETED,
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)				
	public Integer id;
	
	@ManyToOne
	@NotNull
	public Person person;
	
	@ManyToOne
	@NotNull
	public Network network;
	
	@ManyToOne
	public Station station;
	
	@ManyToOne
	public Post post;
	
	public boolean seen = false;
	
	@NotNull
	@Size(min=1,max=500)
	@Pattern(regexp="\\S", message="Empty string validation")
	public String message;
	
	@NotNull
	@Pattern(regexp="\\S", message="Empty string validation")
	public String type;
}
