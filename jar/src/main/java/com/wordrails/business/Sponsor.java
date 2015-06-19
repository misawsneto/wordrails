package com.wordrails.business;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Sponsor {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)	
	public Integer id;
	
	@OneToOne
	public Image logo;
	
	@NotNull
	@ManyToOne
	public Network network;
	
	@OneToMany(mappedBy="publicitySponsor")
	public Set<Image> images;
	
	@Size(min=1, max=200)
	public String name;
	
	@Size(max=1000)
	public String keywords;
}
